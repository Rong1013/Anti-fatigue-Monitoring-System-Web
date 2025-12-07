import request from '@/utils/request'

// 获取权限列表
export function getPermissionList(params) {
  return request({
    url: '/api/permission/list',
    method: 'get',
    params
  })
}

// 新增权限
export function addPermission(data) {
  return request({
    url: '/api/permission/add',
    method: 'post',
    data
  })
}

// 编辑权限
export function updatePermission(data) {
  return request({
    url: '/api/permission/update',
    method: 'post',
    data
  })
}

// 删除权限
export function deletePermission(permissionNo) {
  return request({
    url: `/api/permission/delete/${permissionNo}`,
    method: 'get'
  })
}