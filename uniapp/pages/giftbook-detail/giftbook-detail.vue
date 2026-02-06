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
        <text class="nav-title">礼簿详情</text>
        <view class="nav-right" @click="handleEdit">
          <text class="iconfont icon-edit"></text>
        </view>
      </view>
      
      <!-- 礼簿信息 -->
      <view class="book-info" v-if="giftBook">
        <view class="book-icon" :class="getTypeClass(giftBook.bookType)">
          <text class="iconfont" :class="getTypeIcon(giftBook.bookType)"></text>
        </view>
        <text class="book-name">{{ giftBook.bookName }}</text>
        <text class="book-type">{{ getTypeName(giftBook.bookType) }}</text>
        <text class="book-date">{{ giftBook.bookDate }}</text>
      </view>
    </view>
    
    <!-- 内容区域 -->
    <view class="content-section">
      <!-- 统计卡片 -->
      <view class="card stats-card" v-if="giftBook">
        <view class="stat-row">
          <view class="stat-item">
            <text class="stat-label">总收入</text>
            <text class="stat-value income">¥{{ (giftBook.totalIncome || 0).toFixed(2) }}</text>
          </view>
          <view class="stat-divider"></view>
          <view class="stat-item">
            <text class="stat-label">总支出</text>
            <text class="stat-value expense">¥{{ (giftBook.totalExpense || 0).toFixed(2) }}</text>
          </view>
          <view class="stat-divider"></view>
          <view class="stat-item">
            <text class="stat-label">结余</text>
            <text class="stat-value" :class="(giftBook.totalAmount || 0) >= 0 ? 'income' : 'expense'">
              ¥{{ (giftBook.totalAmount || 0).toFixed(2) }}
            </text>
          </view>
        </view>
      </view>
      
      <!-- 操作按钮 -->
      <view class="action-buttons">
        <button class="btn-primary" @click="handleAddRecord">
          <text class="iconfont icon-plus" style="margin-right: 8rpx;"></text>
          添加记录
        </button>
      </view>
      
      <!-- 记录列表 -->
      <view class="section-title">
        <text>记录明细</text>
        <text class="record-count">共{{ total }}条</text>
      </view>
      
      <scroll-view scroll-y class="record-list" @scrolltolower="loadMore">
        <view class="record-card" v-for="(item, index) in records" :key="index" @click="goToDetail(item.id)">
          <view class="record-header">
            <view class="header-left">
              <image class="contact-avatar" :src="item.avatarUrl || '/static/default-avatar.png'" mode="aspectFill" @error="item.avatarUrl = '/static/default-avatar.png'"></image>
              <view class="contact-info">
                <view class="info-row">
                  <text class="contact-name">{{ item.contactName }}</text>
                  <view class="direction-tag" :class="item.transactionDirection">
                    {{ item.transactionDirection === 'income' ? '收入' : '支出' }}
                  </view>
                </view>
                <view class="info-row">
                  <view class="mode-tag" :class="item.transactionMode">
                    {{ item.transactionMode === 'cash' ? '现金' : '礼物' }}
                  </view>
                  <text class="record-date">{{ item.transactionDate }} · {{ item.relationType }}</text>
                </view>
              </view>
            </view>
            <view class="header-right">
              <text class="record-amount" :class="item.transactionDirection">
                {{ item.transactionDirection === 'income' ? '+' : '-' }}¥{{ getAmount(item) }}
              </text>
            </view>
          </view>
          
          <view class="record-detail" v-if="item.transactionMode === 'gift' && item.giftName">
            <text class="iconfont icon-gift" style="margin-right: 8rpx; color: #94A3B8;"></text>
            <text class="gift-name">{{ item.giftName }}</text>
            <text class="gift-value" v-if="item.giftValue">（价值：¥{{ item.giftValue }}）</text>
          </view>
          
          <view class="record-remark" v-if="item.remark">
            <text class="iconfont icon-file-text" style="margin-right: 8rpx; color: #94A3B8;"></text>
            {{ item.remark }}
          </view>
        </view>
        
        <!-- 空状态 -->
        <view class="empty-state" v-if="records.length === 0 && !loading">
          <view class="empty-icon">
            <text class="iconfont icon-file-text"></text>
          </view>
          <text class="empty-text">暂无记录</text>
          <text class="empty-tip">快去添加第一条记录吧</text>
        </view>
        
        <!-- 加载状态 -->
        <view class="loading-more" v-if="loading">
          <view class="loading-spinner-dark"></view>
          <text class="loading-text">加载中...</text>
        </view>
        
        <!-- 没有更多 -->
        <view class="no-more" v-if="!hasMore && records.length > 0">
          <text class="no-more-text">已经到底啦</text>
        </view>
      </scroll-view>
    </view>
  </view>
