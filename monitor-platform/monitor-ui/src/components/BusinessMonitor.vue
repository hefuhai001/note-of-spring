<template>
  <div class="business-monitor">
    <el-row :gutter="20">
      <!-- 业务指标卡片 -->
      <el-col :span="6" v-for="metric in businessMetrics" :key="metric.title">
        <el-card class="metric-card" shadow="hover">
          <div class="metric-icon" :style="{ backgroundColor: metric.color }">
            <el-icon :size="24">
              <component :is="metric.icon"/>
            </el-icon>
          </div>
          <div class="metric-content">
            <div class="metric-title">{{ metric.title }}</div>
            <div class="metric-value">{{ metric.value }}</div>
            <div class="metric-unit">{{ metric.unit }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <!-- 业务操作面板 -->
      <el-col :span="12">
        <el-card class="operation-card">
          <template #header>
            <span>业务操作</span>
          </template>

          <div class="operation-buttons">
            <el-button type="primary" size="large" @click="executeBusiness" :loading="executing">
              <el-icon>
                <Operation/>
              </el-icon>
              执行业务逻辑
            </el-button>

            <el-button type="success" size="large" @click="simulateUserLogin">
              <el-icon>
                <User/>
              </el-icon>
              模拟用户登录
            </el-button>

            <el-button type="warning" size="large" @click="simulateUserLogout">
              <el-icon>
                <SwitchButton/>
              </el-icon>
              模拟用户登出
            </el-button>

            <el-button type="danger" size="large" @click="simulateError">
              <el-icon>
                <Warning/>
              </el-icon>
              模拟错误
            </el-button>
          </div>

          <el-divider/>

          <div class="execution-log">
            <div class="log-header">
              <span>执行日志</span>
              <el-button size="small" @click="clearLogs">清空</el-button>
            </div>
            <div class="log-content">
              <div v-for="(log, index) in executionLogs" :key="index" :class="['log-item', log.type]">
                <span class="log-time">{{ log.time }}</span>
                <span class="log-message">{{ log.message }}</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 业务图表 -->
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>业务指标趋势</span>
          </template>
          <div ref="businessChart" style="height: 400px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <!-- 实时监控数据 -->
      <el-col :span="24">
        <el-card>
          <template #header>
            <span>实时监控数据</span>
          </template>
          <el-table :data="monitoringData" stripe border style="width: 100%">
            <el-table-column prop="timestamp" label="时间" width="180"/>
            <el-table-column prop="api" label="API端点" width="200"/>
            <el-table-column prop="status" label="状态" width="100">
              <template #default="scope">
                <el-tag :type="scope.row.status === '成功' ? 'success' : 'danger'">
                  {{ scope.row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="duration" label="耗时(ms)" width="120"/>
            <el-table-column prop="message" label="消息" show-overflow-tooltip/>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import {ref, onMounted, onUnmounted} from 'vue'
import {ElMessage, ElNotification} from 'element-plus'
import {
  Operation,
  User,
  SwitchButton,
  Warning,
  DataAnalysis,
  TrendCharts,
  CircleCheck,
  Clock
} from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import axios from 'axios'

// 业务指标数据
const businessMetrics = ref([
  {title: '总请求数', value: 0, unit: '次', icon: 'DataAnalysis', color: '#409EFF'},
  {title: '成功请求数', value: 0, unit: '次', icon: 'CircleCheck', color: '#67C23A'},
  {title: '失败请求数', value: 0, unit: '次', icon: 'Warning', color: '#F56C6C'},
  {title: '成功率', value: '0%', unit: '%', icon: 'TrendCharts', color: '#E6A23C'}
])

const executing = ref(false)
const executionLogs = ref([])
const monitoringData = ref([])

let businessChart = null
let chartInterval = null
let metricsInterval = null

// 执行业务逻辑
const executeBusiness = async () => {
  executing.value = true
  const startTime = Date.now()

  try {
    const response = await axios.post('/api/monitor/business')
    const duration = Date.now() - startTime

    addLog('success', `执行业务逻辑成功: ${response.data.result}`, duration)

    ElNotification.success({
      title: '执行成功',
      message: response.data.result,
      duration: 3000
    })

    // 添加到监控数据表
    addMonitoringData('执行业务逻辑', '成功', duration, response.data.result)

    // 刷新指标
    await fetchBusinessMetrics()

  } catch (error) {
    const duration = Date.now() - startTime
    addLog('error', `执行业务逻辑失败: ${error.message}`, duration)

    ElNotification.error({
      title: '执行失败',
      message: error.message,
      duration: 3000
    })

    addMonitoringData('执行业务逻辑', '失败', duration, error.message)
  } finally {
    executing.value = false
  }
}

// 模拟用户登录
const simulateUserLogin = async () => {
  try {
    const response = await axios.post('/api/monitor/sessions?delta=1')
    addLog('success', '用户登录成功，活跃会话数: ' + response.data.activeSessions)
    ElMessage.success('用户登录成功')
    await fetchBusinessMetrics()
  } catch (error) {
    addLog('error', '用户登录失败: ' + error.message)
    ElMessage.error('用户登录失败')
  }
}

// 模拟用户登出
const simulateUserLogout = async () => {
  try {
    const response = await axios.post('/api/monitor/sessions?delta=-1')
    addLog('info', '用户登出成功，活跃会话数: ' + response.data.activeSessions)
    ElMessage.info('用户登出成功')
    await fetchBusinessMetrics()
  } catch (error) {
    addLog('error', '用户登出失败: ' + error.message)
    ElMessage.error('用户登出失败')
  }
}

// 模拟错误
const simulateError = () => {
  addLog('error', '模拟业务错误发生')
  ElMessage.error('模拟业务错误')

  addMonitoringData('模拟错误', '失败', 0, '模拟业务处理错误')
}

// 添加日志
const addLog = (type, message, duration = null) => {
  const log = {
    type: type,
    message: duration ? `${message} (耗时: ${duration}ms)` : message,
    time: new Date().toLocaleTimeString()
  }

  executionLogs.value.unshift(log)

  // 只保留最近50条日志
  if (executionLogs.value.length > 50) {
    executionLogs.value.pop()
  }
}

// 清空日志
const clearLogs = () => {
  executionLogs.value = []
  ElMessage.success('日志已清空')
}

// 添加监控数据
const addMonitoringData = (api, status, duration, message) => {
  const data = {
    timestamp: new Date().toLocaleString(),
    api: api,
    status: status,
    duration: duration,
    message: message
  }

  monitoringData.value.unshift(data)

  // 只保留最近100条数据
  if (monitoringData.value.length > 100) {
    monitoringData.value.pop()
  }
}

// 获取业务指标
const fetchBusinessMetrics = async () => {
  try {
    // 获取总请求数
    const totalResponse = await axios.get('/actuator/metrics/api.requests.total')
    const totalCount = totalResponse.data.measurements?.[0]?.value || 0

    // 获取错误数
    const errorResponse = await axios.get('/actuator/metrics/api.errors.total')
    const errorCount = errorResponse.data.measurements?.[0]?.value || 0

    const successCount = totalCount - errorCount
    const successRate = totalCount > 0 ? ((successCount / totalCount) * 100).toFixed(2) : 0

    businessMetrics.value = [
      {title: '总请求数', value: Math.floor(totalCount), unit: '次', icon: 'DataAnalysis', color: '#409EFF'},
      {title: '成功请求数', value: Math.floor(successCount), unit: '次', icon: 'CircleCheck', color: '#67C23A'},
      {title: '失败请求数', value: Math.floor(errorCount), unit: '次', icon: 'Warning', color: '#F56C6C'},
      {title: '成功率', value: successRate, unit: '%', icon: 'TrendCharts', color: '#E6A23C'}
    ]

    // 更新图表
    updateBusinessChart(totalCount, successCount, errorCount)

  } catch (error) {
    console.error('获取业务指标失败:', error)
  }
}

// 初始化业务图表
const initBusinessChart = () => {
  const chartDom = document.querySelector('[ref="businessChart"]')
  if (chartDom) {
    businessChart = echarts.init(chartDom)
    const option = {
      tooltip: {trigger: 'axis'},
      legend: {data: ['总请求数', '成功数', '失败数']},
      xAxis: {type: 'category', data: []},
      yAxis: {type: 'value', name: '请求数'},
      series: [
        {name: '总请求数', type: 'line', data: [], smooth: true, color: '#409EFF'},
        {name: '成功数', type: 'line', data: [], smooth: true, color: '#67C23A'},
        {name: '失败数', type: 'line', data: [], smooth: true, color: '#F56C6C'}
      ]
    }
    businessChart.setOption(option)

    // 模拟实时数据
    chartInterval = setInterval(() => {
      const now = new Date().toLocaleTimeString()
      const total = Math.floor(Math.random() * 100) + 50
      const success = Math.floor(total * (0.7 + Math.random() * 0.3))
      const error = total - success

      const option = businessChart.getOption()
      option.xAxis[0].data.push(now)
      option.series[0].data.push(total)
      option.series[1].data.push(success)
      option.series[2].data.push(error)

      if (option.xAxis[0].data.length > 20) {
        option.xAxis[0].data.shift()
        option.series[0].data.shift()
        option.series[1].data.shift()
        option.series[2].data.shift()
      }

      businessChart.setOption(option)
    }, 10000)
  }
}

// 更新业务图表
const updateBusinessChart = (total, success, error) => {
  if (businessChart) {
    const now = new Date().toLocaleTimeString()
    const option = businessChart.getOption()
    option.xAxis[0].data.push(now)
    option.series[0].data.push(total)
    option.series[1].data.push(success)
    option.series[2].data.push(error)

    if (option.xAxis[0].data.length > 20) {
      option.xAxis[0].data.shift()
      option.series[0].data.shift()
      option.series[1].data.shift()
      option.series[2].data.shift()
    }

    businessChart.setOption(option)
  }
}

// 定期刷新指标
const startPeriodicRefresh = () => {
  metricsInterval = setInterval(() => {
    fetchBusinessMetrics()
  }, 5000)
}

onMounted(() => {
  fetchBusinessMetrics()
  setTimeout(() => initBusinessChart(), 1000)
  startPeriodicRefresh()

  // 添加欢迎日志
  addLog('success', '业务监控模块已启动')
})

onUnmounted(() => {
  if (chartInterval) clearInterval(chartInterval)
  if (metricsInterval) clearInterval(metricsInterval)
  if (businessChart) businessChart.dispose()
})
</script>

<style scoped>
.business-monitor {
  padding: 0;
}

.metric-card {
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  padding: 15px;
}

.metric-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.2);
}

.metric-icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  margin-right: 15px;
}

