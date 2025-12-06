const Mock = require('mockjs')
const { param2Obj } = require('./utils')

const taskList = []
const count = 20

// 生成模拟数据（与用户管理一致的手动构造方式）
const taskStatus = ['已发布', '未发布', '进行中', '已完成'];
const surnames = ['张', '李', '王', '陈', '刘', '杨', '赵', '黄', '周', '吴'];
const firstNames = ['伟', '芳', '娜', '敏', '静', '强', '磊', '军', '勇', '艳'];

for (let i = 0; i < count; i++) {
  const taskNo = `TASK-${String(i + 1).padStart(3, '0')}`; // 类似用户管理的编号格式
  const leaderSurname = surnames[Math.floor(Math.random() * surnames.length)];
  const leaderFirstName = firstNames[Math.floor(Math.random() * firstNames.length)];
  const leader = `${leaderSurname}${leaderFirstName}`;
  const name = `任务-${String(i + 1).padStart(3, '0')}-${Math.random().toString(36).substr(2, 4)}`; // 随机任务名称
  const status = taskStatus[Math.floor(Math.random() * taskStatus.length)];
  const createTime = Mock.mock('@datetime()');
  const expectedFinishTime = Mock.mock('@datetime()');

  taskList.push({
    id: i + 1,
    taskNo,
    name,
    leader,
    status,
    createTime,
    expectedFinishTime
  });
}

module.exports = [
  {
    url: '/api/task/list',
    type: 'get',
    response: config => {
      const { name, leader, page = 1, pageSize = 10 } = config.query
      const mockList = taskList.filter(item => {
        if (name && item.name.indexOf(name) === -1) return false
        if (leader && item.leader.indexOf(leader) === -1) return false
        return true
      })
      const pageList = mockList.slice((page - 1) * pageSize, page * pageSize)
      return {
        code: 20000,
        data: {
          list: pageList,
          total: mockList.length
        }
      }
    }
  },
  {
    url: '/api/task/add',
    type: 'post',
    response: config => {
      const data = JSON.parse(config.body)
      const newTask = {
        id: taskList.length + 1,
        ...data,
        createTime: Mock.mock('@datetime()')
      }
      taskList.push(newTask)
      return { code: 20000, data: newTask }
    }
  },
  {
    url: '/api/task/update',
    type: 'put',
    response: config => {
      const data = JSON.parse(config.body)
      const index = taskList.findIndex(item => item.id === data.id)
      taskList.splice(index, 1, data)
      return { code: 20000, data }
    }
  },
  {
    url: '/api/task/delete/\d+',
    type: 'delete',
    response: config => {
      const { id } = config.query
      taskList.splice(taskList.findIndex(item => item.id === Number(id)), 1)
      return { code: 20000, data: { id } }
    }
  }
]