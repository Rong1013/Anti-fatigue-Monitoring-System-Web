<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label="告警类型">
        <el-select v-model="searchForm.type" placeholder="请选择">
          <el-option label="消息告警" value="message"></el-option>
          <el-option label="设备告警" value="device"></el-option>
          <el-option label="网络告警" value="network"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="告警级别">
        <el-select v-model="searchForm.level" placeholder="请选择">
          <el-option label="一般" value="normal"></el-option>
          <el-option label="严重" value="serious"></el-option>
          <el-option label="紧急" value="urgent"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="告警状态">
        <el-select v-model="searchForm.status" placeholder="请选择">
          <el-option label="未处理" value="unprocessed"></el-option>
          <el-option label="已处理" value="processed"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="getList">查询</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 告警列表 -->
    <el-table :data="list" border stripe style="width: 100%">
      <el-table-column prop="typeLabel" label="告警类型" align="center"></el-table-column>
      <el-table-column prop="levelLabel" label="告警级别" align="center">
        <template slot-scope="{ row }">
          <el-tag :type="row.level === 'urgent' ? 'danger' : row.level === 'serious' ? 'warning' : 'info'">
            {{ row.levelLabel }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="告警名称" align="center"></el-table-column>
      <el-table-column prop="productId" label="产品标识" align="center"></el-table-column>
      <el-table-column prop="deviceId" label="设备标识" align="center"></el-table-column>
      <el-table-column prop="statusLabel" label="告警状态" align="center">
        <template slot-scope="{ row }">
          <el-tag :type="row.status === 'unprocessed' ? 'danger' : 'success'">
            {{ row.statusLabel }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="time" label="告警时间" width="180" align="center"></el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="{ row }">
          <el-button type="primary" size="small" @click="handleDetail(row)">详情</el-button>
          <el-button type="danger" size="small" v-if="row.status === 'unprocessed'" @click="handleProcess(row)">处理</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <!-- 详情对话框 -->
    <el-dialog
      title="告警详情"
      :visible.sync="detailVisible"
      width="60%"
      :before-close="() => detailVisible = false">
      <el-form :model="currentAlarm" label-width="100px">
        <el-form-item label="告警类型">
          <span>{{ currentAlarm.typeLabel }}</span>
        </el-form-item>
        <el-form-item label="告警级别">
          <el-tag :type="currentAlarm.level === 'urgent' ? 'danger' : currentAlarm.level === 'serious' ? 'warning' : 'info'">
            {{ currentAlarm.levelLabel }}
          </el-tag>
        </el-form-item>
        <el-form-item label="告警名称">
          <span>{{ currentAlarm.name }}</span>
        </el-form-item>
        <el-form-item label="产品标识">
          <span>{{ currentAlarm.productId }}</span>
        </el-form-item>
        <el-form-item label="设备标识">
          <span>{{ currentAlarm.deviceId }}</span>
        </el-form-item>
        <el-form-item label="告警状态">
          <el-tag :type="currentAlarm.status === 'unprocessed' ? 'danger' : 'success'">
            {{ currentAlarm.statusLabel }}
          </el-tag>
        </el-form-item>
        <el-form-item label="告警时间">
          <span>{{ currentAlarm.time }}</span>
        </el-form-item>
        <el-form-item label="处理意见">
          <span>{{ currentAlarm.comment || '无' }}</span>
        </el-form-item>
      </el-form>
    </el-dialog>

    <!-- 处理对话框 -->
    <el-dialog
      title="处理告警"
      :visible.sync="processVisible"
      width="50%"
      :before-close="handleProcessClose">
      <el-form :model="processForm" label-width="100px">
        <el-form-item label="处理状态" required>
          <el-radio-group v-model="processForm.status">
            <el-radio :label="'unprocessed'">未处理</el-radio>
            <el-radio :label="'processed'">已处理</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="处理意见">
          <el-input v-model="processForm.comment" type="textarea" rows="4" placeholder="请输入处理意见"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="processVisible = false">取消</el-button>
        <el-button type="primary" @click="handleProcessSubmit">提交</el-button>
      </div>
    </el-dialog>

    <el-pagination
      :current-page="page.current"
      :page-sizes="[10, 20, 30, 50]"
      :page-size="page.size"
      layout="total, sizes, prev, pager, next, jumper"
      :total="page.total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    ></el-pagination>
  </div>
</template>

<script>
import { getAlarmList, getAlarmDetail, processAlarm } from '@/api/alarm'

export default {
  name: 'AlarmManagement',
  data() {
    return {
      searchForm: {
        type: '',
        level: '',
        status: ''
      },
      list: [],
      page: {
        current: 1,
        size: 10,
        total: 0
      },
      detailVisible: false, // 详情对话框可见性
      processVisible: false, // 处理对话框可见性
      currentAlarm: {},// 当前选中的告警信息
      processForm: {
        status: 'unprocessed',
        comment: ''
      } // 处理表单数据
    }
  },
  created() {
    this.getList()
  },
  methods: {
    // 获取列表数据
    async getList() {
      try {
        const params = {
          ...this.searchForm,
          page: this.page.current,
          size: this.page.size
        }
        const res = await getAlarmList(params)
        console.log('告警列表接口响应:', res) // 添加日志输出
        this.list = res.data.list
        this.page.total = res.data.total
      } catch (error) {
        this.$message.error('获取告警列表失败，请检查网络或联系管理员')
        console.error('获取告警列表错误:', error)
      }
    },
    // 重置表单
    resetForm() {
      this.searchForm = { type: '', level: '', status: '' }
      this.page.current = 1
      this.getList()
    },
    // 详情操作
    async handleDetail(row) {
      try {
        // 调用详情接口获取详细数据
        const res = await getAlarmDetail({ id: row.id })
        console.log('详情接口返回数据:', res.data) // 添加日志输出
        if (res.data) {
          this.currentAlarm = res.data // 设置当前告警信息（包含typeLabel等完整字段）
          console.log('currentAlarm内容:', this.currentAlarm) // 添加日志输出
          this.detailVisible = true // 显示详情对话框
        } else {
          this.$message.warning('未找到该告警详情')
          this.detailVisible = false
          this.currentAlarm = {}
        }
      } catch (error) {
        this.$message.error('获取告警详情失败，请检查网络或联系管理员')
        console.error('获取告警详情错误:', error)
      }
    },
    // 处理操作
    handleProcess(row) {
      this.currentAlarm = row
      this.processForm.status = row.status // 初始化状态
      this.processForm.comment = row.comment || '' // 初始化处理意见
      this.processVisible = true
    },
    // 处理对话框关闭时重置表单
    handleProcessClose() {
      this.processForm = {
        status: 'unprocessed',
        comment: ''
      }
      this.processVisible = false
    },
    // 提交处理信息
    async handleProcessSubmit() {
      const res = await processAlarm({
        id: this.currentAlarm.id,
        status: this.processForm.status,
        comment: this.processForm.comment
      })
      if (res.code === 200) {
        this.$message.success('处理成功')
        this.processVisible = false
        this.getList()
      }
    },
    // 分页页码变化
    handleCurrentChange(val) {
      this.page.current = val
      this.getList()
    },
    // 分页每页数量变化
    handleSizeChange(val) {
      this.page.size = val
      this.page.current = 1
      this.getList()
    }
  }
}
</script>

<style scoped>
/* 状态标签样式调整 */
.el-tag {
  margin: 0;
}
</style>