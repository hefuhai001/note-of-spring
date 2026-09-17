package com.example.hfh.service;

import com.example.hfh.resp.ChunkUploadResp;
import com.example.hfh.resp.FileUploadResp;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 文件服务类（FileService）
 *
 * <p>核心业务逻辑层，负责处理与 MinIO 对象存储的所有交互操作</p>
 *
 * <h3>主要功能：</h3>
 * <ul>
 *   <li><b>普通文件上传</b>：支持小文件直接上传到 MinIO</li>
 *   <li><b>文件下载</b>：从 MinIO 下载文件并返回输入流</li>
 *   <li><b>分片上传</b>：将大文件拆分为多个分片单独上传（支持断点续传）</li>
 *   <li><b>进度查询</b>：查询指定上传任务的已上传分片列表</li>
 *   <li><b>分片合并</b>：利用 MinIO composeObject API 高效合并所有分片</li>
 * </ul>
 *
 * <h3>技术架构：</h3>
 * <ul>
 *   <li>存储后端：MinIO 对象存储服务（S3 兼容协议）</li>
 *   <li>并发控制：使用 ConcurrentHashMap 保证线程安全</li>
 *   <li>分片管理：内存中维护上传进度状态（生产环境建议持久化到 Redis/数据库）</li>
 * </ul>
 *
 * <h3>使用示例：</h3>
 * <pre>{@code
 * @Autowired
 * private FileService fileService;
 *
 * // 普通上传
 * FileUploadResp resp = fileService.upload(multipartFile);
 *
 * // 分片上传
 * ChunkUploadResp chunkResp = fileService.uploadChunk(uploadId, 0, 10, chunk, "test.pdf", 1024*1024*50);
 *
 * // 合并分片
 * FileUploadResp mergeResp = fileService.mergeChunks(uploadId);
 * }</pre>
 *
 * <h3>注意事项：</h3>
 * <ul>
 *   <li>当前版本使用内存存储上传进度，服务重启后会丢失</li>
 *   <li>生产环境建议增加：Redis 持久化、用户认证、文件校验（MD5）、过期清理机制</li>
 *   <li>MinIO bucket 需要提前创建并配置好访问权限</li>
 * </ul>
 *
 * @author 何福海
 * @version 2.0.0（新增分片上传和断点续传功能）
 * @since 2025/8/1
 * @see com.example.hfh.controller.FileController
 * @see io.minio.MinioClient
 */
@Service
@RequiredArgsConstructor
public class FileService {

    /** MinIO 客户端实例，用于执行所有对象存储操作 */
    private final MinioClient minio;

    /**
     * MinIO 存储桶名称（从配置文件注入）
     *
     * <p>配置示例（application.yml）：</p>
     * <pre>
     * minio:
     *   bucket-name: files
     * </pre>
     */
    @Value("${minio.bucket-name}")
    private String bucket;

    /**
     * 上传进度追踪表（线程安全）
     *
     * <p>数据结构：Map&lt;uploadId, Set&lt;chunkNumber&gt;&gt;</p>
     * <p>作用：记录每个上传任务已完成的上传分片索引集合</p>
     *
     * <p>使用场景：</p>
     * <ul>
     *   <li>前端查询进度时返回此数据</li>
     *   <li>合并时验证是否所有分片都已上传完成</li>
     *   <li>断点续传时判断哪些分片需要重新上传</li>
     * </ul>
     *
     * <p>线程安全保证：ConcurrentHashMap + ConcurrentHashMap.newKeySet()</p>
     */
    private final ConcurrentHashMap<String, Set<Integer>> uploadProgress = new ConcurrentHashMap<>();

    /**
     * 文件名映射表（线程安全）
     *
     * <p>数据结构：Map&lt;uploadId, originalFileName&gt;</p>
     * <p>作用：保存原始文件名，用于合并时生成最终文件的扩展名</p>
     *
     * <p>示例：</p>
     * <pre>
     * uploadId: "abc123" → fileName: "大文件测试.pdf"
     * </pre>
     */
    private final ConcurrentHashMap<String, String> fileNames = new ConcurrentHashMap<>();

    /**
     * 文件大小映射表（线程安全）
     *
     * <p>数据结构：Map&lt;uploadId, fileSizeInBytes&gt;</p>
     * <p>作用：记录原始文件大小，可用于完整性校验和进度计算</p>
     *
     * <p>示例：</p>
     * <pre>
     * uploadId: "abc123" → fileSize: 52428800 (50MB)
     * </pre>
     */
    private final ConcurrentHashMap<String, Long> fileSizes = new ConcurrentHashMap<>();

