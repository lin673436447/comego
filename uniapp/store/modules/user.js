import authApi from '@/api/auth'
import userApi from '@/api/user'

const state = {
  token: uni.getStorageSync('token') || '',
  userInfo: uni.getStorageSync('userInfo') || null
}

const mutations = {
  SET_TOKEN(state, token) {
    state.token = token
    uni.setStorageSync('token', token)
  },
  
  SET_USER_INFO(state, userInfo) {
    state.userInfo = userInfo
    uni.setStorageSync('userInfo', userInfo)
  },
  
  CLEAR_USER(state) {
    state.token = ''
    state.userInfo = null
    uni.removeStorageSync('token')
    uni.removeStorageSync('userInfo')
  }
}

const actions = {
  async login({ commit }, loginData) {
    try {
      const res = await authApi.login(loginData)
      if (res.code === 1) {
        commit('SET_TOKEN', res.data.token)
        commit('SET_USER_INFO', res.data.user)
        return res
      }
      throw new Error(res.msg || '登录失败')
    } catch (error) {
      throw error
    }
  },
  
  async register({ commit }, registerData) {
    try {
      const res = await authApi.register(registerData)
      if (res.code === 1) {
        return res
      }
      throw new Error(res.msg || '注册失败')
    } catch (error) {
      throw error
    }
  },
  
  async logout({ commit }) {
    try {
      await authApi.logout()
      commit('CLEAR_USER')
      uni.reLaunch({
        url: '/pages/login/login'
      })
    } catch (error) {
      commit('CLEAR_USER')
      uni.reLaunch({
        url: '/pages/login/login'
      })
    }
  },
  
  async getUserInfo({ commit }) {
    try {
      const res = await userApi.getUserInfo()
      if (res.code === 1) {
        commit('SET_USER_INFO', res.data)
        return res
      }
      throw new Error(res.msg || '获取用户信息失败')
    } catch (error) {
      throw error
    }
  },
  
  async updateUserInfo({ commit }, userData) {
    try {
      const res = await userApi.updateUserInfo(userData)
      if (res.code === 1) {
        await this.dispatch('user/getUserInfo')
        return res
      }
      throw new Error(res.msg || '更新用户信息失败')
    } catch (error) {
      throw error
    }
  },
  
  async changePassword({ }, passwordData) {
    try {
      const res = await userApi.changePassword(passwordData)
      if (res.code === 1) {
        return res
      }
      throw new Error(res.msg || '修改密码失败')
    } catch (error) {
      throw error
    }
  }
}

const getters = {
  isLoggedIn: (state) => !!state.token,
  username: (state) => state.userInfo?.username || '',
  avatar: (state) => state.userInfo?.avatar_url || ''
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters
}