<template>
  <view class="register-container">
    <!-- 顶部装饰区域 -->
    <view class="header-section">
      <view class="decoration-circle circle-1"></view>
      <view class="decoration-circle circle-2"></view>
      <view class="decoration-circle circle-3"></view>
      
      <view class="brand-area">
        <view class="logo-box">
          <text class="logo-text">注</text>
        </view>
        <text class="app-title">创建账号</text>
        <text class="app-subtitle">开启您的人情记账之旅</text>
      </view>
    </view>
    
    <!-- 表单区域 -->
    <view class="form-section">
      <view class="form-card">
        <view class="form-title">用户注册</view>
        
        <view class="input-group">
          <view class="input-item" :class="{ 'active': usernameFocus }">
            <view class="input-prefix">
              <text class="iconfont icon-user"></text>
            </view>
            <input 
              class="input-field" 
              type="text" 
              v-model="username" 
              placeholder="请输入用户名"
              maxlength="20"
              @focus="usernameFocus = true"
              @blur="usernameFocus = false"
            />
          </view>
          
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
              placeholder="请输入密码（6-20位）"
              maxlength="20"
              @focus="passwordFocus = true"
              @blur="passwordFocus = false"
            />
            <view class="input-suffix" @click="togglePassword">
              <text class="iconfont" :class="showPassword ? 'icon-eye' : 'icon-eye-close'"></text>
            </view>
          </view>
          
          <view class="input-item" :class="{ 'active': confirmPasswordFocus }">
            <view class="input-prefix">
              <text class="iconfont icon-check"></text>
            </view>
            <input 
              class="input-field" 
              :type="showConfirmPassword ? 'text' : 'password'" 
              v-model="confirmPassword" 
              placeholder="请确认密码"
              maxlength="20"
              @focus="confirmPasswordFocus = true"
              @blur="confirmPasswordFocus = false"
            />
            <view class="input-suffix" @click="toggleConfirmPassword">
              <text class="iconfont" :class="showConfirmPassword ? 'icon-eye' : 'icon-eye-close'"></text>
            </view>
          </view>
          
          <view class="input-item" :class="{ 'active': emailFocus }">
            <view class="input-prefix">
              <text class="iconfont icon-email"></text>
            </view>
            <input 
              class="input-field" 
              type="text" 
              v-model="email" 
              placeholder="请输入邮箱（可选）"
              maxlength="50"
              @focus="emailFocus = true"
              @blur="emailFocus = false"
            />
          </view>
        </view>
        
        <!-- 协议勾选 -->
        <view class="agreement-section">
          <view class="checkbox-wrapper">
            <view class="checkbox" :class="{ 'checked': agreeAgreement }" @click="toggleAgreement">
              <text v-if="agreeAgreement" class="check-icon">✓</text>
            </view>
            <view class="agreement-text">
              <text class="text-normal">我已阅读并同意</text>
              <text class="text-link" @click="goToUserAgreement">《用户服务协议》</text>
              <text class="text-normal">和</text>
              <text class="text-link" @click="goToPrivacyPolicy">《隐私政策》</text>
            </view>
          </view>
        </view>
        
        <view class="action-area">
          <button 
            class="register-btn" 
            :class="{ 'loading': loading, 'disabled': !agreeAgreement }"
            @click="handleRegister" 
            :disabled="loading || !agreeAgreement"
          >
            <text v-if="!loading">注 册</text>
            <view v-else class="loading-spinner"></view>
          </button>
        </view>
        
        <view class="footer-area">
          <text class="login-link" @click="goToLogin">
            已有账号？<text class="highlight">立即登录</text>
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
      username: '',
      phone: '',
      password: '',
      confirmPassword: '',
      email: '',
      showPassword: false,
      showConfirmPassword: false,
      loading: false,
      agreeAgreement: false,
      usernameFocus: false,
      phoneFocus: false,
      passwordFocus: false,
      confirmPasswordFocus: false,
      emailFocus: false
    }
  },
  
  methods: {
    ...mapActions('user', ['register']),
    
    togglePassword() {
      this.showPassword = !this.showPassword
    },
    
    toggleConfirmPassword() {
      this.showConfirmPassword = !this.showConfirmPassword
    },
    
    toggleAgreement() {
      this.agreeAgreement = !this.agreeAgreement
    },
    
    goToUserAgreement() {
      uni.navigateTo({
        url: '/pages/agreement/user-agreement'
      })
    },
    
    goToPrivacyPolicy() {
      uni.navigateTo({
        url: '/pages/agreement/privacy-policy'
      })
    },
    
    validateForm() {
      if (!this.username) {
        uni.showToast({
          title: '请输入用户名',
          icon: 'none'
        })
        return false
      }
      
      if (this.username.length < 2) {
        uni.showToast({
          title: '用户名长度不能少于2位',
          icon: 'none'
        })
        return false
      }
      
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
      
      if (this.password.length < 6 || this.password.length > 20) {
        uni.showToast({
          title: '密码长度为6-20位',
          icon: 'none'
        })
        return false
      }
      
      if (this.password !== this.confirmPassword) {
        uni.showToast({
          title: '两次密码不一致',
          icon: 'none'
        })
        return false
      }
      
      if (this.email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.email)) {
        uni.showToast({
          title: '邮箱格式不正确',
          icon: 'none'
        })
        return false
      }
      
      return true
    },
    
    async handleRegister() {
      if (!this.validateForm()) {
        return
      }
      
      this.loading = true
      
      try {
        const registerData = {
          username: this.username,
          phone: this.phone,
          password: this.password
        }
        
        await this.register(registerData)
        
        uni.showToast({
          title: '注册成功',
          icon: 'success'
        })
        
        setTimeout(() => {
          uni.navigateBack()
        }, 1500)
      } catch (error) {
        console.error('注册失败:', error)
      } finally {
        this.loading = false
      }
    },
    
    goToLogin() {
      uni.navigateBack()
    }
  }
}
</script>