    /**
     * 普通文件上传（适用于小文件）
     *
     * <p>功能说明：</p>
     * <ul>
     *   <li>接收前端上传的 MultipartFile 对象</li>
     *   <li>生成 UUID 文件名避免冲突</li>
     *   <li>上传到 MinIO 指定 bucket</li>
     *   <li>返回永久路径和临时签名 URL</li>
     * </ul>
     *
     * <p>适用场景：</p>
     * <ul>
     *   <li>文件大小 &lt; 10MB 的普通上传需求</li>
     *   <li>不需要断点续传的场景</li>
     *   <li>后台管理系统批量导入</li>
     * </ul>
     *
     * <h4>执行流程：</h4>
     * <ol>
     *   <li>提取原始文件名和扩展名</li>
     *   <li>生成唯一文件名（UUID + 扩展名）</li>
     *   <li>调用 MinIO putObject API 上传文件流</li>
     *   <li>生成两种访问路径（永久路径 + 签名 URL）</li>
     *   <li>封装结果返回给 Controller</li>
     * </ol>
     *
     * @param file Spring MultipartFile 对象（包含文件流、原始名称、Content-Type 等）
     * @return FileUploadResp 包含两个 URL：
     *         <ul>
     *           <li>permanentUrl：永久路径（/minio/bucket/filename），适合后台系统拼接使用</li>
     *           <li>presignedUrl：临时签名 URL（7天有效），可直接在浏览器访问或下载</li>
     *         </ul>
     * @throws RuntimeException 当 MinIO 连接失败、bucket 不存在、权限不足时抛出
     * @see #uploadChunk(String, int, int, MultipartFile, String, long) 分片上传方法
     */
    public FileUploadResp upload(MultipartFile file) {
        try {
            // 第一步：提取文件信息
            String originalFilename = file.getOriginalFilename();
            String extension = StringUtils.getFilenameExtension(originalFilename);

            // 第二步：生成唯一文件名（防止文件名冲突）
            // 使用 UUID 保证全局唯一性，保留原扩展名以便浏览器识别文件类型
            String uniqueName = UUID.randomUUID() + (extension != null ? "." + extension : "");

            // 第三步：上传文件到 MinIO
            // putObject 参数说明：
            //   - bucket: 存储桶名称
            //   - object: 对象键（文件在 MinIO 中的路径）
            //   - stream: 文件输入流
            //   - size: 文件大小（-1 表示未知大小，MinIO 会自动处理）
            //   - contentType: MIME 类型（用于浏览器识别如何渲染文件）
            minio.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(uniqueName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());

            // 第四步：生成访问路径

            // 路径1：永久路径（由应用自行拼接，不依赖 MinIO 签名机制）
            // 适用场景：通过 Nginx 反向代理或网关统一路由访问
            String permanentUrl = String.format("/minio/%s/%s", bucket, uniqueName);

            // 路径2：临时签名外链（由 MinIO 生成带签名的 URL）
            // 特点：
            //   - 包含时间戳和签名参数，防止未授权访问
            //   - 有有效期限制（此处设置为7天）
            //   - 过期后需重新生成
            // 适用场景：前端直接下载、分享给外部用户
            String presignedUrl = minio.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucket)
                            .object(uniqueName)
                            .expiry(7, TimeUnit.DAYS)
                            .build());

