import request from '@/utils/request'

// 获取任务列表
export function getTaskList(params) {
  return request({
    url: '/api/task/list',
    method: 'get',
    params
  })
}

// 新增任务
export function addTask(data) {
  return request({
    url: '/api/task/add',
    method: 'post',
    data
  })
}

// 编辑任务
export function updateTask(data) {
  return request({
    url: '/api/task/update',
    method: 'put',
    data
  })
}

// 删除任务
export function deleteTask(id) {
  return request({
    url: `/api/task/delete/${id}`,
    method: 'delete'
  })
}