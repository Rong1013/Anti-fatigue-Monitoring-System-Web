<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :inline="true" class="search-form">
      <el-form-item label="用户名称">
        <el-input v-model="formData.username" placeholder="请输入用户名称" clearable/>
      </el-form-item>
      <el-form-item label="手机号">
        <el-input v-model="formData.phone" placeholder="请输入手机号" clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="handleReset">重置</el-button>
      </el-form-item>
      <el-form-item style="margin-left: auto">
        <el-button type="success" @click="handleAdd">新增用户</el-button>
      </el-form-item>
    </el-form>

    <!-- 新增用户对话框 -->

    <!-- 编辑用户对话框 -->
    <el-dialog
      title="编辑用户"
      :visible.sync="dialogEditVisible"
      width="50%"
      :before-close="handleClose"
    >
      <el-form :model="formDataEdit" label-width="100px">
        <el-form-item label="用户名称">
          <el-input v-model="formDataEdit.username" placeholder="请输入用户名称" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="formDataEdit.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="formDataEdit.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="用户身份">
          <el-select v-model="formDataEdit.role" placeholder="请选择身份">
            <el-option label="管理员" value="管理员" />
            <el-option label="普通用户" value="普通用户" />
          </el-select>
        </el-form-item>
        <el-form-item label="用户状态">
          <el-select v-model="formDataEdit.status" placeholder="请选择状态">
            <el-option label="在线" value="在线" />
            <el-option label="离线" value="离线" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogEditVisible = false">取消</el-button>
        <el-button type="primary" @click="handleEditSubmit">确定</el-button>
      </div>
    </el-dialog>
    <el-dialog
      title="新增用户"
      :visible.sync="dialogVisible"
      width="50%"
      :before-close="handleClose"
    >
      <el-form :model="formDataAdd" label-width="100px">
        <el-form-item label="用户名称">
          <el-input v-model="formDataAdd.username" placeholder="请输入用户名称" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="formDataAdd.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="formDataAdd.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="用户身份">
          <el-select v-model="formDataAdd.role" placeholder="请选择身份">
            <el-option label="管理员" value="管理员" />
            <el-option label="普通用户" value="普通用户" />
          </el-select>
        </el-form-item>
        <el-form-item label="用户状态">
          <el-select v-model="formDataAdd.status" placeholder="请选择状态">
            <el-option label="在线" value="在线" />
            <el-option label="离线" value="离线" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAddSubmit">确定</el-button>
      </div>
    </el-dialog>

    <!-- 用户列表 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="margin-bottom: 20px"
    >
      <el-table-column prop="id" label="用户ID" align="center"/>
      <el-table-column prop="username" label="用户名称" align="center"/>
      <el-table-column prop="phone" label="用户手机号" align="center"/>
      <el-table-column prop="email" label="用户邮箱" align="center"/>
      <el-table-column prop="role" label="用户身份" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.role === '管理员' ? 'danger' : 'warning'">{{ scope.row.role }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="用户状态" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '在线' ? 'success' : ''" :class="scope.row.status === '离线' ? 'gray-tag' : ''">{{ scope.row.status }}</el-tag>
        </template>
      </el-table-column>
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
import { getUserList, addUser, updateUser, deleteUser } from '@/api/user'

export default {
  name: 'UserManagement',
  data() {
    return {
      formData: {
        username: '',
        phone: ''
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
        username: '',
        phone: '',
        email: '',
        role: '普通用户',
        status: '离线'
      },
      formDataEdit: {
        id: '',
        username: '',
        phone: '',
        email: '',
        role: '普通用户',
        status: '离线'
      }
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    // 获取用户列表
    async getList() {
      this.listLoading = true
      try {
        const res = await getUserList({
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
      this.formData = { username: '', phone: '' }
      this.pagination.currentPage = 1
      this.getList()
      this.$message.success('重置成功')
    },
    // 新增用户
    handleAdd() {
      this.dialogVisible = true
    },
    // 提交新增用户
    async handleAddSubmit() {
      try {
        await addUser(this.formDataAdd)
        this.$message.success('新增用户成功')
        this.dialogVisible = false
        this.getList()
      } catch (error) {
        this.$message.error('新增用户失败')
      }
    },
    // 编辑用户
    async handleEdit(row) {
      this.dialogEditVisible = true;
      this.formDataEdit = { ...row };
    },
    // 提交编辑用户
    async handleEditSubmit() {
      try {
        await updateUser(this.formDataEdit);
        this.$message.success('编辑用户成功');
        this.dialogEditVisible = false;
        this.getList();
      } catch (error) {
        this.$message.error('编辑用户失败');
      }
    },
    // 删除用户
    async handleDelete(id) {
      try {
        await deleteUser(id);
        this.$message.success('删除用户成功');
        this.getList();
      } catch (error) {
        this.$message.error('删除用户失败');
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

.gray-tag {
  background-color: #f0f0f0;
  color: #606266;
}
</style>