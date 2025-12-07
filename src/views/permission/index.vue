<template>
  <div class="app-container">
    <el-form :inline="true" class="search-form">
      <el-form-item label="权限名称">
        <el-input v-model="formData.name" placeholder="请输入权限名称" clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="handleReset">重置</el-button>
      </el-form-item>
      <el-form-item style="margin-left: auto">
        <el-button type="success" @click="handleAdd">新增权限</el-button>
      </el-form-item>
    </el-form>
    <el-table
      v-loading="listLoading"
      :data="tableData"
      border
      fit
      highlight-current-row
      style="margin-bottom: 20px"
    >
      <el-table-column prop="permissionNo" label="权限编号" align="center"></el-table-column>
      <el-table-column prop="name" label="权限名称" align="center"></el-table-column>
      <el-table-column prop="status" label="权限状态" align="center">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" align="center"></el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope.row.permissionNo)">删除</el-button>
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

    <!-- 新增权限对话框 -->
    <el-dialog
      title="新增权限"
      :visible.sync="dialogVisible"
      width="50%"
      :before-close="handleClose"
    >
      <el-form :model="formDataAdd" label-width="100px">
        <el-form-item label="权限编号">
          <el-input v-model="formDataAdd.permissionNo" placeholder="请输入权限编号" />
        </el-form-item>
        <el-form-item label="权限名称">
          <el-input v-model="formDataAdd.name" placeholder="请输入权限名称" />
        </el-form-item>
        <el-form-item label="权限状态">
          <el-select v-model="formDataAdd.status" placeholder="请选择状态">
            <el-option label="已生效" value="已生效"></el-option>
            <el-option label="未生效" value="未生效"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAddSubmit">确定</el-button>
      </div>
    </el-dialog>

    <!-- 编辑权限对话框 -->
    <el-dialog
      title="编辑权限"
      :visible.sync="dialogEditVisible"
      width="50%"
      :before-close="handleClose"
    >
      <el-form :model="formDataEdit" label-width="100px">
        <el-form-item label="权限编号">
          <el-input v-model="formDataEdit.permissionNo" placeholder="请输入权限编号" disabled />
        </el-form-item>
        <el-form-item label="权限名称">
          <el-input v-model="formDataEdit.name" placeholder="请输入权限名称" />
        </el-form-item>
        <el-form-item label="权限状态">
          <el-select v-model="formDataEdit.status" placeholder="请选择状态">
            <el-option label="已生效" value="已生效"></el-option>
            <el-option label="未生效" value="未生效"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogEditVisible = false">取消</el-button>
        <el-button type="primary" @click="handleEditSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getPermissionList, addPermission, updatePermission, deletePermission } from '@/api/permission'

export default {
  name: 'PermissionManagement',
  data() {
    return {
      formData: {
        name: ''
      },
      tableData: [],
      listLoading: false,
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      dialogVisible: false,
      dialogEditVisible: false,
      formDataAdd: {
        permissionNo: '',
        name: '',
        status: '未生效'
      },
      formDataEdit: {
        permissionNo: '',
        name: '',
        status: '未生效'
      }
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    async getList() {
      this.listLoading = true
      try {
        const res = await getPermissionList({
          ...this.formData,
          page: this.pagination.currentPage,
          pageSize: this.pagination.pageSize
        })
        this.tableData = res.data.list
        this.pagination.total = res.data.total
      } finally {
        this.listLoading = false
      }
    },
    handleSearch() {
      this.pagination.currentPage = 1
      this.getList()
      this.$message.success('搜索成功')
    },
    handleReset() {
      this.pagination.currentPage = 1
      this.getList()
      this.$message.success('重置成功')
    },
    handleAdd() {
      this.dialogVisible = true
    },
    getStatusType(status) {
      switch (status) {
        case '已生效':
          return 'success';
        case '未生效':
          return 'info';
        default:
          return 'default';
      }
    },
    async handleAddSubmit() {
      try {
        await addPermission(this.formDataAdd)
        this.$message.success('新增权限成功')
        this.dialogVisible = false
        this.getList()
      } catch (error) {
        this.$message.error('新增权限失败')
      }
    },
    async handleEdit(row) {
      this.dialogEditVisible = true
      this.formDataEdit = { ...row }
    },
    async handleEditSubmit() {
      try {
        await updatePermission(this.formDataEdit)
        this.$message.success('编辑权限成功')
        this.dialogEditVisible = false
        this.getList()
      } catch (error) {
        this.$message.error('编辑权限失败')
      }
    },
    async handleDelete(permissionNo) {
      try {
        await deletePermission(permissionNo)
        this.$message.success('删除权限成功')
        this.getList()
      } catch (error) {
        this.$message.error('删除权限失败')
      }
    },
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.getList()
    },
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.pagination.currentPage = 1
      this.getList()
    },
    handleClose() {
      this.dialogVisible = false
      this.dialogEditVisible = false
      this.formDataAdd = { permissionNo: '', name: '', status: '未生效' }
      this.formDataEdit = { permissionNo: '', name: '', status: '未生效' }
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
</style>