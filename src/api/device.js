import request from '@/utils/request'

// 获取设备列表
export function getDeviceList(params) {
  return request({
    url: '/device/list',
    method: 'get',
    params
  })
}

// 新增设备
export function addDevice(data) {
  return request({
    url: '/device/add',
    method: 'post',
    data
  })
}

// 编辑设备
export function updateDevice(data) {
  return request({
    url: '/device/update',
    method: 'post',
    data
  })
}

// 删除设备
export function deleteDevice(id) {
  return request({
    url: `/device/delete/${id}`,
    method: 'post'
  })
}