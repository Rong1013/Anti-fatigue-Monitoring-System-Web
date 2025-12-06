const Mock = require('mockjs')
const { mock } = Mock

// 生成设备模拟数据
const deviceData = mock({
  'list|20': [{
    'id|+1': 1,
    'deviceNo': () => Mock.Random.string('upper', 2) + Mock.Random.integer(10000, 99999),
    'name': '@pick(["温湿度传感器", "网关", "摄像头", "边缘服务器"])',
    'type': '@pick(["监测设备", "网络设备", "监控设备", "计算设备"])',
    'product': () => Mock.Random.string('upper', 2) + Mock.Random.integer(100, 999),
    'status|1': ['在线', '离线', '故障'],
    'location': () => `实验室${Mock.Random.string('upper', 1)}-${Mock.Random.integer(100, 999)}`,
    // 生成随机经纬度（中国范围内）
'coordinate': () => `${(20 + Math.random() * 30).toFixed(6)}, ${(100 + Math.random() * 30).toFixed(6)}`,
// 20-50°N纬度范围，100-130°E经度范围
    'createTime': '@datetime("yyyy-MM-dd HH:mm:ss")'
  }]
})

// 处理分页和搜索过滤
function getDeviceList(params) {
  const { page = 1, limit = 10, keyword } = params
  let filteredList = deviceData.list
  if (keyword) {
    filteredList = filteredList.filter(item => {
      return item.name.includes(keyword) || item.type.includes(keyword)
    })
  }
  const total = filteredList.length
  const start = (page - 1) * limit
  const end = start + limit
  return {
    code: 20000,
    data: {
      list: filteredList.slice(start, end),
      total
    }
  }
}

// 导出mock接口
module.exports = [
  {
    url: '/device/list',
    method: 'get',
    response: config => {
      return getDeviceList(config.query)
    }
  }
]