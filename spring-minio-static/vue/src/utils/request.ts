import axios, {type AxiosInstance, type AxiosProgressEvent, type AxiosResponse} from 'axios'

const service: AxiosInstance = axios.create({
    baseURL: '/api',
    timeout: 30000,
})

service.interceptors.request.use((config) => {
    const token = localStorage.getItem('token')
    if (token && config.headers) {
        config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
})

service.interceptors.response.use(
    (response: AxiosResponse) => {
        const {data} = response
        if (response.config.responseType === 'blob' || response.config.responseType === 'arraybuffer') {
            return data
        }
        if (data?.code !== undefined) {
            if (data.code === 200 || data.code === 0) return data.data
            alert(data.message || '请求失败')
            return Promise.reject(new Error(data.message || '请求失败'))
        }
        return data
    },
    (error) => {
        if (axios.isCancel(error)) return Promise.reject(error)

        let msg = '网络异常，请稍后重试'
        const {response} = error

        if (response) {
            const map: Record<number, string> = {
                400: '请求参数错误', 401: '未授权，请重新登录',
                403: '拒绝访问', 404: '请求资源不存在',
                405: '请求方法不允许', 408: '请求超时',
                500: '服务器内部错误', 502: '网关错误',
                503: '服务不可用', 504: '网关超时',
            }
            msg = response.data?.message || map[response.status] || `请求失败 (${response.status})`
            if (response.status === 401) {
                localStorage.removeItem('token')
                window.location.href = '/login'
            }
        } else if (error.code === 'ECONNABORTED') {
            msg = '请求超时，请检查网络连接'
        }

        alert(msg)
        console.error('API Error:', {url: error.config?.url, status: response?.status, message: msg})
        return Promise.reject(new Error(msg))
    }
)

const request = {
    get<T = any>(url: string, params?: any): Promise<T> {
        return service.get(url, {params})
    },
    post<T = any>(url: string, data?: any, config?: any): Promise<T> {
        return service.post(url, data, config)
    },
    put<T = any>(url: string, data?: any): Promise<T> {
        return service.put(url, data)
    },
    delete<T = any>(url: string, params?: any): Promise<T> {
        return service.delete(url, {params})
    },
    upload<T = any>(url: string, formData: FormData, onProgress?: (e: AxiosProgressEvent) => void): Promise<T> {
        return service.post(url, formData, {
            headers: {'Content-Type': 'multipart/form-data'},
            timeout: 60000,
            onUploadProgress: onProgress,
        })
    },
    download(url: string, params?: any, filename?: string): Promise<Blob> {
        return service.get(url, {params, responseType: 'blob'}).then((res: AxiosResponse<Blob>) => {
            const blob = res.data
            if (filename) {
                const a = document.createElement('a')
                a.href = URL.createObjectURL(blob)
                a.download = filename
                a.click()
                URL.revokeObjectURL(a.href)
            }
            return blob
        })
    },
}

export default request
