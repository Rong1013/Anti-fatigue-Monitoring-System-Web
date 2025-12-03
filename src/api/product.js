import request from '@/utils/request'

// 获取产品列表
export function getProductList(params) {
  return request({
    url: '/vue-admin-template/product/list',
    method: 'get',
    params
  })
}

// 新增产品
export function addProduct(data) {
  return request({
    url: '/vue-admin-template/product/add',
    method: 'post',
    data
  })
}

// 编辑产品（可选，根据视图需求添加）
export function updateProduct(data) {
  return request({
    url: '/vue-admin-template/product/update',
    method: 'post',
    data
  })
}

// 删除产品（可选，根据视图需求添加）
export function deleteProduct(id) {
  return request({
    url: `/vue-admin-template/product/delete/${id}`,
    method: 'delete'
  })
}