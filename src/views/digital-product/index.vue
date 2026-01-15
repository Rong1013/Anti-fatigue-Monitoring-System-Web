<template>
  <div class="digital-product-container">
    <div class="top-bar">
      <div class="search-container">
        <el-input
          v-model="searchParams.productId"
          placeholder="输入产品ID"
          class="search-input"
          prefix-icon="el-icon-search"
        />
        <el-input
          v-model="searchParams.productName"
          placeholder="输入产品名称"
          class="search-input"
          prefix-icon="el-icon-search"
        />
        <el-button type="primary" @click="searchProducts" icon="el-icon-search">搜索</el-button>
        <el-button @click="resetSearch" icon="el-icon-refresh">重置</el-button>
      </div>
      <el-button type="success" @click="exportDigitalProduct" icon="el-icon-download">导出数字产品</el-button>
    </div>

    <!-- 产品信息展示区域 -->
    <div class="product-info">
      <div class="product-info-left">
        <div class="info-item">
          <span class="info-label">产品ID：</span>
          <span class="info-value">{{ productData.id }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">产品名称：</span>
          <span class="info-value">{{ productData.name }}</span>
        </div>
      </div>
      <div class="product-info-right">
        <div class="qr-code">
          <!-- 使用qrcodejs2生成二维码 -->
          <div id="product-qr-code" class="qr-code-img"></div>
        </div>
      </div>
    </div>

    <!-- 流程节点轴 -->
    <div class="process-axis">
      <div
        v-for="(node, index) in processNodes"
        :key="node.id"
        class="process-node"
        :class="{ 'active': currentNode === node.id, [`node-status-${node.status}`]: true }"
        @click="switchNode(node.id)"
      >
        <div class="node-icon">
          <i :class="node.icon"></i>
        </div>
        <div class="node-title">{{ node.title }}</div>
        <div class="node-status" :class="node.status">{{ node.status }}</div>
        <div v-if="index < processNodes.length - 1" class="axis-line"></div>
      </div>
    </div>

    <!-- 数据展示区域 -->
    <div class="data-display">
      <!-- 硬度测试数据 -->
      <div v-if="currentNode === 'hardness'" class="node-data">
        <div class="section-card">
          <h3 class="section-title">硬度测试数据</h3>
          
          <!-- 加工信息 -->
          <div class="data-section">
            <h4 class="section-subtitle">加工信息</h4>
            <div class="info-grid">
              <div class="info-item">
                <span class="info-label">设备</span>
                <span class="info-value">{{ hardnessData.techInfo.device }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">测试方法</span>
                <span class="info-value">{{ hardnessData.techInfo.method }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">测试人员</span>
                <span class="info-value">{{ hardnessData.techInfo.operator }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">工艺流程</span>
                <span class="info-value">{{ hardnessData.techInfo.processFlow }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">实验地点</span>
                <span class="info-value">{{ hardnessData.techInfo.location }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">加工式样</span>
                <span class="info-value">{{ hardnessData.techInfo.sample }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">开始时间</span>
                <span class="info-value">{{ hardnessData.techInfo.startTime }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">结束时间</span>
                <span class="info-value">{{ hardnessData.techInfo.endTime }}</span>
              </div>
            </div>
          </div>

          <!-- 工艺参数 -->
          <div class="data-section">
            <h4 class="section-subtitle">工艺参数</h4>
            <div class="info-grid">
              <div class="info-item" v-for="(param, index) in hardnessData.processParams" :key="index">
                <span class="info-label">{{ param.paramName }}（{{ param.unit }}）：</span>
                <span class="info-value">{{ param.paramValue }}</span>
                <span v-if="param.standard" class="info-standard">（标准：{{ param.standard }}）</span>
                <span class="info-status" :class="param.status">{{ param.status }}</span>
              </div>
            </div>
          </div>

          <!-- 环境参数 -->
          <div class="data-section">
            <h4 class="section-subtitle">环境参数</h4>
            <div class="info-grid">
              <div class="info-item">
                <span class="info-label">温度</span>
                <span class="info-value">{{ hardnessData.envInfo.temperature }} °C</span>
              </div>
              <div class="info-item">
                <span class="info-label">湿度</span>
                <span class="info-value">{{ hardnessData.envInfo.humidity }}%</span>
              </div>
              <div class="info-item">
                <span class="info-label">气压</span>
                <span class="info-value">{{ hardnessData.envInfo.pressure }} kPa</span>
              </div>
              <div class="info-item">
                <span class="info-label">光照</span>
                <span class="info-value">{{ hardnessData.envInfo.illumination }}</span>
              </div>
            </div>
          </div>

          <!-- 实验图片 -->
          <div class="data-section">
            <h4 class="section-subtitle">实验图片</h4>
            <div class="image-table-container">
              <el-table :data="formattedImages" stripe style="width: 100%">
                <el-table-column label="图片名称" width="200">
                  <template slot-scope="scope">
                    {{ scope.row.image1.name.replace(/\.png$/, '') }}
                  </template>
                </el-table-column>
                <el-table-column label="图片" align="center">
                  <template slot-scope="scope">
                    <el-image
                      :src="scope.row.image1.url"
                      :preview-src-list="hardnessData.images.map(item => item.url)"
                      class="table-image"
                      fit="cover"
                    >
                      <div slot="error" class="image-slot">
                        <i class="el-icon-picture-outline"></i>
                      </div>
                    </el-image>
                  </template>
                </el-table-column>
                <el-table-column label="图片名称" width="200">
                  <template slot-scope="scope">
                    {{ scope.row.image2 ? scope.row.image2.name.replace(/\.png$/, '') : '' }}
                  </template>
                </el-table-column>
                <el-table-column label="图片" align="center">
                  <template slot-scope="scope">
                    <el-image
                      v-if="scope.row.image2"
                      :src="scope.row.image2.url"
                      :preview-src-list="hardnessData.images.map(item => item.url)"
                      class="table-image"
                      fit="cover"
                    >
                      <div slot="error" class="image-slot">
                        <i class="el-icon-picture-outline"></i>
                      </div>
                    </el-image>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>

          <!-- 加工视频 -->
          <div class="data-section">
            <h4 class="section-subtitle">加工视频</h4>
            <div class="video-container">
              <video width="100%" height="360" controls>
                <source :src="hardnessData.video.url" type="video/mp4">
                您的浏览器不支持视频播放。
              </video>
              <div class="video-name">{{ hardnessData.video.name }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 三维形貌测试数据 -->
      <div v-if="currentNode === 'morphology'" class="node-data">
        <div class="section-card">
          <h3 class="section-title">三维形貌测试数据</h3>
          
          <!-- 加工信息 -->
          <div class="data-section">
            <h4 class="section-subtitle">加工信息</h4>
            <div class="info-grid">
              <div class="info-item">
                <span class="info-label">设备</span>
                <span class="info-value">{{ morphologyData.techInfo.device }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">精度</span>
                <span class="info-value">{{ morphologyData.techInfo.precision }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">测量范围</span>
                <span class="info-value">{{ morphologyData.techInfo.scope }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">测试人员</span>
                <span class="info-value">{{ morphologyData.techInfo.operator }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">开始时间</span>
                <span class="info-value">{{ morphologyData.techInfo.startTime }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">结束时间</span>
                <span class="info-value">{{ morphologyData.techInfo.endTime }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">加工式样</span>
                <span class="info-value">{{ morphologyData.techInfo.sample }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">实验地点</span>
                <span class="info-value">{{ morphologyData.techInfo.location }}</span>
              </div>
            </div>
          </div>

          <!-- 工艺参数 -->
          <div class="data-section">
            <h4 class="section-subtitle">工艺参数</h4>
            <div class="info-grid">
              <div class="info-item" v-for="(param, index) in morphologyData.processParams" :key="index">
                <span class="info-label">{{ param.paramName }}（{{ param.unit }}）：</span>
                <span class="info-value">{{ param.paramValue }}</span>
                <span v-if="param.standard" class="info-standard">（标准：{{ param.standard }}）</span>
                <span class="info-status" :class="param.status">{{ param.status }}</span>
              </div>
            </div>
          </div>

          <!-- 环境参数 -->
          <div class="data-section">
            <h4 class="section-subtitle">环境参数</h4>
            <div class="info-grid">
              <div class="info-item">
                <span class="info-label">温度</span>
                <span class="info-value">{{ morphologyData.envInfo.temperature }} °C</span>
              </div>
              <div class="info-item">
                <span class="info-label">湿度</span>
                <span class="info-value">{{ morphologyData.envInfo.humidity }}%</span>
              </div>
              <div class="info-item">
                <span class="info-label">气压</span>
                <span class="info-value">{{ morphologyData.envInfo.pressure }} kPa</span>
              </div>
              <div class="info-item">
                <span class="info-label">光照</span>
                <span class="info-value">{{ morphologyData.envInfo.illumination }}</span>
              </div>
            </div>
          </div>

          <!-- 实验图片 -->
          <div class="data-section">
            <h4 class="section-subtitle">实验图片</h4>
            <div class="image-table-container">
              <el-table :data="formattedMorphologyImages" stripe style="width: 100%">
                <el-table-column label="图片名称" width="200">
                  <template slot-scope="scope">
                    {{ scope.row.image1.name.replace(/\.png$/, '') }}
                  </template>
                </el-table-column>
                <el-table-column label="图片" align="center">
                  <template slot-scope="scope">
                    <el-image
                      :src="scope.row.image1.url"
                      :preview-src-list="morphologyData.images.map(item => item.url)"
                      class="table-image"
                      fit="cover"
                    >
                      <div slot="error" class="image-slot">
                        <i class="el-icon-picture-outline"></i>
                      </div>
                    </el-image>
                  </template>
                </el-table-column>
                <el-table-column label="图片名称" width="200">
                  <template slot-scope="scope">
                    {{ scope.row.image2 ? scope.row.image2.name.replace(/\.png$/, '') : '' }}
                  </template>
                </el-table-column>
                <el-table-column label="图片" align="center">
                  <template slot-scope="scope">
                    <el-image
                      v-if="scope.row.image2"
                      :src="scope.row.image2.url"
                      :preview-src-list="morphologyData.images.map(item => item.url)"
                      class="table-image"
                      fit="cover"
                    >
                      <div slot="error" class="image-slot">
                        <i class="el-icon-picture-outline"></i>
                      </div>
                    </el-image>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>

          <!-- 加工视频 -->
          <div class="data-section">
            <h4 class="section-subtitle">加工视频</h4>
            <div class="video-container">
              <video width="100%" height="360" controls>
                <source :src="morphologyData.video.url" type="video/mp4">
                您的浏览器不支持视频播放。
              </video>
              <div class="video-name">{{ morphologyData.video.name }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 残余应力测试数据 -->
      <div v-if="currentNode === 'stress'" class="node-data">
        <div class="section-card">
          <h3 class="section-title">残余应力测试数据</h3>
          
          <!-- 加工信息 -->
          <div class="data-section">
            <h4 class="section-subtitle">加工信息</h4>
            <div class="info-grid">
              <div class="info-item">
                <span class="info-label">设备</span>
                <span class="info-value">{{ stressData.techInfo.device }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">测试方法</span>
                <span class="info-value">{{ stressData.techInfo.method }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">测试人员</span>
                <span class="info-value">{{ stressData.techInfo.operator }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">开始时间</span>
                <span class="info-value">{{ stressData.techInfo.startTime }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">结束时间</span>
                <span class="info-value">{{ stressData.techInfo.endTime }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">加工式样</span>
                <span class="info-value">{{ stressData.techInfo.sample }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">实验地点</span>
                <span class="info-value">{{ stressData.techInfo.location }}</span>
              </div>
            </div>
          </div>

          <!-- 工艺参数 -->
          <div class="data-section">
            <h4 class="section-subtitle">工艺参数</h4>
            <div class="info-grid">
              <div class="info-item" v-for="(param, index) in stressData.processParams" :key="index">
                <span class="info-label">{{ param.paramName }}（{{ param.unit }}）：</span>
                <span class="info-value">{{ param.paramValue }}</span>
                <span v-if="param.standard" class="info-standard">（标准：{{ param.standard }}）</span>
                <span class="info-status" :class="param.status">{{ param.status }}</span>
              </div>
            </div>
          </div>

          <!-- 环境参数 -->
          <div class="data-section">
            <h4 class="section-subtitle">环境参数</h4>
            <div class="info-grid">
              <div class="info-item">
                <span class="info-label">温度</span>
                <span class="info-value">{{ stressData.envInfo.temperature }} °C</span>
              </div>
              <div class="info-item">
                <span class="info-label">湿度</span>
                <span class="info-value">{{ stressData.envInfo.humidity }}%</span>
              </div>
              <div class="info-item">
                <span class="info-label">气压</span>
                <span class="info-value">{{ stressData.envInfo.pressure }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">光照</span>
                <span class="info-value">{{ stressData.envInfo.illumination }}</span>
              </div>
            </div>
          </div>

          <!-- 实验图片 -->
          <div class="data-section">
            <h4 class="section-subtitle">实验图片</h4>
            <div class="image-table-container">
              <el-table :data="formattedStressImages" stripe style="width: 100%">
                <el-table-column label="图片名称" width="200">
                  <template slot-scope="scope">
                    {{ scope.row.image1.name.replace(/\.png$/, '') }}
                  </template>
                </el-table-column>
                <el-table-column label="图片" align="center">
                  <template slot-scope="scope">
                    <el-image
                      :src="scope.row.image1.src"
                      :preview-src-list="stressData.images.map(item => item.src)"
                      class="table-image"
                      fit="cover"
                    >
                      <div slot="error" class="image-slot">
                        <i class="el-icon-picture-outline"></i>
                      </div>
                    </el-image>
                  </template>
                </el-table-column>
                <el-table-column label="图片名称" width="200">
                  <template slot-scope="scope">
                    {{ scope.row.image2 ? scope.row.image2.name.replace(/\.png$/, '') : '' }}
                  </template>
                </el-table-column>
                <el-table-column label="图片" align="center">
                  <template slot-scope="scope">
                    <el-image
                      v-if="scope.row.image2"
                      :src="scope.row.image2.src"
                      :preview-src-list="stressData.images.map(item => item.src)"
                      class="table-image"
                      fit="cover"
                    >
                      <div slot="error" class="image-slot">
                        <i class="el-icon-picture-outline"></i>
                      </div>
                    </el-image>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>

          <!-- 加工视频 -->
          <div class="data-section">
            <h4 class="section-subtitle">加工视频</h4>
            <div class="video-container">
              <video width="100%" height="360" controls>
                <source :src="stressData.video.src" type="video/mp4">
                您的浏览器不支持视频播放。
              </video>
              <div class="video-name">{{ stressData.video.name }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import QRCode from 'qrcodejs2'
import jsPDF from 'jspdf'
import JSZip from 'jszip'
import { saveAs } from 'file-saver'
import html2canvas from 'html2canvas'

export default {
  name: 'DigitalProduct',
  data() {
    return {
      // 搜索参数
      searchParams: {
        productId: '',
        productName: ''
      },
      
      // 产品信息
      productData: {
        id: '123456ABCD12345678901234567890XY', // 32位混合编码
        name: '齿轮测试'
      },
      
      // 流程节点数据
      processNodes: [
        {
          id: 'hardness',
          title: '硬度测试',
          status: '已完成',
          icon: 'el-icon-medal-1'
        },
        {
          id: 'morphology',
          title: '三维形貌测试',
          status: '已完成',
          icon: 'el-icon-picture-outline-round'
        },
        {
          id: 'stress',
          title: '残余应力测试',
          status: '已完成',
          icon: 'el-icon-data-analysis'
        }
      ],
      
      // 当前选中节点
      currentNode: 'hardness',
      
      // 硬度测试数据
      hardnessData: {
        processParams: [
          { paramName: '硬度值', paramValue: '58', unit: 'HRC', standard: '55-62', status: '正常' },
          { paramName: '测试力', paramValue: '150', unit: 'kgf', standard: '150', status: '正常' },
          { paramName: '测试时间', paramValue: '15', unit: 's', standard: '10-20', status: '正常' },
          { paramName: '压痕直径', paramValue: '2.5', unit: 'mm', standard: '2.0-3.0', status: '正常' },
          // 合并齿轮功能测试的工艺参数
          { paramName: '实验负荷', paramValue: '0.5', unit: 'KN', standard: '', status: '正常' },
          { paramName: '测点深度', paramValue: '200', unit: 'μm', standard: '', status: '正常' },
          { paramName: '压痕深度', paramValue: '811.09', unit: 'μm', standard: '', status: '正常' }
        ],
        techInfo: {
          device: '洛氏硬度计HR-150A',
          method: '洛氏硬度测试法',
          standard: 'GB/T 230.1-2018',
          operator: '姜钦露',
          testTime: '2023-10-15 14:30:00',
          // 合并齿轮功能测试的工艺信息
          processFlow: '硬度测试',
          location: '硬度测试室301',
          sample: '长齿轮4号',
          startTime: '2025-10-15 14:30:00',
          endTime: '2025-10-15 15:00:00'
        },
        envInfo: {
          temperature: 25,
          humidity: 45,
          pressure: 101.3,
          // 合并齿轮功能测试的环境信息
          illumination: '500 lux'
        },
        images: [
          { url: require('@/assets/DigitalProduct Data/硬度计/加工图片/仪表盘图片.png'), name: '仪表盘图片.png' },
          { url: require('@/assets/DigitalProduct Data/硬度计/加工图片/左节圆图片.png'), name: '左节圆图片.png' },
          { url: require('@/assets/DigitalProduct Data/硬度计/加工图片/测试前式样图片.png'), name: '测试前式样图片.png' },
          { url: require('@/assets/DigitalProduct Data/硬度计/加工图片/测试前式样表面图片.png'), name: '测试前式样表面图片.png' },
          { url: require('@/assets/DigitalProduct Data/硬度计/加工图片/配重刻度图片.png'), name: '配重刻度图片.png' },
          { url: require('@/assets/DigitalProduct Data/硬度计/加工图片/齿顶圆测试面图片.png'), name: '齿顶圆测试面图片.png' }
        ],
        // 合并齿轮功能测试的视频
        video: {
          url: require('@/assets/DigitalProduct Data/硬度计/加工过程视频.mp4'),
          name: '加工过程视频'
        }
      },
      
      // 三维形貌测试数据
      morphologyData: {
        processParams: [
          { paramName: '表面粗糙度', paramValue: '0.8', unit: 'μm', standard: '≤1.0', status: '正常' },
          { paramName: '轮廓算术平均偏差', paramValue: '0.75', unit: 'μm', standard: '≤0.8', status: '正常' },
          { paramName: '最大轮廓高度', paramValue: '5.2', unit: 'μm', standard: '≤6.0', status: '正常' },
          { paramName: '扫描速度', paramValue: '2', unit: 'mm/s', standard: '1-3', status: '正常' }
        ],
        techInfo: {
          device: '三维形貌仪ContourGT-X8',
          precision: '0.01 μm',
          scope: '10mm×10mm',
          operator: '姜钦露',
          startTime: '2025-10-16 15:45:00',
          endTime: '2025-10-16 16:45:00',
          sample: '长齿轮4号',
          location: '三维形貌室302'
        },
        envInfo: {
          temperature: 26,
          humidity: 42,
          pressure: 101.2,
          illumination: '500 lux'
        },
        images: [
          { url: require('@/assets/DigitalProduct Data/三维型貌仪/加工图片/拼接尺寸.png'), name: '拼接尺寸.png' },
          { url: require('@/assets/DigitalProduct Data/三维型貌仪/加工图片/测试后云图.png'), name: '测试后云图.png' },
          { url: require('@/assets/DigitalProduct Data/三维型貌仪/加工图片/测量参数.png'), name: '测量参数.png' },
          { url: require('@/assets/DigitalProduct Data/三维型貌仪/加工图片/目镜与物镜参数.png'), name: '目镜与物镜参数.png' },
          { url: require('@/assets/DigitalProduct Data/三维型貌仪/加工图片/表面干涉纹.png'), name: '表面干涉纹.png' }
        ],
        video: {
          url: require('@/assets/DigitalProduct Data/三维型貌仪/三维形貌测量过程视频.mp4'),
          name: '三维形貌测量过程视频'
        }
      },
      
      // 残余应力测试数据
      stressData: {
        processParams: [
          { paramName: '表面残余应力', paramValue: '-120', unit: 'MPa', standard: '-150~-100', status: '正常' },
          { paramName: '次表面残余应力', paramValue: '-80', unit: 'MPa', standard: '-100~-50', status: '正常' },
          { paramName: '测试深度', paramValue: '50', unit: 'μm', standard: '0-100', status: '正常' },
          { paramName: 'X射线管电压', paramValue: '30', unit: 'kV', standard: '25-35', status: '正常' },
          { paramName: 'X射线管电流', paramValue: '5', unit: 'mA', standard: '3-7', status: '正常' }
        ],
        techInfo: {
          device: 'X射线残余应力分析仪X-350',
          method: 'X射线衍射法',
          operator: '姜钦露',
          startTime: '2025-10-17 15:45:00',
          endTime: '2025-10-17 16:45:00',
          sample: '长齿轮4号',
          location: '残余应力室304'
        },
        envInfo: {
          temperature: 24,
          humidity: 48,
          pressure: '101.3 kPa',
          illumination: '300 lux'
        },
        images: [
          { src: require('@/assets/DigitalProduct Data/残余应力/加工图片/残余应力数据图像.png'), name: '残余应力数据图像' },
          { src: require('@/assets/DigitalProduct Data/残余应力/加工图片/背底片.png'), name: '背底片' },
          { src: require('@/assets/DigitalProduct Data/残余应力/加工图片/轮廓线与背底线.png'), name: '轮廓线与背底线' },
          { src: require('@/assets/DigitalProduct Data/残余应力/加工图片/靶探头.png'), name: '靶探头' }
        ],
        video: {
          src: require('@/assets/DigitalProduct Data/残余应力/三维形貌测量过程视频.mp4'),
          name: '三维形貌测量过程视频'
        }
      }
    }
  },
  
  computed: {
    formattedImages() {
      const images = this.hardnessData.images;
      const result = [];
      // 将图片两两分组
      for (let i = 0; i < images.length; i += 2) {
        result.push({
          image1: images[i],
          image2: images[i + 1]
        });
      }
      return result;
    },
    formattedMorphologyImages() {
      // 将morphologyData.images数组转换为每行两张图片的格式
      const formatted = [];
      for (let i = 0; i < this.morphologyData.images.length; i += 2) {
        formatted.push({
          image1: this.morphologyData.images[i],
          image2: this.morphologyData.images[i + 1] || null
        });
      }
      return formatted;
    },
    formattedStressImages() {
      // 将stressData.images数组转换为每行两张图片的格式
      const formatted = [];
      for (let i = 0; i < this.stressData.images.length; i += 2) {
        formatted.push({
          image1: this.stressData.images[i],
          image2: this.stressData.images[i + 1] || null
        });
      }
      return formatted;
    },
    productQrCodeContent() {
      // 生成包含产品信息的二维码内容
      return JSON.stringify({
        productId: this.productData.id,
        productName: this.productData.name,
        timestamp: new Date().toISOString(),
        type: 'digital_product'
      });
    }
  },
  
  methods: {
    // 搜索产品
    searchProducts() {
      this.$message({
        message: '搜索功能已触发，产品ID：' + this.searchParams.productId + '，产品名称：' + this.searchParams.productName,
        type: 'info'
      })
    },
    
    // 重置搜索
    resetSearch() {
      this.searchParams = {
        productId: '',
        productName: ''
      }
      this.$message({
        message: '搜索条件已重置',
        type: 'success'
      })
    },
    
    // 导出数字产品
    async exportDigitalProduct() {
      if (!this.productData.id) {
        this.$message.error('产品信息不完整，无法导出')
        return
      }
      
      this.exportLoading = true
      
      try {
        // 生成PDF文件
        const pdf = await this.generatePDF()
        
        // 创建压缩包
        const zip = new JSZip()
        const zipFileName = `${this.productData.id}.zip`
        
        // 将PDF添加到压缩包
        zip.file(`${this.productData.id}.pdf`, pdf.output('blob'))
        
        // 收集并添加多媒体文件
        await this.addMediaFilesToZip(zip)
        
        // 生成压缩包并下载
        const content = await zip.generateAsync({ type: 'blob' })
        
        // 使用file-saver保存文件
        saveAs(content, zipFileName)
        
        this.$message.success('数字产品信息导出成功')
      } catch (error) {
        console.error('导出数字产品信息失败:', error)
        this.$message.error('导出失败，请重试')
      } finally {
        this.exportLoading = false
      }
    },
    
    // 生成PDF文件
    generatePDF() {
      // 创建一个临时的HTML元素来构建PDF内容
      const tempDiv = document.createElement('div')
      tempDiv.id = 'temp-pdf-content'
      tempDiv.style.position = 'absolute'
      tempDiv.style.left = '-9999px'
      tempDiv.style.width = '794px' // A4横向宽度(像素)
      tempDiv.style.padding = '20px'
      tempDiv.style.fontFamily = 'SimSun, serif' // 使用宋体作为默认字体
      document.body.appendChild(tempDiv)
      
      // 构建HTML内容
      let htmlContent = `
        <div style="margin-bottom: 30px;">
          <h1 style="text-align: center; font-size: 24px; margin-bottom: 20px; color: #333;">数字产品信息</h1>
          <div style="display: flex; justify-content: space-between; margin-bottom: 20px;">
            <div>
              <p style="font-size: 16px; margin: 8px 0;"><strong>产品ID:</strong> ${this.productData.id}</p>
              <p style="font-size: 16px; margin: 8px 0;"><strong>产品名称:</strong> ${this.productData.name}</p>
              <p style="font-size: 16px; margin: 8px 0;"><strong>导出时间:</strong> ${new Date().toLocaleString()}</p>
            </div>
            <div id="pdf-qr-code" style="margin-right: 50px;"></div>
          </div>
        </div>`
      
      // 添加各个流程节点数据
      this.processNodes.forEach((node) => {
        // 获取节点数据
        let nodeData
        switch (node.id) {
          case 'hardness':
            nodeData = this.hardnessData
            break
          case 'morphology':
            nodeData = this.morphologyData
            break
          case 'stress':
            nodeData = this.stressData
            break
          default:
            nodeData = {}
        }
        
        // 添加节点标题
        htmlContent += `<div style="margin-bottom: 30px; padding-bottom: 15px; border-bottom: 1px solid #000;">
          <h2 style="font-size: 20px; margin: 15px 0; color: #333;">${node.title}数据</h2>`
        
        // 添加工艺参数
        if (nodeData.processParams && nodeData.processParams.length > 0) {
          htmlContent += `<div style="margin-bottom: 20px;">
            <h3 style="font-size: 18px; margin: 15px 0; color: #555;">工艺参数</h3>
            <table style="border-collapse: collapse; width: 100%;">`
          
          nodeData.processParams.forEach((param) => {
            htmlContent += `<tr>
              <td style="padding: 10px 15px; border-bottom: 1px solid #eee; width: 60%;"><strong>${param.paramName}:</strong> ${param.paramValue} ${param.unit}</td>
              <td style="padding: 10px 15px; border-bottom: 1px solid #eee; width: 40%; text-align: right;"><strong>标准:</strong> ${param.standard} <strong>状态:</strong> ${param.status}</td>
            </tr>`
          })
          
          htmlContent += `</table>
          </div>`
        }
        
        // 添加工艺信息
        if (nodeData.techInfo) {
          htmlContent += `<div style="margin-bottom: 20px;">
            <h3 style="font-size: 18px; margin: 15px 0; color: #555;">工艺信息</h3>
            <table style="border-collapse: collapse; width: 100%;">`
          
          // 获取工艺信息键值对
          const techInfoEntries = Object.entries(nodeData.techInfo)
          for (let i = 0; i < techInfoEntries.length; i += 2) {
            const leftEntry = techInfoEntries[i]
            const rightEntry = techInfoEntries[i + 1]
            
            htmlContent += `<tr>`
            if (leftEntry) {
              const [key, value] = leftEntry
              htmlContent += `<td style="padding: 10px 15px; border-bottom: 1px solid #eee; width: 50%;"><strong>${this.formatKeyName(key)}:</strong> ${value}</td>`
            } else {
              htmlContent += `<td style="padding: 10px 15px; border-bottom: 1px solid #eee; width: 50%;"></td>`
            }
            
            if (rightEntry) {
              const [key, value] = rightEntry
              htmlContent += `<td style="padding: 10px 15px; border-bottom: 1px solid #eee; width: 50%;"><strong>${this.formatKeyName(key)}:</strong> ${value}</td>`
            } else {
              htmlContent += `<td style="padding: 10px 15px; border-bottom: 1px solid #eee; width: 50%;"></td>`
            }
            htmlContent += `</tr>`
          }
          
          htmlContent += `</table>
          </div>`
        }
        
        // 添加环境信息
        if (nodeData.envInfo) {
          htmlContent += `<div style="margin-bottom: 20px;">
            <h3 style="font-size: 18px; margin: 15px 0; color: #555;">环境信息</h3>
            <table style="border-collapse: collapse; width: 100%;">`
          
          // 获取环境信息键值对
          const envInfoEntries = Object.entries(nodeData.envInfo)
          for (let i = 0; i < envInfoEntries.length; i += 2) {
            const leftEntry = envInfoEntries[i]
            const rightEntry = envInfoEntries[i + 1]
            
            htmlContent += `<tr>`
            if (leftEntry) {
              const [key, value] = leftEntry
              htmlContent += `<td style="padding: 10px 15px; border-bottom: 1px solid #eee; width: 50%;"><strong>${this.formatKeyName(key)}:</strong> ${value}</td>`
            } else {
              htmlContent += `<td style="padding: 10px 15px; border-bottom: 1px solid #eee; width: 50%;"></td>`
            }
            
            if (rightEntry) {
              const [key, value] = rightEntry
              htmlContent += `<td style="padding: 10px 15px; border-bottom: 1px solid #eee; width: 50%;"><strong>${this.formatKeyName(key)}:</strong> ${value}</td>`
            } else {
              htmlContent += `<td style="padding: 10px 15px; border-bottom: 1px solid #eee; width: 50%;"></td>`
            }
            htmlContent += `</tr>`
          }
          
          htmlContent += `</table>
          </div>`
        }
        
        htmlContent += `</div>`
      })
      
      // 将HTML内容添加到临时元素
      tempDiv.innerHTML = htmlContent
      
      // 添加二维码到临时元素
      try {
        // 获取页面上已生成的二维码
        const qrCodeElement = document.getElementById('product-qr-code')
        if (qrCodeElement) {
          const qrImg = qrCodeElement.querySelector('img')
          if (qrImg) {
            // 将二维码添加到PDF右上角
            const pdfQrDiv = tempDiv.querySelector('#pdf-qr-code')
            const newQrImg = document.createElement('img')
            newQrImg.src = qrImg.src
            newQrImg.width = 100
            newQrImg.height = 100
            pdfQrDiv.appendChild(newQrImg)
          }
        }
      } catch (error) {
        console.error('添加二维码到PDF失败:', error)
      }
      
      // 使用html2canvas将临时元素转换为canvas
      return html2canvas(tempDiv, {
        scale: 2, // 提高分辨率
        useCORS: true, // 允许跨域图片
        logging: false
      }).then(canvas => {
        // 创建PDF实例
        const pdf = new jsPDF('l', 'mm', 'a4')
        
        // 计算PDF页面的宽高
        const imgWidth = 280
        const pageHeight = 210
        const imgHeight = (canvas.height * imgWidth) / canvas.width
        let heightLeft = imgHeight
        let position = 0
        
        // 将canvas转换为图像并添加到PDF
        const imgData = canvas.toDataURL('image/png')
        
        // 添加第一页
        pdf.addImage(imgData, 'PNG', 10, position, imgWidth, imgHeight)
        heightLeft -= pageHeight
        
        // 如果内容超过一页，添加更多页面
        while (heightLeft >= 0) {
          position = heightLeft - imgHeight
          pdf.addPage()
          pdf.addImage(imgData, 'PNG', 10, position, imgWidth, imgHeight)
          heightLeft -= pageHeight
        }
        
        // 清理临时元素
        document.body.removeChild(tempDiv)
        
        // 返回PDF实例
        return pdf
      }).catch(error => {
        console.error('生成PDF失败:', error)
        // 清理临时元素
        document.body.removeChild(tempDiv)
        throw error
      })
    },
    
    // 辅助方法：添加文本到PDF（处理中文）
    addTextToPdf(pdf, text, fontSize, x, y, align = 'left') {
      // 使用jsPDF的text方法，在旧版本中这是处理中文的基本方法
      pdf.setFontSize(fontSize)
      pdf.text(text, x, y, { align })
    },
    
    // 辅助方法：添加章节标题
    addSectionHeader(pdf, title, yPosition) {
      yPosition += 5
      this.addTextToPdf(pdf, title, 14, 25, yPosition)
      yPosition += 8
      return yPosition
    },
    
    // 辅助方法：检查页面是否需要换行
    checkPageBreak(pdf, yPosition) {
      if (yPosition > 190) {
        pdf.addPage()
        yPosition = 20
      }
      return yPosition
    },
    
    // 辅助方法：生成PDF用的二维码
    generateQrCodeForPdf() {
      try {
        // 创建一个临时的canvas元素
        const canvas = document.createElement('canvas')
        canvas.width = 80
        canvas.height = 80
        const ctx = canvas.getContext('2d')
        
        // 绘制二维码（这里我们使用简化的方式，直接绘制产品ID的二维码）
        // 注意：实际项目中应该使用qrjs2库来生成更可靠的二维码
        ctx.fillStyle = '#ffffff'
        ctx.fillRect(0, 0, 80, 80)
        ctx.fillStyle = '#000000'
        ctx.font = '8px Arial'
        ctx.textAlign = 'center'
        ctx.fillText('QR Code', 40, 40)
        
        // 返回Data URL
        return canvas.toDataURL('image/png')
      } catch (error) {
        console.error('生成PDF二维码失败:', error)
        return null
      }
    },
    
    formatKeyName(key) {
      const keyMap = {
        device: '设备',
        method: '方法',
        standard: '标准',
        operator: '操作员',
        testTime: '测试时间',
        processFlow: '工艺流程',
        location: '位置',
        sample: '样品',
        startTime: '开始时间',
        endTime: '结束时间',
        precision: '精度',
        scope: '测量范围',
        temperature: '温度',
        humidity: '湿度',
        pressure: '压力',
        illumination: '光照度'
      }
      return keyMap[key] || key
    },
    
    // 将媒体文件添加到压缩包
    async addMediaFilesToZip(zip) {
      // 遍历各个流程节点
      for (const node of this.processNodes) {
        // 创建节点文件夹
        const nodeFolder = zip.folder(`${node.id}-${node.title}`)
        
        // 获取节点数据
        let nodeData
        switch (node.id) {
          case 'hardness':
            nodeData = this.hardnessData
            break
          case 'morphology':
            nodeData = this.morphologyData
            break
          case 'stress':
            nodeData = this.stressData
            break
          default:
            continue
        }
        
        // 添加图片文件
        if (nodeData.images && nodeData.images.length > 0) {
          const imagesFolder = nodeFolder.folder('图片')
          for (const image of nodeData.images) {
            try {
              const imageData = await this.fetchFileData(image.url || image.src)
              const fileName = image.name || `image-${Date.now()}.png`
              imagesFolder.file(fileName, imageData)
            } catch (error) {
              console.error(`获取图片失败：${image.name || image.url}`, error)
            }
          }
        }
        
        // 添加视频文件
        if (nodeData.video) {
          try {
            const videoData = await this.fetchFileData(nodeData.video.url || nodeData.video.src)
            const fileName = nodeData.video.name || `video-${Date.now()}.mp4`
            const videosFolder = nodeFolder.folder('视频')
            videosFolder.file(fileName, videoData)
          } catch (error) {
            console.error(`获取视频失败：${nodeData.video.name || nodeData.video.url}`, error)
          }
        }
      }
    },
    
    // 从URL获取文件数据
    fetchFileData(url) {
      return new Promise((resolve, reject) => {
        // 如果是本地资源，需要转换路径
        if (url.startsWith('require(')) {
          // 这是一个require语句，需要提取实际的文件路径
          const filePath = url.match(/require\('([^']+)'\)/)[1]
          // 对于本地资源，我们需要使用fetch API获取数据
          fetch(filePath)
            .then(response => response.blob())
            .then(blob => resolve(blob))
            .catch(error => reject(error))
        } else {
          // 直接获取远程资源
          fetch(url)
            .then(response => response.blob())
            .then(blob => resolve(blob))
            .catch(error => reject(error))
        }
      })
    },
    
    // 切换流程节点
    switchNode(nodeId) {
      this.currentNode = nodeId
      this.$message({
        message: '已切换到' + this.getNodeTitle(nodeId) + '节点',
        type: 'success'
      })
    },
    
    // 根据节点ID获取节点标题
    getNodeTitle(nodeId) {
      const node = this.processNodes.find(n => n.id === nodeId)
      return node ? node.title : ''
    },
    // 生成二维码
    generateQrCode() {
      // 先清除可能存在的旧二维码
      document.getElementById('product-qr-code').innerHTML = ''
      // 生成新的二维码
      new QRCode('product-qr-code', {
        text: this.productQrCodeContent,
        width: 100,
        height: 100,
        colorDark: '#000000',
        colorLight: '#ffffff',
        correctLevel: QRCode.CorrectLevel.H
      })
    }
  },
  
  mounted() {
    // 生成二维码
    this.generateQrCode()
    // 这里可以添加3D齿轮模型的初始化代码
  }
}
</script>

<style scoped>
.digital-product-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

/* 顶部搜索栏 */
.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.search-container {
  display: flex;
  gap: 15px;
  align-items: center;
}

.search-input {
  width: 250px;
}

/* 产品信息展示区域 */
.product-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  margin-bottom: 15px;
  border-radius: 8px;
  border: 1px solid #e4e7ed;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background-color: #f8f9fa;
}

.product-info-left {
  display: flex;
  flex-direction: row;
  gap: 30px;
  align-items: center;
  flex: 1;
  justify-content: flex-start;
}

.product-info-right {
  display: flex;
  align-items: center;
}

.qr-code {
  width: 100px;
  height: 100px;
}

.qr-code-img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

/* 流程节点轴 */
.process-axis {
  display: flex;
  justify-content: space-between;
  position: relative;
  margin-bottom: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e4e7ed;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 15px 20px;
  max-width: 100%; /* 占据整行 */
  margin-left: auto;
  margin-right: auto;
}

.process-node {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  cursor: pointer;
  transition: all 0.3s ease;
  width: 33.333%; /* 让节点占据完整宽度 */
}

.process-node.active {
  transform: translateY(-3px);
}

.axis-line {
  position: absolute;
  top: 17.5px; /* 垂直居中对齐节点图标 */
  left: calc(50% + 17.5px); /* 从当前节点图标的右侧边缘开始（50%是节点中心，17.5px是图标半径） */
  width: calc(100% - 35px); /* 延伸到下一个节点图标的左侧边缘（35px是图标直径） */
  height: 2px;
  background-color: #e0e0e0;
  z-index: 1;
}

.node-icon {
  width: 35px; /* 减小图标大小 */
  height: 35px;
  border-radius: 50%;
  background-color: #409EFF;
  color: white;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 14px; /* 减小字体大小 */
  margin-bottom: 5px;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.3);
  transition: all 0.3s ease;
}

.process-node.active .node-icon {
  background-color: #67C23A;
  box-shadow: 0 4px 12px rgba(103, 194, 58, 0.4);
  transform: scale(1.1);
}

.node-title {
  font-size: 12px; /* 减小字体大小 */
  font-weight: bold;
  color: #333;
  margin-bottom: 3px;
  text-align: center;
  white-space: nowrap;
}

.node-status {
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 10px;
}

.node-status.已完成 {
  background-color: #f0f9eb;
  color: #67C23A;
}

.node-status.进行中 {
  background-color: #ecf5ff;
  color: #409EFF;
}

.node-status.未开始 {
  background-color: #f5f7fa;
  color: #909399;
}

.axis-line {
  position: absolute;
  top: 17.5px; /* 垂直居中对齐节点图标 */
  left: calc(50% + 17.5px); /* 从当前节点图标的右侧边缘开始（50%是节点中心，17.5px是图标半径） */
  width: calc(100% - 35px); /* 延伸到下一个节点图标的左侧边缘（35px是图标直径） */
  height: 2px;
  background-color: #e0e0e0;
  z-index: 1;
}

/* 已完成节点前面的线条变绿 */
.process-node.node-status-已完成 .axis-line {
  background-color: #67C23A;
}

```

/* 数据展示区域 */
.data-display {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.section-card {
  margin-bottom: 20px;
}

.section-title {
  font-size: 20px;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid #e0e0e0;
}

.data-section {
  margin-bottom: 25px;
}

.section-subtitle {
  font-size: 16px;
  font-weight: bold;
  color: #606266;
  margin-bottom: 15px;
  display: flex;
  align-items: center;
}

.section-subtitle::before {
  content: '';
  display: inline-block;
  width: 6px;
  height: 16px;
  background-color: #409EFF;
  margin-right: 10px;
  border-radius: 3px;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 15px;
}

.info-item {
  display: flex;
  flex-direction: row;
  align-items: center;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 6px;
}

.info-label {
  font-size: 14px;
  color: #606266;
  margin-right: 5px;
  margin-bottom: 0;
}

.info-value {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.info-standard {
  font-size: 12px;
  color: #909399;
  margin-left: 5px;
}

.info-status {
  font-size: 12px;
  padding: 2px 6px;
  border-radius: 8px;
  margin-left: 5px;
}

.info-status.正常 {
  background-color: #f0f9eb;
  color: #67C23A;
}

.info-status.异常 {
  background-color: #fef0f0;
  color: #F56C6C;
}

/* 图片和视频容器 */
.image-gallery {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
}

.test-image {
  width: calc(25% - 12px);
  height: 150px;
  object-fit: cover;
  border-radius: 4px;
  cursor: pointer;
  transition: transform 0.3s ease;
}

.test-image:hover {
  transform: scale(1.05);
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background-color: #f5f7fa;
  color: #c0c4cc;
  font-size: 32px;
}

.video-container {
  background-color: #f5f7fa;
  padding: 10px;
  border-radius: 4px;
}

.video-name {
  margin-top: 10px;
  text-align: center;
  font-weight: bold;
  color: #333;
}

/* 媒体容器 */
.media-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-bottom: 20px;
}

.chart-container {
  background-color: #f5f7fa;
  padding: 20px;
  border-radius: 8px;
}

.chart-placeholder {
  height: 300px;
  background-color: white;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #909399;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .info-grid {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  }
  
  .test-image {
    width: calc(33.333% - 10px);
  }
}

@media (max-width: 768px) {
  .top-bar {
    flex-direction: column;
    gap: 15px;
  }
  
  .search-container {
    flex-wrap: wrap;
    justify-content: center;
  }
  
  .process-axis {
    flex-direction: column;
    padding: 40px 0 20px;
  }
  
  .process-node {
    margin-bottom: 30px;
  }
  
  .axis-line {
    position: relative;
    top: auto;
    left: auto;
    width: 2px;
    height: 30px;
    background-color: #e0e0e0;
    margin: 0 auto;
  }
  
  .process-node:last-child .axis-line {
    display: none;
  }
  
  .info-grid {
    grid-template-columns: 1fr;
  }
  
  .test-image {
    width: calc(50% - 8px);
  }
}

@media (max-width: 480px) {
  .search-input {
    width: 100%;
  }
  
  .test-image {
    width: 100%;
  }
}

  .table-image {
    width: 100px;
    height: 100px;
    cursor: pointer;
  }

  .image-table-container {
    margin-top: 10px;
  }
</style>




































