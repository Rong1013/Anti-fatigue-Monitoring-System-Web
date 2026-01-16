<template>
  <div class="app-container">
    <el-form :inline="true" class="search-form">
      <el-form-item label="产品编号">
        <el-input v-model="searchParams.productId" placeholder="请输入产品编号" clearable/>
      </el-form-item>
      <el-form-item label="产品名称">
        <el-input v-model="searchParams.name" placeholder="请输入产品名称" clearable/>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="searchParams.status" placeholder="请选择状态" clearable>
          <el-option label="全部" value=""/>
          <el-option label="未激活" value="未激活"/>
          <el-option label="进行中" value="进行中"/>
          <el-option label="已完成" value="已完成"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="handleReset">重置</el-button>
      </el-form-item>
      <el-form-item style="margin-left: auto">
        <el-button type="success" @click="handleAdd">新增产品</el-button>
      </el-form-item>
    </el-form>

    <el-table
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="margin-bottom: 20px"
    >
      <el-table-column prop="productId" label="产品编号" align="center"/>
      <el-table-column prop="name" label="名称" align="center"/>
      <el-table-column prop="status" label="状态" align="center">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="manager" label="负责人" align="center"/>
      <el-table-column prop="createTime" label="创建时间" align="center"/>
      <el-table-column label="操作" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button type="success" size="small" @click="handleView(scope.row)">查看</el-button>
          <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope.row.productId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      background
      :current-page="pagination.currentPage"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pagination.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="pagination.total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />

    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="600px"
      @close="handleClose"
    >
      <el-form :model="formData" :rules="formRules" ref="productForm" label-width="100px">
        <el-form-item label="产品编号" prop="productId" v-if="dialogMode === 'view'">
          <el-input v-model="formData.productId" disabled/>
        </el-form-item>
        <el-form-item label="产品名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入产品名称" :disabled="dialogMode === 'view'"/>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="formData.status" placeholder="请选择状态" :disabled="dialogMode === 'view'" style="width: 100%">
            <el-option label="未激活" value="未激活"/>
            <el-option label="进行中" value="进行中"/>
            <el-option label="已完成" value="已完成"/>
          </el-select>
        </el-form-item>
        <el-form-item label="负责人" prop="manager">
          <el-input v-model="formData.manager" placeholder="请输入负责人" :disabled="dialogMode === 'view'"/>
        </el-form-item>
        <el-form-item label="制造工艺" prop="processes" v-if="dialogMode !== 'view'">
          <el-collapse v-model="activeCategories" accordion>
            <el-collapse-item v-for="category in processCategories" :key="category.name" :name="category.name">
              <template slot="title">
                <span class="category-title">{{ category.name }}</span>
                <el-tag size="small" type="info" style="margin-left: 10px">{{ getSelectedCount(category.name) }}/{{ category.processes.length }}</el-tag>
              </template>
              <el-checkbox-group v-model="formData.processes">
                <div class="process-list">
                  <el-checkbox v-for="process in category.processes" :key="process" :label="process" class="process-checkbox">
                    {{ process }}
                  </el-checkbox>
                </div>
              </el-checkbox-group>
            </el-collapse-item>
          </el-collapse>
        </el-form-item>
        <el-form-item label="制造工艺" v-if="dialogMode === 'view' && formData.processes && formData.processes.length > 0">
          <div class="selected-processes">
            <el-tag v-for="process in formData.processes" :key="process" style="margin: 5px">{{ process }}</el-tag>
          </div>
        </el-form-item>
        <el-form-item label="创建时间" prop="createTime" v-if="dialogMode === 'view'">
          <el-input v-model="formData.createTime" disabled/>
        </el-form-item>
        <el-form-item v-if="dialogMode === 'view'">
          <div class="qr-code-section">
            <div class="qr-code-container">
              <div id="product-qr-code" class="qr-code"></div>
            </div>
            <div class="qr-code-actions">
              <el-button type="primary" size="small" @click="exportQrCode" icon="el-icon-download">导出二维码</el-button>
              <el-button type="success" size="small" @click="printQrCode" icon="el-icon-printer">打印二维码</el-button>
            </div>
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" v-if="dialogMode !== 'view'">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import QRCode from 'qrcodejs2'

