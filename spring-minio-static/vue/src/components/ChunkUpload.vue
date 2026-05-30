<!--
  组件名称：ChunkUpload（大文件分片上传组件）
  功能描述：
    - 支持大文件分片上传（默认每片 5MB）
    - 支持断点续传（暂停/继续功能）
    - 实时显示上传进度条
    - 并发控制（最多 3 个分片同时上传）
    - 上传完成后返回文件的永久链接和临时签名链接

  使用示例：
    <template>
      <ChunkUpload />
    </template>

    <script setup>
    import ChunkUpload from './ChunkUpload.vue'
    </script>

  API 接口依赖：
    - POST /file/chunk/upload        上传单个分片
    - GET  /file/chunk/progress/{id}  获取已上传的分片列表
    - POST /file/chunk/merge          合并所有分片

  作者：AI Assistant
  版本：1.0.0
  更新日期：2026-05-30
-->
<template>
  <!-- 主容器：包含整个上传组件的所有元素 -->
  <div class="chunk-upload-container">
    <!-- 标题区域 -->
    <h2>大文件分片上传（支持断点续传）</h2>

    <!--
      上传控制区域
      包含：文件选择框、开始上传按钮、暂停按钮、继续按钮
    -->
    <div class="upload-area">
      <!-- 文件输入框：用于选择要上传的文件 -->
      <input
          type="file"
          ref="fileInput"
          @change="handleFileSelect"
          :disabled="uploading"
      />
      <!-- 开始上传按钮：点击后开始分片上传流程 -->
      <button @click="startUpload" :disabled="!selectedFile || uploading">
        {{ uploading ? '上传中...' : '开始上传' }}
      </button>
      <!-- 暂停按钮：仅在上传进行中时显示，点击后暂停上传 -->
      <button @click="pauseUpload" :disabled="!uploading" v-if="uploading">暂停</button>
      <!-- 继续按钮：仅在暂停状态时显示，点击后恢复上传（断点续传） -->
      <button @click="resumeUpload" :disabled="uploading || !paused" v-if="paused">继续</button>
    </div>

    <!--
      文件信息展示区域
      仅在选择文件后显示，展示文件的基本信息和分片信息
    -->
    <div v-if="selectedFile" class="file-info">
      <p>文件名: {{ selectedFile.name }}</p>
      <p>文件大小: {{ formatFileSize(selectedFile.size) }}</p>
      <p>分片大小: {{ formatFileSize(chunkSize) }}</p>
      <p>总分片数: {{ totalChunks }}</p>
    </div>

    <!--
      进度展示区域
      仅在正在上传或已有上传进度时显示
      包含：进度条、百分比文字、已完成分片数统计
    -->
    <div v-if="uploading || uploadProgress > 0" class="progress-container">
      <!-- 进度条容器 -->
      <div class="progress-bar">
        <!-- 进度填充条：宽度根据 uploadProgress 动态计算 -->
        <div
            class="progress-fill"
            :style="{ width: uploadProgress + '%' }"
        ></div>
      </div>
      <!-- 进度百分比文本 -->
      <p class="progress-text">{{ uploadProgress.toFixed(2) }}%</p>
      <!-- 分片完成情况统计 -->
      <p class="chunks-info">
        已完成: {{ completedChunks.length }} / {{ totalChunks }} 分片
      </p>
    </div>

    <!--
      上传结果展示区域
      仅在上传成功并合并完成后显示
      展示服务器返回的文件访问链接
    -->
    <div v-if="uploadResult" class="result">
      <h3>上传成功！</h3>
      <!-- 永久链接：由前端拼接的路径，不会过期，适合后台管理系统使用 -->
      <p>永久链接: {{ uploadResult.permanentUrl }}</p>
      <!-- 临时签名链接：MinIO 生成的预签名 URL，有有效期限制（默认7天），适合直接访问 -->
      <p>
        临时链接:
        <a :href="uploadResult.presignedUrl" target="_blank">{{
            uploadResult.presignedUrl
          }}</a>
      </p>
    </div>
  </div>
</template>

