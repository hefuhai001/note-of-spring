// composables/useMemoryChart.js
import {ref, onUnmounted} from 'vue'
import * as echarts from 'echarts'
import axios from 'axios'

export const useMemoryChart = (chartRef) => {
    let chart = null
    let interval = null

    const initChart = () => {
        // 修复：正确访问 ref 的 value
        if (chartRef.value) {
            chart = echarts.init(chartRef.value)
            const option = {
                tooltip: {
                    trigger: 'axis',
                    formatter: function (params) {
                        return `${params[0].axisValue}<br/>
                    已使用内存: ${params[0].value} MB<br/>
                    已提交内存: ${params[1].value} MB`
                    }
                },
                legend: {
                    data: ['已使用内存', '已提交内存'],
                    top: 0
                },
                grid: {
                    top: 30,
                    left: 50,
                    right: 20,
                    bottom: 20,
                    containLabel: true
                },
                xAxis: {
                    type: 'category',
                    data: [],
                    name: '时间',
                    nameLocation: 'middle',
                    nameGap: 30,
                    axisLabel: {
                        rotate: 30,
                        interval: 5
                    }
                },
                yAxis: {
                    type: 'value',
                    name: '内存 (MB)',
                    nameLocation: 'middle',
                    nameGap: 45,
                    axisLabel: {
                        formatter: '{value} MB'
                    }
                },
                series: [
                    {
                        name: '已使用内存',
                        type: 'line',
                        data: [],
                        smooth: true,
                        lineStyle: {color: '#409EFF', width: 2},
                        areaStyle: {opacity: 0.3, color: '#409EFF'},
                        symbol: 'circle',
                        symbolSize: 6
                    },
                    {
                        name: '已提交内存',
                        type: 'line',
                        data: [],
                        smooth: true,
                        lineStyle: {color: '#67C23A', width: 2},
                        symbol: 'diamond',
                        symbolSize: 6
                    }
                ]
            }
            chart.setOption(option)

            // 立即获取一次数据
            updateChartData()

            // 实时更新图表
            if (interval) clearInterval(interval)
            interval = setInterval(updateChartData, 5000)
        } else {
            console.warn('内存图表容器未找到，延迟初始化')
            // 延迟重试
            setTimeout(() => {
                if (chartRef.value) {
                    initChart()
                }
            }, 500)
        }
    }

    const updateChartData = async () => {
        if (!chart) return

        try {
            // 获取内存数据
            const usedResponse = await axios.get('/actuator/metrics/jvm.memory.used')
            const committedResponse = await axios.get('/actuator/metrics/jvm.memory.committed')
            const maxResponse = await axios.get('/actuator/metrics/jvm.memory.max')

            const usedMemory = (usedResponse.data.measurements?.[0]?.value || 0) / 1024 / 1024
            const committedMemory = (committedResponse.data.measurements?.[0]?.value || 0) / 1024 / 1024
            const maxMemory = (maxResponse.data.measurements?.[0]?.value || 0) / 1024 / 1024
            const now = new Date().toLocaleTimeString()

            const option = chart.getOption()

            // 添加新数据
            option.xAxis[0].data.push(now)
            option.series[0].data.push(usedMemory.toFixed(2))
            option.series[1].data.push(committedMemory.toFixed(2))

            // 限制显示20个数据点
            if (option.xAxis[0].data.length > 20) {
                option.xAxis[0].data.shift()
                option.series[0].data.shift()
                option.series[1].data.shift()
            }

            chart.setOption(option)

            // 可选：更新标题显示当前内存使用率
            const usagePercent = ((usedMemory / maxMemory) * 100).toFixed(1)
            chart.setOption({
                title: {
                    show: true,
                    text: `内存使用率: ${usagePercent}%`,
                    left: 'center',
                    top: 0,
                    textStyle: {fontSize: 12, color: '#666'}
                }
            })
        } catch (error) {
            console.error('更新内存图表失败:', error)
        }
    }

    const resize = () => {
        if (chart) {
            chart.resize()
        }
    }

    const dispose = () => {
        if (interval) {
            clearInterval(interval)
            interval = null
        }
        if (chart) {
            chart.dispose()
            chart = null
        }
    }

    return {initChart, resize, dispose, updateChartData}
}
