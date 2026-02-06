<template>
  <view class="page-container">
    <!-- 顶部渐变头部 -->
    <view class="header-gradient">
      <view class="decoration-circle circle-1"></view>
      <view class="decoration-circle circle-2"></view>
      <view class="decoration-circle circle-3"></view>
      
      <!-- 导航栏 -->
      <view class="navbar">
        <view class="nav-back" @click="goBack">
          <text class="iconfont icon-arrow-left"></text>
        </view>
        <text class="nav-title">通知提醒</text>
        <view class="nav-right" @click="goToSettings">
          <text class="iconfont icon-settings"></text>
        </view>
      </view>
      
      <!-- 通知统计 -->
      <view class="notification-overview">
        <view class="overview-item">
          <text class="overview-value">{{ notifications.length }}</text>
          <text class="overview-label">全部提醒</text>
        </view>
        <view class="overview-divider"></view>
        <view class="overview-item">
          <text class="overview-value">{{ unreadCount }}</text>
          <text class="overview-label">待处理</text>
        </view>
        <view class="overview-divider"></view>
        <view class="overview-item">
          <text class="overview-value">{{ todayCount }}</text>
          <text class="overview-label">今日</text>
        </view>
      </view>
    </view>
    
    <!-- 内容区域 -->
    <view class="content-section">
      <!-- 标签切换 -->
      <view class="tab-bar">
        <view 
          class="tab-item" 
          :class="{ active: activeTab === 'all' }"
          @click="switchTab('all')"
        >
          <text class="tab-text">全部</text>
          <view class="tab-badge" v-if="notifications.length > 0">{{ notifications.length }}</view>
        </view>
        <view 
          class="tab-item" 
          :class="{ active: activeTab === 'unread' }"
          @click="switchTab('unread')"
        >
          <text class="tab-text">待处理</text>
          <view class="tab-badge red" v-if="unreadCount > 0">{{ unreadCount }}</view>
        </view>
        <view 
          class="tab-item" 
          :class="{ active: activeTab === 'today' }"
          @click="switchTab('today')"
        >
          <text class="tab-text">今日</text>
          <view class="tab-badge orange" v-if="todayCount > 0">{{ todayCount }}</view>
        </view>
      </view>
      
      <!-- 通知列表 -->
      <scroll-view scroll-y class="notification-list" @scrolltolower="loadMore">
        <view 
          class="notification-card" 
          v-for="(item, index) in filteredNotifications" 
          :key="index"
          :class="{ unread: item.isNotified === 0 }"
        >
          <view class="notification-header">
            <view class="header-left">
              <view class="type-icon" :class="getIconClass(item.reminderType)">
                <text class="iconfont">{{ getIcon(item.reminderType) }}</text>
              </view>
              <view class="notification-info">
                <view class="info-row">
                  <text class="notification-title">{{ item.reminderTitle }}</text>
                  <view class="status-dot" v-if="item.isNotified === 0"></view>
                </view>
                <text class="notification-time">
                  <text class="iconfont icon-clock" style="font-size: 24rpx; margin-right: 8rpx;"></text>
                  {{ item.reminderDate }} {{ item.reminderTime }}
                </text>
              </view>
            </view>
            <view class="notification-status" :class="{ notified: item.isNotified === 1 }">
              <text class="iconfont" :class="item.isNotified === 1 ? 'icon-check-circle' : 'icon-clock'"></text>
              <text style="margin-left: 6rpx;">{{ item.isNotified === 1 ? '已提醒' : '待提醒' }}</text>
            </view>
          </view>
          
          <view class="notification-content" v-if="item.reminderContent">
            <text class="content-text">{{ item.reminderContent }}</text>
          </view>
          
          <view class="notification-footer">
            <view class="contact-tag">
              <text class="iconfont icon-user" style="font-size: 24rpx; margin-right: 8rpx;"></text>
              <text>{{ item.contactName }}</text>
            </view>
            <view class="notification-actions">
              <view class="action-btn" :class="{ primary: item.isNotified === 0 }" @click="markAsRead(item)" v-if="item.isNotified === 0">
                <text class="iconfont icon-check" style="margin-right: 6rpx;"></text>
                <text>标记已读</text>
              </view>
              <view class="action-btn danger" @click="handleDelete(item)">
                <text class="iconfont icon-trash-2" style="margin-right: 6rpx;"></text>
                <text>删除</text>
              </view>
            </view>
          </view>
        </view>
        
        <!-- 空状态 -->
        <view class="empty-state" v-if="filteredNotifications.length === 0 && !loading">
          <view class="empty-icon">
            <text class="iconfont icon-bell-off"></text>
          </view>
          <text class="empty-text">暂无通知提醒</text>
          <text class="empty-tip">点击右下角添加提醒</text>
        </view>
        
        <!-- 加载状态 -->
        <view class="loading-more" v-if="loading">
          <view class="loading-spinner-dark"></view>
          <text class="loading-text">加载中...</text>
        </view>
        
        <!-- 没有更多 -->
        <view class="no-more" v-if="!hasMore && filteredNotifications.length > 0">
          <text class="no-more-text">已经到底啦</text>
        </view>
      </scroll-view>
    </view>
    
    <!-- 悬浮添加按钮 -->
    <view class="fab-btn" @click="goToAdd">
      <text class="iconfont icon-plus"></text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      activeTab: 'all',
      notifications: [],
      loading: false,
      hasMore: true,
      page: 1,
      pageSize: 10
    }
  },
  
  computed: {
    unreadCount() {
      return this.notifications.filter(item => item.isNotified === 0).length
    },
    
    todayCount() {
      const today = new Date().toISOString().split('T')[0]
      return this.notifications.filter(item => item.reminderDate === today).length
    },
    
    filteredNotifications() {
      if (this.activeTab === 'all') {
        return this.notifications
      } else if (this.activeTab === 'unread') {
        return this.notifications.filter(item => item.isNotified === 0)
      } else if (this.activeTab === 'today') {
        const today = new Date().toISOString().split('T')[0]
        return this.notifications.filter(item => item.reminderDate === today)
      }
      return this.notifications
    }
  },
  
  onLoad() {
    this.loadNotifications()
  },
  
  methods: {
    goBack() {
      const pages = getCurrentPages()
      if (pages.length > 1) {
        uni.navigateBack()
      } else {
        uni.switchTab({
          url: '/pages/index/index'
        })
      }
    },
    
    goToSettings() {
      uni.showToast({
        title: '设置功能开发中',
        icon: 'none'
      })
    },
    
    switchTab(tab) {
      this.activeTab = tab
    },
    
    async loadNotifications() {
      this.loading = true
      
      try {
        uni.showToast({
          title: '加载通知功能开发中',
          icon: 'none'
        })
        
        this.notifications = []
      } catch (error) {
        console.error('加载通知失败:', error)
      } finally {
        this.loading = false
      }
    },
    
    loadMore() {
      if (!this.hasMore || this.loading) return
      this.page++
      this.loadNotifications()
    },
    
    getIcon(type) {
      const iconMap = {
        'birthday': '\ue6b8',
        'festival': '\ue6b9',
        'anniversary': '\ue6ba',
        'other': '\ue6bb'
      }
      return iconMap[type] || '\ue6bb'
    },
    
    getIconClass(type) {
      const classMap = {
        'birthday': 'birthday',
        'festival': 'festival',
        'anniversary': 'anniversary',
        'other': 'other'
      }
      return classMap[type] || 'other'
    },
    
    markAsRead(item) {
      uni.showToast({
        title: '标记功能开发中',
        icon: 'none'
      })
    },
    
    handleDelete(item) {
      uni.showModal({
        title: '确认删除',
        content: '确定要删除这条提醒吗？',
        confirmColor: '#EF4444',
        success: (res) => {
          if (res.confirm) {
            uni.showToast({
              title: '删除功能开发中',
              icon: 'none'
            })
          }
        }
      })
    },
    
    goToAdd() {
      uni.showToast({
        title: '添加提醒功能开发中',
        icon: 'none'
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/styles/theme.scss';
@import '@/styles/components.scss';

// 通知概览
.notification-overview {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 30rpx $page-padding 40rpx;
  
  .overview-item {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    
    .overview-value {
      font-size: $font-size-2xl;
      font-weight: $font-weight-bold;
      color: #FFFFFF;
      margin-bottom: 8rpx;
    }
    
    .overview-label {
      font-size: $font-size-sm;
      color: rgba(255, 255, 255, 0.8);
    }
  }
  
  .overview-divider {
    width: 2rpx;
    height: 60rpx;
    background: rgba(255, 255, 255, 0.3);
  }
}

// 标签栏
.tab-bar {
  display: flex;
  background: #FFFFFF;
  border-radius: $radius-xl;
  padding: 8rpx;
  margin-bottom: $spacing-lg;
  box-shadow: $shadow-sm;
  
  .tab-item {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 24rpx 0;
    font-size: $font-size-sm;
    color: $text-tertiary;
    border-radius: $radius-lg;
    transition: all 0.3s ease;
    position: relative;
    
    &.active {
      background: $primary-gradient;
      color: #FFFFFF;
      font-weight: $font-weight-medium;
      box-shadow: 0 4rpx 12rpx rgba(102, 126, 234, 0.4);
    }
    
    .tab-badge {
      position: absolute;
      top: 8rpx;
      right: 8rpx;
      min-width: 32rpx;
      height: 32rpx;
      background: $primary-color;
      color: #FFFFFF;
      font-size: 20rpx;
      font-weight: $font-weight-bold;
      border-radius: $radius-full;
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 0 8rpx;
      
      &.red {
        background: $danger-color;
      }
      
      &.orange {
        background: $warning-color;
      }
    }
  }
}

// 通知列表
.notification-list {
  height: calc(100vh - 400rpx);
  
  .notification-card {
    background: #FFFFFF;
    border-radius: $radius-xl;
    padding: $spacing-lg;
    margin-bottom: $spacing-md;
    box-shadow: $shadow-sm;
    border-left: 6rpx solid transparent;
    transition: all 0.3s ease;
    
    &.unread {
      border-left-color: $primary-color;
      background: linear-gradient(135deg, #FFFFFF 0%, rgba(102, 126, 234, 0.02) 100%);
    }
    
    .notification-header {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      margin-bottom: $spacing-md;
      
      .header-left {
        flex: 1;
        display: flex;
        align-items: flex-start;
        gap: $spacing-sm;
        
        .type-icon {
          width: 80rpx;
          height: 80rpx;
          border-radius: $radius-lg;
          display: flex;
          align-items: center;
          justify-content: center;
          flex-shrink: 0;
          
          .iconfont {
            font-size: 40rpx;
          }
          
          &.birthday {
            background: linear-gradient(135deg, #FEF3C7 0%, #FDE68A 100%);
            
            .iconfont {
              color: #D97706;
            }
          }
          
          &.festival {
            background: linear-gradient(135deg, #D1FAE5 0%, #A7F3D0 100%);
            
            .iconfont {
              color: #059669;
            }
          }
          
          &.anniversary {
            background: linear-gradient(135deg, #FCE7F3 0%, #FBCFE8 100%);
            
            .iconfont {
              color: #DB2777;
            }
          }
          
          &.other {
            background: linear-gradient(135deg, #E0E7FF 0%, #C7D2FE 100%);
            
            .iconfont {
              color: $primary-color;
            }
          }
        }
        
        .notification-info {
          flex: 1;
          
          .info-row {
            display: flex;
            align-items: center;
            gap: $spacing-xs;
            margin-bottom: 8rpx;
            
            .notification-title {
              font-size: $font-size-base;
              font-weight: $font-weight-bold;
              color: $text-primary;
            }
            
            .status-dot {
              width: 16rpx;
              height: 16rpx;
              background: $danger-color;
              border-radius: $radius-full;
              animation: pulse 2s infinite;
            }
          }
          
          .notification-time {
            font-size: $font-size-xs;
            color: $text-tertiary;
            display: flex;
            align-items: center;
          }
        }
      }
      
      .notification-status {
        display: flex;
        align-items: center;
        font-size: $font-size-xs;
        padding: 8rpx 16rpx;
        border-radius: $radius-full;
        background: $bg-secondary;
        color: $text-tertiary;
        
        &.notified {
          background: rgba(16, 185, 129, 0.1);
          color: $success-color;
        }
        
        .iconfont {
          font-size: 24rpx;
        }
      }
    }
    
    .notification-content {
      padding: $spacing-md 0;
      border-top: 2rpx solid $bg-secondary;
      border-bottom: 2rpx solid $bg-secondary;
      margin-bottom: $spacing-md;
      
      .content-text {
        font-size: $font-size-sm;
        color: $text-secondary;
        line-height: 1.6;
      }
    }
    
    .notification-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;
      
      .contact-tag {
        display: flex;
        align-items: center;
        font-size: $font-size-sm;
        color: $text-secondary;
        background: $bg-secondary;
        padding: 8rpx 16rpx;
        border-radius: $radius-full;
      }
      
      .notification-actions {
        display: flex;
        gap: $spacing-sm;
        
        .action-btn {
          display: flex;
          align-items: center;
          font-size: $font-size-xs;
          padding: 12rpx 20rpx;
          border-radius: $radius-md;
          background: $bg-secondary;
          color: $text-secondary;
          transition: all 0.3s ease;
          
          &.primary {
            background: rgba(102, 126, 234, 0.1);
            color: $primary-color;
            
            &:active {
              background: rgba(102, 126, 234, 0.2);
            }
          }
          
          &.danger {
            background: rgba(239, 68, 68, 0.1);
            color: $danger-color;
            
            &:active {
              background: rgba(239, 68, 68, 0.2);
            }
          }
          
          .iconfont {
            font-size: 24rpx;
          }
        }
      }
    }
  }
}

// 脉冲动画
@keyframes pulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
}

// 悬浮按钮
.fab-btn {
  position: fixed;
  bottom: 120rpx;
  right: 30rpx;
  width: 112rpx;
  height: 112rpx;
  border-radius: $radius-full;
  background: $primary-gradient;
  box-shadow: 0 8rpx 24rpx rgba(102, 126, 234, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 100;
  transition: all 0.3s ease;
  
  &:active {
    transform: scale(0.95);
    box-shadow: 0 4rpx 12rpx rgba(102, 126, 234, 0.3);
  }
  
  .iconfont {
    font-size: 48rpx;
    color: #FFFFFF;
  }
}

// 没有更多
.no-more {
  display: flex;
  justify-content: center;
  padding: 40rpx 0;
  
  .no-more-text {
    font-size: $font-size-sm;
    color: $text-tertiary;
  }
}
</style>
