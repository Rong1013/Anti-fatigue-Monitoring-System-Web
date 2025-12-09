<template>
  <div class="digital-product-container">
    <!-- 标题、产品选择和导出按钮 -->
    <div class="title-row">
      <div class="title-selector">
        <h2 class="page-title">选择产品</h2>
        <el-select v-model="selectedProduct" placeholder="选择产品" style="width: 300px;">
          <el-option
            v-for="product in productList"
            :key="product.id"
            :label="product.name + ' (' + product.category + ')'"
            :value="product.id"
          ></el-option>
        </el-select>
      </div>
      <el-button type="primary" size="large" @click="exportReport">
        <i class="el-icon-download"></i> 导出数字产品报告
      </el-button>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 左侧：数字孪生齿轮 -->
      <div class="left-section">
        <div class="section-card">
          <h3 class="section-title">数字孪生模型</h3>
          <div class="gear-container" ref="gearContainer">
            <!-- 3D齿轮模型 -->
          </div>
        </div>

        <!-- 加工视频 -->
        <div class="section-card">
          <h3 class="section-title">加工视频</h3>
          <div class="video-container">
            <video width="100%" height="400" controls>
              <source src="@/assets/三维形貌测量过程视频.mp4" type="video/mp4">
              您的浏览器不支持 HTML5 视频。
            </video>
          </div>
        </div>
      </div>

      <!-- 右侧：参数数据 -->
      <div class="right-section">
        <!-- 抗疲劳制造参数 -->
        <div class="section-card">
          <h3 class="section-title">抗疲劳制造参数</h3>
          <div class="parameters-grid">
            <div class="parameter-item">
              <span class="parameter-label">材料硬度</span>
              <span class="parameter-value">{{ currentParameters.hardness }}</span>
            </div>
            <div class="parameter-item">
              <span class="parameter-label">表面粗糙度</span>
              <span class="parameter-value">{{ currentParameters.roughness }}</span>
            </div>
            <div class="parameter-item">
              <span class="parameter-label">热处理温度</span>
              <span class="parameter-value">{{ currentParameters.temperature }}</span>
            </div>
            <div class="parameter-item">
              <span class="parameter-label">加工精度</span>
              <span class="parameter-value">{{ currentParameters.precision }}</span>
            </div>
            <div class="parameter-item">
              <span class="parameter-label">疲劳寿命预测</span>
              <span class="parameter-value">{{ currentParameters.fatigueLife }}</span>
            </div>
            <div class="parameter-item">
              <span class="parameter-label">残余应力</span>
              <span class="parameter-value">{{ currentParameters.residualStress }}</span>
            </div>
            <div class="parameter-item">
              <span class="parameter-label">加工速度</span>
              <span class="parameter-value">{{ currentParameters.speed }}</span>
            </div>
            <div class="parameter-item">
              <span class="parameter-label">刀具磨损</span>
              <span class="parameter-value">{{ currentParameters.toolWear }}</span>
            </div>
          </div>
        </div>

        <!-- 制造过程数据 -->
        <div class="section-card">
          <h3 class="section-title">制造过程数据</h3>
          <el-table :data="processData" stripe style="width: 100%">
            <el-table-column prop="step" label="工序" width="100"></el-table-column>
            <el-table-column prop="name" label="工序名称"></el-table-column>
            <el-table-column prop="time" label="加工时间"></el-table-column>
            <el-table-column prop="temperature" label="温度(°C)"></el-table-column>
            <el-table-column prop="pressure" label="压力(MPa)"></el-table-column>
            <el-table-column prop="humidity" label="湿度(%)"></el-table-column>
            <el-table-column prop="vibration" label="振动(mm/s)"></el-table-column>
            <el-table-column prop="noise" label="噪声(dB)"></el-table-column>
            <el-table-column prop="torque" label="扭矩(N·m)"></el-table-column>
          </el-table>
        </div>

        <!-- 导出报告按钮已移至页面上方 -->
      </div>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request'
