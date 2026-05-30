import request from '@/utils/request'

export const fileApi = {
  uploadChunk(formData) {
    return request.upload('/file/chunk/upload', formData)
  },

  getProgress(uploadId) {
    return request.get(`/file/chunk/progress/${uploadId}`)
  },

  mergeChunks(uploadId) {
    return request.post('/file/chunk/merge', null, { params: { uploadId } })
  }
}
