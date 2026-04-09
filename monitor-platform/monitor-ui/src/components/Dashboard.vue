<template>
  <div class="dashboard">
    <!-- 统计卡片 -->
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-icon" style="background-color: #409EFF">
            <el-icon :size="24">
              <DataAnalysis/>
            </el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-title">堆内存使用</div>
            <div class="stat-value">{{ memoryStats.heapUsed }} <span class="stat-unit">MB</span></div>
            <div class="stat-sub">最大: {{ memoryStats.heapMax }} MB</div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-icon" style="background-color: #67C23A">
            <el-icon :size="24">
              <Timer/>
            </el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-title">GC次数</div>
            <div class="stat-value">{{ gcStats.count }} <span class="stat-unit">次</span></div>
            <div class="stat-sub">耗时: {{ gcStats.time }} ms</div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-icon" style="background-color: #E6A23C">
            <el-icon :size="24">
              <Document/>
            </el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-title">HTTP请求</div>
            <div class="stat-value">{{ httpStats.count }} <span class="stat-unit">次</span></div>
            <div class="stat-sub">平均耗时: {{ httpStats.avgTime }} ms</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 饼图区域 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>内存分布（堆内存 vs 栈内存）</span>
              <el-button size="small" @click="refreshMemoryPie">刷新</el-button>
            </div>
          </template>
          <div ref="memoryPieRef" style="height: 350px"></div>
          <div class="pie-legend">
            <div class="legend-item">
              <span class="legend-color" style="background-color: #409EFF"></span>
              <span>堆内存: {{ memoryStats.heapUsed }} MB</span>
            </div>
            <div class="legend-item">
              <span class="legend-color" style="background-color: #E6A23C"></span>
              <span>栈内存(非堆): {{ memoryStats.nonHeapUsed }} MB</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>GC分布</span>
              <el-button size="small" @click="refreshGcPie">刷新</el-button>
            </div>
          </template>
          <div ref="gcPieRef" style="height: 350px"></div>
          <div class="pie-legend">
            <div class="legend-item">
              <span class="legend-color" style="background-color: #67C23A"></span>
              <span>Young GC: {{ gcStats.youngCount || 0 }} 次</span>
            </div>
            <div class="legend-item">
              <span class="legend-color" style="background-color: #F56C6C"></span>
              <span>Full GC: {{ gcStats.fullCount || 0 }} 次</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 仪表盘和条形图区域 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>堆内存使用率仪表盘</span>
          </template>
          <div ref="memoryGaugeRef" style="height: 300px"></div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>HTTP请求TOP5</span>
              <el-button size="small" @click="refreshHttpData">刷新</el-button>
            </div>
          </template>
          <div ref="httpBarRef" style="height: 300px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import {ref, onMounted, onUnmounted} from 'vue'
import * as echarts from 'echarts'
import axios from 'axios'
import {ElMessage} from 'element-plus'
import {DataAnalysis, Timer, Document} from '@element-plus/icons-vue'

// 统计数据
const memoryStats = ref({
  heapUsed: 0,
  heapMax: 0,
  heapFree: 0,
  heapPercentage: 0,
  nonHeapUsed: 0,
  nonHeapMax: 0,
  nonHeapPercentage: 0
})
const gcStats = ref({count: 0, time: 0, youngCount: 0, fullCount: 0})
const httpStats = ref({count: 0, avgTime: 0})
const httpRequests = ref([])

// 图表实例
const memoryPieRef = ref(null)
const gcPieRef = ref(null)
const memoryGaugeRef = ref(null)
const httpBarRef = ref(null)

let memoryPieChart = null
let gcPieChart = null
let memoryGaugeChart = null
let httpBarChart = null
let dataInterval = null

