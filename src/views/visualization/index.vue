<template>
  <div class="visualization-container">


    <!-- 第一部分：三个饼状图 -->
    <div class="section section-1">
      <h2 class="section-title">资源状态分布</h2>
      <div class="chart-group">
        <div class="chart-item">
          <h3>产品状态分布</h3>
          <div id="productPieChart" class="pie-chart"></div>
        </div>
        <div class="chart-item">
          <h3>设备状态分布</h3>
          <div id="devicePieChart" class="pie-chart"></div>
        </div>
        <div class="chart-item">
          <h3>任务状态分布</h3>
          <div id="taskPieChart" class="pie-chart"></div>
        </div>
      </div>
    </div>

    <!-- 第二部分：两个曲线图 -->
    <div class="section section-2">
      <h2 class="section-title">数据趋势分析</h2>
      <div class="chart-row">
        <div class="chart-item">
          <h3>设备消息数量趋势</h3>
          <div id="messageTrendChart" class="line-chart"></div>
        </div>
      </div>
      <div class="chart-row">
        <div class="chart-item">
          <h3>设备传感器数据</h3>
          <div class="chart-header">
            <el-select v-model="selectedDevice" placeholder="选择设备" style="width: 200px;">
              <el-option
                v-for="device in deviceList"
                :key="device.id"
                :label="device.name + '(' + device.deviceNo + ')'"
                :value="device.id"
              ></el-option>
            </el-select>
          </div>
          <div id="sensorDataChart" class="line-chart"></div>
        </div>
      </div>
    </div>

    <!-- 第三部分：滚动表格 -->
    <div class="section section-3">
      <h2 class="section-title">告警信息</h2>
      <div class="table-container">
        <el-table :data="alarmList" stripe style="width: 100%">
          <el-table-column prop="id" label="ID" width="80"></el-table-column>
          <el-table-column prop="typeLabel" label="类型" width="120"></el-table-column>
          <el-table-column prop="levelLabel" label="级别" width="100">
            <template slot-scope="scope">
              <el-tag :type="scope.row.level === 'urgent' ? 'danger' : scope.row.level === 'serious' ? 'warning' : 'info'">
                {{ scope.row.levelLabel }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="名称"></el-table-column>
          <el-table-column prop="productId" label="产品ID" width="150"></el-table-column>
          <el-table-column prop="deviceId" label="设备ID" width="150"></el-table-column>
          <el-table-column prop="statusLabel" label="状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="scope.row.status === 'processed' ? 'success' : 'warning'">
                {{ scope.row.statusLabel }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="time" label="时间" width="200"></el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import request from '@/utils/request'

export default {
  name: 'DataVisualization',
  data() {
    return {
      selectedDevice: '',
      deviceList: [],
      alarmList: [],
      productData: [],
      deviceData: [],
      taskData: [],
      messageTrendData: [],
      sensorData: []
    }
  },
  mounted() {
    this.initData()
    this.initCharts()
    window.addEventListener('resize', this.resizeCharts)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.resizeCharts)
  },
  methods: {

    async initData() {
      // 获取产品数据
      const productRes = await request({ url: '/vue-admin-template/product/list', method: 'get' })
      this.productData = productRes.data.list

      // 获取设备数据
      const deviceRes = await request({ url: '/device/list', method: 'get' })
      this.deviceData = deviceRes.data.list
      this.deviceList = deviceRes.data.list
      if (this.deviceList.length > 0) {
        this.selectedDevice = this.deviceList[0].id
      }

      // 获取任务数据
      const taskRes = await request({ url: '/api/task/list', method: 'get' })
      this.taskData = taskRes.data.list

      // 获取告警数据
      const alarmRes = await request({ url: '/api/alarm/list', method: 'get', params: { size: 50 } })
      this.alarmList = alarmRes.data.list

      // 模拟消息趋势数据
      this.generateMessageTrendData()
      
      // 模拟传感器数据
      this.generateSensorData()

      // 更新图表
      this.updateCharts()
    },
    generateMessageTrendData() {
      // 模拟最近7天的消息数量趋势
      const dates = []
      const counts = []
      const today = new Date()
      
      for (let i = 6; i >= 0; i--) {
        const date = new Date(today)
        date.setDate(today.getDate() - i)
        dates.push(date.toLocaleDateString())
        counts.push(Math.floor(Math.random() * 100) + 50)
      }
      
      this.messageTrendData = { dates, counts }
    },
    generateSensorData() {
      // 模拟传感器数据（温度和湿度）
      const times = []
      const temperatures = []
      const humidities = []
      
      for (let i = 0; i < 24; i++) {
        times.push(`${i}:00`)
        temperatures.push(Math.floor(Math.random() * 20) + 15) // 15-35度
        humidities.push(Math.floor(Math.random() * 40) + 40) // 40-80%湿度
      }
      
      this.sensorData = { times, temperatures, humidities }
    },
    initCharts() {
      // 初始化饼状图
      this.productPieChart = echarts.init(document.getElementById('productPieChart'))
      this.devicePieChart = echarts.init(document.getElementById('devicePieChart'))
      this.taskPieChart = echarts.init(document.getElementById('taskPieChart'))
      
      // 初始化曲线图
      this.messageTrendChart = echarts.init(document.getElementById('messageTrendChart'))
      this.sensorDataChart = echarts.init(document.getElementById('sensorDataChart'))
    },
    updateCharts() {
      this.updatePieCharts()
      this.updateLineCharts()
    },
    updatePieCharts() {
      // 更新产品饼状图
      const productStatusCount = this.countProductStatus()
      this.productPieChart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '产品状态',
            type: 'pie',
            radius: '50%',
            data: [
              { value: productStatusCount.enabled, name: '已启用' },
              { value: productStatusCount.disabled, name: '未启用' }
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      })

      // 更新设备饼状图
      const deviceStatusCount = this.countDeviceStatus()
      this.devicePieChart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '设备状态',
            type: 'pie',
            radius: '50%',
            data: [
              { value: deviceStatusCount.online, name: '在线' },
              { value: deviceStatusCount.offline, name: '离线' },
              { value: deviceStatusCount.fault, name: '故障' }
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      })

      // 更新任务饼状图
      const taskStatusCount = this.countTaskStatus()
      this.taskPieChart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '任务状态',
            type: 'pie',
            radius: '50%',
            data: [
              { value: taskStatusCount.published, name: '已发布' },
              { value: taskStatusCount.unpublished, name: '未发布' },
              { value: taskStatusCount.processing, name: '进行中' },
              { value: taskStatusCount.completed, name: '已完成' }
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      })
    },
    updateLineCharts() {
      // 更新设备消息数量趋势图
      this.messageTrendChart.setOption({
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['消息数量']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: this.messageTrendData.dates
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '消息数量',
            type: 'line',
            stack: 'Total',
            data: this.messageTrendData.counts,
            smooth: true,
            itemStyle: {
              color: '#36a2eb'
            },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  offset: 0,
                  color: 'rgba(54, 162, 235, 0.5)'
                },
                {
                  offset: 1,
                  color: 'rgba(54, 162, 235, 0.1)'
                }
              ])
            }
          }
        ]
      })

      // 更新设备传感器数据图
      this.sensorDataChart.setOption({
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['温度', '湿度']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: this.sensorData.times
        },
        yAxis: [
          {
            type: 'value',
            name: '温度(°C)',
            position: 'left'
          },
          {
            type: 'value',
            name: '湿度(%)',
            position: 'right'
          }
        ],
        series: [
          {
            name: '温度',
            type: 'line',
            yAxisIndex: 0,
            data: this.sensorData.temperatures,
            smooth: true,
            itemStyle: {
              color: '#ff6384'
            },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  offset: 0,
                  color: 'rgba(255, 99, 132, 0.5)'
                },
                {
                  offset: 1,
                  color: 'rgba(255, 99, 132, 0.1)'
                }
              ])
            }
          },
          {
            name: '湿度',
            type: 'line',
            yAxisIndex: 1,
            data: this.sensorData.humidities,
            smooth: true,
            itemStyle: {
              color: '#4bc0c0'
            },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  offset: 0,
                  color: 'rgba(75, 192, 192, 0.5)'
                },
                {
                  offset: 1,
                  color: 'rgba(75, 192, 192, 0.1)'
                }
              ])
            }
          }
        ]
      })
    },
    countProductStatus() {
      return {
        enabled: this.productData.filter(p => p.status === '已启用').length,
        disabled: this.productData.filter(p => p.status === '未启用').length
      }
    },
    countDeviceStatus() {
      return {
        online: this.deviceData.filter(d => d.status === '在线').length,
        offline: this.deviceData.filter(d => d.status === '离线').length,
        fault: this.deviceData.filter(d => d.status === '故障').length
      }
    },
    countTaskStatus() {
      return {
        published: this.taskData.filter(t => t.status === '已发布').length,
        unpublished: this.taskData.filter(t => t.status === '未发布').length,
        processing: this.taskData.filter(t => t.status === '进行中').length,
        completed: this.taskData.filter(t => t.status === '已完成').length
      }
    },
    resizeCharts() {
      // 饼状图自适应
      this.productPieChart.resize()
      this.devicePieChart.resize()
      this.taskPieChart.resize()
      
      // 曲线图自适应
      this.messageTrendChart.resize()
      this.sensorDataChart.resize()
    }
  },
  watch: {
    selectedDevice() {
      // 当选择设备变化时，重新生成传感器数据
      this.generateSensorData()
      this.updateLineCharts()
    }
  }
}
</script>

