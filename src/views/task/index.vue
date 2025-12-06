<template>
  <div class="app-container">
    <el-form :inline="true" class="search-form">
      <el-form-item label="任务名称">
        <el-input v-model="formData.name" placeholder="请输入任务名称" clearable/>
      </el-form-item>
      <el-form-item label="任务负责人">
        <el-input v-model="formData.leader" placeholder="请输入任务负责人" clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="handleReset">重置</el-button>
      </el-form-item>
      <el-form-item style="margin-left: auto">
        <el-button type="success" @click="handleAdd">新增任务</el-button>
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
      <el-table-column prop="taskNo" label="任务编号" align="center"></el-table-column>
      <el-table-column prop="name" label="任务名称" align="center"></el-table-column>
      <el-table-column prop="leader" label="任务负责人" align="center"></el-table-column>
      <el-table-column prop="status" label="状态" align="center">
  <template slot-scope="scope">
    <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
  </template>
</el-table-column>
      <el-table-column prop="createTime" label="创建时间" align="center"></el-table-column>
      <el-table-column prop="expectedFinishTime" label="预期完成时间" align="center"></el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope.row.taskNo)">删除</el-button>
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

    <!-- 新增任务对话框 -->
    <el-dialog
      title="新增任务"
      :visible.sync="dialogVisible"
      width="50%"
      :before-close="handleClose"
    >
      <el-form :model="formDataAdd" label-width="100px">
        <el-form-item label="任务编号">
          <el-input v-model="formDataAdd.taskNo" placeholder="请输入任务编号" />
        </el-form-item>
        <el-form-item label="任务名称">
          <el-input v-model="formDataAdd.name" placeholder="请输入任务名称" />
        </el-form-item>
        <el-form-item label="任务负责人">
          <el-input v-model="formDataAdd.leader" placeholder="请输入任务负责人" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="formDataAdd.status" placeholder="请选择状态">
            <el-option label="已发布" value="已发布"></el-option>
            <el-option label="未发布" value="未发布"></el-option>
            <el-option label="进行中" value="进行中"></el-option>
            <el-option label="已完成" value="已完成"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="预期完成时间">
          <el-date-picker v-model="formDataAdd.expectedFinishTime" type="datetime" placeholder="选择预期完成时间" value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAddSubmit">确定</el-button>
      </div>
    </el-dialog>

    <!-- 编辑任务对话框 -->
    <el-dialog
      title="编辑任务"
      :visible.sync="dialogEditVisible"
      width="50%"
      :before-close="handleClose"
    >
      <el-form :model="formDataEdit" label-width="100px">
        <el-form-item label="任务编号">
          <el-input v-model="formDataEdit.taskNo" placeholder="请输入任务编号" disabled />
        </el-form-item>
        <el-form-item label="任务名称">
          <el-input v-model="formDataEdit.name" placeholder="请输入任务名称" />
        </el-form-item>
        <el-form-item label="任务负责人">
          <el-input v-model="formDataEdit.leader" placeholder="请输入任务负责人" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="formDataEdit.status" placeholder="请选择状态">
            <el-option label="已发布" value="已发布"></el-option>
            <el-option label="未发布" value="未发布"></el-option>
            <el-option label="进行中" value="进行中"></el-option>
            <el-option label="已完成" value="已完成"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="预期完成时间">
          <el-date-picker v-model="formDataEdit.expectedFinishTime" type="datetime" placeholder="选择预期完成时间" value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
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
import { getTaskList, addTask, updateTask, deleteTask } from '@/api/task'

export default {
  name: 'TaskManagement',
  data() {
    return {
      formData: {
        name: '',
        leader: ''
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
        taskNo: '',
        name: '',
        leader: '',
        status: '未发布',
        expectedFinishTime: ''
      },
      formDataEdit: {
        taskNo: '',
        name: '',
        leader: '',
        status: '未发布',
        expectedFinishTime: ''
      }
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    // 获取任务列表
    async getList() {
      this.listLoading = true
      try {
        const res = await getTaskList({
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
    // 搜索
    handleSearch() {
      this.pagination.currentPage = 1
      this.getList()
      this.$message.success('搜索成功')
    },
    // 重置搜索
    handleReset() {
      this.pagination.currentPage = 1
      this.getList()
      this.$message.success('重置成功')
    },
    // 新增任务
    handleAdd() {
      this.dialogVisible = true
    },
    // 根据状态获取标签类型
    getStatusType(status) {
      switch (status) {
        case '已发布':
          return 'success';
        case '未发布':
          return 'info';
        case '进行中':
          return 'warning';
        case '已完成':
          return 'danger';
        default:
          return 'default';
      }
    },
    // 提交新增任务
    async handleAddSubmit() {
      try {
        await addTask(this.formDataAdd)
        this.$message.success('新增任务成功')
        this.dialogVisible = false
        this.getList()
      } catch (error) {
        this.$message.error('新增任务失败')
      }
    },
    // 编辑任务
    async handleEdit(row) {
      this.dialogEditVisible = true
      this.formDataEdit = { ...row }
    },
    // 提交编辑任务
    async handleEditSubmit() {
      try {
        await updateTask(this.formDataEdit)
        this.$message.success('编辑任务成功')
        this.dialogEditVisible = false
        this.getList()
      } catch (error) {
        this.$message.error('编辑任务失败')
      }
    },
    // 删除任务
    async handleDelete(taskNo) {
      try {
        await deleteTask(taskNo)
        this.$message.success('删除任务成功')
        this.getList()
      } catch (error) {
        this.$message.error('删除任务失败')
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
    handleClose() {
      this.dialogVisible = false
      this.dialogEditVisible = false
      this.formDataAdd = { taskNo: '', name: '', leader: '', status: '未发布', expectedFinishTime: '' }
      this.formDataEdit = { taskNo: '', name: '', leader: '', status: '未发布', expectedFinishTime: '' }
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