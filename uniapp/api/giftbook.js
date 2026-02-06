import request from '@/utils/request'

export default {
  addGiftBook(data) {
    return request.post('/giftbook/add', data)
  },

  getGiftBookList(data) {
    return request.post('/giftbook/list', data)
  },

  getGiftBookDetail(data) {
    return request.post('/giftbook/detail', data)
  },

  updateGiftBook(data) {
    return request.post('/giftbook/update', data)
  },

  deleteGiftBook(data) {
    return request.post('/giftbook/delete', data)
  },

  getActiveGiftBooks(data) {
    return request.post('/giftbook/active', data)
  },

  getRecentGiftBooksWithRecords(data) {
    return request.post('/giftbook/recent-with-records', data)
  }
}
