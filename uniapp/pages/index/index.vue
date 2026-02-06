<template>
  <view class="index-container">
    <!-- 顶部渐变区域 -->
    <view class="header-section">
      <view class="decoration-circle circle-1"></view>
      <view class="decoration-circle circle-2"></view>
      
      <view class="header-content">
        <view class="user-area" @click="goToProfile">
          <image class="user-avatar" :src="avatar || '/static/default-avatar.png'" mode="aspectFill" @error="avatar = '/static/default-avatar.png'"></image>
          <view class="user-info">
            <text class="user-greeting">您好，</text>
            <text class="user-name">{{ username || '未登录' }}</text>
          </view>
        </view>
        <view class="notification-btn" @click="goToNotifications">
          <text class="iconfont icon-bell"></text>
          <view class="badge" v-if="notificationCount > 0">{{ notificationCount }}</view>
        </view>
      </view>

      <!-- 人情往来文案 -->
        <view class="quotes-section">
          <view class="quote-content">
            <text class="quote-title">往来寄语：</text>
            <view class="quote-item" :key="currentQuoteIndex">
              <text class="quote-text">{{ quotes[currentQuoteIndex] }}</text>
            </view>
          </view>
        </view>
    </view>
    
    <!-- 内容区域 -->
    <scroll-view scroll-y class="content-section">
      <!-- 快捷操作 -->
      <view class="quick-actions">
        <view class="action-item" @click="goToRecord">
          <view class="action-icon blue">
            <text class="iconfont icon-edit"></text>
          </view>
          <text class="action-label">快速记账</text>
        </view>
        <view class="action-item" @click="goToContacts">
          <view class="action-icon green">
            <text class="iconfont icon-users"></text>
          </view>
          <text class="action-label">联系人</text>
        </view>
        <view class="action-item" @click="goToGiftBook">
          <view class="action-icon orange">
            <text class="iconfont icon-file-text"></text>
          </view>
          <text class="action-label">礼簿</text>
        </view>
        <view class="action-item" @click="goToStats">
          <view class="action-icon purple">
            <text class="iconfont icon-bar-chart"></text>
          </view>
          <text class="action-label">往来</text>
        </view>
      </view>
      
      <!-- 最近记账礼簿 -->
      <view class="section-card" v-if="recentGiftBooks.length > 0">
        <view class="section-header">
          <view class="header-left">
            <view class="header-icon orange">
              <text class="iconfont icon-file-text"></text>
            </view>
            <text class="header-title">最近记账礼簿</text>
          </view>
          <text class="header-more" @click="goToGiftBook">查看全部</text>
        </view>
        
        <view class="giftbook-tags">
          <view 
            class="giftbook-tag" 
            v-for="(item, index) in recentGiftBooks" 
            :key="index"
            @click="goToGiftBookDetail(item.id)"
          >
            <text class="tag-text">{{ item.bookName }}</text>
          </view>
        </view>
      </view>
      
      <!-- 最近记账 -->
      <view class="section-card">
        <view class="section-header">
          <view class="header-left">
            <view class="header-icon blue">
              <text class="iconfont icon-file-text"></text>
            </view>
            <text class="header-title">最近记账</text>
          </view>
          <text class="header-more" @click="goToGiftBook">查看全部</text>
        </view>
        
        <view class="transaction-list" v-if="recentTransactions.length > 0">
          <view class="transaction-item" v-for="(item, index) in recentTransactions" :key="index" @click="goToDetail(item.id)">
            <view class="item-icon" :class="getRelationIconClass(item.relationType)">
              <text class="iconfont" :class="getRelationIcon(item.relationType)"></text>
            </view>
            <view class="item-content">
              <view class="item-main">
                <view class="item-top">
                  <text class="item-name">{{ item.contactName }}</text>
                  <text class="item-amount" :class="item.transactionDirection">
                    {{ item.transactionDirection === 'income' ? '+' : '-' }}
                    ¥{{ item.transactionMode === 'cash' ? item.amount : item.giftValue || 0 }}
                  </text>
                </view>
                <view class="item-bottom">
                  <view class="item-bottom-left">
                    <text class="item-type">{{ item.relationType }}</text>
                    <text class="item-date">{{ item.transactionDate }}</text>
                  </view>
                  <view class="item-giftbook" v-if="item.giftBookName">
                    <text class="giftbook-name">{{ item.giftBookName }}</text>
                  </view>
                </view>
              </view>
            </view>
          </view>
        </view>
        
        <view class="empty-state" v-else>
          <view class="empty-icon">
            <text class="iconfont icon-inbox"></text>
          </view>
          <text class="empty-text">暂无记账记录</text>
          <text class="empty-tip">点击"快速记账"开始记录</text>
        </view>
      </view>
      
      <!-- 本月预算 -->
      <view class="section-card">
        <view class="section-header">
          <view class="header-left">
            <view class="header-icon purple">
              <text class="iconfont icon-pie-chart"></text>
            </view>
            <text class="header-title">本月预算</text>
          </view>
          <text class="header-more" @click="goToBudget">设置</text>
        </view>
        
        <view class="budget-content">
          <view class="budget-progress">
            <view class="progress-track">
              <view class="progress-fill" :style="{ width: budgetPercentage + '%' }" :class="budgetStatus"></view>
            </view>
            <view class="progress-info">
              <text class="progress-percent">{{ budgetPercentage }}%</text>
              <text class="progress-status" :class="budgetStatus">{{ budgetStatusText }}</text>
            </view>
          </view>
          <view class="budget-numbers">
            <view class="number-item">
              <text class="number-label">已使用</text>
              <text class="number-value used">¥{{ budgetUsed }}</text>
            </view>
            <view class="number-item">
              <text class="number-label">总预算</text>
              <text class="number-value total">¥{{ budgetTotal }}</text>
            </view>
          </view>
        </view>
      </view>
    </scroll-view>
  </view>
