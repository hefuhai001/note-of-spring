import axios from 'axios'
import qs from 'qs'

const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  },
  paramsSerializer: {
    serialize: (params) => qs.stringify(params, { arrayFormat: 'brackets' })
  }
})

request.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    console.log(`[Request] ${config.method?.toUpperCase()} ${config.url}`, config.data || config.params)
    return config
  },
  (error) => {
    console.error('[Request Error]', error)
    return Promise.reject(error)
  }
)

request.interceptors.response.use(
  (response) => {
    console.log(`[Response] ${response.config.method?.toUpperCase()} ${response.config.url}`, response.data)
    return response.data
  },
  (error) => {
    const { response } = error
    let message = '请求失败'

    if (response) {
      switch (response.status) {
        case 400:
          message = '请求参数错误'
          break
        case 401:
          message = '未授权，请重新登录'
          localStorage.removeItem('token')
          break
        case 403:
          message = '拒绝访问'
          break
        case 404:
          message = '请求地址不存在'
          break
        case 500:
          message = '服务器内部错误'
          break
        case 502:
          message = '网关错误'
          break
        case 503:
          message = '服务不可用'
          break
        case 504:
          message = '网关超时'
          break
        default:
          message = `请求失败: ${response.status}`
      }
    } else if (error.code === 'ECONNABORTED') {
      message = '请求超时'
    } else if (error.message.includes('Network Error')) {
      message = '网络错误，请检查网络连接'
    }

    console.error('[Response Error]', message, error)
    return Promise.reject(new Error(message))
  }
)

export const get = (url, params, config = {}) => {
  return request.get(url, { params, ...config })
}

export const post = (url, data, config = {}) => {
  return request.post(url, data, config)
}

export const put = (url, data, config = {}) => {
  return request.put(url, data, config)
}

export const del = (url, params, config = {}) => {
  return request.delete(url, { params, ...config })
}

export const postForm = (url, data, config = {}) => {
  return request.post(url, qs.stringify(data), {
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    ...config
  })
}

export default request
