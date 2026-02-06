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
        <text class="nav-title">统计分析</text>
        <view class="nav-right">
          <picker mode="date" :value="selectedDate" fields="month" @change="onDateChange">
            <view class="date-picker">
              <text class="date-text">{{ selectedDate }}</text>
              <text class="iconfont icon-calendar" style="font-size: 24rpx; margin-left: 8rpx;"></text>
            </view>
          </picker>
        </view>
      </view>
      
      <!-- 月度支出统计 -->
      <view class="expense-overview">
        <view class="overview-item">
          <text class="overview-label">本月支出</text>
          <text class="overview-value">¥{{ currentMonthExpense }}</text>
        </view>
        <view class="overview-divider"></view>
        <view class="overview-item">
          <text class="overview-label">较上月</text>
          <view class="overview-compare" :class="{ up: growthRate > 0, down: growthRate < 0 }">
            <text class="iconfont" :class="growthRate > 0 ? 'icon-trend-up' : 'icon-trend-down'"></text>
            <text>{{ Math.abs(growthRate) }}%</text>
          </view>
        </view>
        <view class="overview-divider"></view>
        <view class="overview-item">
          <text class="overview-label">本年累计</text>
          <text class="overview-value small">¥{{ yearlyTotal }}</text>
        </view>
      </view>
    </view>
    
    <!-- 内容区域 -->
    <view class="content-section">
      <!-- 账目次数排行 -->
      <view class="card">
        <view class="card-header">
          <view class="header-left">
            <view class="icon-box orange icon-box-sm">
              <text class="iconfont icon-file-text"></text>
            </view>
            <text class="header-title">往来热度</text>
          </view>
        </view>
        
        <view class="heat-list">
          <view class="heat-item" v-for="(item, index) in transactionCountRank.slice(0, 3)" :key="index" @click="goToContactTransactions(item.contactId, item.contactName)">
            <view class="heat-rank" :class="{ top: index < 3 }">{{ index + 1 }}</view>
            <text class="heat-name">{{ item.contactName }}</text>
            <view class="heat-bar">
              <view class="heat-fill" :style="{ width: item.percentage + '%' }"></view>
            </view>
            <text class="heat-value">{{ item.transactionCount }}次</text>
          </view>
        </view>
      </view>
      
      <!-- 联系人往来明细 -->
      <view class="card">
        <view class="card-header">
          <view class="header-left">
            <view class="icon-box blue icon-box-sm">
              <text class="iconfont icon-users"></text>
            </view>
            <text class="header-title">联系人往来明细</text>
          </view>
        </view>
        
        <view class="contact-transaction-list" v-if="contactTransactions.length > 0">
          <view class="contact-transaction-item" v-for="(item, index) in contactTransactions" :key="index" @click="goToContactTransactions(item.contactId, item.contactName)">
            <view class="contact-info-row">
              <image class="contact-avatar" :src="item.avatarUrl || '/static/default-avatar.png'" mode="aspectFill" @error="item.avatarUrl = '/static/default-avatar.png'"></image>
              <view class="contact-detail">
                <view class="contact-name-row">
                  <text class="contact-name">{{ item.contactName }}</text>
                  <view class="relation-tag" :class="getRelationClass(item.relationType)">{{ item.relationType }}</view>
                </view>
                <view class="transaction-stats">
                  <text class="stat-item income">收 ¥{{ item.totalIncome }}</text>
                  <text class="stat-item expense">送 ¥{{ item.totalExpense }}</text>
                </view>
              </view>
            </view>
            <view class="balance-info" :class="{ 'balance-positive': item.balance > 0, 'balance-negative': item.balance < 0, 'balance-zero': item.balance === 0 }">
              <text class="balance-label">{{ item.balance > 0 ? '盈余' : item.balance < 0 ? '亏损' : '平衡' }}</text>
              <text class="balance-value">¥{{ Math.abs(item.balance) }}</text>
            </view>
          </view>
        </view>
        <view class="empty-state" v-else>
          <view class="empty-icon">
            <text class="iconfont icon-users"></text>
          </view>
          <text class="empty-text">暂无往来记录</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import analyticsApi from '@/api/analytics'