</template>

<script>
import giftbookApi from '@/api/giftbook'
import transactionApi from '@/api/transaction'

export default {
  data() {
    return {
      giftBookId: null,
      giftBook: null,
      records: [],
      page: 1,
      size: 10,
      total: 0,
      loading: false,
      hasMore: true
    }
  },
  
  onLoad(options) {
    if (options.id) {
      this.giftBookId = options.id
      this.loadGiftBookDetail()
      this.loadRecords()
    }
  },

  methods: {
    getTypeClass(type) {
      const map = {
        '婚礼': 'wedding',
        '生日': 'birthday',
        '葬礼': 'funeral',
        '其他': 'other'
      }
      return map[type] || 'other'
    },

    getTypeIcon(type) {
      const map = {
        '婚礼': 'icon-heart',
        '生日': 'icon-bell',
        '葬礼': 'icon-lock',
        '其他': 'icon-file-text'
      }
      return map[type] || 'icon-file-text'
    },

    getTypeName(type) {
      const map = {
        '婚礼': '婚礼',
        '生日': '生日',
        '葬礼': '葬礼',
        '其他': '其他'
      }
      return map[type] || type || '其他'
    },
    
    getAmount(item) {
      if (item.transactionMode === 'cash') {
        return item.amount.toFixed(2)
      } else if (item.giftValue) {
        return item.giftValue.toFixed(2)
      }
      return '0.00'
    },
    
    async loadGiftBookDetail() {
      try {
        const res = await giftbookApi.getGiftBookDetail({ id: this.giftBookId })
        
        if (res.code === 1) {
          this.giftBook = res.data
        }
      } catch (error) {
        console.error('加载礼簿详情失败:', error)
      }
    },
    
    async loadRecords(reset = true) {
      if (reset) {
        this.page = 1
        this.hasMore = true
      }
      
      if (this.loading || !this.hasMore) {
        return
      }
      
      this.loading = true
      
      try {
        const params = {
          page: this.page,
          size: this.size,
          giftBookId: this.giftBookId
        }
        
        const res = await transactionApi.getTransactionList(params)
        
        if (res.code === 1) {
          if (reset) {
            this.records = res.data.list
          } else {
            this.records = [...this.records, ...res.data.list]
          }
          
          this.total = res.data.total
          this.hasMore = this.records.length < this.total
        }
      } catch (error) {
        console.error('加载记录失败:', error)
      } finally {
        this.loading = false
      }
    },
    
    loadMore() {
      if (this.hasMore && !this.loading) {
        this.page++
        this.loadRecords(false)
      }
    },
    
    handleAddRecord() {
      // 将礼簿ID和来源页面存储到本地存储，然后跳转到记账页面
      uni.setStorageSync('selectedGiftBookId', this.giftBookId)
      uni.setStorageSync('selectedGiftBook', this.giftBook)
      uni.setStorageSync('selectedFromPage', 'giftbook-detail')
      uni.navigateTo({
        url: '/pages/record/record?from=giftbook-detail'
      })
    },
    
    handleEdit() {
      uni.navigateTo({
        url: `/pages/giftbook/giftbook?edit=true&id=${this.giftBookId}`
      })
    },
    
    goToDetail(id) {
      uni.navigateTo({
        url: `/pages/transaction-detail/transaction-detail?id=${id}`
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

// 礼簿信息
.book-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40rpx $page-padding 60rpx;
  
  .book-icon {
    width: 120rpx;
    height: 120rpx;
    border-radius: $radius-xl;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: $spacing-md;
    
    .iconfont {
      font-size: 60rpx;
      color: #FFFFFF;
    }
    
    &.wedding {
      background: linear-gradient(135deg, #F59E0B, #EF4444);
    }
    
    &.birthday {
      background: linear-gradient(135deg, #3B82F6, #8B5CF6);
    }
    
    &.funeral {
      background: linear-gradient(135deg, #6B7280, #374151);
    }
    
    &.other {
      background: linear-gradient(135deg, #10B981, #059669);
    }
  }
  
  .book-name {
    font-size: $font-size-xl;
    font-weight: $font-weight-bold;
    color: #FFFFFF;
    margin-bottom: 8rpx;
  }
  
  .book-type {
    font-size: $font-size-base;
    color: rgba(255, 255, 255, 0.9);
    margin-bottom: 8rpx;
  }
  
  .book-date {
    font-size: $font-size-sm;
    color: rgba(255, 255, 255, 0.8);
  }
}

// 统计卡片
.stats-card {
  margin-bottom: $spacing-md;
  
  .stat-row {
    display: flex;
    align-items: center;
    padding: $spacing-md 0;
  }
  
  .stat-item {
    flex: 1;
    text-align: center;
  }
  
  .stat-divider {
    width: 2rpx;
    height: 60rpx;
    background: $bg-secondary;
  }
  
  .stat-label {
    font-size: $font-size-sm;
    color: $text-secondary;
    display: block;
    margin-bottom: 8rpx;
  }
  
  .stat-value {
    font-size: $font-size-xl;
    font-weight: $font-weight-bold;
    color: $text-primary;
    
    &.income {
      color: #EF4444;
    }

    &.expense {
      color: #10B981;
    }
  }
}

// 操作按钮
.action-buttons {
  margin-bottom: $spacing-md;
}

// 区域标题
.section-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: $spacing-md 0;
  
  .record-count {
    font-size: $font-size-sm;
    color: $text-secondary;
  }
}

// 记录列表
.record-list {
  height: calc(100vh - 700rpx);
}

// 记录卡片
.record-card {
  background: #FFFFFF;
  border-radius: $radius-xl;
  padding: $spacing-md;
  margin-bottom: $spacing-md;
  box-shadow: $shadow-sm;
  transition: all 0.3s ease;
  
  &:active {
    transform: scale(0.98);
    box-shadow: $shadow-md;
  }
  
  .record-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: $spacing-sm;
    
    .header-left {
      flex: 1;
      display: flex;
      align-items: center;
    }
    
    .contact-avatar {
      width: 80rpx;
      height: 80rpx;
      border-radius: $radius-full;
      margin-right: $spacing-md;
      border: 4rpx solid $bg-secondary;
    }
    
    .contact-info {
      flex: 1;
    }
    
    .info-row {
      display: flex;
      align-items: center;
      gap: $spacing-sm;
      margin-bottom: 6rpx;
    }
    
    .contact-name {
      font-size: $font-size-md;
      font-weight: $font-weight-bold;
      color: $text-primary;
    }
    
    .direction-tag {
      font-size: 20rpx;
      padding: 4rpx 12rpx;
      border-radius: 20rpx;
      font-weight: $font-weight-medium;
      
      &.income {
        background: #D1FAE5;
        color: #059669;
      }
      
      &.expense {
        background: #FEE2E2;
        color: #DC2626;
      }
    }
    
    .mode-tag {
      font-size: 20rpx;
      padding: 4rpx 12rpx;
      border-radius: 20rpx;
      font-weight: $font-weight-medium;
      
      &.cash {
        background: #DBEAFE;
        color: #2563EB;
      }
      
      &.gift {
        background: #FCE7F3;
        color: #DB2777;
      }
    }
    
    .record-date {
      font-size: $font-size-sm;
      color: $text-secondary;
    }
    
    .header-right {
      .record-amount {
        font-size: $font-size-xl;
        font-weight: $font-weight-bold;
        
        &.income {
          color: #EF4444;
        }

        &.expense {
          color: #10B981;
        }
      }
    }
  }
  
  .record-detail {
    background: $bg-primary;
    border-radius: $radius-lg;
    padding: $spacing-sm $spacing-md;
    font-size: $font-size-sm;
    color: $text-secondary;
    margin-bottom: $spacing-sm;
    display: flex;
    align-items: center;
    
    .gift-name {
      flex: 1;
    }
    
    .gift-value {
      color: $text-primary;
      font-weight: $font-weight-medium;
    }
  }
  
  .record-remark {
    background: $bg-primary;
    border-radius: $radius-lg;
    padding: $spacing-sm $spacing-md;
    font-size: $font-size-sm;
    color: $text-secondary;
    display: flex;
    align-items: center;
  }
}

// 加载更多
.loading-more {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: $spacing-lg 0;
  gap: $spacing-sm;
  
  .loading-text {
    font-size: $font-size-base;
    color: $text-secondary;
  }
}

.no-more {
  display: flex;
  justify-content: center;
  padding: $spacing-lg 0;
  
  .no-more-text {
    font-size: $font-size-sm;
    color: $text-tertiary;
  }
}
</style>