.metric-content {
  flex: 1;
}

.metric-title {
  font-size: 14px;
  color: #666;
  margin-bottom: 5px;
}

.metric-value {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.metric-unit {
  font-size: 12px;
  color: #999;
}

.operation-buttons {
  display: flex;
  gap: 15px;
  flex-wrap: wrap;
  margin-bottom: 20px;
}

.operation-buttons .el-button {
  flex: 1;
  min-width: 120px;
}

.execution-log {
  margin-top: 20px;
}

.log-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  font-weight: bold;
}

.log-content {
  height: 300px;
  overflow-y: auto;
  background-color: #f5f5f5;
  border-radius: 4px;
  padding: 10px;
}

.log-item {
  padding: 5px 10px;
  margin-bottom: 5px;
  border-radius: 3px;
  font-size: 12px;
  font-family: monospace;
}

.log-item.success {
  background-color: #f0f9ff;
  border-left: 3px solid #67C23A;
}

.log-item.error {
  background-color: #fef0f0;
  border-left: 3px solid #F56C6C;
}

.log-item.info {
  background-color: #f4f4f5;
  border-left: 3px solid #909399;
}

.log-time {
  color: #666;
  margin-right: 10px;
}

.log-message {
  color: #333;
}

.el-table {
  font-size: 12px;
}
</style>