<style lang="scss" scoped>
.register-container {
  min-height: 100vh;
  background: linear-gradient(180deg, #f093fb 0%, #f5576c 100%);
  position: relative;
  overflow: hidden;
}

/* 顶部装饰区域 */
.header-section {
  height: 35vh;
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
  bottom: 30rpx;
  left: -60rpx;
}

.circle-3 {
  width: 150rpx;
  height: 150rpx;
  top: 60rpx;
  left: 60rpx;
  background: rgba(255, 255, 255, 0.05);
}

.brand-area {
  text-align: center;
  z-index: 1;
}

.logo-box {
  width: 120rpx;
  height: 120rpx;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 28rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 24rpx;
  box-shadow: 0 16rpx 32rpx rgba(0, 0, 0, 0.15);
}

.logo-text {
  font-size: 60rpx;
  font-weight: bold;
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.app-title {
  display: block;
  font-size: 44rpx;
  font-weight: bold;
  color: #ffffff;
  margin-bottom: 10rpx;
  letter-spacing: 4rpx;
}

.app-subtitle {
  display: block;
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.85);
  letter-spacing: 2rpx;
}

/* 表单区域 */
.form-section {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 70vh;
  background: #ffffff;
  border-radius: 40rpx 40rpx 0 0;
  padding: 40rpx 40rpx;
  overflow-y: auto;
}

.form-card {
  padding-bottom: 40rpx;
}

.form-title {
  font-size: 36rpx;
  font-weight: bold;
  color: #333333;
  margin-bottom: 40rpx;
  text-align: center;
}

.input-group {
  margin-bottom: 30rpx;
}

.input-item {
  display: flex;
  align-items: center;
  height: 90rpx;
  background: #f8f9fa;
  border-radius: 16rpx;
  padding: 0 28rpx;
  margin-bottom: 20rpx;
  border: 2rpx solid transparent;
  transition: all 0.3s ease;
  
  &.active {
    background: #ffffff;
    border-color: #f5576c;
    box-shadow: 0 4rpx 20rpx rgba(245, 87, 108, 0.12);
  }
}

.input-prefix {
  margin-right: 16rpx;
}

.iconfont {
  font-size: 36rpx;
  color: #999999;
}

.input-item.active .iconfont {
  color: #f5576c;
}

.input-field {
  flex: 1;
  font-size: 28rpx;
  color: #333333;
  height: 100%;
}

.input-suffix {
  padding: 10rpx;
}

/* 注册按钮 */
.action-area {
  margin-top: 30rpx;
}

/* 协议勾选区域 */
.agreement-section {
  margin-top: 30rpx;
  padding: 0 10rpx;
}

.checkbox-wrapper {
  display: flex;
  align-items: flex-start;
  gap: 16rpx;
}

.checkbox {
  width: 36rpx;
  height: 36rpx;
  border: 2rpx solid #CCCCCC;
  border-radius: 8rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  margin-top: 4rpx;
  transition: all 0.3s ease;
  
  &.checked {
    background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
    border-color: #f5576c;
  }
  
  .check-icon {
    font-size: 24rpx;
    color: #FFFFFF;
    font-weight: bold;
  }
}

.agreement-text {
  flex: 1;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 4rpx;
  
  .text-normal {
    font-size: 26rpx;
    color: #666666;
    line-height: 1.6;
  }
  
  .text-link {
    font-size: 26rpx;
    color: #f5576c;
    font-weight: 500;
    line-height: 1.6;
  }
}

.register-btn {
  width: 100%;
  height: 90rpx;
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  border-radius: 45rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #ffffff;
  font-size: 30rpx;
  font-weight: 500;
  letter-spacing: 8rpx;
  box-shadow: 0 8rpx 24rpx rgba(245, 87, 108, 0.35);
  transition: all 0.3s ease;
  
  &:active {
    transform: translateY(2rpx);
    box-shadow: 0 4rpx 16rpx rgba(245, 87, 108, 0.25);
  }
  
  &[disabled] {
    opacity: 0.7;
  }
  
  &.disabled {
    background: linear-gradient(135deg, #ccc 0%, #999 100%);
    box-shadow: 0 8rpx 24rpx rgba(153, 153, 153, 0.35);
  }
}

.loading-spinner {
  width: 36rpx;
  height: 36rpx;
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
  margin-top: 30rpx;
  text-align: center;
}

.login-link {
  font-size: 26rpx;
  color: #666666;
}

.highlight {
  color: #f5576c;
  font-weight: 500;
}
</style>
