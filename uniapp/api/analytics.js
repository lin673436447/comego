import request from '@/utils/request'

export default {
  getExpenseStatistics(data) {
    return request.post('/analytics/expense', data)
  },
  
  getCategoryAnalysis(data) {
    return request.post('/analytics/category', data)
  },
  
  getRelationData(data) {
    return request.post('/analytics/relation', data)
  },
  
  getContactTransactions(data) {
    return request.post('/analytics/contact-transactions', data)
  }
}
