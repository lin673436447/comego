import request from '@/utils/request'

export default {
  getContactList(data) {
    return request.post('/contact/list', data)
  },

  getContactDetail(data) {
    return request.post('/contact/detail', data)
  },

  addContact(data) {
    return request.post('/contact/add', data)
  },

  updateContact(data) {
    return request.post('/contact/update', data)
  },

  deleteContact(data) {
    return request.post('/contact/delete', data)
  },

  getFavorites() {
    return request.post('/contact/favorites')
  }
}