<script setup>
/**
 * ChunkUpload 组件逻辑部分
 * 使用 Vue 3 Composition API (script setup) 编写
 */
import {ref, computed} from 'vue'
import {fileApi} from '@/api/file'

// ==================== 响应式状态定义 ====================

/** @type {Ref<HTMLElement|null>} 文件输入框的 DOM 引用 */
const fileInput = ref(null)

/** @type {Ref<File|null>} 当前选择的文件对象 */
const selectedFile = ref(null)

/** @type {Ref<boolean>} 是否正在上传中 */
const uploading = ref(false)

/** @type {Ref<boolean>} 是否处于暂停状态（用于断点续传） */
const paused = ref(false)

/** @type {Ref<string>} 本次上传的唯一标识符（用于关联所有分片） */
const uploadId = ref('')

/** @type {Ref<number[]>} 已成功上传的分片索引列表 */
const completedChunks = ref([])

/** @type {Ref<Object|null>} 上传成功后服务器返回的结果数据 */
const uploadResult = ref(null)

// ==================== 配置常量 ====================

/**
 * 分片大小配置
 * 默认值：5MB (5 * 1024 * 1024 bytes)
 * 可根据实际需求调整：
 *   - 较小的值：更适合网络不稳定环境，但请求次数多
 *   - 较大的值：减少请求次数，但单次失败重传成本高
 */
const chunkSize = 5 * 1024 * 1024

// ==================== 计算属性 ====================

/**
 * 计算总分片数
 * 使用 Math.ceil 向上取整，确保最后一片也能被计算在内
 *
 * 示例：
 *   文件大小 12MB，分片大小 5MB → 总分片数 = ceil(12/5) = 3 片
 */
const totalChunks = computed(() => {
  if (!selectedFile.value) return 0
  return Math.ceil(selectedFile.value.size / chunkSize)
})

/**
 * 计算当前上传进度百分比
 * 公式：(已完成分片数 / 总分片数) * 100
 * 返回值保留两位小数，用于精确展示进度
 */
const uploadProgress = computed(() => {
  if (!selectedFile.value || totalChunks.value === 0) return 0
  return (completedChunks.value.length / totalChunks.value) * 100
})

// ==================== 事件处理函数 ====================

/**
 * 处理文件选择事件
 * 当用户通过 input[type=file] 选择文件时触发
 *
 * @param {Event} event - 原生 DOM 事件对象
 *
 * 功能：
 *   1. 从事件中获取用户选择的第一个文件
 *   2. 重置上传状态（清除之前的上传记录）
 *   3. 将选中的文件保存到 selectedFile 状态中
 */
function handleFileSelect(event) {
  const file = event.target.files[0]
  if (file) {
    selectedFile.value = file
    resetUploadState()
  }
}

/**
 * 重置上传状态到初始值
 * 在以下场景调用：
 *   - 用户选择了新文件
 *   - 开始新的上传任务前
 *
 * 清理内容：
 *   - uploadId：清空上传标识
 *   - completedChunks：清空已完成分片列表
 *   - uploadResult：清空上次的上传结果
 *   - uploading/paused：重置为未上传/未暂停状态
 */
function resetUploadState() {
  uploadId.value = ''
  completedChunks.value = []
  uploadResult.value = null
  uploading.value = false
  paused.value = false
}

// ==================== 核心业务函数 ====================

/**
 * 开始上传流程
 * 点击"开始上传"按钮时触发
 *
 * 执行步骤：
 *   1. 校验是否已选择文件
 *   2. 重置所有上传状态
 *   3. 设置 uploading 为 true（启用加载状态）
 *   4. 生成唯一的 uploadId（用于服务端识别本次上传任务）
 *   5. 调用 uploadChunks() 开始分片上传
 *
 * @async
 * @returns {Promise<void>}
 */
async function startUpload() {
  if (!selectedFile.value) return

  resetUploadState()
  uploading.value = true
  uploadId.value = generateUploadId()

  await uploadChunks()
}

