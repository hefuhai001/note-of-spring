<template>
  <el-row :gutter="20">
    <el-col :span="12">
      <el-card shadow="hover">
        <template #header>
          <div style="display: flex; justify-content: space-between; align-items: center">
            <span>内存使用情况</span>
            <el-button size="small" @click="refreshChart">
              <el-icon>
                <Refresh/>
              </el-icon>
              刷新
            </el-button>
          </div>
        </template>
        <div ref="memoryChartRef" style="height: 350px; width: 800px"></div>
        <div v-if="chartError" class="error-message">
          <el-alert title="图表加载失败" type="error" :description="chartError" show-icon :closable="false"/>
        </div>
      </el-card>
    </el-col>
    <el-col :span="12">
      <el-card shadow="hover">
        <template #header>
          <span>类加载信息</span>
        </template>
        <div v-loading="classLoading">
          <el-descriptions :column="1" border>
            <el-descriptions-item label="已加载类数">
              {{ formatNumber(classLoadingInfo.loadedClassCount) }}
            </el-descriptions-item>
            <el-descriptions-item label="总加载类数">
              {{ formatNumber(classLoadingInfo.totalLoadedClassCount) }}
            </el-descriptions-item>
            <el-descriptions-item label="已卸载类数">
              {{ formatNumber(classLoadingInfo.unloadedClassCount) }}
            </el-descriptions-item>
          </el-descriptions>

        </div>
      </el-card>
    </el-col>
  </el-row>
</template>

<script setup>
import {ref, onMounted, onUnmounted, nextTick} from 'vue'
import {Refresh} from '@element-plus/icons-vue'
import {ElMessage} from 'element-plus'
import {useClassLoadingInfo} from './composables/useJvmMetrics'
import {useMemoryChart} from './composables/useMemoryChart'
import axios from "axios";

const memoryChartRef = ref(null)
const chartError = ref('')
const {classLoadingInfo, loading: classLoading, fetchData: fetchClassLoading} = useClassLoadingInfo()
const {initChart, resize, dispose, updateChartData} = useMemoryChart(memoryChartRef)

// 格式化数字
const formatNumber = (num) => {
  if (!num) return '0'
  return num.toLocaleString()
}

// 刷新图表
const refreshChart = async () => {
  try {
    await updateChartData()
    ElMessage.success('图表已刷新')
  } catch (error) {
    console.error('刷新图表失败:', error)
    ElMessage.error('刷新图表失败')
  }
}

// 等待DOM渲染完成后再初始化图表
onMounted(async () => {
  // 先获取类加载信息
  await fetchClassLoading()

  // 等待DOM更新
  await nextTick()

  // 延迟初始化图表，确保DOM完全准备好
  setTimeout(() => {
    if (memoryChartRef.value) {
      try {
        initChart()
        chartError.value = ''
      } catch (error) {
        console.error('图表初始化失败:', error)
        chartError.value = error.message
      }
    } else {
      chartError.value = '未找到图表容器'
      console.error('图表容器不存在')
    }
  }, 500)

  // 监听窗口大小变化
  window.addEventListener('resize', resize)
})

onUnmounted(() => {
  window.removeEventListener('resize', resize)
  dispose()
})
</script>

<style scoped>
.error-message {
  margin-top: 10px;
}

:deep(.el-card__body) {
  padding: 20px;
}

:deep(.el-descriptions__label) {
  width: 120px;
}

/* 确保图表容器有明确的高度 */
[ref="memoryChartRef"] {
  min-height: 350px;
}
</style>