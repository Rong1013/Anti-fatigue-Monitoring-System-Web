const Mock = require('mockjs')
const { param2Obj } = require('./utils')

const alarmList = Mock.mock({
  'data|100': [{
    'id|+1': 1,
    'type|1': ['message', 'device', 'network'],
    'level|1': ['normal', 'serious', 'urgent'],
    'name': '@cword(5, 10)',
    'productId': '@string("uppercase", 8)',
    'deviceId': '@string("uppercase", 10)',
    'status|1': ['unprocessed', 'processed'],
    'time': '@datetime("yyyy-MM-dd HH:mm:ss")'
  }]
}).data

const response = (config) => {
  const params = config.query
  const { type, level, status, page = 1, size = 10 } = params

  let mockList = alarmList.filter(item => {
    if (type && item.type !== type) return false
    if (level && item.level !== level) return false
    if (status && item.status !== status) return false
    return true
  })

  const total = mockList.length
  const start = (page - 1) * size
  const end = start + size
  mockList = mockList.slice(start, end)

  return {
    code: 20000,
    data: {
      list: mockList.map(item => ({
        ...item,
        typeLabel: item.type === 'message' ? '消息告警' : item.type === 'device' ? '设备告警' : '网络告警',
        levelLabel: item.level === 'normal' ? '一般' : item.level === 'serious' ? '严重' : '紧急',
        statusLabel: item.status === 'unprocessed' ? '未处理' : '已处理'
      })),
      total
    }
  }
}

module.exports = [
  {
    url: '/api/alarm/list',
    type: 'get',
    response
  },
  {
    url: '/api/alarm/detail',
    type: 'get',
    response: (config) => {
      const { id } = config.query
      const alarm = alarmList.find(item => item.id === Number(id))
      return {
        code: 20000,
        data: alarm ? { ...alarm, typeLabel: alarm.type === 'message' ? '消息告警' : alarm.type === 'device' ? '设备告警' : '网络告警', levelLabel: alarm.level === 'normal' ? '一般' : alarm.level === 'serious' ? '严重' : '紧急', statusLabel: alarm.status === 'unprocessed' ? '未处理' : '已处理' } : null
      }
    }
  },
  {
    url: '/api/alarm/process',
    type: 'post',
    response: (config) => {
      const { id } = config.query
      const index = alarmList.findIndex(item => item.id === Number(id))
      if (index !== -1) {
        alarmList[index].status = 'processed'
        return { code: 200, message: '处理成功' }
      }
      return { code: 404, message: '告警不存在' }
    }
  }
]