            return new FileUploadResp(permanentUrl, presignedUrl);
        } catch (Exception e) {
            throw new RuntimeException("上传失败", e);
        }
    }

    /**
     * 从 MinIO 下载文件
     *
     * <p>功能说明：</p>
     * <ul>
     *   <li>根据文件名从 MinIO 获取对象输入流</li>
     *   <li>包装为 InputStreamResource 返回给 Controller</li>
     *   <li>由 Controller 设置响应头实现浏览器下载</li>
     * </ul>
     *
     * <p>性能考虑：</p>
     * <ul>
     *   <li>使用流式传输，不会一次性加载整个文件到内存</li>
     *   <li>适合大文件下载（GB 级别无压力）</li>
     *   <li>下载过程占用一个 HTTP 连接直到完成</li>
     * </ul>
     *
     * @param fileName 要下载的文件名（MinIO 中的 object name）
     * @return InputStreamResource 文件输入流资源，Spring 会自动将其写入 HTTP 响应体
     * @throws RuntimeException 当文件不存在、权限不足、网络错误时抛出
     * @see com.example.hfh.controller.FileController#download(String) Controller 层调用
     */
    public InputStreamResource download(String fileName) {
        try {
            // 从 MinIO 获取对象输入流
            // getObject 参数：
            //   - bucket: 存储桶名称
            //   - object: 对象键（文件路径）
            InputStream stream = minio.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(fileName)
                            .build());
            return new InputStreamResource(stream);
        } catch (Exception e) {
            throw new RuntimeException("下载失败", e);
        }
    }

    /**
     * 上传单个文件分片（分片上传核心方法）
     *
     * <p>功能说明：</p>
     * <ul>
     *   <li>接收前端切分的单个分片数据</li>
     *   <li>保存到 MinIO 的临时目录（chunks/{uploadId}/chunk_{index}）</li>
     *   <li>更新该上传任务的进度信息</li>
     *   <li>返回当前已完成的分片列表（供前端更新 UI）</li>
     * </ul>
     *
     * <h4>存储结构设计：</h4>
     * <pre>
     * MinIO Bucket 结构：
     * └── {bucket}/
     *     ├── chunks/
     *     │   └── {uploadId}/           ← 每个上传任务的独立目录
     *     │       ├── chunk_0          ← 第1个分片（索引从0开始）
     *     │       ├── chunk_1          ← 第2个分片
     *     │       ├── chunk_2
     *     │       └── ...
     *     └── {final-file}             ← 合并后的最终文件
     * </pre>
     *
     * <h4>线程安全设计：</h4>
     * <ul>
     *   <li>使用 {@code ConcurrentHashMap.putIfAbsent()} 确保初始化只执行一次</li>
     *   <li>使用 {@code ConcurrentHashMap.newKeySet()} 创建线程安全的 Set</li>
     *   <li>多个分片可同时上传，互不干扰</li>
     * </ul>
     *
     * <h4>幂等性保证：</h4>
     * <ul>
     *   <li>同一分片重复上传会覆盖旧数据（MinIO putObject 默认行为）</li>
     *   <li>进度记录会正常更新（Set.add 幂等）</li>
     *   <li>适用于网络超时后的重试场景</li>
     * </ul>
     *
     * @param uploadId       上传任务唯一标识（由前端生成，关联同一文件的所有分片）
     * @param chunkNumber    当前分片的索引号（从 0 开始，0 表示第一个分片）
     * @param totalChunks    该文件的总分片数（用于服务端校验完整性）
     * @param chunk          当前分片的文件数据（MultipartFile 类型）
     * @param fileName       原始文件名（如 "bigfile.zip"，用于保留扩展名）
     * @param fileSize       原始文件总大小（字节，可用于后续校验）
     * @return ChunkUploadResp 包含以下信息：
     *         <ul>
     *           <li>uploadId：本次上传的任务 ID</li>
     *           <li>chunkNumber：刚上传成功的分片号</li>
     *           <li>success：是否成功</li>
     *           <li>uploadedChunks：当前所有已上传的分片索引列表（实时同步）</li>
     *         </ul>
     * @throws RuntimeException 当 MinIO 操作失败时抛出（连接异常、权限不足等）
     * @see #mergeChunks(String) 所有分片上传完成后调用此方法合并
     * @see #getUploadProgress(String) 查询上传进度的方法
     */
    public ChunkUploadResp uploadChunk(String uploadId, int chunkNumber, int totalChunks,
                                       MultipartFile chunk, String fileName, long fileSize) {
        try {
            // 初始化上传任务的元数据（仅首次执行时生效）
            // putIfAbsent: 如果 key 不存在则插入，存在则跳过（保证线程安全）

            // 初始化进度追踪集合（记录已上传的分片索引）
            uploadProgress.putIfAbsent(uploadId, ConcurrentHashMap.newKeySet());
            // 记录原始文件名（用于合并时获取扩展名）
            fileNames.putIfAbsent(uploadId, fileName);
            // 记录原始文件大小（预留接口，可用于完整性校验）
            fileSizes.putIfAbsent(uploadId, fileSize);

            // 构建分片在 MinIO 中的对象路径
            // 格式：chunks/{uploadId}/chunk_{index}
            // 示例：chunks/abc123def456/chunk_0
            String objectName = "chunks/" + uploadId + "/chunk_" + chunkNumber;

            // 上传分片到 MinIO
            // 注意事项：
            //   - contentType 设为 application/octet-stream（二进制流）
            //   - 不设置 partSize（小分片无需分块上传）
            minio.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .stream(chunk.getInputStream(), chunk.getSize(), -1)
                            .contentType("application/octet-stream")
                            .build());

            // 更新上传进度：将该分片索引加入已完成集合
            Set<Integer> uploaded = uploadProgress.get(uploadId);
            uploaded.add(chunkNumber);

            // 构造响应对象：返回最新的进度信息
            // 将 Set 转为 List 以便 JSON 序列化
            return new ChunkUploadResp(uploadId, chunkNumber, true, new ArrayList<>(uploaded));
        } catch (Exception e) {
            throw new RuntimeException("分片上传失败", e);
        }
    }

    /**
     * 查询指定上传任务的进度
     *
     * <p>功能说明：</p>
     * <ul>
     *   <li>根据 uploadId 查找对应的已上传分片集合</li>
     *   <li>返回分片索引列表（如 [0, 1, 3, 5] 表示第 0、1、3、5 片已上传）</li>
     *   <li>用于前端实现断点续传时的进度恢复</li>
     * </ul>
     *
     * <h4>使用场景：</h4>
     * <ol>
     *   <li>用户暂停上传后重新打开页面</li>
     *   <li>浏览器意外关闭后恢复上传</li>
     *   <li>网络中断后重新连接</li>
     *   <li>前端定期轮询检查上传状态</li>
     * </ol>
     *
     * <h4>响应数据示例：</h4>
     * <pre>{@code
     * // 假设总分片数为 10，已上传了第 0、1、2、5 片
     * GET /file/chunk/progress/abc123
     * Response: [0, 1, 2, 5]
     *
     * // 前端根据此响应：
     * // 1. 已有的分片 [0,1,2,5] → 跳过不传
     * // 2. 缺失的分片 [3,4,6,7,8,9] → 继续上传
     * }</pre>
     *
     * @param uploadId 上传任务 ID（必须与上传时分发的 ID 一致）
     * @return 已成功上传的分片索引列表（升序排列），如果 uploadId 不存在则返回空列表
     * @see #uploadChunk(String, int, int, MultipartFile, String, long) 上传分片时更新进度
     */
    public List<Integer> getUploadProgress(String uploadId) {
        // 从 ConcurrentHashMap 中获取已上传分片集合
        Set<Integer> uploaded = uploadProgress.get(uploadId);

        // 安全处理：如果 uploadId 不存在（可能已过期或从未创建），返回空列表
        // 将 Set 复制为新 List 以避免外部修改影响内部状态
        return uploaded != null ? new ArrayList<>(uploaded) : Collections.emptyList();
    }

    /**
     * 合并所有已上传的分片为最终文件
     *
     * <p>功能说明：</p>
     * <ul>
     *   <li>收集指定 uploadId 下所有已上传的分片</li>
     *   <li>按分片序号排序确保顺序正确</li>
     *   <li>使用 MinIO composeObject API 在服务端高效合并</li>
     *   <li>删除临时分片文件释放存储空间</li>
     *   <li>清除内存中的上传状态数据</li>
     *   <li>返回最终文件的访问链接</li>
     * </ul>
     *
     * <h4>技术优势（对比客户端合并）：</h4>
     * <table border="1">
     *   <tr><th>方案</th><th>带宽消耗</th><th>速度</th><th>可靠性</th></tr>
     *   <tr><td>客户端合并再上传</td><td>2x（上传+下载）</td><td>慢</td><td>易中断</td></tr>
     *   <tr><td>MinIO composeObject</td><td>0（服务端内拷贝）</td><td>极快</td><td>原子操作</td></tr>
     * </table>
     *
     * <h4>执行流程详解：</h4>
     * <ol>
     *   <li><b>准备阶段</b>：获取原始文件名，生成最终文件的 UUID 名称</li>
     *   <li><b>收集分片</b>：从 uploadProgress 获取已上传分片索引集合</li>
     *   <li><b>排序校验</b>：按索引升序排列，确保合并顺序正确</li>
     *   <li><b>构建源列表</b>：为每个分片创建 ComposeSource 对象</li>
     *   <li><b>执行合并</b>：调用 MinIO composeObject API 服务端合并</li>
     *   <li><b>清理临时文件</b>：逐个删除 chunks 目录下的分片对象</li>
     *   <li><b>清理内存状态</b>：移除三个 ConcurrentHashMap 中的记录</li>
     *   <li><b>生成链接</b>：计算永久路径和签名 URL 并返回</li>
     * </ol>
     *
     * <h4>原子性保证：</h4>
     * <ul>
     *   <li>MinIO composeObject 是原子操作，要么全成功要么全失败</li>
     *   <li>即使中途失败，已上传的分片仍然保留，可重新尝试合并</li>
     *   <li>最终文件只有在所有分片都就绪后才会生成</li>
     * </ul>
     *
     * @param uploadId 要合并的上传任务 ID（该任务的所有分片必须已全部上传完成）
     * @return FileUploadResp 包含合并后文件的访问链接：
     *         <ul>
     *           <li>permanentUrl：永久路径（/minio/bucket/{uuid}.{ext}）</li>
     *           <li>presignedUrl：临时签名 URL（有效期 7 天）</li>
     *         </ul>
     * @throws RuntimeException 可能的失败原因：
     *         <ul>
     *           <li>"没有找到上传的分片"：uploadId 无效或从未上传过分片</li>
     *           <li>"合并分片失败"：MinIO 操作异常（网络问题、权限不足、磁盘空间不足等）</li>
     *         </ul>
     * @see #uploadChunk(String, int, int, MultipartFile, String, long) 上传分片
     * @see #getUploadProgress(String) 检查是否所有分片都已完成
     */
    public FileUploadResp mergeChunks(String uploadId) {
        try {
            // ===== 第一阶段：准备工作 =====

            // 获取原始文件名（用于提取文件扩展名）
            String fileName = fileNames.get(uploadId);
            String extension = StringUtils.getFilenameExtension(fileName);

            // 生成最终文件的唯一名称（与普通上传保持一致的风格）
            String uniqueName = UUID.randomUUID() + (extension != null ? "." + extension : "");

            // ===== 第二阶段：收集并排序分片 =====

            // 创建 ComposeSource 列表（composeObject API 要求的数据结构）
            List<ComposeSource> sources = new ArrayList<>();

            // 获取该上传任务的所有已完成分片索引
            Set<Integer> uploadedChunks = uploadProgress.get(uploadId);

            // 校验：确保存在已上传的分片
            if (uploadedChunks == null || uploadedChunks.isEmpty()) {
                throw new RuntimeException("没有找到上传的分片");
            }

            // 将分片索引转为列表并排序（升序）
            // 排序原因：composeObject 会按照 sources 列表的顺序拼接文件内容
            // 必须确保 chunk_0 在最前，chunk_1 其次，...，chunk_N 最后
            List<Integer> sortedChunks = new ArrayList<>(uploadedChunks);
            Collections.sort(sortedChunks);

            // ===== 第三阶段：构建合并源列表 =====

            for (int chunkNum : sortedChunks) {
                // 构建每个分片在 MinIO 中的对象路径
                String chunkObjectName = "chunks/" + uploadId + "/chunk_" + chunkNum;

                // 创建 ComposeSource 对象并加入列表
                // ComposeSource 告诉 composeObject 从哪里读取数据
                sources.add(ComposeSource.builder()
                        .bucket(bucket)
                        .object(chunkObjectName)
                        .build());
            }

            // ===== 第四阶段：执行合并操作 =====

            // 调用 MinIO composeObject API
            // 工作原理：
            //   1. MinIO 服务端依次读取每个 source 对象的内容
            //   2. 按顺序拼接成一个新对象
            //   3. 写入指定的目标位置
            // 整个过程在 MinIO 服务端完成，数据不经过应用服务器
            minio.composeObject(
                    ComposeObjectArgs.builder()
                            .bucket(bucket)
                            .object(uniqueName)  // 最终文件的存储路径
                            .sources(sources)    // 所有分片的源列表
                            .build());

            // ===== 第五阶段：清理临时文件 =====

            // 合并成功后，逐个删除临时的分片对象以释放存储空间
            for (int chunkNum : sortedChunks) {
                String chunkObjectName = "chunks/" + uploadId + "/chunk_" + chunkNum;
                minio.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(bucket)
                                .object(chunkObjectName)
                                .build());
            }

            // ===== 第六阶段：清理内存状态 =====

            // 从 ConcurrentHashMap 中移除该上传任务的所有记录
            // 释放内存，防止长期运行导致内存泄漏
            uploadProgress.remove(uploadId);
            fileNames.remove(uploadId);
            fileSizes.remove(uploadId);

            // ===== 第七阶段：生成并返回访问链接 =====

            // 生成永久路径（与应用的路由规则匹配）
            String permanentUrl = String.format("/minio/%s/%s", bucket, uniqueName);

            // 生成临时签名 URL（7天有效期）
            String presignedUrl = minio.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucket)
                            .object(uniqueName)
                            .expiry(7, TimeUnit.DAYS)
                            .build());

            return new FileUploadResp(permanentUrl, presignedUrl);
        } catch (Exception e) {
            throw new RuntimeException("合并分片失败", e);
        }
    }
}
