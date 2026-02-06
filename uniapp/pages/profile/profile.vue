<template>
  <view class="page-container">
    <!-- 顶部渐变头部 -->
    <view class="header-gradient">
      <view class="decoration-circle circle-1"></view>
      <view class="decoration-circle circle-2"></view>
      <view class="decoration-circle circle-3"></view>
      
      <!-- 设置按钮 -->
      <view class="header-actions">
        <view class="action-btn" @click="goToNotifications">
          <text class="iconfont icon-bell"></text>
          <view class="badge" v-if="notificationCount > 0">{{ notificationCount }}</view>
        </view>
        <view class="action-btn" @click="goToSettings">
          <text class="iconfont icon-settings"></text>
        </view>
      </view>
      
      <!-- 用户信息 -->
      <view class="user-profile">
        <view class="avatar-wrapper" @click="changeAvatar">
          <image class="user-avatar" :src="avatar || '/static/default-avatar.png'" mode="aspectFill" @error="avatar = '/static/default-avatar.png'"></image>
          <view class="avatar-badge">
            <text class="iconfont icon-camera"></text>
          </view>
        </view>
        <view class="user-info">
          <text class="user-name">{{ username || '未登录' }}</text>
          <text class="user-phone" v-if="phone">{{ maskPhone(phone) }}</text>
        </view>
      </view>
      
      <!-- 数据统计 -->
      <view class="user-stats">
        <view class="stat-item">
          <text class="stat-value">{{ totalContacts }}</text>
          <text class="stat-label">联系人</text>
        </view>
        <view class="stat-divider"></view>
        <view class="stat-item">
          <text class="stat-value">{{ totalTransactions }}</text>
          <text class="stat-label">记账数</text>
        </view>
        <view class="stat-divider"></view>
        <view class="stat-item">
          <text class="stat-value">¥{{ totalAmount }}</text>
          <text class="stat-label">总支出</text>
        </view>
      </view>
    </view>
    
    <!-- 内容区域 -->
    <view class="content-section">
      <!-- 快捷功能 -->
      <view class="card">
        <view class="quick-menu-grid">
          <view class="quick-menu-item" @click="goToBudget">
            <view class="menu-icon blue">
              <text class="iconfont icon-wallet"></text>
            </view>
            <text class="menu-label">预算设置</text>
          </view>
          <view class="quick-menu-item" @click="goToNotifications">
            <view class="menu-icon orange">
              <text class="iconfont icon-bell"></text>
            </view>
            <text class="menu-label">通知提醒</text>
          </view>
          <view class="quick-menu-item" @click="goToDataExport">
            <view class="menu-icon green">
              <text class="iconfont icon-download"></text>
            </view>
            <text class="menu-label">数据导出</text>
          </view>
          <view class="quick-menu-item" @click="goToStats">
            <view class="menu-icon purple">
              <text class="iconfont icon-bar-chart"></text>
            </view>
            <text class="menu-label">往来热度</text>
          </view>
        </view>
      </view>
      
      <!-- 账户设置 -->
      <view class="card">
        <view class="card-header">
          <view class="header-left">
            <view class="icon-box blue icon-box-sm">
              <text class="iconfont icon-user"></text>
            </view>
            <text class="header-title">账户设置</text>
          </view>
        </view>
        
        <view class="menu-list">
          <view class="menu-item" @click="goToEditProfile">
            <view class="item-left">
              <view class="item-icon blue">
                <text class="iconfont icon-edit"></text>
              </view>
              <text class="item-label">编辑资料</text>
            </view>
            <view class="item-right">
              <text class="iconfont icon-arrow-right"></text>
            </view>
          </view>
          
          <view class="menu-item" @click="goToChangePassword">
            <view class="item-left">
              <view class="item-icon green">
                <text class="iconfont icon-lock"></text>
              </view>
              <text class="item-label">修改密码</text>
            </view>
            <view class="item-right">
              <text class="iconfont icon-arrow-right"></text>
            </view>
          </view>
        </view>
      </view>
      
      <!-- 数据管理 -->
      <view class="card">
        <view class="card-header">
          <view class="header-left">
            <view class="icon-box green icon-box-sm">
              <text class="iconfont icon-database"></text>
            </view>
            <text class="header-title">数据管理</text>
          </view>
        </view>
        
        <view class="menu-list">
          <view class="menu-item" @click="goToDataBackup">
            <view class="item-left">
              <view class="item-icon purple">
                <text class="iconfont icon-cloud-upload"></text>
              </view>
              <text class="item-label">数据备份</text>
            </view>
            <view class="item-right">
              <text class="iconfont icon-arrow-right"></text>
            </view>
          </view>
          
          <view class="menu-item" @click="goToDataClear">
            <view class="item-left">
              <view class="item-icon red">
                <text class="iconfont icon-trash"></text>
              </view>
              <text class="item-label">清空数据</text>
            </view>
            <view class="item-right">
              <text class="iconfont icon-arrow-right"></text>
            </view>
          </view>
        </view>
      </view>
      
      <!-- 关于 -->
      <view class="card">
        <view class="card-header">
          <view class="header-left">
            <view class="icon-box orange icon-box-sm">
              <text class="iconfont icon-info"></text>
            </view>
            <text class="header-title">关于</text>
          </view>
        </view>
        
        <view class="menu-list">
          <view class="menu-item" @click="goToHelp">
            <view class="item-left">
              <view class="item-icon blue">
                <text class="iconfont icon-help-circle"></text>
              </view>
              <text class="item-label">帮助与反馈</text>
            </view>
            <view class="item-right">
              <text class="iconfont icon-arrow-right"></text>
            </view>
          </view>
          
          <view class="menu-item" @click="goToAbout">
            <view class="item-left">
              <view class="item-icon green">
                <text class="iconfont icon-info"></text>
              </view>
              <text class="item-label">关于我们</text>
            </view>
            <view class="item-right">
              <text class="item-label" style="color: #94A3B8; margin-right: 12rpx;">v1.0.0</text>
              <text class="iconfont icon-arrow-right"></text>
            </view>
          </view>
        </view>
      </view>
      
      <!-- 退出登录 -->
      <view style="padding: 40rpx 0;">
        <button class="btn-danger" @click="handleLogout">
          <text class="iconfont icon-log-out" style="margin-right: 12rpx;"></text>
          退出登录
        </button>
      </view>
      
      <!-- 版本信息 -->
      <view class="version-info">
        <text>往来记 v1.0.0</text>
        <text style="margin-top: 8rpx; display: block;">记录每一份人情往来</text>
      </view>
    </view>
  </view>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'