import * as THREE from 'three'
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls'

export default {
  name: 'DigitalProduct',
  data() {
    return {
      productList: [
        { id: '1', name: '直齿圆柱齿轮', category: '齿轮类' },
        { id: '2', name: '斜齿圆柱齿轮', category: '齿轮类' },
        { id: '3', name: '圆锥齿轮', category: '齿轮类' },
        { id: '4', name: '蜗杆蜗轮', category: '齿轮类' },
        { id: '5', name: '光轴', category: '轴类' },
        { id: '6', name: '阶梯轴', category: '轴类' },
        { id: '7', name: '花键轴', category: '轴类' },
        { id: '8', name: '深沟球轴承', category: '轴承类' },
        { id: '9', name: '圆锥滚子轴承', category: '轴承类' },
        { id: '10', name: '推力轴承', category: '轴承类' }
      ],
      selectedProduct: '1',
      currentParameters: {
        hardness: '58 HRC',
        roughness: '0.8 μm',
        temperature: '850 °C',
        precision: '±0.005 mm',
        fatigueLife: '1.2e6 次',
        residualStress: '-120 MPa',
        speed: '1200 rpm',
        toolWear: '0.15 mm'
      },
      // 3D场景相关
      scene: null,
      camera: null,
      renderer: null,
      controls: null,
      gear: null,
      processData: [
        { step: '01', name: '原材料准备', time: '30 min', temperature: '25', pressure: '0', humidity: '45%', vibration: '0.1 mm/s', noise: '50 dB', torque: '120 N·m' },
        { step: '02', name: '粗加工', time: '45 min', temperature: '28', pressure: '0', humidity: '42%', vibration: '1.2 mm/s', noise: '75 dB', torque: '850 N·m' },
        { step: '03', name: '半精加工', time: '35 min', temperature: '32', pressure: '0', humidity: '40%', vibration: '0.8 mm/s', noise: '70 dB', torque: '680 N·m' },
        { step: '04', name: '热处理', time: '180 min', temperature: '850', pressure: '0.2', humidity: '25%', vibration: '0.3 mm/s', noise: '60 dB', torque: '0' },
        { step: '05', name: '冷却', time: '60 min', temperature: '50', pressure: '0', humidity: '60%', vibration: '0.1 mm/s', noise: '45 dB', torque: '0' },
        { step: '06', name: '精加工', time: '50 min', temperature: '30', pressure: '0', humidity: '45%', vibration: '0.5 mm/s', noise: '65 dB', torque: '520 N·m' },
        { step: '07', name: '表面处理', time: '90 min', temperature: '150', pressure: '0.5', humidity: '35%', vibration: '0.2 mm/s', noise: '55 dB', torque: '0' },
        { step: '08', name: '精度检测', time: '40 min', temperature: '25', pressure: '0', humidity: '48%', vibration: '0.1 mm/s', noise: '48 dB', torque: '0' },
        { step: '09', name: '质量检测', time: '30 min', temperature: '25', pressure: '0', humidity: '50%', vibration: '0.1 mm/s', noise: '47 dB', torque: '0' },
        { step: '10', name: '包装', time: '20 min', temperature: '25', pressure: '0', humidity: '52%', vibration: '0.1 mm/s', noise: '45 dB', torque: '0' }
      ]
    }
  },
  mounted() {
    this.loadProducts()
    // 延迟初始化3D场景，确保DOM已渲染
    this.$nextTick(() => {
      this.init3DGear()
    })
  },
  
  beforeDestroy() {
    this.cleanup3D()
  },
  methods: {
    async loadProducts() {
      // 从API获取产品列表
      const res = await request({ url: '/vue-admin-template/product/list', method: 'get' })
      this.productList = res.data.list
      if (this.productList.length > 0) {
        this.selectedProduct = this.productList[0].id
      }
    },
    exportReport() {
      // 模拟导出报告功能
      this.$message({
        message: '数字产品报告导出成功！',
        type: 'success'
      })
      
      // 这里可以添加实际的报告生成和下载逻辑
      // 例如生成PDF或Excel文件
    },
    // 初始化3D场景 - 使用CAE软件风格
    init3DGear() {
      if (!this.$refs.gearContainer) return
      
      const container = this.$refs.gearContainer
      const width = container.clientWidth
      const height = container.clientHeight
      
      // 创建场景 - CAE软件风格的深色背景
      this.scene = new THREE.Scene()
      this.scene.background = new THREE.Color(0x0f1419) // 深色背景，接近黑色，更适合CAE仿真环境
      
      // 创建相机 - 最佳观察实体齿轮的角度
      this.camera = new THREE.PerspectiveCamera(45, width / height, 0.1, 1000)
      this.camera.position.set(15, 10, 20) // 调整位置，从斜上方更好地观察实体齿轮的3D结构
      
      // 创建渲染器 - CAE软件风格的渲染设置
      this.renderer = new THREE.WebGLRenderer({ antialias: true, alpha: true })
      this.renderer.setSize(width, height)
      this.renderer.setPixelRatio(window.devicePixelRatio)
      this.renderer.shadowMap.enabled = true
      this.renderer.shadowMap.type = THREE.PCFSoftShadowMap
      this.renderer.toneMapping = THREE.NoToneMapping // CAE软件通常不使用色调映射
      this.renderer.toneMappingExposure = 1.0
      this.renderer.shadowMap.bias = -0.0001
      this.renderer.shadowMap.radius = 2
      container.appendChild(this.renderer.domElement)
      
      // 添加光源 - CAE软件风格的照明
      this.addLights()
      
      // 创建高质量实体齿轮模型 - CAE软件标准
      this.createHighQualityGear()
      
      // 创建OrbitControls
      this.controls = new OrbitControls(this.camera, this.renderer.domElement)
      this.controls.enableDamping = true
      this.controls.dampingFactor = 0.05
      this.controls.minDistance = 5
      this.controls.maxDistance = 50
      this.controls.maxPolarAngle = Math.PI / 2.1 // 限制旋转角度，更符合工程视图
      
      // 开始动画循环
      this.animate()
      
      // 监听窗口大小变化
      window.addEventListener('resize', this.onWindowResize)
    },
    
    // 创建高质量实体齿轮模型 - CAE软件标准
    createHighQualityGear() {
      // 齿轮参数 - CAE仿真软件标准实体齿轮参数
      const radius = 5.0 // 齿轮半径
      const toothCount = 20 // 标准齿数
      const toothHeight = 1.8 // 标准齿深
      const toothWidth = 0.8 // 标准齿宽比例
      const holeRadius = 1.2 // 中心孔半径
      const thickness = 5.0 // 实体厚度
      
      // 创建齿轮形状 - 使用精确的齿形生成算法
      const gearShape = new THREE.Shape()
      
      // 计算齿的基本参数
      const fullAngle = Math.PI * 2
      const toothAngle = fullAngle / toothCount
      const rootRadius = radius - toothHeight
      const toothWidthAngle = toothAngle * 0.35 // 齿宽占35%
      const toothGapAngle = toothAngle - toothWidthAngle
      
      // 绘制齿轮齿形
      for (let i = 0; i < toothCount; i++) {
        const currentAngle = i * toothAngle
        
        // 计算当前齿的角度位置
        const toothStartAngle = currentAngle + toothGapAngle / 2
        const toothEndAngle = toothStartAngle + toothWidthAngle
        
        // 绘制齿槽左侧
        gearShape.moveTo(
          rootRadius * Math.cos(toothStartAngle),
          rootRadius * Math.sin(toothStartAngle)
        )
        
        // 绘制齿左侧面
        gearShape.lineTo(
          radius * Math.cos(toothStartAngle),
          radius * Math.sin(toothStartAngle)
        )
        
        // 绘制齿顶面
        gearShape.lineTo(
          radius * Math.cos(toothEndAngle),
          radius * Math.sin(toothEndAngle)
        )
        
        // 绘制齿右侧面
        gearShape.lineTo(
          rootRadius * Math.cos(toothEndAngle),
          rootRadius * Math.sin(toothEndAngle)
        )
      }
      
      // 闭合齿轮外轮廓
      gearShape.closePath()
      
      // 添加中心孔
      const holePath = new THREE.Path()
      holePath.arc(0, 0, holeRadius, 0, Math.PI * 2)
      gearShape.holes.push(holePath)
      
      // 挤出设置 - 高精度实体效果
      const extrudeSettings = {
        depth: thickness,
        bevelEnabled: true, // 启用斜面
        bevelSegments: 12, // 高精度斜面分段
        steps: 8, // 高精度挤出步骤
        bevelSize: 0.15,
        bevelThickness: 0.15,
        curveSegments: 48, // 高精度曲线分段
        twist: 0,
        twistSegments: 1,
        bevelOffset: 0
      }
      
      // 创建齿轮几何体
      const geometry = new THREE.ExtrudeGeometry(gearShape, extrudeSettings)
      
      // 创建CAE软件标准仿真齿轮材质
      const material = new THREE.MeshLambertMaterial({
        color: 0xb0b0b0, // 标准钢灰色，CAE软件最常用的齿轮颜色
        wireframe: false
      })
      
      // 创建齿轮网格
      this.gear = new THREE.Mesh(geometry, material)
      this.gear.castShadow = true
      this.gear.receiveShadow = true
      
      // 将齿轮添加到场景
      this.scene.add(this.gear)
      
      // 添加合适的旋转动画，让用户能够清晰观察到实体结构
      this.gear.userData.rotationSpeed = 0.008
    },
    
    // 添加CAE风格的光源系统
    addLights() {
      // 环境光 - CAE软件常用的均匀照明
      const ambientLight = new THREE.AmbientLight(0x404040, 0.5) // 暗灰色环境光，符合CAE仿真氛围
      this.scene.add(ambientLight)
      
      // 主方向光 - CAE软件常用的顶光源
      const mainLight = new THREE.DirectionalLight(0xffffff, 1.2)
      mainLight.position.set(10, 15, 8)
      mainLight.castShadow = true
      mainLight.shadow.mapSize.width = 4096
      mainLight.shadow.mapSize.height = 4096
      mainLight.shadow.camera.near = 0.5
      mainLight.shadow.camera.far = 100
      mainLight.shadow.camera.left = -30
      mainLight.shadow.camera.right = 30
      mainLight.shadow.camera.top = 30
      mainLight.shadow.camera.bottom = -30
      this.scene.add(mainLight)
      
      // 辅助侧光 - 增强齿轮轮廓
      const sideLight = new THREE.DirectionalLight(0xffffff, 0.6)
      sideLight.position.set(-12, 8, 12)
      this.scene.add(sideLight)
      
      // 底光 - 填充阴影，使底部细节可见
      const bottomLight = new THREE.DirectionalLight(0xffffff, 0.3)
      bottomLight.position.set(0, -8, 6)
      this.scene.add(bottomLight)
      
      // 添加坐标轴辅助线 - CAE软件常用的参考线
      const axesHelper = new THREE.AxesHelper(15) // 标准尺寸坐标轴
      this.scene.add(axesHelper)
      
      // 添加网格辅助线 - CAE软件常用的网格
      const gridHelper = new THREE.GridHelper(30, 30, 0x444444, 0x222222) // 暗色调网格，符合CAE仿真环境
      gridHelper.position.y = -3.1 // 放置在齿轮下方，与厚度匹配
      this.scene.add(gridHelper)
    },
    
    // 动画循环
    animate() {
      requestAnimationFrame(this.animate)
      
      // 旋转齿轮
      if (this.gear) {
        this.gear.rotation.y += this.gear.userData.rotationSpeed
      }
      
      // 更新控制器
      if (this.controls) {
        this.controls.update()
      }
      
      // 渲染场景
      if (this.renderer && this.scene && this.camera) {
        this.renderer.render(this.scene, this.camera)
      }
    },
    
    // 窗口大小变化处理
    onWindowResize() {
      if (!this.$refs.gearContainer || !this.camera || !this.renderer) return
      
      const container = this.$refs.gearContainer
      const width = container.clientWidth
      const height = container.clientHeight
      
      this.camera.aspect = width / height
      this.camera.updateProjectionMatrix()
      this.renderer.setSize(width, height)
    },
    
    // 清理3D场景
    cleanup3D() {
      if (this.renderer) {
        this.renderer.dispose()
        this.renderer = null
      }
      
      if (this.gear) {
        this.gear.geometry.dispose()
        this.gear.material.dispose()
        this.gear = null
      }
      
      if (this.controls) {
        this.controls.dispose()
      }
      
      window.removeEventListener('resize', this.onWindowResize)
    },
    updateParameters(productId) {
      // 根据选择的产品ID更新参数数据
      // 这里可以从API获取对应产品的详细参数
      // 目前使用模拟数据
      this.$message({
        message: `已加载产品${productId}的参数数据`,
        type: 'info'
      })
      
      // 模拟不同产品的参数差异
      const productIndex = this.productList.findIndex(p => p.id === productId)
      if (productIndex % 2 === 0) {
        this.currentParameters = {
          hardness: '60 HRC',
          roughness: '0.6 μm',
          temperature: '880 °C',
          precision: '±0.003 mm',
          fatigueLife: '1.5e6 次',
          residualStress: '-150 MPa',
          speed: '1500 rpm',
          toolWear: '0.12 mm'
        }
      } else {
        this.currentParameters = {
          hardness: '56 HRC',
          roughness: '1.0 μm',
          temperature: '820 °C',
          precision: '±0.008 mm',
          fatigueLife: '1.0e6 次',
          residualStress: '-100 MPa',
          speed: '1000 rpm',
          toolWear: '0.18 mm'
        }
      }
    }
  },
  watch: {
    selectedProduct(newProductId) {
      // 当选择不同产品时，更新参数数据
      this.updateParameters(newProductId)
    }
  },
  // 移除旧的计算属性
}
</script>