export default {
  data() {
    return {
      selectedDate: '',
      startDate: '',
      endDate: '',
      currentMonthExpense: '0.00',
      lastMonthExpense: '0.00',
      growthRate: 0,
      yearlyTotal: '0.00',
      transactionCountRank: [],
      contactTransactions: []
    }
  },

  computed: {
  },

  onLoad() {
    this.initDates()
    this.loadExpenseStats()
    this.loadRelationData()
    this.loadContactTransactions()
  },
  
  methods: {
    initDates() {
      const now = new Date()
      const year = now.getFullYear()
      const month = String(now.getMonth() + 1).padStart(2, '0')
      this.selectedDate = `${year}-${month}`
      
      const firstDay = new Date(year, now.getMonth(), 1)
      const lastDay = new Date(year, now.getMonth() + 1, 0)
      this.startDate = `${year}-${String(firstDay.getMonth() + 1).padStart(2, '0')}-${String(firstDay.getDate()).padStart(2, '0')}`
      this.endDate = `${year}-${String(lastDay.getMonth() + 1).padStart(2, '0')}-${String(lastDay.getDate()).padStart(2, '0')}`
    },
    
    onDateChange(e) {
      this.selectedDate = e.detail.value
      const [year, month] = this.selectedDate.split('-')
      this.loadExpenseStats(parseInt(year), parseInt(month))
    },
    
    async loadExpenseStats(year, month) {
      try {
        const now = new Date()
        const params = {
          year: year || now.getFullYear(),
          month: month || now.getMonth() + 1
        }
        
        const res = await analyticsApi.getExpenseStatistics(params)
        
        if (res.code === 1) {
          this.currentMonthExpense = res.data.currentMonth ? res.data.currentMonth.toFixed(2) : '0.00'
          this.lastMonthExpense = res.data.lastMonth ? res.data.lastMonth.toFixed(2) : '0.00'
          this.growthRate = res.data.growthRate ? res.data.growthRate.toFixed(1) : '0.0'
          this.yearlyTotal = res.data.yearlyTotal ? res.data.yearlyTotal.toFixed(2) : '0.00'
        }
      } catch (error) {
        console.error('加载支出统计失败:', error)
      }
    },
    
    async loadRelationData() {
      try {
        const res = await analyticsApi.getRelationData({ limit: 10 })

        if (res.code === 1) {
          this.transactionCountRank = res.data.transactionCountRank || []
        }
      } catch (error) {
        console.error('加载关系数据失败:', error)
      }
    },
    
    async loadContactTransactions() {
      try {
        const res = await analyticsApi.getContactTransactions({
          startDate: this.startDate,
          endDate: this.endDate
        })
        
        if (res.code === 1) {
          this.contactTransactions = res.data || []
        }
      } catch (error) {
        console.error('加载联系人往来明细失败:', error)
      }
    },
    
    getRelationClass(name) {
      const map = {
        '亲戚': 'family',
        '朋友': 'friend',
        '同事': 'colleague',
        '同学': 'classmate',
        '其他': 'other'
      }
      return map[name] || 'default'
    },
    
    goToContactTransactions(contactId, contactName) {
      uni.navigateTo({
        url: `/pages/contact-transactions/contact-transactions?contactId=${contactId}&contactName=${encodeURIComponent(contactName || '')}`
      })
    },
    
    goBack() {
      uni.switchTab({
        url: '/pages/index/index'
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/styles/theme.scss';
@import '@/styles/components.scss';

// 日期选择器
.date-picker {
  display: flex;
  align-items: center;
  background: rgba(255, 255, 255, 0.2);
  padding: 12rpx 24rpx;
  border-radius: $radius-lg;
  
  .date-text {
    font-size: $font-size-base;
    color: #FFFFFF;
    font-weight: $font-weight-medium;
  }
}

// 支出概览
.expense-overview {
  display: flex;
  align-items: center;
  padding: 40rpx $page-padding 60rpx;
  
  .overview-item {
    flex: 1;
    text-align: center;
  }
  
  .overview-divider {
    width: 2rpx;
    height: 80rpx;
    background: rgba(255, 255, 255, 0.3);
  }
  
  .overview-label {
    font-size: $font-size-sm;
    color: rgba(255, 255, 255, 0.8);
    display: block;
    margin-bottom: 12rpx;
  }
  
  .overview-value {
    font-size: 44rpx;
    font-weight: $font-weight-bold;
    color: #FFFFFF;
    display: block;
    
    &.small {
      font-size: 36rpx;
    }
  }
  
  .overview-compare {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8rpx;
    font-size: $font-size-lg;
    font-weight: $font-weight-bold;
    
    &.up {
      color: #FECACA;
    }
    
    &.down {
      color: #A7F3D0;
    }
    
    .iconfont {
      font-size: 32rpx;
    }
  }
}

// 联系人往来明细
.contact-transaction-list {
  .contact-transaction-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: $spacing-md 0;
    border-bottom: 1rpx solid $bg-secondary;
    
    &:last-child {
      border-bottom: none;
    }
    
    &:active {
      background: $bg-primary;
    }
    
    .contact-info-row {
      display: flex;
      align-items: center;
      flex: 1;
      
      .contact-avatar {
        width: 80rpx;
        height: 80rpx;
        border-radius: $radius-full;
        margin-right: $spacing-md;
      }
      
      .contact-detail {
        flex: 1;
        
        .contact-name-row {
          display: flex;
          align-items: center;
          margin-bottom: 8rpx;
          
          .contact-name {
            font-size: $font-size-base;
            font-weight: $font-weight-medium;
            color: $text-primary;
            margin-right: $spacing-sm;
          }
          
          .relation-tag {
            padding: 4rpx 12rpx;
            border-radius: $radius-sm;
            font-size: 20rpx;
            
            &.family { background: #FEF3C7; color: #D97706; }
            &.friend { background: #DBEAFE; color: #2563EB; }
            &.colleague { background: #D1FAE5; color: #059669; }
            &.classmate { background: #FCE7F3; color: #DB2777; }
            &.other, &.default { background: $bg-secondary; color: $text-secondary; }
          }
        }
        
        .transaction-stats {
          display: flex;
          gap: $spacing-md;
          
          .stat-item {
            font-size: $font-size-sm;
            
            &.income {
              color: #EF4444;
            }

            &.expense {
              color: #10B981;
            }
          }
        }
      }
    }
    
    .balance-info {
      text-align: right;
      padding-left: $spacing-md;
      
      .balance-label {
        font-size: 20rpx;
        display: block;
        margin-bottom: 4rpx;
      }
      
      .balance-value {
        font-size: $font-size-lg;
        font-weight: $font-weight-bold;
      }
      
      &.balance-positive {
        .balance-label { color: #EF4444; }
        .balance-value { color: #EF4444; }
      }
      
      &.balance-negative {
        .balance-label { color: #10B981; }
        .balance-value { color: #10B981; }
      }
      
      &.balance-zero {
        .balance-label { color: $text-secondary; }
        .balance-value { color: $text-secondary; }
      }
    }
  }
}

// 空状态
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: $spacing-xl 0;
  
  .empty-icon {
    width: 120rpx;
    height: 120rpx;
    border-radius: $radius-full;
    background: $primary-gradient;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: $spacing-md;
    
    .iconfont {
      font-size: 60rpx;
      color: #FFFFFF;
    }
  }
  
  .empty-text {
    font-size: $font-size-base;
    color: $text-secondary;
  }
}

// 热度列表
.heat-list {
  .heat-item {
    display: flex;
    align-items: center;
    gap: $spacing-md;
    padding: $spacing-sm 0;
    
    .heat-rank {
      width: 48rpx;
      height: 48rpx;
      border-radius: $radius-full;
      background: $bg-secondary;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: $font-size-sm;
      font-weight: $font-weight-bold;
      color: $text-secondary;
      flex-shrink: 0;
      
      &.top {
        background: $primary-gradient;
        color: #FFFFFF;
      }
    }
    
    .heat-name {
      width: 160rpx;
      font-size: $font-size-base;
      color: $text-primary;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      flex-shrink: 0;
    }
    
    .heat-bar {
      flex: 1;
      height: 12rpx;
      background: $bg-secondary;
      border-radius: 6rpx;
      overflow: hidden;
      
      .heat-fill {
        height: 100%;
        background: $warning-gradient;
        border-radius: 6rpx;
        transition: width 0.3s ease;
      }
    }
    
    .heat-value {
      width: 60rpx;
      text-align: right;
      font-size: $font-size-base;
      font-weight: $font-weight-bold;
      color: $text-primary;
      flex-shrink: 0;
    }
  }
}
</style>
