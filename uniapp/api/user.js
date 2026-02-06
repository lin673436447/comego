import request from '@/utils/request'

export default {
  getUserInfo() {
    return request.post('/user/info')
  },
  
  updateUserInfo(data) {
    return request.post('/user/update', data)
  },
  
  changePassword(data) {
    return request.post('/user/change-password', data, true)
  }
}