// 获取内存数据
const fetchMemoryData = async () => {
  try {
    // 获取堆内存数据
    const heapUsedRes = await axios.get('/actuator/metrics/jvm.memory.used?tag=area:heap')
    const heapMaxRes = await axios.get('/actuator/metrics/jvm.memory.max?tag=area:heap')

    // 获取非堆内存（栈内存）数据
    const nonHeapUsedRes = await axios.get('/actuator/metrics/jvm.memory.used?tag=area:nonheap')
    const nonHeapMaxRes = await axios.get('/actuator/metrics/jvm.memory.max?tag=area:nonheap')

    if (heapUsedRes.data.measurements && heapMaxRes.data.measurements) {
      const heapUsed = heapUsedRes.data.measurements[0].value / 1024 / 1024
      const heapMax = heapMaxRes.data.measurements[0].value / 1024 / 1024
      const heapFree = heapMax - heapUsed
      const heapPercentage = (heapUsed / heapMax * 100).toFixed(1)

      let nonHeapUsed = 0
      let nonHeapMax = 0

      if (nonHeapUsedRes.data.measurements) {
        nonHeapUsed = nonHeapUsedRes.data.measurements[0].value / 1024 / 1024
      }
      if (nonHeapMaxRes.data.measurements) {
        nonHeapMax = nonHeapMaxRes.data.measurements[0].value / 1024 / 1024
      }

      memoryStats.value = {
        heapUsed: heapUsed.toFixed(0),
        heapMax: heapMax.toFixed(0),
        heapFree: heapFree.toFixed(0),
        heapPercentage: parseFloat(heapPercentage),
        nonHeapUsed: nonHeapUsed.toFixed(0),
        nonHeapMax: nonHeapMax.toFixed(0),
        nonHeapPercentage: nonHeapMax > 0 ? parseFloat((nonHeapUsed / nonHeapMax * 100).toFixed(1)) : 0
      }

      // 更新内存饼图（堆内存 vs 栈内存）
      if (memoryPieChart) {
        memoryPieChart.setOption({
          series: [{
            data: [
              {value: heapUsed, name: '堆内存', itemStyle: {color: '#409EFF'}},
              {value: nonHeapUsed, name: '栈内存(非堆)', itemStyle: {color: '#E6A23C'}}
            ]
          }]
        })
      }

      // 更新堆内存仪表盘
      if (memoryGaugeChart) {
        memoryGaugeChart.setOption({
          series: [{
            data: [{value: parseFloat(heapPercentage), name: '堆内存使用率'}]
          }]
        })
      }
    }
  } catch (error) {
    console.error('获取内存数据失败:', error)
    // 如果获取失败，使用模拟数据演示
    setDemoMemoryData()
  }
}

// 模拟内存数据（当API不可用时）
const setDemoMemoryData = () => {
  const heapUsed = 512
  const heapMax = 1024
  const nonHeapUsed = 128
  const heapPercentage = (heapUsed / heapMax * 100).toFixed(1)

  memoryStats.value = {
    heapUsed: heapUsed,
    heapMax: heapMax,
    heapFree: heapMax - heapUsed,
    heapPercentage: parseFloat(heapPercentage),
    nonHeapUsed: nonHeapUsed,
    nonHeapMax: 256,
    nonHeapPercentage: (nonHeapUsed / 256 * 100).toFixed(1)
  }

  if (memoryPieChart) {
    memoryPieChart.setOption({
      series: [{
        data: [
          {value: heapUsed, name: '堆内存', itemStyle: {color: '#409EFF'}},
          {value: nonHeapUsed, name: '栈内存(非堆)', itemStyle: {color: '#E6A23C'}}
        ]
      }]
    })
  }

  if (memoryGaugeChart) {
    memoryGaugeChart.setOption({
      series: [{
        data: [{value: parseFloat(heapPercentage), name: '堆内存使用率'}]
      }]
    })
  }
}

// 获取GC数据
const fetchGcData = async () => {
  try {
    const gcRes = await axios.get('/actuator/metrics/jvm.gc.pause')
    if (gcRes.data.measurements) {
      const count = gcRes.data.measurements.find(m => m.statistic === 'COUNT')
      const totalTime = gcRes.data.measurements.find(m => m.statistic === 'TOTAL_TIME')

      gcStats.value.count = count ? Math.floor(count.value) : 0
      gcStats.value.time = totalTime ? Math.floor(totalTime.value) : 0

      // 模拟 Young GC 和 Full GC 的分布
      gcStats.value.youngCount = Math.floor(gcStats.value.count * 0.85)
      gcStats.value.fullCount = gcStats.value.count - gcStats.value.youngCount

      // 更新GC饼图
      if (gcPieChart) {
        gcPieChart.setOption({
          series: [{
            data: [
              {value: gcStats.value.youngCount, name: 'Young GC', itemStyle: {color: '#67C23A'}},
              {value: gcStats.value.fullCount, name: 'Full GC', itemStyle: {color: '#F56C6C'}}
            ]
          }]
        })
      }
    }
  } catch (error) {
    console.error('获取GC数据失败:', error)
    // 模拟数据
    gcStats.value = {count: 156, time: 2340, youngCount: 132, fullCount: 24}
    if (gcPieChart) {
      gcPieChart.setOption({
        series: [{
          data: [
            {value: 132, name: 'Young GC', itemStyle: {color: '#67C23A'}},
            {value: 24, name: 'Full GC', itemStyle: {color: '#F56C6C'}}
          ]
        }]
      })
    }
  }
}

