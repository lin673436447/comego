const BASE_URL = 'http://113.45.152.52:18101/api/v1'

class Request {
  constructor() {
    this.baseURL = BASE_URL
    this.timeout = 30000
  }

  request(options) {
    return new Promise((resolve, reject) => {
      const token = uni.getStorageSync('token')
      
      // 构建请求参数
      let requestData = options.data || {}
      let requestUrl = options.url
      
      // 如果是GET或POST请求并且需要使用查询参数
      if ((options.method === 'GET' || options.useQueryParams) && Object.keys(requestData).length > 0) {
        const params = Object.entries(requestData)
          .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
          .join('&')
        requestUrl += '?' + params
        requestData = {}
      }
      
      uni.request({
        url: this.baseURL + requestUrl,
        method: options.method || 'POST',
        data: requestData,
        header: {
          'Content-Type': options.useQueryParams ? 'application/x-www-form-urlencoded' : 'application/json',
          'Authorization': token ? `Bearer ${token}` : '',
          ...options.header
        },
        timeout: this.timeout,
        success: (res) => {
          if (res.statusCode === 200) {
            const data = res.data
            if (data.code === 1) {
              resolve(data)
            } else {
              uni.showToast({
                title: data.msg || '请求失败',
                icon: 'none',
                duration: 2000
              })
              reject(data)
            }
          } else if (res.statusCode === 401) {
            uni.removeStorageSync('token')
            uni.removeStorageSync('userInfo')
            uni.reLaunch({
              url: '/pages/login/login'
            })
            reject(res)
          } else {
            uni.showToast({
              title: '网络错误',
              icon: 'none',
              duration: 2000
            })
            reject(res)
          }
        },
        fail: (err) => {
          uni.showToast({
            title: '网络连接失败',
            icon: 'none',
            duration: 2000
          })
          reject(err)
        }
      })
    })
  }

  get(url, data = {}) {
    return this.request({
      url,
      method: 'GET',
      data
    })
  }

  post(url, data = {}, useQueryParams = false) {
    return this.request({
      url,
      method: 'POST',
      data,
      useQueryParams
    })
  }

  put(url, data = {}) {
    return this.request({
      url,
      method: 'PUT',
      data
    })
  }

  delete(url, data = {}) {
    return this.request({
      url,
      method: 'DELETE',
      data
    })
  }
}

export default new Request()