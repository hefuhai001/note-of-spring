<template>
  <div v-loading="loading">
    <el-table :data="data" stripe border style="width: 100%">
      <el-table-column prop="key" label="配置项" width="350"/>
      <el-table-column prop="value" label="配置值" show-overflow-tooltip/>
    </el-table>
  </div>
</template>

<script setup>
import {onMounted} from 'vue'
import {useAppConfigs} from './composables/useEnvironment'

const {data, loading, fetchData} = useAppConfigs()

onMounted(() => {
  fetchData()
})
</script>