<style scoped>
.visualization-container {
  padding: 20px;
  background-color: #f5f7fa;
}



.section {
  margin-bottom: 30px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.section-title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #333;
  border-bottom: 2px solid #e6e9ed;
  padding-bottom: 10px;
}

.chart-group {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
}

.chart-row {
  margin-bottom: 20px;
}

.chart-item {
  margin: 10px;
  padding: 15px;
  background-color: #fafafa;
  border-radius: 8px;
  box-shadow: 0 1px 6px 0 rgba(0, 0, 0, 0.05);
}

.chart-item h3 {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 15px;
  color: #606266;
  text-align: center;
}

.chart-header {
  margin-bottom: 15px;
  display: flex;
  justify-content: flex-end;
}

.pie-chart {
  width: 300px;
  height: 250px;
}

.line-chart {
  width: 100%;
  height: 300px;
}

.table-container {
  max-height: 400px;
  overflow-y: auto;
}

/* 滚动表格样式 */
.table-container::-webkit-scrollbar {
  width: 8px;
}

.table-container::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

.table-container::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 4px;
}

.table-container::-webkit-scrollbar-thumb:hover {
  background: #555;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .chart-group {
    flex-direction: column;
    align-items: center;
  }
  
  .pie-chart {
    width: 250px;
    height: 200px;
  }
}
</style>