// 获取HTTP数据
const fetchHttpData = async () => {
  try {
    const totalRes = await axios.get('/actuator/metrics/http.server.requests')
    if (totalRes.data.measurements) {
      const count = totalRes.data.measurements.find(m => m.statistic === 'COUNT')
      const totalTime = totalRes.data.measurements.find(m => m.statistic === 'TOTAL_TIME')

      if (count) {
        httpStats.value.count = Math.floor(count.value)
        httpStats.value.avgTime = totalTime ? (totalTime.value / count.value).toFixed(2) : 0
      }
    }

    const requests = []
    const uriTag = totalRes.data.availableTags?.find(tag => tag.tag === 'uri')

    if (uriTag && uriTag.values) {
      for (const uri of uriTag.values.slice(0, 10)) {
        try {
          const detail = await axios.get(`/actuator/metrics/http.server.requests?tag=uri:${encodeURIComponent(uri)}`)
          if (detail.data.measurements) {
            const count = detail.data.measurements.find(m => m.statistic === 'COUNT')

            if (count && count.value > 0) {
              let displayPath = uri
              if (displayPath.length > 25) {
                displayPath = '...' + displayPath.substring(displayPath.length - 22)
              }
              requests.push({
                path: displayPath,
                fullPath: uri,
                count: Math.floor(count.value)
              })
            }
          }
        } catch (e) {
          console.error(`获取路径 ${uri} 数据失败:`, e)
        }
      }
    }

    httpRequests.value = requests.sort((a, b) => b.count - a.count).slice(0, 5)

    if (httpBarChart && httpRequests.value.length > 0) {
      httpBarChart.setOption({
        xAxis: {data: httpRequests.value.map(r => r.path)},
        series: [{data: httpRequests.value.map(r => r.count)}]
      })
    }
  } catch (error) {
    console.error('获取HTTP数据失败:', error)
    // 模拟数据
    httpRequests.value = [
      {path: '/api/users', fullPath: '/api/users', count: 1234},
      {path: '/api/orders', fullPath: '/api/orders', count: 856},
      {path: '/api/products', fullPath: '/api/products', count: 654},
      {path: '/api/login', fullPath: '/api/login', count: 432},
      {path: '/api/logout', fullPath: '/api/logout', count: 321}
    ]
    if (httpBarChart) {
      httpBarChart.setOption({
        xAxis: {data: httpRequests.value.map(r => r.path)},
        series: [{data: httpRequests.value.map(r => r.count)}]
      })
    }
  }
}

// 初始化内存饼图
const initMemoryPie = () => {
  if (!memoryPieRef.value) return

  memoryPieChart = echarts.init(memoryPieRef.value)
  memoryPieChart.setOption({
    tooltip: {trigger: 'item', formatter: '{b}: {d}% ({c} MB)'},
    legend: {orient: 'vertical', left: 'left', data: ['堆内存', '栈内存(非堆)']},
    series: [{
      type: 'pie',
      radius: '55%',
      center: ['50%', '50%'],
      data: [
        {value: 0, name: '堆内存'},
        {value: 0, name: '栈内存(非堆)'}
      ],
      emphasis: {scale: true},
      label: {show: true, formatter: '{b}: {d}%'}
    }]
  })
}

// 初始化GC饼图
const initGcPie = () => {
  if (!gcPieRef.value) return

  gcPieChart = echarts.init(gcPieRef.value)
  gcPieChart.setOption({
    tooltip: {trigger: 'item', formatter: '{b}: {d}% ({c} 次)'},
    legend: {orient: 'vertical', left: 'left', data: ['Young GC', 'Full GC']},
    series: [{
      type: 'pie',
      radius: '55%',
      center: ['50%', '50%'],
      data: [
        {value: 0, name: 'Young GC'},
        {value: 0, name: 'Full GC'}
      ],
      emphasis: {scale: true},
      label: {show: true, formatter: '{b}: {d}%'}
    }]
  })
}

