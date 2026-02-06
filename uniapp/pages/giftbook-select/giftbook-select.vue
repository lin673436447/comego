<template>
  <view class="page-container">
    <!-- 顶部渐变头部 -->
    <view class="header-gradient">
      <!-- 导航栏 -->
      <view class="navbar">
        <view class="nav-back" @click="goBack">
          <text class="iconfont icon-arrow-left"></text>
        </view>
        <text class="nav-title">选择礼簿</text>
        <view class="nav-right"></view>
      </view>
    </view>
    
    <!-- 内容区域 -->
    <view class="content-section">
      <!-- 礼簿列表 -->
      <view class="card" v-if="giftBooks.length > 0">
        <view class="giftbook-list">
          <view 
            class="giftbook-item" 
            v-for="(item, index) in giftBooks" 
            :key="index"
            @click="selectGiftBook(item)"
          >
            <view class="item-icon" :class="getIconClass(item.bookType)">
              <text class="iconfont">{{ getIcon(item.bookType) }}</text>
            </view>
            <view class="item-content">
              <view class="item-top">
                <text class="item-name">{{ item.bookName }}</text>
                <text class="item-amount" :class="(item.totalAmount || 0) >= 0 ? 'income' : 'expense'">
                  ¥{{ (item.totalAmount || 0).toFixed(2) }}
                </text>
              </view>
              <view class="item-bottom">
                <text class="item-type">{{ getBookTypeName(item.bookType) }}</text>
                <text class="item-date">{{ item.bookDate }}</text>
              </view>
            </view>
            <view class="item-arrow">
              <text class="iconfont icon-arrow-right"></text>
            </view>
          </view>
        </view>
      </view>
      
      <!-- 空状态 -->
      <view class="empty-state" v-else>
        <view class="empty-icon">
          <text class="iconfont icon-book"></text>
        </view>
        <text class="empty-text">暂无礼簿</text>
        <text class="empty-tip">请先创建礼簿</text>
        <button class="btn-add-giftbook" @click="goToAddGiftBook">
          <text class="iconfont icon-plus" style="margin-right: 8rpx;"></text>
          创建礼簿
        </button>
      </view>
      
      <!-- 添加礼簿按钮（固定在底部） -->
      <view class="add-btn-container" v-if="giftBooks.length > 0">
        <button class="btn-add-giftbook-float" @click="goToAddGiftBook">
          <text class="iconfont icon-plus" style="margin-right: 8rpx;"></text>
          创建新礼簿
        </button>
      </view>
    </view>
  </view>
</template>

<script>
import giftbookApi from '@/api/giftbook'

