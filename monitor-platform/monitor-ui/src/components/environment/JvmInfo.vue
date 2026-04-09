<template>
  <div v-loading="loading">
    <el-descriptions :column="2" border>
      <el-descriptions-item label="Java版本">
        {{ jvmInfo.javaVersion || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="Java供应商">
        {{ jvmInfo.javaVendor || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="JVM名称">
        {{ jvmInfo.jvmName || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="JVM版本">
        {{ jvmInfo.jvmVersion || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="JVM启动时间">
        {{ jvmInfo.jvmStartTime || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="JVM正常运行时间">
        {{ jvmInfo.formatUptime ? jvmInfo.formatUptime(jvmInfo.jvmUptime) : '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="系统名称">
        {{ jvmInfo.osName || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="系统架构">
        {{ jvmInfo.osArch || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="可用处理器">
        {{ jvmInfo.availableProcessors || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="最大内存">
        {{ jvmInfo.formatMemory ? jvmInfo.formatMemory(jvmInfo.maxMemory) : '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="总内存">
        {{ jvmInfo.formatMemory ? jvmInfo.formatMemory(jvmInfo.totalMemory) : '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="已使用内存">
        {{ jvmInfo.formatMemory ? jvmInfo.formatMemory(jvmInfo.usedMemory) : '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="空闲内存">
        {{ jvmInfo.formatMemory ? jvmInfo.formatMemory(jvmInfo.freeMemory) : '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="内存使用率">
        {{ jvmInfo.memoryUsage }}%
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>

<script setup>
import {onMounted} from 'vue'
import {useJvmMetrics} from './composables/useJvmMetrics'

const {jvmInfo, loading, fetchData} = useJvmMetrics()

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.el-descriptions {
  margin-top: 10px;
}

:deep(.el-descriptions__label) {
  width: 150px;
  font-weight: bold;
}

:deep(.el-descriptions__content) {
  color: #666;
}
</style>