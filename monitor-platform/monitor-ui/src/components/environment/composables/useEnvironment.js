// composables/useEnvironment.js
import {ref} from 'vue'
import {ElMessage} from 'element-plus'
import axios from 'axios'

// 全局状态，避免重复请求
const systemProperties = ref([])
const environmentVars = ref([])
const appConfigs = ref([])
const jvmInfo = ref({})
const classLoadingInfo = ref({})

// 刷新事件总线
const refreshEvent = new EventTarget()

export const useEnvironmentRefresh = () => {
    const refreshAll = async () => {
        try {
            // 触发所有子组件刷新
            refreshEvent.dispatchEvent(new CustomEvent('refresh'))
            ElMessage.success('数据刷新成功')
        } catch (error) {
            console.error('刷新数据失败:', error)
            ElMessage.error('刷新数据失败')
        }
    }

    return {refreshAll}
}

export const useSystemProperties = () => {
    const data = ref([])
    const loading = ref(false)

    const fetchData = async () => {
        loading.value = true
        try {
            const response = await axios.get('/actuator/env')
            const properties = []

            if (response.data.propertySources) {
                response.data.propertySources.forEach(source => {
                    if (source.properties) {
                        Object.entries(source.properties).forEach(([key, value]) => {
                            if (key.includes('java.') || key.includes('os.') || key.includes('user.')) {
                                properties.push({
                                    key: key,
                                    value: value.value || value
                                })
                            }
                        })
                    }
                })
            }

            data.value = properties.slice(0, 50)
        } catch (error) {
            console.error('获取系统属性失败:', error)
        } finally {
            loading.value = false
        }
    }

    // 监听刷新事件
    refreshEvent.addEventListener('refresh', fetchData)

    return {data, loading, fetchData}
}

export const useEnvironmentVars = () => {
    const data = ref([])
    const loading = ref(false)

    const fetchData = async () => {
        loading.value = true
        try {
            const response = await axios.get('/actuator/env')
            const envVars = []

            if (response.data.propertySources) {
                const systemEnv = response.data.propertySources.find(s => s.name === 'systemEnvironment')
                if (systemEnv && systemEnv.properties) {
                    Object.entries(systemEnv.properties).forEach(([key, value]) => {
                        envVars.push({
                            key: key,
                            value: value.value
                        })
                    })
                }
            }

            data.value = envVars.slice(0, 50)
        } catch (error) {
            console.error('获取环境变量失败:', error)
        } finally {
            loading.value = false
        }
    }

    refreshEvent.addEventListener('refresh', fetchData)

    return {data, loading, fetchData}
}

export const useAppConfigs = () => {
    const data = ref([])
    const loading = ref(false)

    const fetchData = async () => {
        loading.value = true
        try {
            const response = await axios.get('/actuator/configprops')
            const configs = []

            if (response.data.contexts) {
                const contexts = response.data.contexts
                for (const contextName in contexts) {
                    const beans = contexts[contextName].beans
                    if (beans) {
                        for (const beanName in beans) {
                            const bean = beans[beanName]
                            if (bean.properties) {
                                Object.entries(bean.properties).forEach(([key, value]) => {
                                    configs.push({
                                        key: `${beanName}.${key}`,
                                        value: typeof value === 'object' ? JSON.stringify(value) : value
                                    })
                                })
                            }
                        }
                    }
                }
            }

            data.value = configs.slice(0, 50)
        } catch (error) {
            console.error('获取应用配置失败:', error)
        } finally {
            loading.value = false
        }
    }

    refreshEvent.addEventListener('refresh', fetchData)

    return {data, loading, fetchData}
}