export default {
  name: 'ProductManagement',
  data() {
    return {
      searchParams: {
        productId: '',
        name: '',
        status: ''
      },
      list: [],
      filteredList: [],
      listLoading: false,
      submitLoading: false,
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      dialogVisible: false,
      dialogMode: 'add',
      dialogTitle: '新增产品',
      activeCategories: [],
      formData: {
        productId: '',
        name: '',
        status: '未激活',
        manager: '',
        processes: [],
        createTime: ''
      },
      processCategories: [
        {
          name: '粗加工',
          processes: ['车削', '铣削', '刨削', '钻削', '镗削', '锯削', '拉削', '磨削']
        },
        {
          name: '精加工',
          processes: ['精车', '精铣', '精磨', '研磨', '珩磨', '抛光', '超精加工']
        },
        {
          name: '表面强化',
          processes: ['淬火', '回火', '正火', '退火', '渗碳', '渗氮', '表面淬火', '激光强化']
        },
        {
          name: '表面光整与检测',
          processes: ['喷砂', '喷丸', '电镀', '阳极氧化', '化学镀', '超声波检测', '磁粉检测', '渗透检测']
        }
      ],
      formRules: {
        name: [
          { required: true, message: '请输入产品名称', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '请选择状态', trigger: 'change' }
        ],
        manager: [
          { required: true, message: '请输入负责人', trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    this.initProductList()
  },
  methods: {
    getSelectedCount(categoryName) {
      const category = this.processCategories.find(c => c.name === categoryName)
      if (!category) return 0
      return category.processes.filter(process => this.formData.processes.includes(process)).length
    },
    
    getStatusType(status) {
      const typeMap = {
        '未激活': 'info',
        '进行中': 'warning',
        '已完成': 'success'
      }
      return typeMap[status] || 'info'
    },
    
    generateUUID() {
      return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        const r = Math.random() * 16 | 0
        const v = c === 'x' ? r : (r & 0x3 | 0x8)
        return v.toString(16)
      })
    },
    
    generateRandomDate() {
      const year = 2025
      const month = Math.floor(Math.random() * 12) + 1
      const day = Math.floor(Math.random() * 28) + 1
      const hour = Math.floor(Math.random() * 24)
      const minute = Math.floor(Math.random() * 60)
      const second = Math.floor(Math.random() * 60)
      
      return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')} ${String(hour).padStart(2, '0')}:${String(minute).padStart(2, '0')}:${String(second).padStart(2, '0')}`
    },
    
    initProductList() {
      const managers = ['姜钦露', '金荣']
      const statuses = ['未激活', '进行中', '已完成']
      const productNames = ['齿轮测试', '轴承检测', '轴套加工', '齿轮箱组装', '传动轴校验', '离合器测试', '变速箱调试', '发动机检测']
      
      const allProcesses = []
      this.processCategories.forEach(category => {
        allProcesses.push(...category.processes)
      })
      
      for (let i = 0; i < 10; i++) {
        const processCount = Math.floor(Math.random() * 5) + 3
        const shuffledProcesses = [...allProcesses].sort(() => Math.random() - 0.5)
        const selectedProcesses = shuffledProcesses.slice(0, processCount)
        
        this.list.push({
          productId: this.generateUUID(),
          name: productNames[i % productNames.length] + (Math.floor(i / productNames.length) + 1),
          status: statuses[Math.floor(Math.random() * statuses.length)],
          manager: managers[Math.floor(Math.random() * managers.length)],
          processes: selectedProcesses,
          createTime: this.generateRandomDate()
        })
      }
      
      this.filterProducts()
    },
    
    filterProducts() {
      let result = [...this.list]
      
      if (this.searchParams.productId) {
        result = result.filter(item => item.productId.toLowerCase().includes(this.searchParams.productId.toLowerCase()))
      }
      
      if (this.searchParams.name) {
        result = result.filter(item => item.name.toLowerCase().includes(this.searchParams.name.toLowerCase()))
      }
      
      if (this.searchParams.status) {
        result = result.filter(item => item.status === this.searchParams.status)
      }
      
      this.pagination.total = result.length
      this.filteredList = result
    },
    
    handleSearch() {
      this.pagination.currentPage = 1
      this.filterProducts()
      this.$message.success('搜索成功')
    },
    
    handleReset() {
      this.searchParams = {
        productId: '',
        name: '',
        status: ''
      }
      this.pagination.currentPage = 1
      this.filterProducts()
      this.$message.success('重置成功')
    },
    
    handleAdd() {
      this.dialogMode = 'add'
      this.dialogTitle = '新增产品'
      this.dialogVisible = true
      this.resetForm()
    },
    
    handleView(row) {
      this.dialogMode = 'view'
      this.dialogTitle = '查看产品'
      this.dialogVisible = true
      this.formData = {
        productId: row.productId,
        name: row.name,
        status: row.status,
        manager: row.manager,
        processes: row.processes || [],
        createTime: row.createTime
      }
      
      this.$nextTick(() => {
        this.generateQrCode()
      })
    },
    
    handleEdit(row) {
      this.dialogMode = 'edit'
      this.dialogTitle = '编辑产品'
      this.dialogVisible = true
      this.formData = {
        productId: row.productId,
        name: row.name,
        status: row.status,
        manager: row.manager,
        processes: row.processes || [],
        createTime: row.createTime
      }
    },
    
    handleSubmit() {
      this.$refs.productForm.validate((valid) => {
        if (valid) {
          this.submitLoading = true
          
          setTimeout(() => {
            if (this.dialogMode === 'add') {
              this.list.unshift({
                productId: this.generateUUID(),
                name: this.formData.name,
                status: this.formData.status,
                manager: this.formData.manager,
                processes: this.formData.processes,
                createTime: this.generateRandomDate()
              })
              
              this.$message.success('新增产品成功')
            } else if (this.dialogMode === 'edit') {
              const index = this.list.findIndex(item => item.productId === this.formData.productId)
              if (index !== -1) {
                this.list[index].name = this.formData.name
                this.list[index].status = this.formData.status
                this.list[index].manager = this.formData.manager
                this.list[index].processes = this.formData.processes
              }
              
              this.$message.success('编辑产品成功')
            }
            
            this.filterProducts()
            this.dialogVisible = false
            this.submitLoading = false
          }, 500)
        }
      })
    },
    
    handleDelete(productId) {
      this.$confirm('确定要删除该产品吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const index = this.list.findIndex(item => item.productId === productId)
        if (index !== -1) {
          this.list.splice(index, 1)
          this.filterProducts()
          this.$message.success('删除产品成功')
        }
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    },
    
    handleCurrentChange(val) {
      this.pagination.currentPage = val
    },
    
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.pagination.currentPage = 1
    },
    
    resetForm() {
      this.formData = {
        productId: '',
        name: '',
        status: '未激活',
        manager: '',
        processes: [],
        createTime: ''
      }
      if (this.$refs.productForm) {
        this.$refs.productForm.clearValidate()
      }
    },
    
    handleClose() {
      this.dialogVisible = false
      this.resetForm()
    },
    
    generateQrCode() {
      const qrCodeElement = document.getElementById('product-qr-code')
      if (qrCodeElement) {
        qrCodeElement.innerHTML = ''
        const qrCodeContent = JSON.stringify({
          productId: this.formData.productId,
          productName: this.formData.name,
          status: this.formData.status,
          manager: this.formData.manager,
          createTime: this.formData.createTime,
          processes: this.formData.processes
        })
        
        new QRCode('product-qr-code', {
          text: qrCodeContent,
          width: 150,
          height: 150,
          colorDark: '#000000',
          colorLight: '#ffffff',
          correctLevel: QRCode.CorrectLevel.H
        })
      }
    },
    
    exportQrCode() {
      const qrCodeElement = document.getElementById('product-qr-code')
      if (qrCodeElement) {
        const canvas = qrCodeElement.querySelector('canvas')
        if (canvas) {
          const link = document.createElement('a')
          link.download = `${this.formData.name}_二维码.png`
          link.href = canvas.toDataURL('image/png')
          link.click()
          this.$message.success('二维码导出成功')
        }
      }
    },
    
    printQrCode() {
      const qrCodeElement = document.getElementById('product-qr-code')
      if (qrCodeElement) {
        const canvas = qrCodeElement.querySelector('canvas')
        if (canvas) {
          const printWindow = window.open('', '_blank')
          printWindow.document.write(`
            <!DOCTYPE html>
            <html>
            <head>
              <title>${this.formData.name} - 二维码</title>
              <style>
                body {
                  display: flex;
                  flex-direction: column;
                  align-items: center;
                  justify-content: center;
                  padding: 20px;
                  font-family: Arial, sans-serif;
                }
                .product-info {
                  text-align: center;
                  margin-bottom: 20px;
                }
                .product-info h2 {
                  margin: 0 0 10px 0;
                  color: #333;
                }
                .product-info p {
                  margin: 5px 0;
                  color: #666;
                }
                .qr-code {
                  border: 2px solid #333;
                  padding: 10px;
                }
              </style>
            </head>
            <body>
              <div class="product-info">
                <h2>${this.formData.name}</h2>
                <p>产品编号：${this.formData.productId}</p>
                <p>负责人：${this.formData.manager}</p>
                <p>状态：${this.formData.status}</p>
                <p>创建时间：${this.formData.createTime}</p>
              </div>
              <div class="qr-code">
                <img src="${canvas.toDataURL('image/png')}" width="150" height="150"/>
              </div>
            </body>
            </html>
          `)
          printWindow.document.close()
          printWindow.print()
          this.$message.success('二维码打印成功')
        }
      }
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
}

.dialog-footer {
  text-align: right;
}

.category-title {
  font-weight: bold;
  color: #303133;
  font-size: 14px;
}

.process-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
  padding: 10px 0;
}

.process-checkbox {
  margin: 0;
  padding: 8px 12px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  transition: all 0.3s;
}

.process-checkbox:hover {
  background-color: #f5f7fa;
  border-color: #409eff;
}

.process-checkbox.is-checked {
  background-color: #ecf5ff;
  border-color: #409eff;
}

.selected-processes {
  display: flex;
  flex-wrap: wrap;
}

.qr-code-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
}

.qr-code-container {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 10px;
  background: #f5f7fa;
  border-radius: 4px;
}

.qr-code {
  display: flex;
  justify-content: center;
  align-items: center;
}

.qr-code-actions {
  display: flex;
  gap: 10px;
}
</style>