</template>

<script>
import { mapGetters } from 'vuex'
import transactionApi from '@/api/transaction'
import analyticsApi from '@/api/analytics'
import budgetApi from '@/api/budget'
import giftbookApi from '@/api/giftbook'

export default {
  data() {
    return {
      notificationCount: 3,
      recentTransactions: [],
      recentGiftBooks: [],
      budgetUsed: '0.00',
      budgetTotal: '2000.00',
      budgetPercentage: 0,
      currentQuoteIndex: 0,
      quoteTimer: null,
      quotes: [
        '人间烟火里，最暖是人情。',
        '礼轻情意重，记得才长久。',
        '往来皆是情，记录亦是敬。',
        '不欠人情债，不负真心意。',
        '记下每一次奔赴，都是情谊的回声。',
        '礼尚往来，心意有迹可循。',
        '人情不糊涂，往来更从容。',
        '记下的不是钱，是情分与牵挂。',
        '一份礼，一份情，好好记，慢慢还。',
        '人情有来有往，心中不慌不忙。'
      ]
    }
  },
  
  computed: {
    ...mapGetters('user', ['username', 'avatar']),
    
    budgetStatus() {
      if (this.budgetPercentage >= 100) return 'danger'
      if (this.budgetPercentage >= 80) return 'warning'
      return 'success'
    },
    
    budgetStatusText() {
      if (this.budgetPercentage >= 100) return '已超支'
      if (this.budgetPercentage >= 80) return '即将超支'
      return '正常'
    }
  },
  
  onLoad() {
    this.loadData()
    this.startQuoteTimer()
  },
  
  onShow() {
    this.loadData()
    this.startQuoteTimer()
  },
  
  onHide() {
    this.stopQuoteTimer()
  },
  
  onUnload() {
    this.stopQuoteTimer()
  },
  
  methods: {
    async loadData() {
      try {
        await Promise.all([
          this.loadRecentTransactions(),
          this.loadRecentGiftBooks(),
          this.loadBudget()
        ])
      } catch (error) {
        console.error('加载数据失败:', error)
      }
    },
    
    async loadRecentTransactions() {
      try {
        const res = await transactionApi.getRecentTransactions({ limit: 5 })
        
        if (res.code === 1) {
          this.recentTransactions = res.data
        }
      } catch (error) {
        console.error('加载最近记账失败:', error)
      }
    },
    
    async loadRecentGiftBooks() {
      try {
        const res = await giftbookApi.getRecentGiftBooksWithRecords({ limit: 5 })
        
        if (res.code === 1) {
          this.recentGiftBooks = res.data
        }
      } catch (error) {
        console.error('加载最近记账礼簿失败:', error)
      }
    },
    
    async loadBudget() {
      try {
        const now = new Date()
        const res = await budgetApi.getBudget({
          budgetType: 'monthly',
          budgetYear: now.getFullYear(),
          budgetMonth: now.getMonth() + 1
        })
        
        if (res.code === 1 && res.data) {
          const currentAmount = res.data.currentAmount || 0
          const budgetAmount = res.data.budgetAmount || 0
          this.budgetUsed = currentAmount.toFixed(2)
          this.budgetTotal = budgetAmount.toFixed(2)
          this.budgetPercentage = budgetAmount > 0 ? (currentAmount / budgetAmount * 100).toFixed(1) : '0.0'
        }
      } catch (error) {
        console.error('加载预算失败:', error)
      }
    },

    goToRecord() {
      // 先跳转到礼簿选择页面
      uni.navigateTo({
        url: '/pages/giftbook-select/giftbook-select?from=record'
      })
    },
    
    goToContacts() {
      uni.navigateTo({
        url: '/pages/contacts/contacts'
      })
    },
    
    goToGiftBook() {
      uni.switchTab({
        url: '/pages/giftbook/giftbook'
      })
    },

    goToGiftBookDetail(id) {
      uni.navigateTo({
        url: `/pages/giftbook-detail/giftbook-detail?id=${id}`
      })
    },

    goToStats() {
      uni.switchTab({
        url: '/pages/stats/stats'
      })
    },
    
    goToNotifications() {
      uni.navigateTo({
        url: '/pages/notifications/notifications'
      })
    },
    
    goToBudget() {
      uni.navigateTo({
        url: '/pages/budget/budget'
      })
    },
    
    goToProfile() {
      uni.switchTab({
        url: '/pages/profile/profile'
      })
    },
    
    goToDetail(id) {
      uni.navigateTo({
        url: `/pages/transaction-detail/transaction-detail?id=${id}`
      })
    },

    getRelationIcon(relationType) {
      const iconMap = {
        '亲戚': 'icon-heart',
        '朋友': 'icon-users',
        '同事': 'icon-message-square',
        '同学': 'icon-star',
        '其他': 'icon-more'
      }
      return iconMap[relationType] || 'icon-user'
    },

    getRelationIconClass(relationType) {
      const classMap = {
        '亲戚': 'family',
        '朋友': 'friend',
        '同事': 'colleague',
        '同学': 'classmate',
        '其他': 'other'
      }
      return classMap[relationType] || 'default'
    },

    startQuoteTimer() {
      this.stopQuoteTimer()
      this.quoteTimer = setInterval(() => {
        this.currentQuoteIndex = (this.currentQuoteIndex + 1) % this.quotes.length
      }, 5000)
    },

    stopQuoteTimer() {
      if (this.quoteTimer) {
        clearInterval(this.quoteTimer)
        this.quoteTimer = null
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.index-container {
  min-height: 100vh;
  background: #F8FAFC;
}

/* 顶部区域 */
.header-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 60rpx 30rpx 100rpx;
  position: relative;
  overflow: hidden;
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

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 40rpx;
  position: relative;
  z-index: 1;
}

.user-area {
  display: flex;
  align-items: center;
}

.user-avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  border: 4rpx solid rgba(255, 255, 255, 0.3);
  margin-right: 20rpx;
}

.user-info {
  display: flex;
  flex-direction: column;
}

.user-greeting {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.8);
  margin-bottom: 4rpx;
}

.user-name {
  font-size: 36rpx;
  font-weight: bold;
  color: #FFFFFF;
}

.notification-btn {
  position: relative;
  width: 80rpx;
  height: 80rpx;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.iconfont {
  font-size: 40rpx;
  color: #FFFFFF;
}

.badge {
  position: absolute;
  top: 0;
  right: 0;
  background: #EF4444;
  color: #FFFFFF;
  font-size: 20rpx;
  padding: 2rpx 10rpx;
  border-radius: 20rpx;
  min-width: 32rpx;
  text-align: center;
  font-weight: 500;
}

/* 人情往来文案 */
.quotes-section {
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(20rpx);
  border-radius: 24rpx;
  padding: 40rpx 30rpx;
  border: 1rpx solid rgba(255, 255, 255, 0.2);
  position: relative;
  z-index: 1;
  overflow: hidden;
  min-height: 160rpx;
  display: flex;
  align-items: flex-start;
}

.quote-content {
  width: 100%;
  text-align: left;
}

.quote-item {
  animation: fadeIn 0.6s ease-out;
}

.quote-title {
  font-size: 28rpx;
  font-weight: 400;
  color: rgba(255, 255, 255, 0.85);
  margin-bottom: 20rpx;
  text-align: left;
  display: block;
  line-height: 1.4;
}

.quote-text {
  font-size: 32rpx;
  font-weight: 500;
  color: #FFFFFF;
  line-height: 1.8;
  letter-spacing: 2rpx;
  text-align: left;
  display: block;
  padding-left: 30rpx;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10rpx);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 内容区域 */
.content-section {
  padding: 0 30rpx 40rpx;
  margin-top: -60rpx;
  position: relative;
  z-index: 2;
}

/* 快捷操作 */
.quick-actions {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20rpx;
  margin-bottom: 30rpx;
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx 0;
}

.action-icon {
  width: 100rpx;
  height: 100rpx;
  border-radius: 24rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 12rpx;
  box-shadow: 0 8rpx 20rpx rgba(0, 0, 0, 0.1);
}

.action-icon.blue {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.action-icon.green {
  background: linear-gradient(135deg, #10B981 0%, #059669 100%);
}

.action-icon.orange {
  background: linear-gradient(135deg, #F59E0B 0%, #D97706 100%);
}

.action-icon.purple {
  background: linear-gradient(135deg, #8B5CF6 0%, #7C3AED 100%);
}

.action-icon .iconfont {
  font-size: 44rpx;
  color: #FFFFFF;
}

.action-label {
  font-size: 24rpx;
  color: #64748B;
  font-weight: 500;
}

/* 卡片区域 */
.section-card {
  background: #FFFFFF;
  border-radius: 24rpx;
  padding: 30rpx;
  margin-bottom: 24rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24rpx;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.header-icon {
  width: 48rpx;
  height: 48rpx;
  border-radius: 12rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.header-icon.blue {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.header-icon.orange {
  background: linear-gradient(135deg, #F59E0B 0%, #D97706 100%);
}

.header-icon.purple {
  background: linear-gradient(135deg, #8B5CF6 0%, #7C3AED 100%);
}

.header-icon .iconfont {
  font-size: 28rpx;
  color: #FFFFFF;
}

.header-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #1E293B;
}

.header-more {
  font-size: 26rpx;
  color: #667eea;
  font-weight: 500;
}

/* 最近记账礼簿标签 */
.giftbook-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.giftbook-tag {
  padding: 16rpx 24rpx;
  background: #FEF3C7;
  border-radius: 32rpx;
  transition: all 0.3s ease;
}

.giftbook-tag:active {
  background: #FDE68A;
  transform: scale(0.95);
}

.giftbook-tag .tag-text {
  font-size: 26rpx;
  color: #92400E;
  font-weight: 500;
}

/* 交易列表 */
.transaction-list {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}

.transaction-item {
  display: flex;
  align-items: center;
  padding: 20rpx;
  background: #F8FAFC;
  border-radius: 16rpx;
  transition: all 0.3s ease;
}

.transaction-item:active {
  background: #F1F5F9;
}

.item-icon {
  width: 72rpx;
  height: 72rpx;
  border-radius: 16rpx;
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

.item-icon.festival {
  background: #D1FAE5;
}

.item-icon.house {
  background: #FCE7F3;
}

.item-icon.dinner {
  background: #E0E7FF;
}

.item-icon.activity {
  background: #FEE2E2;
}

.item-icon.default {
  background: #F1F5F9;
}

/* 关系类型图标背景色 */
.item-icon.family {
  background: linear-gradient(135deg, #FEF3C7 0%, #FDE68A 100%);
}

.item-icon.family .iconfont {
  color: #D97706;
}

.item-icon.friend {
  background: linear-gradient(135deg, #DBEAFE 0%, #93C5FD 100%);
}

.item-icon.friend .iconfont {
  color: #2563EB;
}

.item-icon.colleague {
  background: linear-gradient(135deg, #D1FAE5 0%, #6EE7B7 100%);
}

.item-icon.colleague .iconfont {
  color: #059669;
}

.item-icon.classmate {
  background: linear-gradient(135deg, #FCE7F3 0%, #F9A8D4 100%);
}

.item-icon.classmate .iconfont {
  color: #DB2777;
}

.item-icon.other {
  background: linear-gradient(135deg, #E0E7FF 0%, #A5B4FC 100%);
}

.item-icon.other .iconfont {
  color: #4F46E5;
}

.item-icon .iconfont {
  font-size: 36rpx;
  color: #1E293B;
}

.item-content {
  flex: 1;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  gap: 16rpx;
}

.item-main {
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
  font-size: 32rpx;
  font-weight: bold;
  color: #1E293B;
  
  &.income {
    color: #EF4444;
  }

  &.expense {
    color: #10B981;
  }
}

.item-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16rpx;
}

.item-bottom-left {
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

/* 礼簿名称样式 */
.item-giftbook {
  display: flex;
  align-items: center;
  flex-shrink: 0;
  background: linear-gradient(135deg, #F0F9FF 0%, #E0F2FE 100%);
  border-radius: 20rpx;
  padding: 6rpx 14rpx;
  border: 1rpx solid #BAE6FD;
}

.giftbook-name {
  font-size: 20rpx;
  color: #0369A1;
  font-weight: 600;
  max-width: 160rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 预算区域 */
.budget-content {
  display: flex;
  flex-direction: column;
  gap: 24rpx;
}

.budget-progress {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}

.progress-track {
  width: 100%;
  height: 12rpx;
  background: #E2E8F0;
  border-radius: 6rpx;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  border-radius: 6rpx;
  transition: width 0.3s ease;
}

.progress-fill.success {
  background: linear-gradient(90deg, #10B981 0%, #059669 100%);
}

.progress-fill.warning {
  background: linear-gradient(90deg, #F59E0B 0%, #D97706 100%);
}

.progress-fill.danger {
  background: linear-gradient(90deg, #EF4444 0%, #DC2626 100%);
}

.progress-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.progress-percent {
  font-size: 28rpx;
  font-weight: bold;
  color: #1E293B;
}

.progress-status {
  font-size: 24rpx;
  font-weight: 500;
  padding: 4rpx 16rpx;
  border-radius: 20rpx;
}

.progress-status.success {
  background: #D1FAE5;
  color: #059669;
}

.progress-status.warning {
  background: #FEF3C7;
  color: #D97706;
}

.progress-status.danger {
  background: #FEE2E2;
  color: #DC2626;
}

.budget-numbers {
  display: flex;
  justify-content: space-between;
  padding-top: 20rpx;
  border-top: 2rpx solid #F1F5F9;
}

.number-item {
  display: flex;
  flex-direction: column;
  gap: 8rpx;
}

.number-label {
  font-size: 24rpx;
  color: #64748B;
}

.number-value {
  font-size: 32rpx;
  font-weight: bold;
}

.number-value.used {
  color: #1E293B;
}

.number-value.total {
  color: #64748B;
}

/* 空状态 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 60rpx 0;
}

.empty-icon {
  width: 120rpx;
  height: 120rpx;
  background: #F1F5F9;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20rpx;
}

.empty-icon .iconfont {
  font-size: 60rpx;
  color: #94A3B8;
}

.empty-text {
  font-size: 28rpx;
  color: #64748B;
  margin-bottom: 8rpx;
}

.empty-tip {
  font-size: 24rpx;
  color: #94A3B8;
}
</style>
