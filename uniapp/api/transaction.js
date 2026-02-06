import request from '@/utils/request'

export default {
  addTransaction(data) {
    return request.post('/transaction/add', data)
  },

  getTransactionList(data) {
    return request.post('/transaction/list', data)
  },

  getTransactionDetail(data) {
    return request.post('/transaction/detail', data)
  },

  updateTransaction(data) {
    return request.post('/transaction/update', data)
  },

  deleteTransaction(data) {
    return request.post('/transaction/delete', data)
  },

  getRecentTransactions(data) {
    return request.post('/transaction/recent', data)
  },

  exportTransactions(data) {
    return request.post('/transaction/export', data)
  },

  batchAddTransactions(data) {
    return request.post('/transaction/batch-add', data)
  }
}