/**
 * 暂停当前上传任务
 * 点击"暂停"按钮时触发
 *
 * 实现原理：
 *   - 设置 paused = true 标记暂停状态
 *   - 设置 uploading = false 停止 UI 加载效果
 *   - uploadChunks() 函数会检测 paused 状态并停止发送新请求
 *   - 已发出的请求会继续执行完毕（不会中断）
 *
 * 断点续传基础：
 *   - 服务端已保存已完成的分片信息（通过 uploadId 关联）
 *   - 下次继续时可查询进度，只上传缺失的分片
 *
 * @async
 * @returns {Promise<void>}
 */
async function pauseUpload() {
  paused.value = true
  uploading.value = false
}

/**
 * 恢复已暂停的上传任务（断点续传核心功能）
 * 点击"继续"按钮时触发
 *
 * 执行步骤：
 *   1. 校验是否存在有效的 uploadId
 *   2. 向服务端请求该 uploadId 的已上传进度
 *   3. 用服务端返回的数据更新本地 completedChunks 列表
 *   4. 重置暂停状态，恢复上传
 *   5. 调用 uploadChunks() 只上传缺失的分片
 *
 * 技术优势：
 *   - 避免重复上传已完成的分片，节省带宽和时间
 *   - 即使浏览器刷新或关闭后重新打开，只要记住 uploadId 即可续传
 *
 * @async
 * @returns {Promise<void>}
 * @throws {Error} 当获取进度失败时输出错误日志
 */
async function resumeUpload() {
  if (!uploadId.value) return

  try {
    // 调用服务端接口获取已上传的分片列表
    const response = await fileApi.getProgress(uploadId.value)
    // 更新本地的已完成分片列表（从服务端同步最新状态）
    completedChunks.value = response
  } catch (error) {
    console.error('获取上传进度失败:', error)
  }

  // 恢复上传状态
  paused.value = false
  uploading.value = true
  // 重新开始上传流程（会自动跳过已完成的分片）
  await uploadChunks()
}

/**
 * 分片上传主控函数
 * 负责管理所有分片的并发上传流程
 *
 * 核心算法：并发池模式（Concurrency Pool Pattern）
 *   - 维护一个正在执行的 Promise 集合（executing Set）
 *   - 限制同时进行的请求数量（concurrencyLimit = 3）
 *   - 当某个请求完成时，自动启动下一个待上传的分片
 *
 * 执行流程：
 *   1. 遍历所有分片，过滤掉已完成的分片
 *   2. 对每个待上传分片创建上传 Promise
 *   3. 控制并发数量不超过 concurrencyLimit
 *   4. 使用 Promise.race() 等待任意一个完成后再启动新的
 *   5. 所有分片上传完成后，检查是否需要合并
 *
 * @async
 * @returns {Promise<void>}
 */
async function uploadChunks() {
  const file = selectedFile.value
  const chunks = []

  /**
   * 第一步：生成待上传的分片列表
   * 跳过已在 completedChunks 中的分片（支持断点续传）
   */
  for (let i = 0; i < totalChunks.value; i++) {
    // 如果该分片已经上传过，则跳过（断点续传优化）
    if (completedChunks.value.includes(i)) continue

    // 计算当前分片在文件中的起始和结束位置
    const start = i * chunkSize
    const end = Math.min(start + chunkSize, file.size)

    // 使用 Blob.slice() 方法切割文件（不会占用额外内存）
    const chunk = file.slice(start, end)

    chunks.push({
      index: i,
      data: chunk,
    })
  }

  /**
   * 第二步：并发控制上传
   * 使用 Set 来跟踪正在执行的 Promise
   * 当 Set 大小达到上限时，等待其中一个完成再继续
   */

  /** @type {number} 最大并发数：同时最多上传 3 个分片 */
  const concurrencyLimit = 3

  /** @type {Set<Promise>} 正在执行的 Promise 集合 */
  const executing = new Set()

  for (const chunk of chunks) {
    // 如果处于暂停状态，立即停止发送新请求
    if (paused.value) break

    /**
     * 创建单个分片的上传 Promise
     * 上传完成后从 executing 集合中移除该 Promise
     */
    const promise = uploadSingleChunk(chunk).then(() => {
      executing.delete(promise)
    })

    // 将 Promise 加入执行集合
    executing.add(promise)

    /**
     * 并发控制关键逻辑：
     * 当正在执行的 Promise 数量达到上限时，
     * 使用 Promise.race() 等待其中任意一个完成，
     * 完成后会释放一个位置，循环可以继续添加新的 Promise
     */
    if (executing.size >= concurrencyLimit) {
      await Promise.race(executing)
    }
  }

  // 等待所有剩余的 Promise 全部完成
  await Promise.all(executing)

  /**
   * 第三步：检查是否需要合并分片
   * 条件：
   *   1. 不处于暂停状态
   *   2. 所有分片都已上传完成
   */
  if (!paused.value && completedChunks.value.length === totalChunks.value) {
    await mergeChunks()
  }
}

