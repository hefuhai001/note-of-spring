<template>
  <div class="environment-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>系统环境信息</span>
          <el-button type="primary" size="small" @click="refreshData">
            <el-icon>
              <Refresh/>
            </el-icon>
            刷新
          </el-button>
        </div>
      </template>

      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="系统属性" name="system">
          <SystemProperties/>
        </el-tab-pane>

        <el-tab-pane label="环境变量" name="env">
          <EnvironmentVars/>
        </el-tab-pane>

        <el-tab-pane label="应用配置" name="config">
          <AppConfigs/>
        </el-tab-pane>

        <el-tab-pane label="JVM信息" name="jvm">
          <JvmInfo/>
        </el-tab-pane>

        <el-tab-pane label="运行时指标" name="runtime">
          <RuntimeMetrics/>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import {ref} from 'vue'
import {Refresh} from '@element-plus/icons-vue'
import SystemProperties from './environment/SystemProperties.vue'
import EnvironmentVars from './environment/EnvironmentVars.vue'
import AppConfigs from './environment/AppConfigs.vue'
import JvmInfo from './environment/JvmInfo.vue'
import RuntimeMetrics from './environment/RuntimeMetrics.vue'
import {useEnvironmentRefresh} from './environment/composables/useEnvironment'

const activeTab = ref('system')
const {refreshAll} = useEnvironmentRefresh()

const refreshData = async () => {
  await refreshAll()
}
</script>

<style scoped>
.environment-container {
  padding: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.box-card {
  width: 100%;
}
</style>