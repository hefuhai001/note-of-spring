<template>
  <div v-loading="loading">
    <el-table :data="data" stripe border style="width: 100%">
      <el-table-column prop="key" label="属性名称" width="300"/>
      <el-table-column prop="value" label="属性值" show-overflow-tooltip/>
    </el-table>
  </div>
</template>

<script setup>
import {onMounted} from 'vue'
import {useSystemProperties} from './composables/useEnvironment'

const {data, loading, fetchData} = useSystemProperties()

onMounted(() => {
  fetchData()
})
</script>