import contactApi from '@/api/contact'
import transactionApi from '@/api/transaction'

export default {
  data() {
    return {
      phone: '',
      notificationCount: 0,
      totalContacts: 0,
      totalTransactions: 0,
      totalAmount: '0.00'
    }
  },
  
  computed: {
    ...mapGetters('user', ['username', 'avatar'])
  },
  
  onShow() {
    this.loadUserInfo()
    this.loadStatistics()
  },
  
  methods: {
    ...mapActions('user', ['logout', 'getUserInfo']),
    
    async loadUserInfo() {
      try {
        await this.getUserInfo()
        const userInfo = uni.getStorageSync('userInfo')
        if (userInfo) {
          this.phone = userInfo.phone || ''
        }
      } catch (error) {
        console.error('加载用户信息失败:', error)
      }
    },
    
    async loadStatistics() {
      try {
        // 加载联系人数量
        const contactRes = await contactApi.getContactList({ page: 1, size: 1 })
        if (contactRes.code === 1) {
          this.totalContacts = contactRes.data.total
        }
        
        // 加载记账数量和金额
        const transRes = await transactionApi.getTransactionList({ page: 1, size: 1000 })
        if (transRes.code === 1) {
          this.totalTransactions = transRes.data.total
          const total = transRes.data.list.reduce((sum, item) => sum + item.amount, 0)
          this.totalAmount = total.toFixed(2)
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },
    
    maskPhone(phone) {
      if (!phone || phone.length !== 11) return phone
      return phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2')
    },
    
    changeAvatar() {
      uni.chooseImage({
        count: 1,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: (res) => {
          uni.showToast({
            title: '头像上传功能开发中',
            icon: 'none'
          })
        }
      })
    },
    
    goToEditProfile() {
      uni.showToast({
        title: '个人资料编辑功能开发中',
        icon: 'none'
      })
    },
    
    goToChangePassword() {
      uni.showModal({
        title: '修改密码',
        editable: true,
        placeholderText: '请输入新密码',
        success: async (res) => {
          if (res.confirm && res.content) {
            if (res.content.length < 6) {
              uni.showToast({
                title: '密码长度不能少于6位',
                icon: 'none'
              })
              return
            }
            
            try {
              const changeRes = await this.$store.dispatch('user/changePassword', {
                old_password: '123456',
                new_password: res.content
              })
              
              if (changeRes.code === 1) {
                uni.showToast({
                  title: '密码修改成功',
                  icon: 'success'
                })
              }
            } catch (error) {
              console.error('修改密码失败:', error)
            }
          }
        }
      })
    },
    
    goToBudget() {
      uni.navigateTo({
        url: '/pages/budget/budget'
      })
    },
    
    goToNotifications() {
      uni.navigateTo({
        url: '/pages/notifications/notifications'
      })
    },
    
    goToSettings() {
      uni.showToast({
        title: '设置功能开发中',
        icon: 'none'
      })
    },
    
    goToStats() {
      uni.switchTab({
        url: '/pages/stats/stats'
      })
    },
    
    goToDataExport() {
      uni.showToast({
        title: '数据导出功能开发中',
        icon: 'none'
      })
    },
    
    goToDataBackup() {
      uni.showToast({
        title: '数据备份功能开发中',
        icon: 'none'
      })
    },
    
    goToDataClear() {
      uni.showModal({
        title: '确认清空',
        content: '确定要清空所有数据吗？此操作不可恢复！',
        confirmColor: '#EF4444',
        success: (res) => {
          if (res.confirm) {
            uni.showToast({
              title: '数据清空功能开发中',
              icon: 'none'
            })
          }
        }
      })
    },
    
    goToAbout() {
      uni.showModal({
        title: '关于我们',
        content: '往来记 v1.0.0\n\n一款专注于人情记账管理的小程序，帮助您轻松管理人情往来。',
        showCancel: false
      })
    },
    
    goToHelp() {
      uni.showToast({
        title: '帮助与反馈功能开发中',
        icon: 'none'
      })
    },
    
    handleLogout() {
      uni.showModal({
        title: '确认退出',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            this.logout()
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/styles/theme.scss';
@import '@/styles/components.scss';

// 头部操作按钮
.header-actions {
  display: flex;
  justify-content: flex-end;
  gap: $spacing-md;
  padding: 0 $page-padding $spacing-lg;
  
  .action-btn {
    position: relative;
    width: 72rpx;
    height: 72rpx;
    border-radius: $radius-full;
    background: rgba(255, 255, 255, 0.2);
    display: flex;
    align-items: center;
    justify-content: center;
    backdrop-filter: blur(10rpx);
    
    .iconfont {
      font-size: 36rpx;
      color: #FFFFFF;
    }
    
    .badge {
      position: absolute;
      top: -4rpx;
      right: -4rpx;
      min-width: 32rpx;
      height: 32rpx;
      background: #EF4444;
      color: #FFFFFF;
      font-size: 20rpx;
      font-weight: $font-weight-bold;
      border-radius: 16rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 0 8rpx;
    }
  }
}

// 用户资料
.user-profile {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx $page-padding 40rpx;
  
  .avatar-wrapper {
    position: relative;
    width: 160rpx;
    height: 160rpx;
    margin-bottom: $spacing-md;
    
    .user-avatar {
      width: 100%;
      height: 100%;
      border-radius: $radius-full;
      border: 4rpx solid rgba(255, 255, 255, 0.3);
    }
    
    .avatar-badge {
      position: absolute;
      bottom: 0;
      right: 0;
      width: 48rpx;
      height: 48rpx;
      background: #FFFFFF;
      border-radius: $radius-full;
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow: $shadow-sm;
      
      .iconfont {
        font-size: 24rpx;
        color: $text-secondary;
      }
    }
  }
  
  .user-info {
    text-align: center;
  }
  
  .user-name {
    font-size: $font-size-2xl;
    font-weight: $font-weight-bold;
    color: #FFFFFF;
    display: block;
    margin-bottom: 8rpx;
  }
  
  .user-phone {
    font-size: $font-size-base;
    color: rgba(255, 255, 255, 0.8);
  }
}

// 用户统计
.user-stats {
  display: flex;
  align-items: center;
  padding: 0 $page-padding 40rpx;
  
  .stat-item {
    flex: 1;
    text-align: center;
  }
  
  .stat-divider {
    width: 2rpx;
    height: 60rpx;
    background: rgba(255, 255, 255, 0.3);
  }
  
  .stat-value {
    font-size: $font-size-xl;
    font-weight: $font-weight-bold;
    color: #FFFFFF;
    display: block;
    margin-bottom: 8rpx;
  }
  
  .stat-label {
    font-size: $font-size-sm;
    color: rgba(255, 255, 255, 0.8);
  }
}

// 快捷菜单网格
.quick-menu-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: $spacing-md;
  padding: $spacing-md 0;
  
  .quick-menu-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: $spacing-sm;
    
    &:active {
      opacity: 0.7;
    }
    
    .menu-icon {
      width: 100rpx;
      height: 100rpx;
      border-radius: $radius-xl;
      display: flex;
      align-items: center;
      justify-content: center;
      
      &.blue { background: $primary-gradient; }
      &.green { background: $success-gradient; }
      &.orange { background: $warning-gradient; }
      &.purple { background: $purple-gradient; }
      
      .iconfont {
        font-size: 44rpx;
        color: #FFFFFF;
      }
    }
    
    .menu-label {
      font-size: $font-size-sm;
      color: $text-secondary;
    }
  }
}

// 菜单列表
.menu-list {
  .menu-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: $spacing-md 0;
    border-bottom: 1rpx solid $bg-secondary;
    transition: all 0.3s ease;
    
    &:last-child {
      border-bottom: none;
    }
    
    &:active {
      background: $bg-primary;
      margin: 0 -$spacing-md;
      padding-left: $spacing-md;
      padding-right: $spacing-md;
      border-radius: $radius-lg;
    }
    
    .item-left {
      display: flex;
      align-items: center;
      gap: $spacing-sm;
    }
    
    .item-icon {
      width: 48rpx;
      height: 48rpx;
      border-radius: $radius-md;
      display: flex;
      align-items: center;
      justify-content: center;
      
      &.blue { background: #DBEAFE; }
      &.green { background: #D1FAE5; }
      &.orange { background: #FEF3C7; }
      &.purple { background: #E0E7FF; }
      &.red { background: #FEE2E2; }
      
      .iconfont {
        font-size: 28rpx;
        
        .blue & { color: #2563EB; }
        .green & { color: #059669; }
        .orange & { color: #D97706; }
        .purple & { color: #7C3AED; }
        .red & { color: #DC2626; }
      }
    }
    
    .item-label {
      font-size: $font-size-base;
      color: $text-primary;
      font-weight: $font-weight-medium;
    }
    
    .item-right {
      display: flex;
      align-items: center;
      
      .iconfont {
        font-size: 32rpx;
        color: $text-tertiary;
      }
    }
  }
}

// 版本信息
.version-info {
  text-align: center;
  padding: $spacing-xl 0;
  
  text {
    font-size: $font-size-sm;
    color: $text-tertiary;
  }
}
</style>
