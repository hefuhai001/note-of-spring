<template>
  <div>
    <el-input
        v-model="searchText"
        placeholder="搜索指标"
        style="width: 300px; margin-bottom: 20px"
        clearable
    />

    <el-table :data="filteredMetrics" stripe style="width: 100%">
      <el-table-column prop="name" label="指标名称" width="300"/>
      <el-table-column prop="value" label="值" width="200">
        <template #default="scope">
          <el-tag v-if="scope.row.value" type="info">
            {{ scope.row.value }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="描述"/>
      <el-table-column label="操作" width="100">
        <template #default="scope">
          <el-button size="small" @click="viewDetails(scope.row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import {ref, computed, onMounted} from 'vue'
import axios from 'axios'
import {ElMessage} from 'element-plus'

const searchText = ref('')
const metrics = ref([])

const filteredMetrics = computed(() => {
  if (!searchText.value) return metrics.value
  return metrics.value.filter(m =>
      m.name.toLowerCase().includes(searchText.value.toLowerCase())
  )
})

const fetchMetrics = async () => {
  try {
    const response = await axios.get('/actuator/metrics')
    const metricNames = response.data.names || []

    const metricsData = []
    for (const name of metricNames.slice(0, 20)) { // 限制数量
      try {
        const detail = await axios.get(`/actuator/metrics/${name}`)
        let value = '-'
        if (detail.data.measurements && detail.data.measurements.length > 0) {
          value = detail.data.measurements[0].value
          if (name.includes('memory')) {
            value = `${(value / 1024 / 1024).toFixed(2)} MB`
          } else if (name.includes('time')) {
            value = `${value.toFixed(2)} ms`
          } else {
            value = value.toFixed(2)
          }
        }

        metricsData.push({
          name: name,
          value: value,
          description: detail.data.description || '无描述',
          measurements: detail.data.measurements
        })
      } catch (error) {
        console.error(`获取指标 ${name} 失败:`, error)
      }
    }

    metrics.value = metricsData
  } catch (error) {
    ElMessage.error('获取指标列表失败')
    console.error(error)
  }
}

const viewDetails = (metric) => {
  ElMessage.info(`指标详情: ${metric.name} = ${metric.value}`)
}

onMounted(() => {
  fetchMetrics()
})
</script>