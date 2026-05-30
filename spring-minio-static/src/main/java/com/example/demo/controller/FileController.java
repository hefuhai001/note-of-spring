package com.example.demo.controller;

import com.example.demo.resp.ChunkUploadResp;
import com.example.demo.resp.FileUploadResp;
import com.example.demo.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件控制器（FileController）
 *
 * <p>REST API 接口层，负责接收前端 HTTP 请求并调用 FileService 处理业务逻辑</p>
 *
 * <h3>接口概览：</h3>
 * <table border="1">
 *   <tr>
 *     <th>方法</th>
 *     <th>路径</th>
 *     <th>功能</th>
 *     <th>Content-Type</th>
 *   </tr>
 *   <tr>
 *     <td>POST</td>
 *     <td>/file/upload</td>
 *     <td>普通文件上传（小文件）</td>
 *     <td>multipart/form-data</td>
 *   </tr>
 *   <tr>
 *     <td>GET</td>
 *     <td>/file/download/{fileName}</td>
 *     <td>文件下载</td>
 *     <td>-</td>
 *   </tr>
 *   <tr>
 *     <td>POST</td>
 *     <td>/file/chunk/upload</td>
 *     <td>分片上传（大文件）</td>
 *     <td>multipart/form-data</td>
 *   </tr>
 *   <tr>
 *     <td>GET</td>
 *     <td>/file/chunk/progress/{uploadId}</td>
 *     <td>查询上传进度</td>
 *     <td>-</td>
 *   </tr>
 *   <tr>
 *     <td>POST</td>
 *     <td>/file/chunk/merge</td>
 *     <td>合并分片</td>
 *     <td>application/x-www-form-urlencoded</td>
 *   </tr>
 * </table>
 *
 * <h3>使用场景：</h3>
 * <ul>
 *   <li><b>普通上传</b>：头像、文档、图片等小于 10MB 的文件</li>
 *   <li><b>分片上传</b>：视频、安装包、数据库备份等大文件（支持断点续传）</li>
 *   <li><b>文件下载</b>：提供安全的文件下载服务</li>
 * </ul>
 *
 * <h3>请求示例：</h3>
 * <pre>{@code
 * # 1. 普通上传
 * curl -X POST http://localhost:8081/file/upload \
 *   -F "file=@test.pdf"
 *
 * # 2. 分片上传
 * curl -X POST http://localhost:8081/file/chunk/upload \
 *   -F "uploadId=abc123" \
 *   -F "chunkNumber=0" \
 *   -F "totalChunks=10" \
 *   -F "chunk=@chunk_0.bin" \
 *   -F "fileName=bigfile.zip" \
 *   -F "fileSize=104857600"
 *
 * # 3. 查询进度
 * curl http://localhost:8081/file/chunk/progress/abc123
 *
 * # 4. 合并分片
 * curl -X POST "http://localhost:8081/file/chunk/merge?uploadId=abc123"
 *
 * # 5. 下载文件
 * curl -O http://localhost:8081/file/download/uuid-filename.pdf
 * }</pre>
 *
 * <h3>注意事项：</h3>
 * <ul>
 *   <li>所有接口基础路径为 /file（通过 @RequestMapping 配置）</li>
 *   <li>文件大小限制在 application.yml 中配置（默认 10MB）</li>
 *   <li>分片上传建议每片 5MB，通过前端控制</li>
 *   <li>生产环境建议添加：JWT 认证、请求频率限制、日志记录</li>
 * </ul>
 *
 * @author 何福海
 * @version 2.0.0（新增分片上传和断点续传接口）
 * @since 2025/8/1
 * @see com.example.demo.service.FileService 业务逻辑层
 * @see com.example.demo.resp.FileUploadResp 上传响应对象
 * @see com.example.demo.resp.ChunkUploadResp 分片上传响应对象
 */
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    /** 文件服务实例，由 Spring 自动注入（构造器注入） */
    private final FileService fileService;

    /**
     * 普通文件上传接口
     *
     * <p>适用于小文件直接上传（推荐 &lt; 10MB）</p>
     *
     * <h4>功能说明：</h4>
     * <ul>
     *   <li>接收 multipart/form-data 格式的文件数据</li>
     *   <li>调用 FileService.upload() 完成上传到 MinIO</li>
     *   <li>返回包含永久链接和临时签名 URL 的 JSON 响应</li>
     * </ul>
     *
     * <h4>请求格式：</h4>
     * <pre>
     * POST /file/upload
     * Content-Type: multipart/form-data
     *
     * 参数：
     *   - file (必填): 要上传的文件
     * </pre>
     *
     * <h4>响应示例：</h4>
     * <pre>{@code
     * {
     *   "permanentUrl": "/minio/files/a1b2c3d4-e5f6-7890-abcd-ef1234567890.pdf",
     *   "presignedUrl": "http://localhost:9000/files/a1b2c3d4...?X-Amz-Signature=xxx&X-Amz-Expires=604800"
     * }
     * }</pre>
     *
     * <h4>错误处理：</h4>
     * <ul>
     *   <li>400 Bad Request：文件为空或超过大小限制</li>
     *   <li>500 Internal Server Error：MinIO 连接失败或存储异常</li>
     * </ul>
     *
     * @param file 上传的文件对象（Spring 自动封装为 MultipartFile）
     * @return FileUploadResp 包含文件的访问路径信息
     * @see FileService#upload(org.springframework.web.multipart.MultipartFile) 服务层实现
     */
    // **上传**
    @PostMapping("/upload")
    public FileUploadResp upload(@RequestPart("file") MultipartFile file) {
        return fileService.upload(file);
    }

    /**
     * 文件下载接口
     *
     * <p>提供安全的文件下载服务，触发浏览器下载行为</p>
     *
     * <h4>功能说明：</h4>
     * <ul>
     *   <li>根据文件名从 MinIO 获取文件流</li>
     *   <li>设置 HTTP 响应头触发浏览器下载（而非在浏览器中打开）</li>
     *   <li>使用流式传输，支持大文件下载（不占用服务器内存）</li>
     * </ul>
     *
     * <h4>HTTP 响应头设置：</h4>
     * <ul>
     *   <li><b>Content-Disposition</b>: attachment; filename="xxx"<br>
     *       告知浏览器以附件形式下载，并指定默认保存的文件名</li>
     *   <li><b>Content-Type</b>: application/octet-stream<br>
     *       二进制流类型，浏览器不会尝试解析内容</li>
     * </ul>
     *
     * <h4>请求示例：</h4>
     * <pre>
     * GET /file/download/a1b2c3d4-e5f6-7890-abcd-ef1234567890.pdf
     *
     * 响应：二进制文件流 + 下载提示
     * </pre>
     *
     * <h4>安全考虑：</h4>
     * <ul>
     *   <li>当前版本未做权限校验（任何知道文件名的人都能下载）</li>
     *   <li>生产环境建议添加：用户认证、文件访问权限控制、下载次数限制</li>
     *   <li>可考虑使用 presigned URL 替代此接口（更灵活的权限控制）</li>
     * </ul>
     *
     * @param fileName 要下载的文件名（MinIO object name，通常是 UUID 格式）
     * @return ResponseEntity 包含文件流和正确的 HTTP 响应头，
     *         Spring MVC 会自动将 InputStreamResource 写入响应体
     * @see FileService#download(String) 服务层实现
     */
    // **下载**
    @GetMapping("/download/{fileName}")
    public ResponseEntity<InputStreamResource> download(@PathVariable String fileName) {
        // 从 MinIO 获取文件输入流
        InputStreamResource resource = fileService.download(fileName);

        // 构建响应实体，设置下载相关的 HTTP 头
        return ResponseEntity.ok()
                // 设置 Content-Disposition 头，触发浏览器"另存为"对话框
                // filename 参数指定默认保存的文件名
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\"")
                // 设置 MIME 类型为二进制流
                // 防止浏览器尝试渲染文件内容（如 PDF、图片等）
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                // 将文件流作为响应体返回
                .body(resource);
    }

    /**
     * 分片上传接口（大文件断点续传核心接口）
     *
     * <p>用于上传大文件的单个分片，是整个分片上传流程的核心 API</p>
     *
     * <h4>在整个流程中的位置：</h4>
     * <pre>
     * 前端切分文件 → 循环调用此接口（每次传一个分片） → 全部完成后调用 /chunk/merge
     * </pre>
     *
     * <h4>参数说明：</h4>
     * <table border="1">
     *   <tr><th>参数名</th><th>类型</th><th>必填</th><th>说明</th></tr>
     *   <tr><td>uploadId</td><td>String</td><td>是</td><td>上传任务唯一标识（前端生成）</td></tr>
     *   <tr><td>chunkNumber</td><td>int</td><td>是</td><td>当前分片索引（从 0 开始）</td></tr>
     *   <tr><td>totalChunks</td><td>int</td><td>是</td><td>总分片数</td></tr>
     *   <tr><td>chunk</td><td>MultipartFile</td><td>是</td><td>分片二进制数据</td></tr>
     *   <tr><td>fileName</td><td>String</td><td>是</td><td>原始文件名</td></tr>
     *   <tr><td>fileSize</td><td>long</td><td>是</td><td>原始文件总大小（字节）</td></tr>
     * </table>
     *
     * <h4>请求示例（curl）：</h4>
     * <pre>
     * POST /file/chunk/upload
     * Content-Type: multipart/form-data
     *
     * curl -X POST http://localhost:8081/file/chunk/upload \
     *   -F "uploadId=lz1v8xqk2m3n4o5p" \
     *   -F "chunkNumber=0" \
     *   -F "totalChunks=10" \
     *   -F "chunk=@chunk_0.dat" \
     *   -F "fileName=video.mp4" \
     *   -F "fileSize=104857600"
     * </pre>
     *
     * <h4>响应示例：</h4>
     * <pre>{@code
     * {
     *   "uploadId": "lz1v8xqk2m3n4o5p",
     *   "chunkNumber": 0,
     *   "success": true,
     *   "uploadedChunks": [0, 1, 2, 5]  // 当前已完成的分片列表（可能包含其他并发上传的分片）
     * }
     * }</pre>
     *
     * <h4>幂等性：</h4>
     * <ul>
     *   <li>同一分片重复上传会覆盖旧数据（支持网络超时重试）</li>
     *   <li>uploadedChunks 会实时反映最新的进度状态</li>
     * </ul>
     *
     * <h4>并发支持：</h4>
     * <ul>
     *   <li>多个分片可以同时上传（建议 3 个并发）</li>
     *   <li>服务端使用 ConcurrentHashMap 保证线程安全</li>
     *   <li>每个分片独立存储，互不影响</li>
     * </ul>
     *
     * @param uploadId     上传任务 ID（前端生成，用于关联同一文件的所有分片）
     * @param chunkNumber  当前分片的序号（从 0 开始，0 = 第一片）
     * @param totalChunks  该文件被切分的总片数
     * @param chunk        当前分片的二进制数据
     * @param fileName     原始文件名（保留扩展名用）
     * @param fileSize     原始文件的总字节数
     * @return ChunkUploadResp 包含上传结果和当前进度信息
     * @see FileService#uploadChunk(String, int, int, MultipartFile, String, long) 服务层实现
     * @see #getChunkUploadProgress(String) 查询进度的接口
     * @see #mergeChunks(String) 合并分片的接口
     */
    @PostMapping("/chunk/upload")
    public ChunkUploadResp uploadChunk(
            @RequestParam("uploadId") String uploadId,
            @RequestParam("chunkNumber") int chunkNumber,
            @RequestParam("totalChunks") int totalChunks,
            @RequestPart("chunk") MultipartFile chunk,
            @RequestParam("fileName") String fileName,
            @RequestParam("fileSize") long fileSize) {
        return fileService.uploadChunk(uploadId, chunkNumber, totalChunks, chunk, fileName, fileSize);
    }

    /**
     * 查询分片上传进度接口
     *
     * <p>用于实现断点续传功能，让前端了解哪些分片已经成功上传</p>
     *
     * <h4>使用场景：</h4>
     * <ol>
     *   <li><b>暂停后恢复</b>：用户点击"继续"按钮时，先查询已完成的分片</li>
     *   <li><b>页面刷新恢复</b>：浏览器意外关闭后重新打开，根据 uploadId 恢复进度</li>
     *   <li><b>网络中断恢复</b>：检测到网络恢复后，重新同步服务端状态</li>
     *   <li><b>状态轮询</b>：前端定时查询以更新 UI 进度条（可选方案）</li>
     * </ol>
     *
     * <h4>请求示例：</h4>
     * <pre>
     * GET /file/chunk/progress/lz1v8xqk2m3n4o5p
     *
     * Response (200 OK):
     * [0, 1, 2, 3, 5, 7]  // 表示第 0、1、2、3、5、7 片已完成
     * </pre>
     *
     * <h4>响应数据解读：</h4>
     * <ul>
     *   <li>返回值是一个整数数组，表示已成功上传的分片索引</li>
     *   <li>数组中的数字范围：[0, totalChunks-1]</li>
     *   <li>如果 uploadId 不存在或已过期，返回空数组 []</li>
     *   <li>前端据此判断：数组中存在的索引 → 跳过；缺失的索引 → 需要上传</li>
     * </ul>
     *
     * <h4>前端使用示例：</h4>
     * <pre>{@code
     * // Vue.js 示例
     * const response = await axios.get(`/file/chunk/progress/${this.uploadId}`);
     * const uploadedChunks = response.data;  // 例如 [0, 1, 2, 5]
     *
     * // 计算需要上传的分片
     * const chunksToUpload = [];
     * for (let i = 0; i < this.totalChunks; i++) {
     *   if (!uploadedChunks.includes(i)) {
     *     chunksToUpload.push(i);  // [3, 4, 6, 7, 8, 9] 这些需要重新上传
     *   }
     * }
     * }</pre>
     *
     * @param uploadId 上传任务 ID（必须与上传时使用的 ID 一致）
     * @return 已成功上传的分片索引列表（整数数组），按升序排列；
     *         如果该 uploadId 不存在则返回空列表
     * @see FileService#getUploadProgress(String) 服务层实现
     * @see #uploadChunk(String, int, int, MultipartFile, String, long) 上传分片接口
     */
    @GetMapping("/chunk/progress/{uploadId}")
    public List<Integer> getChunkUploadProgress(@PathVariable String uploadId) {
        return fileService.getUploadProgress(uploadId);
    }

    /**
     * 合并分片接口
     *
     * <p>在所有分片都上传完成后调用，触发服务端的合并操作</p>
     *
     * <h4>调用时机：</h4>
     * <ul>
     *   <li>前端检测到所有分片都已上传完成（completedChunks.length === totalChunks）</li>
     *   <li>通常在前端的上传循环结束后自动调用</li>
     *   <li>也可以手动触发（如用户点击"合并"按钮）</li>
     * </ul>
     *
     * <h4>执行过程（服务端）：</h4>
     * <ol>
     *   <li>验证 uploadId 有效且存在已上传的分片</li>
     *   <li>收集所有分片并按序号排序</li>
     *   <li>调用 MinIO composeObject API 在服务端高效合并</li>
     *   <li>删除临时的分片文件释放存储空间</li>
     *   <li>清理内存中的上传状态</li>
     *   <li>生成最终文件的访问链接并返回</li>
     * </ol>
     *
     * <h4>请求示例：</h4>
     * <pre>
     * POST /file/chunk/merge?uploadId=lz1v8xqk2m3n4o5p
     * Content-Type: application/x-www-form-urlencoded
     *
     * Response (200 OK):
     * {
     *   "permanentUrl": "/minio/files/final-uuid.mp4",
     *   "presignedUrl": "http://localhost:9000/files/final-uuid...?X-Amz-Signature=xxx"
     * }
     * </pre>
     *
     * <h4>性能特点：</h4>
     * <ul>
     *   <li><b>零带宽消耗</b>：合并操作完全在 MinIO 服务端完成</li>
     *   <li><b>极快速度</b>：通常毫秒级完成（取决于分片数量和大小）</li>
     *   <li><b>原子操作</b>：要么全部成功，要么全部失败</li>
     *   <li><b>自动清理</b>：合并成功后自动删除临时分片</li>
     * </ul>
     *
     * <h4>错误情况：</h4>
     * <ul>
     *   <li>400 Bad Request：uploadId 为空或不存在</li>
     *   <li>500 Internal Server Error：MinIO 合并失败、磁盘空间不足等</li>
     *   <li>注意：即使合并失败，已上传的分片仍然保留，可以重试</li>
     * </ul>
     *
     * @param uploadId 要合并的上传任务 ID（该任务的所有分片必须已全部上传完成）
     * @return FileUploadResp 包含合并后最终文件的访问链接
     * @see FileService#mergeChunks(String) 服务层实现（包含详细的合并算法说明）
     * @see #uploadChunk(String, int, int, MultipartFile, String, long) 上传分片接口
     * @see #getChunkUploadProgress(String) 检查是否所有分片都已完成
     */
    @PostMapping("/chunk/merge")
    public FileUploadResp mergeChunks(@RequestParam("uploadId") String uploadId) {
        return fileService.mergeChunks(uploadId);
    }
}
