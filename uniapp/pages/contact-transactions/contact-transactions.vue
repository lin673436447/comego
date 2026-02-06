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
        <text class="nav-title">往来账目</text>
        <view class="nav-right"></view>
      </view>

      <!-- 联系人信息 -->
      <view class="contact-info-header">
        <image class="contact-avatar" :src="contactInfo.avatarUrl || '/static/default-avatar.png'" mode="aspectFill" @error="contactInfo.avatarUrl = '/static/default-avatar.png'"></image>
        <view class="contact-details">
          <text class="contact-name">{{ contactInfo.name || contactName }}</text>
          <text class="contact-relation" v-if="contactInfo.relationType">{{ contactInfo.relationType }}</text>
        </view>
      </view>

      <!-- 统计概览 -->
      <view class="stats-overview">
        <view class="stat-item">
          <text class="stat-label">总收入</text>
          <text class="stat-value income">¥{{ totalIncome }}</text>
        </view>
        <view class="stat-divider"></view>
        <view class="stat-item">
          <text class="stat-label">总支出</text>
          <text class="stat-value expense">¥{{ totalExpense }}</text>
        </view>
        <view class="stat-divider"></view>
        <view class="stat-item">
          <text class="stat-label">总次数</text>
          <text class="stat-value total">{{ totalCount }}</text>
        </view>
      </view>
    </view>

    <!-- 内容区域 -->
    <view class="content-section">
      <view class="card">
        <view class="card-header">
          <text class="header-title">所有账目</text>
        </view>

        <view class="transaction-list" v-if="transactions.length > 0">
          <view class="transaction-item" v-for="(item, index) in transactions" :key="index" @click="goToTransactionDetail(item.id)">
            <!-- 第一行：时间 + 金额/礼物 -->
            <view class="item-row">
              <text class="item-date">{{ item.transactionDate }}</text>
              <view class="item-amount-wrap">
                <template v-if="isPhysicalGift(item)">
                  <text class="item-gift">{{ item.giftName }}</text>
                </template>
                <template v-else>
                  <text class="item-amount" :class="{ income: item.transactionDirection === 'income', expense: item.transactionDirection === 'expense' }">
                    {{ item.transactionDirection === 'income' ? '+' : '-' }}¥{{ formatAmount(item.amount) }}
                  </text>
                </template>
              </view>
            </view>

            <!-- 第二行：礼簿名称（右对齐） -->
            <view class="item-row secondary" v-if="item.giftBookName">
              <view class="item-giftbook-spacer"></view>
              <view class="item-giftbook">
                <text class="giftbook-name">{{ item.giftBookName }}</text>
              </view>
            </view>
          </view>
        </view>

        <view class="empty-state" v-else>
          <view class="empty-icon">
            <text class="iconfont icon-file-text"></text>
          </view>
          <text class="empty-text">暂无交易记录</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import contactApi from '@/api/contact'
import transactionApi from '@/api/transaction'

