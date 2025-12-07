import request from '@/utils/request'

// 获取告警列表
export function getAlarmList(params) {
  return request({
    url: '/api/alarm/list',
    method: 'get',
    params
  })
}

// 获取告警详情
export function getAlarmDetail(params) {
  return request({
    url: '/api/alarm/detail',
    method: 'get',
    params
  })
}

// 处理告警
export function processAlarm(data) {
  return request({
    url: '/api/alarm/process',
    method: 'post',
    data
  })
}