export default {
  data() {
    return {
      giftBooks: [],
      from: '' // 来源页面：record-快速记账
    }
  },
  
  onLoad(options) {
    this.from = options.from || ''
    this.loadGiftBooks()
  },

  onShow() {
    // 每次页面显示时刷新礼簿列表
    this.loadGiftBooks()
  },

  methods: {
    async loadGiftBooks() {
      try {
        const res = await giftbookApi.getActiveGiftBooks()
        if (res.code === 1) {
          this.giftBooks = res.data || []
        }
      } catch (error) {
        console.error('加载礼簿失败:', error)
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        })
      }
    },
    
    selectGiftBook(giftBook) {
      // 将礼簿ID存储到本地存储
      uni.setStorageSync('selectedGiftBookId', giftBook.id)
      uni.setStorageSync('selectedGiftBook', giftBook)
      // 跳转到记账页面
      uni.navigateTo({
        url: '/pages/record/record?from=giftbook-select'
      })
    },
    
    goBack() {
      // 后退到首页
      uni.switchTab({
        url: '/pages/index/index'
      })
    },
    
    goToAddGiftBook() {
      uni.navigateTo({
        url: '/pages/giftbook-add/giftbook-add'
      })
    },
    
    getIcon(type) {
      const iconMap = {
        '婚礼': '💒',
        '生日': '🎂',
        '葬礼': '🕯️',
        '其他': '📖'
      }
      return iconMap[type] || '📖'
    },

    getIconClass(type) {
      const classMap = {
        '婚礼': 'wedding',
        '生日': 'birthday',
        '葬礼': 'funeral',
        '其他': 'other'
      }
      return classMap[type] || 'other'
    },

    getBookTypeName(type) {
      const typeMap = {
        '婚礼': '婚礼',
        '生日': '生日',
        '葬礼': '葬礼',
        '其他': '其他'
      }
      return typeMap[type] || type || '其他'
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/styles/theme.scss';
@import '@/styles/components.scss';

.page-container {
  background: #F8FAFC;
}

/* 内容区域 */
.content-section {
  padding: 0 30rpx 40rpx;
  margin-top: -60rpx;
  position: relative;
  z-index: 2;
}

/* 卡片 */
.card {
  background: #FFFFFF;
  border-radius: 24rpx;
  padding: 30rpx;
  margin-bottom: 24rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
}

/* 礼簿列表 */
.giftbook-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.giftbook-item {
  display: flex;
  align-items: center;
  padding: 24rpx;
  background: #F8FAFC;
  border-radius: 16rpx;
  transition: all 0.3s ease;
}

.giftbook-item:active {
  background: #F1F5F9;
}

.item-icon {
  width: 80rpx;
  height: 80rpx;
  border-radius: 20rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
  flex-shrink: 0;
}

.item-icon.wedding {
  background: #FEF3C7;
}

.item-icon.birthday {
  background: #DBEAFE;
}

.item-icon.funeral {
  background: #E5E7EB;
}

.item-icon.other {
  background: #F3E8FF;
}

.item-icon .iconfont {
  font-size: 40rpx;
}

.item-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8rpx;
}

.item-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.item-name {
  font-size: 30rpx;
  font-weight: 500;
  color: #1E293B;
}

.item-amount {
  font-size: 28rpx;
  font-weight: bold;
  color: #1E293B;
}

.item-amount.income {
  color: #EF4444;
}

.item-amount.expense {
  color: #10B981;
}

.item-bottom {
  display: flex;
  gap: 16rpx;
}

.item-type {
  font-size: 24rpx;
  color: #64748B;
}

.item-date {
  font-size: 24rpx;
  color: #94A3B8;
}

.item-arrow {
  margin-left: 16rpx;
}

.item-arrow .iconfont {
  font-size: 32rpx;
  color: #94A3B8;
}

/* 空状态 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 100rpx 0;
}

.empty-icon {
  width: 140rpx;
  height: 140rpx;
  background: #F1F5F9;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 24rpx;
}

.empty-icon .iconfont {
  font-size: 70rpx;
  color: #94A3B8;
}

.empty-text {
  font-size: 32rpx;
  color: #64748B;
  margin-bottom: 12rpx;
}

.empty-tip {
  font-size: 26rpx;
  color: #94A3B8;
  margin-bottom: 40rpx;
}

/* 创建礼簿按钮 */
.btn-add-giftbook {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #FFFFFF;
  border: none;
  border-radius: 40rpx;
  padding: 24rpx 48rpx;
  font-size: 28rpx;
  font-weight: 500;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 20rpx;
}

.btn-add-giftbook::after {
  border: none;
}

/* 固定底部按钮容器 */
.add-btn-container {
  position: fixed;
  bottom: 40rpx;
  left: 30rpx;
  right: 30rpx;
  z-index: 100;
}

/* 浮动创建按钮 */
.btn-add-giftbook-float {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #FFFFFF;
  border: none;
  border-radius: 48rpx;
  padding: 28rpx 0;
  font-size: 30rpx;
  font-weight: 500;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8rpx 24rpx rgba(102, 126, 234, 0.4);
}

.btn-add-giftbook-float::after {
  border: none;
}
</style>
