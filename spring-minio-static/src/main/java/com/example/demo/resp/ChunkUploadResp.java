package com.example.demo.resp;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 分片上传响应对象（ChunkUploadResp）
 *
 * <p>封装分片上传接口（POST /file/chunk/upload）的返回数据</p>
 *
 * <h3>设计目的：</h3>
 * <ul>
 *   <li>告知前端当前分片是否上传成功</li>
 *   <li>返回最新的上传进度信息（所有已完成的分片索引）</li>
 *   <li>支持前端实时更新 UI 进度条</li>
 *   <li>为断点续传提供必要的状态同步机制</li>
 * </ul>
 *
 * <h3>在整体架构中的位置：</h3>
 * <pre>
 * 前端调用 /chunk/upload → 服务端处理 → 返回此对象 → 前端更新进度
 * </pre>
 *
 * <h3>JSON 序列化示例：</h3>
 * <pre>{@code
 * // 第一次上传第 0 片成功后：
 * {
 *   "uploadId": "lz1v8xqk2m3n4o5p",
 *   "chunkNumber": 0,
 *   "success": true,
 *   "uploadedChunks": [0]
 * }
 *
 * // 并发上传过程中（假设已上传了第 0、1、2、5 片，当前正在上传第 3 片）：
 * {
 *   "uploadId": "lz1v8xqk2m3n4o5p",
 *   "chunkNumber": 3,
 *   "success": true,
 *   "uploadedChunks": [0, 1, 2, 3, 5]  // 注意：包含其他并发上传完成的分片
 * }
 * }</pre>
 *
 * <h3>字段说明：</h3>
 * <table border="1">
 *   <tr>
 *     <th>字段名</th>
 *     <th>类型</th>
 *     <th>说明</th>
 *     <th>示例值</th>
 *   </tr>
 *   <tr>
 *     <td>uploadId</td>
 *     <td>String</td>
 *     <td>本次上传任务的唯一标识符</td>
 *     <td>"lz1v8xqk2m3n4o5p"</td>
 *   </tr>
 *   <tr>
 *     <td>chunkNumber</td>
 *     <td>int</td>
 *     <td>刚刚上传完成的分片序号</td>
 *     <td>0, 1, 2, ...</td>
 *   </tr>
 *   <tr>
 *     <td>success</td>
 *     <td>boolean</td>
 *     <td>本次分片上传是否成功</td>
 *     <td>true / false</td>
 *   </tr>
 *   <tr>
 *     <td>uploadedChunks</td>
 *     <td>List&lt;Integer&gt;</td>
 *     <td>当前所有已成功上传的分片索引列表（实时同步）</td>
 *     <td>[0, 1, 2, 5]</td>
 *   </tr>
 * </table>
 *
 * <h3>使用场景（前端）：</h3>
 * <pre>{@code
 * // 1. 更新进度条
 * const progress = (response.uploadedChunks.length / totalChunks) * 100;
 * progressBar.style.width = `${progress}%`;
 *
 * // 2. 判断是否全部完成
 * if (response.uploadedChunks.length === totalChunks) {
 *   await mergeChunks(uploadId);  // 调用合并接口
 * }
 *
 * // 3. 断点续传时恢复状态
 * if (isResuming) {
 *   completedChunks = response.uploadedChunks;  // 直接使用服务端的最新状态
 * }
 * }</pre>
 *
 * <h3>技术细节：</h3>
 * <ul>
 *   <li><b>线程安全</b>：uploadedChunks 来自服务端的 ConcurrentHashMap 快照，保证一致性</li>
 *   <li><b>实时性</b>：每次返回的都是服务端当前的完整状态（包含其他并发请求的结果）</li>
 *   <li><b>幂等性</b>：同一分片重复请求会得到相同的 success=true 响应</li>
 *   <li><b>Lombok 注解</b>：使用 @Data 自动生成 getter/setter/equals/hashCode/toString</li>
 *   <li><b>@AllArgsConstructor</b>：生成全参构造器，方便创建不可变对象</li>
 * </ul>
 *
 * @author 何福海
 * @version 1.0.0
 * @since 2025/8/1
 * @see com.example.demo.controller.FileController#uploadChunk(String, int, int, org.springframework.web.multipart.MultipartFile, String, long) 返回此对象的接口
 * @see com.example.demo.service.FileService#uploadChunk(String, int, int, org.springframework.web.multipart.MultipartFile, String, long) 构建此对象的业务逻辑
 * @see com.example.demo.resp.FileUploadResp 普通上传的响应对象（对比参考）
 */
@Data
@AllArgsConstructor
public class ChunkUploadResp {

    /**
     * 上传任务唯一标识符（Upload ID）
     *
     * <p>作用：</p>
     * <ul>
     *   <li>关联同一文件的所有分片（一个 uploadId 对应一次完整的上传任务）</li>
     *   <li>用于查询上传进度（GET /file/chunk/progress/{uploadId}）</li>
     *   <li>用于触发合并操作（POST /file/chunk/merge?uploadId=xxx）</li>
     *   <li>实现断点续传的关键标识（浏览器刷新后通过此 ID 恢复）</li>
     * </ul>
     *
     * <p>生成规则（前端）：</p>
     * <ul>
     *   <li>由前端在开始上传时生成（时间戳 + 随机数）</li>
     *   <li>格式示例："lz1v8xqk2m3n4o5p"（36进制编码）</li>
     *   <li>要求全局唯一（避免不同文件的上传任务冲突）</li>
     * </ul>
     *
     * <p>生命周期：</p>
     * <ol>
     *   <li>前端生成并随第一个分片一起发送到服务端</li>
     *   <li>服务端在整个上传过程中持续跟踪此 ID 的状态</li>
     *   <li>合并完成后，服务端清除与此 ID 相关的所有内存数据</li>
     *   <li>ID 失效后再次查询将返回空列表</li>
     * </ol>
     */
    private String uploadId;

