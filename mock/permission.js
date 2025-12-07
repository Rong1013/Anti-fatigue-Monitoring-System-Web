const Mock = require('mockjs')
const { param2Obj } = require('./utils')

const permissionList = []
const count = 20

for (let i = 0; i < count; i++) {
  permissionList.push(Mock.mock({
    permissionNo: 'P' + Mock.Random.string('number', 5),
    name: '权限' + Mock.Random.cword(2, 4),
    status: Mock.Random.pick(['已生效', '未生效']),
    createTime: Mock.Random.datetime('yyyy-MM-dd HH:mm:ss')
  }))
}

module.exports = [
  {
    url: '/api/permission/list',
    type: 'get',
    response: config => {
      const { name, page = 1, pageSize = 10 } = config.query
      const mockList = permissionList.filter(item => {
        if (name && item.name.indexOf(name) === -1) return false
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
    url: '/api/permission/add',
    type: 'post',
    response: config => {
      const data = JSON.parse(config.body)
      permissionList.push({
        ...data,
        createTime: Mock.Random.datetime('yyyy-MM-dd HH:mm:ss')
      })
      return { code: 20000, data: {} }
    }
  },
  {
    url: '/api/permission/update',
    type: 'put',
    response: config => {
      const data = JSON.parse(config.body)
      const index = permissionList.findIndex(item => item.permissionNo === data.permissionNo)
      permissionList.splice(index, 1, data)
      return { code: 20000, data: {} }
    }
  },
  {
    url: '/api/permission/delete',
    type: 'delete',
    response: config => {
      const { permissionNo } = config.query
      const index = permissionList.findIndex(item => item.permissionNo === permissionNo)
      permissionList.splice(index, 1)
      return { code: 20000, data: {} }
    }
  }
]