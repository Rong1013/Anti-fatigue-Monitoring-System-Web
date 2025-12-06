<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :inline="true" class="search-form">
      <el-form-item label="设备名称">
        <el-input v-model="formData.name" placeholder="请输入设备名称" clearable/>
      </el-form-item>
      <el-form-item label="设备类型">
        <el-input v-model="formData.type" placeholder="请输入设备类型" clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="handleReset">重置</el-button>
      </el-form-item>
      <el-form-item style="margin-left: auto">
        <el-button type="success" @click="handleAdd">新增设备</el-button>
      </el-form-item>
    </el-form>

    <!-- 新增设备对话框 -->
    <el-dialog
      title="新增设备"
      :visible.sync="dialogVisible"
      width="50%"
      :before-close="handleClose"
    >
      <el-form :model="formDataAdd" label-width="100px">
        <el-form-item label="设备编号">
          <el-input v-model="formDataAdd.code" placeholder="请输入设备编号"/>
        </el-form-item>
        <el-form-item label="设备名称">
          <el-input v-model="formDataAdd.name" placeholder="请输入设备名称"/>
        </el-form-item>
        <el-form-item label="设备类型">
          <el-select v-model="formDataAdd.type" placeholder="请选择设备类型" required>
            <el-option label="网络设备" value="网络设备"/>
            <el-option label="监测设备" value="监测设备"/>
            <el-option label="视频设备" value="视频设备"/>
          </el-select>
        </el-form-item>
        <el-form-item label="所属产品">
          <el-input v-model="formDataAdd.product" placeholder="请输入所属产品"/>
        </el-form-item>
        <el-form-item label="设备状态">
          <el-select v-model="formDataAdd.status" placeholder="请选择设备状态" required>
            <el-option label="在线" value="在线"/>
            <el-option label="离线" value="离线"/>
            <el-option label="故障" value="故障"/>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAddSubmit">确定</el-button>
      </div>
    </el-dialog>

    <!-- 编辑设备对话框 -->
    <el-dialog
      title="编辑设备"
      :visible.sync="dialogEditVisible"
      width="50%"
      :before-close="handleClose"
    >
      <el-form :model="formDataEdit" label-width="100px">
        <el-form-item label="设备编号">
          <el-input v-model="formDataEdit.code" placeholder="请输入设备编号"/>
        </el-form-item>
        <el-form-item label="设备名称">
          <el-input v-model="formDataEdit.name" placeholder="请输入设备名称"/>
        </el-form-item>
        <el-form-item label="设备类型">
          <el-select v-model="formDataEdit.type" placeholder="请选择设备类型" required>
            <el-option label="网络设备" value="网络设备"/>
            <el-option label="监测设备" value="监测设备"/>
            <el-option label="视频设备" value="视频设备"/>
          </el-select>
        </el-form-item>
        <el-form-item label="所属产品">
          <el-input v-model="formDataEdit.product" placeholder="请输入所属产品"/>
        </el-form-item>
        <el-form-item label="设备状态">
          <el-select v-model="formDataEdit.status" placeholder="请选择设备状态" required>
            <el-option label="在线" value="在线"/>
            <el-option label="离线" value="离线"/>
            <el-option label="故障" value="故障"/>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogEditVisible = false">取消</el-button>
        <el-button type="primary" @click="handleEditSubmit">确定</el-button>
      </div>
    </el-dialog>

    <!-- 查看设备对话框 -->
    <el-dialog
      title="查看设备"
      :visible.sync="dialogViewVisible"
      width="50%"
      :before-close="handleClose"
    >
      <el-form :model="viewData" label-width="100px">
        <el-form-item label="设备位置">
          <el-input v-model="viewData.location" placeholder="无设备位置信息" disabled/>
        </el-form-item>
        <el-form-item label="地图坐标">
          <div ref="echartsMap" style="width: 100%; height: 500px; margin-top: 15px; border: 1px solid #e6e6e6; border-radius: 4px;"></div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogViewVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 设备列表 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%; margin-bottom: 20px"
    >
      <el-table-column prop="deviceNo" label="设备编号" align="center"></el-table-column>
      <el-table-column prop="name" label="设备名称" align="center"></el-table-column>
      <el-table-column prop="type" label="设备类型" align="center">
        <template slot-scope="scope">
          <el-tag :type="getTagType(scope.row.type)">{{ scope.row.type }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="product" label="所属产品" align="center"></el-table-column>
      <el-table-column prop="status" label="状态" align="center">
        <template slot-scope="scope">
          <el-tag :type="getTagStatus(scope.row.status)">{{ scope.row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" align="center"></el-table-column>
      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <div style="display: flex; gap: 8px; justify-content: center;">
            <el-button type="success" size="small" style="background: #87d068; border-color: #87d068;" @click="handleView(scope.row)">查看</el-button>
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row.id)">删除</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      background
      :current-page="pagination.currentPage"
      :page-sizes="[10, 20, 30, 50]"
      :page-size="pagination.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="pagination.total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
  </div>
</template>

<script>
import { getDeviceList, addDevice, updateDevice, deleteDevice } from '@/api/device'
import { formatDate } from '@/utils/index'
import * as echarts from 'echarts'
import henanJson from '@/assets/henan.json' // 显式导入中国地图数据

export default {
  name: 'DeviceManagement',
  data() {
    return {
      formData: {
        name: '',
        type: ''
      },
      list: [],
      listLoading: false,
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      dialogVisible: false,
      dialogEditVisible: false,
      dialogViewVisible: false, // 查看对话框可见性
      viewData: {
        location: '', // 设备位置（实验室号）
        coordinate: '' // 地图坐标
      },
      formDataAdd: {
        code: '',
        name: '',
        type: '网络设备',
        product: '',
        status: '在线'
      },
      formDataEdit: {
        id: '',
        code: '',
        name: '',
        type: '网络设备',
        product: '',
        status: '在线',
        createTime: ''
      }
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    getTagType(type) {
      switch (type) {
        case '网络设备': return 'primary'; // 网络设备蓝色（#409EFF）
        case '监控设备': return 'success'; // 监测设备绿色（#67C23A）
        case '监测设备': return 'danger'; // 视频设备红色（#F56C6C）
        default: return 'default';
      }
    },
    getTagStatus(status) {
      switch (status) {
        case '在线': return 'success';
        case '离线': return 'info'; // 离线改为灰色
        case '故障': return 'danger'; // 故障改为红色
        default: return 'default';
      }
    },
    // 获取设备列表
    async getList() {
      this.listLoading = true
      try {
        const res = await getDeviceList({
          ...this.formData,
          page: this.pagination.currentPage,
          pageSize: this.pagination.pageSize
        })
        this.list = res.data.list
        this.pagination.total = res.data.total
      } finally {
        this.listLoading = false
      }
    },
    // 搜索
    handleSearch() {
      this.pagination.currentPage = 1
      this.getList()
      this.$message.success('搜索成功')
    },
    // 重置搜索
    handleReset() {
      this.formData = { name: '', type: '' }
      this.pagination.currentPage = 1
      this.getList()
      this.$message.success('重置成功')
    },
    // 新增设备
    handleAdd() {
      this.dialogVisible = true
      this.formDataAdd = { code: '', name: '', type: '网络设备', product: '', status: '在线' }
    },
    // 提交新增设备
    async handleAddSubmit() {
      try {
        await addDevice(this.formDataAdd)
        this.$message.success('新增设备成功')
        this.dialogVisible = false
        this.getList()
      } catch (error) {
        this.$message.error('新增设备失败')
      }
    },
    // 查看设备
    handleView(row) {
      this.dialogViewVisible = true;
      this.viewData = { ...row };
      this.$nextTick(() => {
        // 确保对话框渲染完成且容器存在后初始化地图
        if (this.$refs.echartsMap) {
          this.initMap();
        }
      });
    },
    // 初始化ECharts地图
    initMap() {
  // 1. 注册地图
  echarts.registerMap('henan', henanJson);

  // 2. 初始化图表
  const chart = echarts.init(this.$refs.echartsMap);

  // 3. 提取所有城市并解析坐标
  const cities = henanJson.features.map(feature => {
    const name = feature.properties.name;
    // 处理多层嵌套的coordinates（兼容Polygon和MultiPolygon）
    let coordinates = [];
    if (feature.geometry.type === 'Polygon') {
      coordinates = feature.geometry.coordinates[0]; // 取外边界
    } else if (feature.geometry.type === 'MultiPolygon') {
      coordinates = feature.geometry.coordinates[0][0]; // 取第一个多边形的外边界
    }
    return { name, coordinates };
  });

  // 4. 随机选择一个城市
  const randomCity = cities[Math.floor(Math.random() * cities.length)];
  console.log('随机选择城市:', randomCity.name);

  // 5. 计算城市边界（跳过无效坐标）
  let minX = Infinity, maxX = -Infinity, minY = Infinity, maxY = -Infinity;
  randomCity.coordinates.forEach(point => {
    const [x, y] = point;
    if (!isNaN(x) && !isNaN(y)) { // 过滤无效坐标
      minX = Math.min(minX, x);
      maxX = Math.max(maxX, x);
      minY = Math.min(minY, y);
      maxY = Math.max(maxY, y);
    }
  });

  // 6. 生成随机点（确保范围有效）
  const randomX = minX + Math.random() * (maxX - minX);
  const randomY = minY + Math.random() * (maxY - minY);
  // 水滴图标（蓝色，可替换为其他颜色）
  const bubbleArrowIcon = 'image://data:image/svg+xml;base64,' + btoa(`
    <svg xmlns="http://www.w3.org/2000/svg" width="60" height="60" viewBox="0 0 60 60">
      <circle cx="30" cy="25" r="20" fill="#ff4d4f" stroke="#fff" stroke-width="2"/>
      <path d="M30 45 L20 35 L40 35 Z" fill="#ff4d4f" stroke="#fff" stroke-width="1.5"/>
    </svg>
  `);

  // 7. 渲染图表（配置项同前）
  const option = {
    title: {
          text: '河南省地图',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{b}'
        },
    geo: { 
        map: 'henan', 
        roam: true, 
        label: { show: true },
        itemStyle: {
            areaColor: '#f0f9ff',
            borderColor: '#40a9ff'
        } 
    },
    series: [
      { 
        type: 'scatter',
        coordinateSystem: 'geo',
        symbol: bubbleArrowIcon, // 使用自定义图标
        data: [[randomX, randomY]],
        symbolSize: 50,
        tooltip: {             // 悬停提示
      trigger: 'item',
      formatter: params => {
        return `位置: ${randomCity.name}<br>坐标: ${params.value[0].toFixed(4)}, ${params.value[1].toFixed(4)}`;
      }
    },
    itemStyle: {
      opacity: 0.8,
      shadowBlur: 10,
      shadowColor: 'rgba(64, 169, 255, 0.5)'
    },
    emphasis: { // 高亮样式
      itemStyle: {
        opacity: 1,
        shadowBlur: 15
      }
    }
      }
    ]
  };
  chart.setOption(option);
  window.addEventListener('resize', () => chart.resize());
},
    // 编辑设备
    handleEdit(row) {
      this.dialogEditVisible = true
      this.formDataEdit = { ...row }
    },
    // 提交编辑设备
    async handleEditSubmit() {
      try {
        await updateDevice(this.formDataEdit)
        this.$message.success('编辑设备成功')
        this.dialogEditVisible = false
        this.getList()
      } catch (error) {
        this.$message.error('编辑设备失败')
      }
    },
    // 删除设备
    async handleDelete(id) {
      try {
        await deleteDevice(id)
        this.$message.success('删除设备成功')
        this.getList()
      } catch (error) {
        this.$message.error('删除设备失败')
      }
    },
    // 切换当前页
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.getList()
    },
    // 切换每页数量
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.pagination.currentPage = 1
      this.getList()
    },
    // 关闭对话框
    handleClose(done) {
      done()
    }
  }
}
</script>

<style scoped>
.search-form {
  margin-bottom: 10px;
  padding: 10px;
  background: #f8f8f8;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
</style>