/**
 * 上传单个分片到服务端
 *
 * @param {Object} chunk - 分片对象
 * @param {number} chunk.index - 分片索引号（从 0 开始）
 * @param {Blob} chunk.data - 分片的二进制数据
 *
 * 请求参数说明：
 *   - uploadId: 本次上传任务的唯一标识
 *   - chunkNumber: 当前分片的序号（用于服务端排序）
 *   - totalChunks: 总分片数（服务端可用于校验完整性）
 *   - chunk: 分片文件数据（MultipartFile 类型）
 *   - fileName: 原始文件名（用于合并后命名）
 *   - fileSize: 原始文件大小（用于校验）
 *
 * 成功响应：
 *   - success: boolean 是否成功
 *   - uploadedChunks: number[] 服务端记录的所有已完成分片列表
 *
 * @async
 * @returns {Promise<void>}
 * @throws {Error} 上传失败时抛出错误，中断后续流程
 */
async function uploadSingleChunk(chunk) {
  // 构建 FormData 表单数据（multipart/form-data 格式）
  const formData = new FormData()
  formData.append('uploadId', uploadId.value)
  formData.append('chunkNumber', chunk.index)
  formData.append('totalChunks', totalChunks.value)
  formData.append('chunk', chunk.data)
  formData.append('fileName', selectedFile.value.name)
  formData.append('fileSize', selectedFile.value.size)

  try {
    // 发送 POST 请求到服务端的分片上传接口
    const response = await fileApi.uploadChunk(formData)

    // 上传成功后，更新本地的已完成分片列表（与服务端保持同步）
    if (response.success) {
      completedChunks.value = response.uploadedChunks
    }
  } catch (error) {
    console.error(`分片 ${chunk.index} 上传失败:`, error)
    throw error
  }
}

/**
 * 合并所有已上传的分片
 * 在所有分片都上传完成后自动调用
 *
 * 执行过程：
 *   1. 向服务端发送合并请求（附带 uploadId）
 *   2. 服务端接收请求后：
 *      a. 根据 uploadId 查找所有已上传的分片
 *      b. 按分片序号排序
 *      c. 使用 MinIO 的 composeObject API 合并分片
 *      d. 删除临时的分片文件
 *      e. 返回最终文件的访问链接
 *   3. 前端接收结果并更新 UI 显示
 *
 * 返回数据结构（FileUploadResp）：
 *   - permanentUrl: string 永久访问路径（如 /minio/files/xxx.pdf）
 *   - presignedUrl: string MinIO 签名的临时 URL（7天有效）
 *
 * @async
 * @returns {Promise<void>}
 * @throws {Error} 合并失败时抛出错误
 */
async function mergeChunks() {
  try {
    // 发送 POST 请求触发服务端合并操作
    const response = await fileApi.mergeChunks(uploadId.value)

    // 保存上传结果，用于在界面上显示链接
    uploadResult.value = response
    // 结束上传状态
    uploading.value = false
  } catch (error) {
    console.error('合并分片失败:', error)
    throw error
  }
}

// ==================== 工具函数 ====================

