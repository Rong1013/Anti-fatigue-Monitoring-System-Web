<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :inline="true" class="search-form">
      <el-form-item label="产品名称">
        <el-input v-model="formData.name" placeholder="请输入产品名称" clearable/>
      </el-form-item>
      <el-form-item label="产品类别">
        <el-input v-model="formData.category" placeholder="请输入产品分类" clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="handleReset">重置</el-button>
      </el-form-item>
      <el-form-item style="margin-left: auto">
        <el-button type="success" @click="handleAdd">新增产品</el-button>
      </el-form-item>
    </el-form>

    <!-- 新增产品对话框 -->
    <el-dialog
      title="新增产品"
      :visible.sync="dialogVisible"
      width="50%"
      :before-close="handleClose"
    >
      <el-form :model="formDataAdd" label-width="100px">
        <el-form-item label="产品名称">
          <el-input v-model="formDataAdd.name" placeholder="请输入产品名称"/>
        </el-form-item>
        <el-form-item label="产品类别" prop="categoryName">
          <el-input v-model="formDataAdd.categoryName" placeholder="请输入分类名称"/>
          <el-form-item v-if="formDataAdd.categoryNameError" style="color: #F56C6C; font-size: 12px;">{{ formDataAdd.categoryNameError }}</el-form-item>
        </el-form-item>
        <el-form-item label="设备数量">
          <el-input v-model.number="formDataAdd.deviceCount" placeholder="请输入设备数量" type="number"/>
        </el-form-item>
        <el-form-item label="产品状态">
          <el-select v-model="formDataAdd.status" placeholder="请选择产品状态" required>
            <el-option label="已启用" value="已启用"/>
            <el-option label="未启用" value="未启用"/>
          </el-select>
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="formDataAdd.leader" placeholder="请输入负责人"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAddSubmit">确定</el-button>
      </div>
    </el-dialog>

    <!-- 编辑产品对话框 -->
    <el-dialog
      title="编辑产品"
      :visible.sync="dialogEditVisible"
      width="50%"
      :before-close="handleClose"
    >
      <el-form :model="formDataEdit" label-width="100px">
        <el-form-item label="产品名称">
          <el-input v-model="formDataEdit.name" placeholder="请输入产品名称"/>
        </el-form-item>
        <el-form-item label="产品类别">
          <el-input v-model="formDataEdit.category" placeholder="请输入产品类别"/>
        </el-form-item>
        <el-form-item label="设备数量">
          <el-input v-model.number="formDataEdit.deviceCount" placeholder="请输入设备数量" type="number"/>
        </el-form-item>
        <el-form-item label="产品状态">
          <el-select v-model="formDataEdit.status" placeholder="请选择产品状态" required>
            <el-option label="已启用" value="已启用"/>
            <el-option label="未启用" value="未启用"/>
          </el-select>
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="formDataEdit.leader" placeholder="请输入负责人"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogEditVisible = false">取消</el-button>
        <el-button type="primary" @click="handleEditSubmit">确定</el-button>
      </div>
    </el-dialog>

    <!-- 产品列表 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="margin-bottom: 20px"
    >
      <el-table-column prop="id" label="标识" align="center"/>
      <el-table-column prop="name" label="名称" align="center"/>
      <el-table-column prop="category" label="分类" align="center">
        <template slot-scope="scope">
          <el-tag :type="getTagType(scope.row.category)">{{ scope.row.category }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="deviceCount" label="设备数量" align="center"/>
      <el-table-column prop="status" label="状态" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '已启用' ? 'success' : 'danger'">{{ scope.row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="leader" label="负责人" align="center"/>
      <el-table-column prop="createTime" label="创建时间" align="center"/>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope.row.id)">删除</el-button>
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
import { getProductList, addProduct, updateProduct, deleteProduct } from '@/api/product'
import { formatDate } from '@/utils/index'

export default {
  name: 'ProductManagement',
  data() {
    return {
      formData: {
        name: '',
        category: ''
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
      formDataAdd: {
        name: '',
        category: '粗加工',
        deviceCount: 0,
        status: '未启用',
        leader: ''
      },
      formDataEdit: {
        id: '',
        name: '',
        category: '粗加工',
        deviceCount: 0,
        status: '未启用',
        leader: '',
        createTime: ''
      }
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    getTagType(category) {
      switch (category) {
        case '粗加工': return 'info';
        case '精加工': return 'success';
        case '装配': return 'warning';
        default: return 'default';
      }
    },
    // 获取产品列表
    async getList() {
      this.listLoading = true
      try {
        const res = await getProductList({
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
      this.formData = { name: '', category: '' }
      this.pagination.currentPage = 1
      this.getList()
      this.$message.success('重置成功')
    },
    // 新增产品
    handleAdd() {
      this.dialogVisible = true
      this.formDataAdd = { name: '', category: '粗加工', deviceCount: 0, status: '未启用', leader: '' }
    },
    // 提交新增产品
    async handleAddSubmit() {
      if (!this.formDataAdd.categoryName) {
        this.formDataAdd.categoryNameError = '分类名称不能为空'
        return
      }
      this.formDataAdd.categoryNameError = ''
      try {
        await addProduct(this.formDataAdd)
        this.$message.success('新增产品成功')
        this.dialogVisible = false
        this.getList()
      } catch (error) {
        this.$message.error('新增产品失败')
      }
    },
    // 编辑产品
    handleEdit(row) {
      this.dialogEditVisible = true
      this.formDataEdit = { ...row }
    },
    // 提交编辑产品
    async handleEditSubmit() {
      try {
        await updateProduct(this.formDataEdit)
        this.$message.success('编辑产品成功')
        this.dialogEditVisible = false
        this.getList()
      } catch (error) {
        this.$message.error('编辑产品失败')
      }
    },
    // 删除产品
    async handleDelete(id) {
      try {
        await deleteProduct(id)
        this.$message.success('删除产品成功')
        this.getList()
      } catch (error) {
        this.$message.error('删除产品失败')
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
}
</style>"