export default {
  data() {
    return {
      contactId: null,
      contactName: '',
      contactInfo: {
        name: '',
        avatarUrl: '',
        relationType: ''
      },
      transactions: [],
      totalIncome: '0.00',
      totalExpense: '0.00',
      totalCount: 0
    }
  },

  onLoad(options) {
    this.contactId = options.contactId
    this.contactName = decodeURIComponent(options.contactName || '')
    if (this.contactId) {
      this.loadContactInfo()
      this.loadTransactions()
    }
  },

  methods: {
    async loadContactInfo() {
      try {
        const res = await contactApi.getContactDetail({ id: this.contactId })
        if (res.code === 1 && res.data) {
          this.contactInfo = res.data
        }
      } catch (error) {
        console.error('加载联系人信息失败:', error)
      }
    },

    async loadTransactions() {
      try {
        const res = await transactionApi.getTransactionList({
          contactId: this.contactId,
          page: 1,
          size: 1000
        })

        if (res.code === 1) {
          this.transactions = res.data.list || []
          this.calculateStats()
        }
      } catch (error) {
        console.error('加载交易记录失败:', error)
      }
    },

    calculateStats() {
      let income = 0
      let expense = 0

      this.transactions.forEach(item => {
        if (!this.isPhysicalGift(item)) {
          const amount = parseFloat(item.amount) || 0
          if (item.transactionDirection === 'income') {
            income += amount
          } else if (item.transactionDirection === 'expense') {
            expense += amount
          }
        }
      })

      this.totalIncome = income.toFixed(2)
      this.totalExpense = expense.toFixed(2)
      this.totalCount = this.transactions.length
    },

    isPhysicalGift(item) {
      return item.transactionMode === '实物' || (item.giftName && item.giftName.trim() !== '')
    },

    formatAmount(amount) {
      if (!amount) return '0.00'
      const num = parseFloat(amount)
      if (num >= 10000) {
        return (num / 10000).toFixed(2) + '万'
      }
      return num.toFixed(2)
    },

    goToTransactionDetail(transactionId) {
      uni.navigateTo({
        url: `/pages/transaction-detail/transaction-detail?id=${transactionId}`
      })
    },

    goBack() {
      uni.navigateBack()
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/styles/theme.scss';
@import '@/styles/components.scss';

/* 联系人信息头部 */
.contact-info-header {
  display: flex;
  align-items: center;
  padding: 20rpx $page-padding 40rpx;

  .contact-avatar {
    width: 120rpx;
    height: 120rpx;
    border-radius: $radius-full;
    border: 4rpx solid rgba(255, 255, 255, 0.3);
    margin-right: $spacing-lg;
  }

  .contact-details {
    flex: 1;

    .contact-name {
      font-size: 40rpx;
      font-weight: $font-weight-bold;
      color: #FFFFFF;
      display: block;
      margin-bottom: 12rpx;
    }

    .contact-relation {
      font-size: $font-size-base;
      color: rgba(255, 255, 255, 0.8);
      background: rgba(255, 255, 255, 0.2);
      padding: 8rpx 20rpx;
      border-radius: $radius-lg;
    }
  }
}

/* 统计概览 */
.stats-overview {
  display: flex;
  align-items: center;
  padding: 0 $page-padding 40rpx;

  .stat-item {
    flex: 1;
    text-align: center;
  }

  .stat-divider {
    width: 2rpx;
    height: 80rpx;
    background: rgba(255, 255, 255, 0.3);
  }

  .stat-label {
    font-size: $font-size-sm;
    color: rgba(255, 255, 255, 0.8);
    display: block;
    margin-bottom: 12rpx;
  }

  .stat-value {
    font-size: 36rpx;
    font-weight: $font-weight-bold;
    display: block;

    &.income {
      color: #FECACA;
    }

    &.expense {
      color: #A7F3D0;
    }

    &.total {
      color: #FFFFFF;
    }
  }
}

/* 交易列表 - 简洁版 */
.transaction-list {
  .transaction-item {
    display: flex;
    flex-direction: column;
    padding: 24rpx 0;
    border-bottom: 1rpx solid $bg-secondary;

    &:last-child {
      border-bottom: none;
    }

    &:active {
      background: $bg-primary;
    }

    /* 行布局 */
    .item-row {
      display: flex;
      align-items: center;
      justify-content: space-between;

      &.secondary {
        margin-top: 8rpx;
      }

      /* 时间 */
      .item-date {
        font-size: $font-size-base;
        color: $text-secondary;
      }

      /* 礼簿名称 */
      .item-giftbook {
        display: flex;
        align-items: center;
        padding: 4rpx 12rpx;
        background: linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%);
        border-radius: $radius-md;

        .giftbook-name {
          font-size: $font-size-sm;
          color: #667eea;
          font-weight: $font-weight-medium;
        }
      }

      .item-giftbook-spacer {
        flex: 1;
      }

      /* 金额/礼物 */
      .item-amount-wrap {
        display: flex;
        align-items: center;

        .item-amount {
          font-size: 36rpx;
          font-weight: $font-weight-bold;

          &.income {
            color: #EF4444;
          }

          &.expense {
            color: #10B981;
          }
        }

        .item-gift {
          font-size: 32rpx;
          font-weight: $font-weight-medium;
          color: #8B5CF6;
        }
      }
    }
  }
}

/* 空状态 */
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
</style>