    /**
     * 当前分片的序号（Chunk Number）
     *
     * <p>说明：</p>
     * <ul>
     *   <li>表示刚刚完成上传的分片索引（从 0 开始计数）</li>
     *   <li>0 = 第一个分片，1 = 第二个分片，...，N-1 = 最后一个分片</li>
     *   <li>用于前端确认哪个分片刚处理完毕</li>
     * </ul>
     *
     * <p>与 totalChunks 的关系：</p>
     * <ul>
     *   <li>有效范围：[0, totalChunks-1]</li>
     *   <li>例如总分片数为 10，则 chunkNumber 范围是 0-9</li>
     *   <li>超出范围的服务端应返回 400 错误</li>
     * </ul>
     *
     * <p>前端用途：</p>
     * <ul>
     *   <li>日志记录：记录每个分片的上传时间</li>
     *   <li>错误追踪：定位失败的是哪个分片</li>
     *   <li>UI 反馈：可选择性显示"第 X/Y 片已完成"</li>
     * </ul>
     */
    private int chunkNumber;

    /**
     * 本次分片上传是否成功（Success Flag）
     *
     * <p>可能的值：</p>
     * <ul>
     *   <li><b>true</b>：分片成功保存到 MinIO，进度已更新</li>
     *   <li><b>false</b>：理论上不应出现（失败时会抛异常返回 500）</li>
     * </ul>
     *
     * <p>设计考量：</p>
     * <ul>
     *   <li>提供显式的成功标志，便于前端统一处理响应</li>
     *   <li>即使成功也应检查 uploadedChunks 确认进度更新</li>
     *   <li>建议前端同时判断 success == true && uploadedChunks 包含 chunkNumber</li>
     * </ul>
     *
     * <p>前端处理逻辑：</p>
     * <pre>{@code
     * if (response.success) {
     *   // 成功：更新本地进度缓存
     *   this.completedChunks = response.uploadedChunks;
     *   this.updateProgressBar();
     * } else {
     *   // 失败：重试或提示用户
     *   this.retryChunk(response.chunkNumber);
     * }
     * }</pre>
     */
    private boolean success;

    /**
     * 已成功上传的分片索引列表（Uploaded Chunks List）
     *
     * <p>核心作用：</p>
     * <ul>
     *   <li><b>进度同步</b>：反映服务端当前的真实状态（包含所有并发上传的结果）</li>
     *   <li><b>UI 更新</b>：前端据此计算并渲染进度条百分比</li>
     *   <li><b>断点续传</b>：暂停恢复时直接使用此列表初始化本地状态</li>
     *   <li><b>完整性校验</b>：前端可检查是否所有分片都已完成</li>
     * </ul>
     *
     * <p>数据特征：</p>
     * <ul>
     *   <li><b>类型</b>：List&lt;Integer&gt;（JSON 序列化为数组）</li>
     *   <li><b>内容</b>：已成功的分片索引（如 [0, 1, 2, 5] 表示第 0、1、2、5 片已完成）</li>
     *   <li><b>顺序</b>：不保证有序（基于 Set 转换），前端如需排序请自行处理</li>
     *   <li><b>实时性</b>：每次请求都返回服务端最新快照</li>
     *   <li><b>线程安全</b>：来自 ConcurrentHashMap 的防御性拷贝，不会被外部修改</li>
     * </ul>
     *
     * <p>典型场景示例：</p>
     * <pre>{@code
     * 场景1：刚开始上传（只有第 0 片完成）
     * uploadedChunks: [0]
     * 进度: 1/10 = 10%
     *
     * 场景2：并发上传中（第 0、1、2、5 片已完成，3、4 正在上传）
     * uploadedChunks: [0, 1, 2, 5]
     * 进度: 4/10 = 40%
     *
     * 场景3：即将完成（只差最后一片）
     * uploadedChunks: [0, 1, 2, 3, 4, 5, 6, 7, 8]
     * 进度: 9/10 = 90%
     * → 前端检测到还差第 9 片，上传完成后调用合并接口
     *
     * 场景4：全部完成（合并前的最终状态）
     * uploadedChunks: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
     * 进度: 10/10 = 100%
     * → 前端立即调用 POST /file/chunk/merge
     * }</pre>
     *
     * <p>前端最佳实践：</p>
     * <pre>{@code
     * // 1. 接收响应后直接替换本地缓存（不要增量更新）
     * this.completedChunks = response.uploadedChunks;  // 使用服务端的权威数据
     *
     * // 2. 计算进度
     * const progress = (this.completedChunks.length / this.totalChunks) * 100;
     *
     * // 3. 判断是否需要合并
     * if (this.completedChunks.length === this.totalChunks) {
     *   await this.mergeChunks();  // 所有分片就绪，触发合并
     * }
     *
     * // 4. 断点续传恢复时
     * const progressResp = await axios.get(`/progress/${this.uploadId}`);
     * this.completedChunks = progressResp.data;  // 从服务端恢复完整状态
     * }</pre>
     */
    private List<Integer> uploadedChunks;
}