/**
 * 生成唯一的上传 ID
 * 用于标识一次完整的大文件上传任务
 *
 * 生成规则：
 *   - 时间戳（36进制） + 随机数（36进制）
 *   - 示例："lz1v8xqk2m3n4o5p"
 *
 * 设计考量：
 *   - 唯一性：时间戳 + 随机数确保几乎不可能重复
 *   - 可读性：使用 36 进制使字符串更短
 *   - 无序性：随机数部分防止 ID 被猜测
 *
 * 应用场景：
 *   - 服务端根据此 ID 关联同一文件的所有分片
 *   - 断点续传时通过此 ID 查询已上传的进度
 *   - 最终合并时告诉服务端要合并哪些分片
 *
 * @returns {string} 唯一的上传标识符
 */
function generateUploadId() {
  return (
      Date.now().toString(36) + Math.random().toString(36).substr(2)
  )
}

/**
 * 格式化文件大小为人类可读的字符串
 *
 * 转换规则：
 *   - Bytes (< 1024B)
 *   - KB (1024B - 1MB)
 *   - MB (1MB - 1GB)
 *   - GB (> 1GB)
 *
 * 算法说明：
 *   - 使用对数计算确定合适的单位（Math.log(bytes) / Math.log(1024)）
 *   - 保留 2 位小数以提高可读性
 *
 * 示例：
 *   - formatFileSize(0) → "0 Bytes"
 *   - formatFileSize(1024) → "1 KB"
 *   - formatFileSize(1048576) → "1 MB"
 *   - formatFileSize(5242880) → "5 MB"
 *
 * @param {number} bytes - 字节数
 * @returns {string} 格式化后的文件大小字符串
 */
function formatFileSize(bytes) {
  if (bytes === 0) return '0 Bytes'
  const k = 1024
  const sizes = ['Bytes', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}
</script>

<style scoped>
/*
  组件样式说明
  使用 scoped 确保样式仅作用于当前组件
  采用 BEM 命名规范的变体风格
*/

/* ========== 主容器样式 ========== */
.chunk-upload-container {
  max-width: 800px;
  margin: 20px auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
}

/* ========== 上传控制区域样式 ========== */
.upload-area {
  display: flex;
  gap: 10px;
  align-items: center;
  margin-bottom: 20px;
}

/* 文件选择框：占据剩余空间 */
.upload-area input[type='file'] {
  flex: 1;
}

/* 按钮通用样式 */
.upload-area button {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  background-color: #409eff;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

/* 悬停效果：仅在非禁用状态下生效 */
.upload-area button:hover:not(:disabled) {
  background-color: #66b1ff;
}

/* 禁用状态样式：降低视觉优先级，提示不可交互 */
.upload-area button:disabled {
  background-color: #c0c4cc;
  cursor: not-allowed;
}

/* ========== 文件信息展示区域样式 ========== */
.file-info {
  background-color: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.file-info p {
  margin: 5px 0;
  color: #606266;
}

/* ========== 进度展示区域样式 ========== */
.progress-container {
  margin: 20px 0;
}

/* 进度条外框：灰色背景作为轨道 */
.progress-bar {
  width: 100%;
  height: 20px;
  background-color: #ebeef5;
  border-radius: 10px;
  overflow: hidden;
  margin-bottom: 10px;
}

/* 进度填充条：绿色表示正常进行中 */
.progress-fill {
  height: 100%;
  background-color: #67c23a;
  transition: width 0.3s ease;
}

/* 进度百分比文本：居中加粗显示 */
.progress-text {
  text-align: center;
  font-weight: bold;
  color: #606266;
}

/* 分片统计信息：次要文本样式 */
.chunks-info {
  text-align: center;
  color: #909399;
  font-size: 14px;
}

/* ========== 上传结果展示区域样式 ========== */
.result {
  margin-top: 20px;
  padding: 15px;
  background-color: #f0f9eb;
  border: 1px solid #e1f3d8;
  border-radius: 4px;
}

/* 成功标题：绿色强调 */
.result h3 {
  color: #67c23a;
  margin-top: 0;
}

/* 结果文本：适当的行间距 */
.result p {
  margin: 8px 0;
  word-break: break-all;
}

/* 链接样式：蓝色可点击 */
.result a {
  color: #409eff;
  text-decoration: none;
}

/* 链接悬停效果：显示下划线提示可点击 */
.result a:hover {
  text-decoration: underline;
}
</style>