<style scoped>
.digital-product-container {
  padding: 20px;
  background-color: #f5f7fa;
}

.page-title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.title-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.title-selector {
  display: flex;
  align-items: center;
  gap: 20px;
}

.page-title {
  margin: 0;
}

.product-selector h2 {
  margin-right: 20px;
}

.main-content {
  display: flex;
  justify-content: space-between;
  gap: 20px;
}

.left-section, .right-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.section-card {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 15px;
  color: #606266;
  border-bottom: 1px solid #e6e9ed;
  padding-bottom: 10px;
}

.gear-container {
  height: 300px;
  background-color: #fafafa;
  border-radius: 8px;
  position: relative;
  overflow: hidden;
}

.gear-container canvas {
  width: 100% !important;
  height: 100% !important;
}



.video-container {
  background-color: #000;
  border-radius: 8px;
  overflow: hidden;
  height: 400px;
}

.parameters-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
}

.parameter-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  background-color: #fafafa;
  border-radius: 4px;
  border-left: 4px solid #4A90E2;
}

.parameter-label {
  font-weight: 500;
  color: #606266;
}

.parameter-value {
  font-weight: bold;
  color: #303133;
}

/* 导出按钮已移至页面上方，不再需要此样式 */

/* 响应式设计 */
@media (max-width: 1200px) {
  .main-content {
    flex-direction: column;
  }
  
  .left-section, .right-section {
    width: 100%;
  }
}

@media (max-width: 768px) {
  .parameters-grid {
    grid-template-columns: 1fr;
  }
  
  .title-row {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .title-selector {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .page-title {
    margin-bottom: 0;
  }
}
</style>