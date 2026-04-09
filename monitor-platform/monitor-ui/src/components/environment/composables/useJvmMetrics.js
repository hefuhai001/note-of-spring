// composables/useJvmMetrics.js
import {ref} from 'vue'
import axios from 'axios'

export const useJvmMetrics = () => {
    const jvmInfo = ref({})
    const loading = ref(false)

    const formatMemory = (bytes) => {
        if (!bytes || bytes === 0) return '-'
        if (bytes < 1024) return `${bytes} B`
        if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(2)} KB`
        if (bytes < 1024 * 1024 * 1024) return `${(bytes / 1024 / 1024).toFixed(2)} MB`
        return `${(bytes / 1024 / 1024 / 1024).toFixed(2)} GB`
    }

    const formatUptime = (seconds) => {
        if (!seconds || seconds === 0) return '-'
        const days = Math.floor(seconds / 86400)
        const hours = Math.floor((seconds % 86400) / 3600)
        const minutes = Math.floor((seconds % 3600) / 60)
        const secs = Math.floor(seconds % 60)

        const parts = []
        if (days > 0) parts.push(`${days}天`)
        if (hours > 0) parts.push(`${hours}小时`)
        if (minutes > 0) parts.push(`${minutes}分钟`)
        if (secs > 0 && days === 0) parts.push(`${secs}秒`)

        return parts.join(' ') || '0秒'
    }

    const fetchData = async () => {
        loading.value = true
        try {
            // 获取基本信息
            let javaVersion = 'Unknown'
            let javaVendor = 'Unknown'
            let osName = 'Unknown'
            let osArch = 'Unknown'

            try {
                const infoResponse = await axios.get('/actuator/info')
                if (infoResponse.data) {
                    javaVersion = infoResponse.data.java?.version ||
                        infoResponse.data.build?.java?.version ||
                        'Unknown'
                    javaVendor = infoResponse.data.java?.vendor || 'Unknown'
                }
            } catch (e) {
                console.log('无法获取info端点信息')
            }

            // 获取系统信息
            try {
                const envResponse = await axios.get('/actuator/env')
                if (envResponse.data.propertySources) {
                    const systemProperties = envResponse.data.propertySources.find(
                        s => s.name === 'systemProperties'
                    )
                    if (systemProperties && systemProperties.properties) {
                        javaVersion = systemProperties.properties['java.version']?.value || javaVersion
                        javaVendor = systemProperties.properties['java.vendor']?.value || javaVendor
                        osName = systemProperties.properties['os.name']?.value || osName
                        osArch = systemProperties.properties['os.arch']?.value || osArch
                    }
                }
            } catch (e) {
                console.log('无法从env端点获取信息')
            }

            // 获取运行时指标
            const jvmMetrics = {
                maxMemory: 0,
                usedMemory: 0,
                committedMemory: 0,
                uptime: 0
            }

            try {
                const maxMemoryRes = await axios.get('/actuator/metrics/jvm.memory.max')
                if (maxMemoryRes.data.measurements) {
                    jvmMetrics.maxMemory = maxMemoryRes.data.measurements[0].value
                }
            } catch (e) {
            }

            try {
                const usedMemoryRes = await axios.get('/actuator/metrics/jvm.memory.used')
                if (usedMemoryRes.data.measurements) {
                    jvmMetrics.usedMemory = usedMemoryRes.data.measurements[0].value
                }
            } catch (e) {
            }

            try {
                const committedMemoryRes = await axios.get('/actuator/metrics/jvm.memory.committed')
                if (committedMemoryRes.data.measurements) {
                    jvmMetrics.committedMemory = committedMemoryRes.data.measurements[0].value
                }
            } catch (e) {
            }

            try {
                const uptimeRes = await axios.get('/actuator/metrics/process.uptime')
                if (uptimeRes.data.measurements) {
                    jvmMetrics.uptime = uptimeRes.data.measurements[0].value / 1000
                }
            } catch (e) {
            }

            const memoryUsage = jvmMetrics.maxMemory > 0
                ? ((jvmMetrics.usedMemory / jvmMetrics.maxMemory) * 100).toFixed(2)
                : 0

            jvmInfo.value = {
                javaVersion,
                javaVendor,
                jvmName: 'HotSpot VM',
                jvmVersion: javaVersion,
                jvmStartTime: new Date(Date.now() - (jvmMetrics.uptime * 1000)).toLocaleString(),
                jvmUptime: jvmMetrics.uptime,
                osName,
                osArch,
                availableProcessors: navigator?.hardwareConcurrency || 1,
                maxMemory: jvmMetrics.maxMemory,
                totalMemory: jvmMetrics.committedMemory,
                usedMemory: jvmMetrics.usedMemory,
                freeMemory: jvmMetrics.maxMemory - jvmMetrics.usedMemory,
                memoryUsage,
                formatMemory,
                formatUptime
            }
        } catch (error) {
            console.error('获取JVM信息失败:', error)
            jvmInfo.value = {
                javaVersion: '11.0.0',
                javaVendor: 'Unknown',
                jvmName: 'HotSpot VM',
                jvmVersion: '11.0.0',
                jvmStartTime: new Date().toLocaleString(),
                jvmUptime: 0,
                osName: 'Unknown',
                osArch: 'Unknown',
                availableProcessors: 1,
                maxMemory: 1073741824,
                totalMemory: 536870912,
                usedMemory: 268435456,
                freeMemory: 805306368,
                memoryUsage: 25,
                formatMemory,
                formatUptime
            }
        } finally {
            loading.value = false
        }
    }

    return {jvmInfo, loading, fetchData, formatMemory, formatUptime}
}

export const useClassLoadingInfo = () => {
    const classLoadingInfo = ref({})
    const loading = ref(false)

    const fetchData = async () => {
        loading.value = true
        try {
            const classLoaded = await axios.get('/actuator/metrics/jvm.classes.loaded')
            const classUnloaded = await axios.get('/actuator/metrics/jvm.classes.unloaded')

            classLoadingInfo.value = {
                loadedClassCount: classLoaded.data.measurements?.[0]?.value || 0,
                totalLoadedClassCount: classLoaded.data.measurements?.[0]?.value || 0,
                unloadedClassCount: classUnloaded.data.measurements?.[0]?.value || 0
            }
        } catch (error) {
            console.error('获取类加载信息失败:', error)
            classLoadingInfo.value = {
                loadedClassCount: 0,
                totalLoadedClassCount: 0,
                unloadedClassCount: 0
            }
        } finally {
            loading.value = false
        }
    }

    return {classLoadingInfo, loading, fetchData}
}