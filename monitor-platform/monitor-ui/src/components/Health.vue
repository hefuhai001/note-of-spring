<template>
  <div class="health-container">
    <!-- 健康状态警报 -->
    <el-alert
        :title="healthStatus"
        :type="healthType"
        :closable="false"
        show-icon
        class="health-alert"
    />

    <el-divider/>

    <!-- 磁盘空间饼状图 -->
    <el-card class="disk-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <span>磁盘空间监控</span>
          <el-tag :type="diskHealthType" size="small">
            {{ diskHealthStatus }}
          </el-tag>
        </div>
      </template>

      <el-row :gutter="20">
        <el-col :span="12">
          <div ref="diskChart" class="disk-chart"></div>
        </el-col>
        <el-col :span="12">
          <el-descriptions :column="1" border class="disk-info">
            <el-descriptions-item label="总容量">
              <span class="disk-value">{{ formatBytes(diskInfo.total) }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="可用空间">
              <span class="disk-value success">{{ formatBytes(diskInfo.free) }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="已用空间">
              <span class="disk-value warning">{{ formatBytes(diskInfo.used) }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="使用率">
              <el-progress
                  :percentage="diskUsagePercentage"
                  :color="diskProgressColor"
                  :format="() => `${diskUsagePercentage}%`"
              />
            </el-descriptions-item>
            <el-descriptions-item label="健康阈值">
              {{ formatBytes(diskInfo.threshold) }}
            </el-descriptions-item>
          </el-descriptions>
        </el-col>
      </el-row>
    </el-card>

    <el-divider/>

    <!-- 组件健康状态 -->
    <el-card class="components-card">
      <template #header>
        <span>组件健康状态</span>
      </template>

      <el-row :gutter="20">
        <el-col
            :span="8"
            v-for="(component, name) in healthComponents"
            :key="name"
        >
          <div class="component-item" :class="getStatusClass(component.status)">
            <div class="component-name">{{ formatComponentName(name) }}</div>
            <div class="component-status">
              <el-tag :type="getStatusType(component.status)" size="large">
                {{ component.status }}
              </el-tag>
            </div>
            <div v-if="component.details" class="component-details">
              <div v-for="(value, key) in component.details" :key="key" class="detail-item">
                <span class="detail-key">{{ formatDetailKey(key) }}:</span>
                <span class="detail-value">{{ formatDetailValue(key, value) }}</span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>

  </div>
</template>

<script setup>
import {ref, computed, onMounted, onUnmounted} from 'vue'
import * as echarts from 'echarts'
import axios from 'axios'

const healthData = ref(null)
const diskChart = ref(null)
let chartInstance = null
let historyInterval = null

// 磁盘信息
const diskInfo = ref({
  total: 0,
  free: 0,
  used: 0,
  threshold: 0
})

// 健康历史记录
const healthHistory = ref([])

// 健康状态
const healthStatus = computed(() => {
  if (!healthData.value) return '检查中...'
  return `系统健康状态: ${healthData.value.status.toUpperCase()}`
})

const healthType = computed(() => {
  if (!healthData.value) return 'info'
  return healthData.value.status === 'UP' ? 'success' : 'error'
})

// 磁盘健康状态
const diskHealthStatus = computed(() => {
  if (!diskInfo.value.free) return '未知'
  const usage = (diskInfo.value.used / diskInfo.value.total) * 100
  if (usage > 90) return '危险'
  if (usage > 80) return '警告'
  if (usage > 70) return '注意'
  return '正常'
})

const diskHealthType = computed(() => {
  const status = diskHealthStatus.value
  if (status === '危险') return 'danger'
  if (status === '警告') return 'warning'
  if (status === '注意') return 'info'
  return 'success'
})

// 磁盘使用率百分比
const diskUsagePercentage = computed(() => {
  if (!diskInfo.value.total) return 0
  return Math.round((diskInfo.value.used / diskInfo.value.total) * 100)
})

// 磁盘进度条颜色
const diskProgressColor = computed(() => {
  const percentage = diskUsagePercentage.value
  if (percentage > 90) return '#F56C6C'
  if (percentage > 80) return '#E6A23C'
  if (percentage > 70) return '#909399'
  return '#67C23A'
})

// 健康组件
const healthComponents = computed(() => {
  if (!healthData.value || !healthData.value.components) return {}
  return healthData.value.components
})

// 格式化字节数
const formatBytes = (bytes) => {
  if (!bytes || bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

// 格式化组件名称
const formatComponentName = (name) => {
  const nameMap = {
    'diskSpace': '磁盘空间',
    'ping': '网络连通性',
    'db': '数据库',
    'redis': 'Redis缓存',
    'rabbit': '消息队列',
    'elasticsearch': '搜索引擎'
  }
  return nameMap[name] || name
}

// 格式化详情键名
const formatDetailKey = (key) => {
  const keyMap = {
    'total': '总容量',
    'free': '可用空间',
    'used': '已用空间',
    'threshold': '告警阈值',
    'exists': '是否存在',
    'status': '状态'
  }
  return keyMap[key] || key
}

// 格式化详情值
const formatDetailValue = (key, value) => {
  if (key === 'total' || key === 'free' || key === 'used' || key === 'threshold') {
    return formatBytes(value)
  }
  if (typeof value === 'boolean') {
    return value ? '是' : '否'
  }
  return value
}

// 获取状态类型
const getStatusType = (status) => {
  return status === 'UP' ? 'success' : 'danger'
}

// 获取状态类名
const getStatusClass = (status) => {
  return status === 'UP' ? 'status-up' : 'status-down'
}

// 获取健康检查数据
const fetchHealth = async () => {
  try {
    const response = await axios.get('/actuator/health')
    healthData.value = response.data

    // 提取磁盘信息
    if (response.data.components && response.data.components.diskSpace) {
      const diskDetails = response.data.components.diskSpace.details
      diskInfo.value = {
        total: diskDetails.total || 0,
        free: diskDetails.free || 0,
        used: (diskDetails.total || 0) - (diskDetails.free || 0),
        threshold: diskDetails.threshold || 0
      }

      // 更新图表
      updateDiskChart()

      // 添加历史记录
      addHistoryRecord(response.data)
    }
  } catch (error) {
    console.error('获取健康检查失败:', error)
  }
}

// 添加历史记录
const addHistoryRecord = (data) => {
  const record = {
    timestamp: new Date().toLocaleString(),
    status: data.status,
    diskUsage: diskUsagePercentage.value,
    components: Object.keys(data.components || {}).length,
    details: `磁盘使用率: ${diskUsagePercentage.value}%, 组件数: ${Object.keys(data.components || {}).length}`
  }

  healthHistory.value.unshift(record)

  // 只保留最近20条记录
  if (healthHistory.value.length > 20) {
    healthHistory.value.pop()
  }
}

// 刷新历史记录
const refreshHistory = () => {
  fetchHealth()
}

// 初始化磁盘饼状图
const initDiskChart = () => {
  if (diskChart.value) {
    chartInstance = echarts.init(diskChart.value)
    const option = {
      tooltip: {
        trigger: 'item',
        formatter: '{b}: {d}% ({c})'
      },
      legend: {
        orient: 'vertical',
        left: 'left',
        data: ['已用空间', '可用空间']
      },
      series: [
        {
          name: '磁盘空间',
          type: 'pie',
          radius: '55%',
          center: ['50%', '50%'],
          data: [
            {value: 0, name: '已用空间', itemStyle: {color: '#F56C6C'}},
            {value: 0, name: '可用空间', itemStyle: {color: '#67C23A'}}
          ],
          emphasis: {
            scale: true,
            label: {
              show: true,
              formatter: '{b}: {d}%'
            }
          },
          label: {
            show: true,
            formatter: '{b}: {d}%'
          },
          labelLine: {
            show: true
          }
        }
      ]
    }
    chartInstance.setOption(option)
  }
}

// 更新磁盘饼状图
const updateDiskChart = () => {
  if (chartInstance && diskInfo.value.total > 0) {
    const used = diskInfo.value.used
    const free = diskInfo.value.free

    chartInstance.setOption({
      series: [{
        data: [
          {value: used, name: '已用空间', itemStyle: {color: '#F56C6C'}},
          {value: free, name: '可用空间', itemStyle: {color: '#67C23A'}}
        ]
      }]
    })
  }
}

// 调整图表大小
const handleResize = () => {
  if (chartInstance) {
    chartInstance.resize()
  }
}

// 添加演示数据（如果后端没有完整数据）
const addDemoData = () => {
  // 如果没有真实数据，添加演示数据用于展示
  if (!healthData.value) {
    healthData.value = {
      status: "UP",
      components: {
        diskSpace: {
          status: "UP",
          details: {
            total: 1023114473472,
            free: 856337506304,
            threshold: 10485760,
            exists: true
          }
        },
        ping: {
          status: "UP"
        }
      }
    }

    diskInfo.value = {
      total: 1023114473472,
      free: 856337506304,
      used: 1023114473472 - 856337506304,
      threshold: 10485760
    }

    updateDiskChart()
    addHistoryRecord(healthData.value)
  }
}

onMounted(() => {
  fetchHealth()
  initDiskChart()

  // 如果没有真实数据，添加演示数据
  setTimeout(addDemoData, 1000)

  // 定时刷新
  const healthInterval = setInterval(fetchHealth, 30000)
  historyInterval = setInterval(() => {
    if (healthData.value) {
      addHistoryRecord(healthData.value)
    }
  }, 60000) // 每分钟记录一次历史

  // 监听窗口大小变化
  window.addEventListener('resize', handleResize)

  // 清理函数
  onUnmounted(() => {
    clearInterval(healthInterval)
    if (historyInterval) clearInterval(historyInterval)
    window.removeEventListener('resize', handleResize)
    if (chartInstance) {
      chartInstance.dispose()
    }
  })
})
</script>

<style scoped>
.health-container {
  padding: 20px;
}

.health-alert {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.disk-card {
  margin-bottom: 20px;
}

.disk-chart {
  width: 100%;
  height: 300px;
}

.disk-info {
  margin-top: 20px;
}

.disk-value {
  font-weight: bold;
  font-size: 16px;
}

.disk-value.success {
  color: #67C23A;
}

.disk-value.warning {
  color: #E6A23C;
}

.components-card {
  margin-bottom: 20px;
}

.component-item {
  padding: 15px;
  border-radius: 8px;
  margin-bottom: 15px;
  transition: all 0.3s;
  border: 1px solid #e0e0e0;
}

.component-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.component-item.status-up {
  background-color: #f0f9ff;
  border-left: 4px solid #67C23A;
}

.component-item.status-down {
  background-color: #fef0f0;
  border-left: 4px solid #F56C6C;
}

.component-name {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #333;
}

.component-status {
  margin-bottom: 10px;
}

.component-details {
  margin-top: 10px;
  padding-top: 10px;
  border-top: 1px solid #e0e0e0;
}

.detail-item {
  font-size: 12px;
  margin-bottom: 5px;
  line-height: 1.5;
}

.detail-key {
  color: #666;
  margin-right: 8px;
}

.detail-value {
  color: #333;
  font-weight: 500;
}

.history-card {
  margin-top: 20px;
}

:deep(.el-descriptions__label) {
  font-weight: bold;
  background-color: #f5f7fa;
}

:deep(.el-progress-bar__outer) {
  background-color: #ebeef5;
}

@media (max-width: 768px) {
  .health-container {
    padding: 10px;
  }

  .disk-chart {
    height: 250px;
  }

  .component-item {
    padding: 10px;
  }
}
</style>