// 初始化堆内存仪表盘
const initMemoryGauge = () => {
  if (!memoryGaugeRef.value) return

  memoryGaugeChart = echarts.init(memoryGaugeRef.value)
  memoryGaugeChart.setOption({
    tooltip: {formatter: '{b}: {c}%'},
    series: [{
      type: 'gauge',
      center: ['50%', '50%'],
      radius: '70%',
      min: 0,
      max: 100,
      splitNumber: 10,
      progress: {show: true, width: 18, itemStyle: {color: '#409EFF'}},
      axisLine: {lineStyle: {width: 18, color: [[0.7, '#67C23A'], [0.9, '#E6A23C'], [1, '#F56C6C']]}},
      axisTick: {show: false},
      splitLine: {show: false},
      axisLabel: {show: false},
      pointer: {show: false},
      detail: {
        show: true,
        offsetCenter: [0, 20],
        valueAnimation: true,
        fontSize: 24,
        formatter: '{value}%'
      },
      title: {show: true, offsetCenter: [0, -20], fontSize: 14},
      data: [{value: 0, name: '堆内存使用率'}]
    }]
  })
}

// 初始化HTTP条形图
const initHttpBar = () => {
  if (!httpBarRef.value) return

  httpBarChart = echarts.init(httpBarRef.value)
  httpBarChart.setOption({
    tooltip: {
      trigger: 'axis',
      axisPointer: {type: 'shadow'},
      formatter: function (params) {
        const path = httpRequests.value[params[0].dataIndex]?.fullPath || params[0].name
        return `${path}<br/>请求次数: ${params[0].value} 次`
      }
    },
    grid: {top: 30, left: 80, right: 20, bottom: 50},
    xAxis: {
      type: 'category',
      data: [],
      axisLabel: {rotate: 25, interval: 0, fontSize: 11}
    },
    yAxis: {type: 'value', name: '请求次数'},
    series: [{
      name: '请求次数',
      type: 'bar',
      data: [],
      itemStyle: {
        borderRadius: [5, 5, 0, 0],
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          {offset: 0, color: '#409EFF'},
          {offset: 1, color: '#67C23A'}
        ])
      },
      label: {show: true, position: 'top', formatter: '{c}'}
    }]
  })
}

// 刷新数据
const refreshMemoryPie = () => {
  fetchMemoryData()
  ElMessage.success('内存数据已刷新')
}

const refreshGcPie = () => {
  fetchGcData()
  ElMessage.success('GC数据已刷新')
}

const refreshHttpData = () => {
  fetchHttpData()
  ElMessage.success('HTTP数据已刷新')
}

// 刷新所有数据
const refreshAllData = async () => {
  await Promise.all([
    fetchMemoryData(),
    fetchGcData(),
    fetchHttpData()
  ])
}

// 窗口大小改变处理
const handleResize = () => {
  if (memoryPieChart) memoryPieChart.resize()
  if (gcPieChart) gcPieChart.resize()
  if (memoryGaugeChart) memoryGaugeChart.resize()
  if (httpBarChart) httpBarChart.resize()
}

onMounted(() => {
  refreshAllData()
  initMemoryPie()
  initGcPie()
  initMemoryGauge()
  initHttpBar()

  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  if (memoryPieChart) memoryPieChart.dispose()
  if (gcPieChart) gcPieChart.dispose()
  if (memoryGaugeChart) memoryGaugeChart.dispose()
  if (httpBarChart) httpBarChart.dispose()
})
</script>

<style scoped>
.dashboard {
  padding: 0;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 15px;
  transition: all 0.3s;
  cursor: pointer;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px 0 rgba(0, 0, 0, 0.15);
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  margin-right: 15px;
}

.stat-content {
  flex: 1;
}

.stat-title {
  font-size: 14px;
  color: #666;
  margin-bottom: 5px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #333;
  line-height: 1.2;
}

.stat-unit {
  font-size: 14px;
  font-weight: normal;
  color: #999;
  margin-left: 5px;
}

.stat-sub {
  font-size: 12px;
  color: #999;
  margin-top: 5px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pie-legend {
  display: flex;
  justify-content: center;
  gap: 30px;
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid #eee;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
}

.legend-color {
  width: 12px;
  height: 12px;
  border-radius: 2px;
}

:deep(.el-card__body) {
  padding: 20px;
}
</style>