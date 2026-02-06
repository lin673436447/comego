<template>
  <view class="login-container">
    <!-- 顶部装饰区域 -->
    <view class="header-section">
      <view class="decoration-circle circle-1"></view>
      <view class="decoration-circle circle-2"></view>
      <view class="decoration-circle circle-3"></view>
      
      <view class="brand-area">
        <view class="logo-box">
          <text class="logo-text">往</text>
        </view>
        <text class="app-title">往来记</text>
        <text class="app-subtitle">记录每一份人情往来</text>
      </view>
    </view>
    
    <!-- 表单区域 -->
    <view class="form-section">
      <view class="form-card">
        <view class="form-title">欢迎登录</view>
        
        <view class="input-group">
          <view class="input-item" :class="{ 'active': phoneFocus }">
            <view class="input-prefix">
              <text class="iconfont icon-phone"></text>
            </view>
            <input 
              class="input-field" 
              type="number" 
              v-model="phone" 
              placeholder="请输入手机号"
              maxlength="11"
              @focus="phoneFocus = true"
              @blur="phoneFocus = false"
            />
          </view>
          
          <view class="input-item" :class="{ 'active': passwordFocus }">
            <view class="input-prefix">
              <text class="iconfont icon-lock"></text>
            </view>
            <input 
              class="input-field" 
              :type="showPassword ? 'text' : 'password'" 
              v-model="password" 
              placeholder="请输入密码"
              maxlength="20"
              @focus="passwordFocus = true"
              @blur="passwordFocus = false"
            />
            <view class="input-suffix" @click="togglePassword">
              <text class="iconfont" :class="showPassword ? 'icon-eye' : 'icon-eye-close'"></text>
            </view>
          </view>
        </view>
        
        <view class="action-area">
          <button 
            class="login-btn" 
            :class="{ 'loading': loading }"
            @click="handleLogin" 
            :disabled="loading"
          >
            <text v-if="!loading">登 录</text>
            <view v-else class="loading-spinner"></view>
          </button>
        </view>
        
        <view class="footer-area">
          <text class="register-link" @click="goToRegister">
            还没有账号？<text class="highlight">立即注册</text>
          </text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  data() {
    return {
      phone: '13800138001',
      password: '123456',
      showPassword: false,
      loading: false,
      phoneFocus: false,
      passwordFocus: false
    }
  },
  
  methods: {
    ...mapActions('user', ['login']),
    
    togglePassword() {
      this.showPassword = !this.showPassword
    },
    
    validateForm() {
      if (!this.phone) {
        uni.showToast({
          title: '请输入手机号',
          icon: 'none'
        })
        return false
      }
      
      if (!/^1[3-9]\d{9}$/.test(this.phone)) {
        uni.showToast({
          title: '手机号格式不正确',
          icon: 'none'
        })
        return false
      }
      
      if (!this.password) {
        uni.showToast({
          title: '请输入密码',
          icon: 'none'
        })
        return false
      }
      
      if (this.password.length < 6) {
        uni.showToast({
          title: '密码长度不能少于6位',
          icon: 'none'
        })
        return false
      }
      
      return true
    },
    
    async handleLogin() {
      if (!this.validateForm()) {
        return
      }
      
      this.loading = true
      
      try {
        await this.login({
          phone: this.phone,
          password: this.password
        })
        
        uni.showToast({
          title: '登录成功',
          icon: 'success'
        })
        
        setTimeout(() => {
          uni.switchTab({
            url: '/pages/index/index'
          })
        }, 1500)
      } catch (error) {
        console.error('登录失败:', error)
      } finally {
        this.loading = false
      }
    },
    
    goToRegister() {
      uni.navigateTo({
        url: '/pages/register/register'
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(180deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
}

/* 顶部装饰区域 */
.header-section {
  height: 45vh;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.decoration-circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
}

.circle-1 {
  width: 300rpx;
  height: 300rpx;
  top: -100rpx;
  right: -80rpx;
}

.circle-2 {
  width: 200rpx;
  height: 200rpx;
  bottom: 50rpx;
  left: -60rpx;
}

.circle-3 {
  width: 150rpx;
  height: 150rpx;
  top: 80rpx;
  left: 60rpx;
  background: rgba(255, 255, 255, 0.05);
}

.brand-area {
  text-align: center;
  z-index: 1;
}

.logo-box {
  width: 140rpx;
  height: 140rpx;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 32rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 30rpx;
  box-shadow: 0 20rpx 40rpx rgba(0, 0, 0, 0.2);
}

.logo-text {
  font-size: 72rpx;
  font-weight: bold;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.app-title {
  display: block;
  font-size: 48rpx;
  font-weight: bold;
  color: #ffffff;
  margin-bottom: 12rpx;
  letter-spacing: 4rpx;
}

.app-subtitle {
  display: block;
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.8);
  letter-spacing: 2rpx;
}

/* 表单区域 */
.form-section {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 60vh;
  background: #ffffff;
  border-radius: 40rpx 40rpx 0 0;
  padding: 50rpx 40rpx;
}

.form-card {
  height: 100%;
}

.form-title {
  font-size: 40rpx;
  font-weight: bold;
  color: #333333;
  margin-bottom: 50rpx;
  text-align: center;
}

.input-group {
  margin-bottom: 40rpx;
}

.input-item {
  display: flex;
  align-items: center;
  height: 100rpx;
  background: #f8f9fa;
  border-radius: 16rpx;
  padding: 0 30rpx;
  margin-bottom: 24rpx;
  border: 2rpx solid transparent;
  transition: all 0.3s ease;
  
  &.active {
    background: #ffffff;
    border-color: #667eea;
    box-shadow: 0 4rpx 20rpx rgba(102, 126, 234, 0.15);
  }
}

.input-prefix {
  margin-right: 20rpx;
}

.iconfont {
  font-size: 40rpx;
  color: #999999;
}

.input-item.active .iconfont {
  color: #667eea;
}

.input-field {
  flex: 1;
  font-size: 30rpx;
  color: #333333;
  height: 100%;
}

.input-suffix {
  padding: 10rpx;
}

/* 登录按钮 */
.action-area {
  margin-top: 50rpx;
}

.login-btn {
  width: 100%;
  height: 100rpx;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #ffffff;
  font-size: 32rpx;
  font-weight: 500;
  letter-spacing: 8rpx;
  box-shadow: 0 10rpx 30rpx rgba(102, 126, 234, 0.4);
  transition: all 0.3s ease;
  
  &:active {
    transform: translateY(2rpx);
    box-shadow: 0 6rpx 20rpx rgba(102, 126, 234, 0.3);
  }
  
  &[disabled] {
    opacity: 0.7;
  }
}

.loading-spinner {
  width: 40rpx;
  height: 40rpx;
  border: 4rpx solid rgba(255, 255, 255, 0.3);
  border-top-color: #ffffff;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

/* 底部区域 */
.footer-area {
  margin-top: 40rpx;
  text-align: center;
}

.register-link {
  font-size: 28rpx;
  color: #666666;
}

.highlight {
  color: #667eea;
  font-weight: 500;
}
</style>
