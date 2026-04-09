<template>
  <div v-loading="loading">
    <el-table :data="data" stripe border style="width: 100%">
      <el-table-column prop="key" label="变量名称" width="300"/>
      <el-table-column prop="value" label="变量值" show-overflow-tooltip/>
    </el-table>
  </div>
</template>

<script setup>
import {onMounted} from 'vue'
import {useEnvironmentVars} from './composables/useEnvironment'

const {data, loading, fetchData} = useEnvironmentVars()

onMounted(() => {
  fetchData()
})
</script>