import request from '@/utils/request'

export default {
  getBudget(data) {
    return request.post('/budget/get', data)
  },
  
  setBudget(data) {
    return request.post('/budget/set', data)
  },
  
  getBudgetStatistics(data) {
    return request.post('/budget/statistics', data)
  }
}