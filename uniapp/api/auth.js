import request from '@/utils/request'

export default {
  login(data) {
    return request.post('/user/login', data, true)
  },
  
  register(data) {
    return request.post('/user/register', data, true)
  },
  
  logout() {
    return request.post('/user/logout')
  }
}