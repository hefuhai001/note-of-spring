<template>
  <div id="app">
    <el-container>
      <el-header>
        <h1>Spring Boot Actuator 监控平台</h1>
      </el-header>
      <el-container>
        <el-aside width="200px">
          <el-menu :default-active="activeMenu" @select="handleMenuSelect">
            <el-menu-item index="dashboard">
              <span>仪表盘</span>
            </el-menu-item>
            <el-menu-item index="metrics">
              <span>性能指标</span>
            </el-menu-item>
            <el-menu-item index="health">
              <span>健康检查</span>
            </el-menu-item>
            <el-menu-item index="environment">
              <span>环境信息</span>
            </el-menu-item>
            <el-menu-item index="business">
              <span>业务监控</span>
            </el-menu-item>
          </el-menu>
        </el-aside>
        <el-main>
          <component :is="currentComponent"></component>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import {ref, shallowRef} from 'vue'
import Dashboard from './components/Dashboard.vue'
import Metrics from './components/Metrics.vue'
import Health from './components/Health.vue'
import Environment from './components/Environment.vue'
import BusinessMonitor from './components/BusinessMonitor.vue'

const activeMenu = ref('dashboard')
const currentComponent = shallowRef(Dashboard)

const handleMenuSelect = (index) => {
  activeMenu.value = index
  const components = {
    dashboard: Dashboard,
    metrics: Metrics,
    health: Health,
    environment: Environment,
    business: BusinessMonitor
  }
  currentComponent.value = components[index]
}
</script>

<style>
#app {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
  height: 100vh;
}

.el-header {
  background-color: #409EFF;
  color: white;
  text-align: center;
  line-height: 60px;
}

.el-aside {
  background-color: #f5f5f5;
  border-right: 1px solid #e0e0e0;
}

.el-main {
  background-color: #f9f9f9;
  padding: 20px;
}
</style>