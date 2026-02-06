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
        <text class="nav-title">账单明细</text>
        <view class="nav-right" @click="handleExport">
          <text class="iconfont icon-download"></text>
        </view>
      </view>
      
      <!-- 统计概览 -->
      <view class="stats-overview">
        <view class="stat-item">
          <text class="stat-label">总支出</text>
          <text class="stat-value">¥{{ totalAmount.toFixed(2) }}</text>
        </view>
        <view class="stat-divider"></view>
        <view class="stat-item">
          <text class="stat-label">记录数</text>
          <text class="stat-value">{{ total }}笔</text>
        </view>
      </view>
    </view>
    
    <!-- 内容区域 -->
    <view class="content-section">
      <!-- 筛选卡片 -->
      <view class="card filter-card">
        <view class="filter-row">
          <view class="filter-item">
            <text class="filter-label">时间</text>
            <picker mode="selector" :range="timeRanges" @change="onTimeRangeChange">
              <view class="filter-value">
                <text>{{ selectedTimeRange }}</text>
                <text class="iconfont icon-arrow-down" style="margin-left: 8rpx; font-size: 24rpx; color: #94A3B8;"></text>
              </view>
            </picker>
          </view>
          
          <view class="filter-item">
            <text class="filter-label">关系</text>
            <picker mode="selector" :range="relationTypes" @change="onRelationChange">
              <view class="filter-value">
                <text>{{ relationType || '全部' }}</text>
                <text class="iconfont icon-arrow-down" style="margin-left: 8rpx; font-size: 24rpx; color: #94A3B8;"></text>
              </view>
            </picker>
          </view>
        </view>
        
        <!-- 搜索框 -->
        <view class="search-box">
          <text class="iconfont icon-search"></text>
          <input 
            class="search-input" 
            type="text" 
            v-model="contactName" 
            placeholder="搜索联系人姓名"
            @confirm="handleSearch"
          />
          <text class="clear-btn" v-if="contactName" @click="clearSearch">×</text>
        </view>
        
        <!-- 筛选操作 -->
        <view class="filter-actions">
          <button class="btn-secondary btn-sm" style="flex: 1;" @click="handleReset">
            <text class="iconfont icon-refresh" style="margin-right: 8rpx;"></text>
            重置
          </button>
          <button class="btn-primary btn-sm" style="flex: 1;" @click="handleSearch">
            <text class="iconfont icon-search" style="margin-right: 8rpx;"></text>
            查询
          </button>
        </view>
      </view>
      
      <!-- 账单列表 -->
      <scroll-view scroll-y class="bill-list" @scrolltolower="loadMore">
        <view class="bill-card" v-for="(item, index) in transactions" :key="index" @click="goToDetail(item.id)">
          <view class="bill-header">
            <view class="header-left">
              <image class="contact-avatar" :src="item.avatarUrl || '/static/default-avatar.png'" mode="aspectFill" @error="item.avatarUrl = '/static/default-avatar.png'"></image>
              <view class="contact-info">
                <view class="info-row">
                  <text class="contact-name">{{ item.contactName }}</text>
                  <view class="type-tag" :class="item.transactionDirection">
                    {{ item.relationType }}
                  </view>
                </view>
                <text class="bill-date">{{ item.transactionDate }}</text>
              </view>
            </view>
            <view class="header-right">
              <text class="bill-amount">¥{{ item.amount }}</text>
            </view>
          </view>
          
          <view class="bill-remark" v-if="item.remark">
            <text class="iconfont icon-file-text" style="margin-right: 8rpx; color: #94A3B8;"></text>
            {{ item.remark }}
          </view>
          
          <view class="bill-actions">
            <view class="action-btn edit" @click.stop="handleEdit(item)">
              <text class="iconfont icon-edit"></text>
              <text>编辑</text>
            </view>
            <view class="action-btn delete" @click.stop="handleDelete(item)">
              <text class="iconfont icon-trash"></text>
              <text>删除</text>
            </view>
          </view>
        </view>
        
        <!-- 空状态 -->
        <view class="empty-state" v-if="transactions.length === 0 && !loading">
          <view class="empty-icon">
            <text class="iconfont icon-file-text"></text>
          </view>
          <text class="empty-text">暂无账单记录</text>
          <text class="empty-tip">快去记一笔吧</text>
        </view>
        
        <!-- 加载状态 -->
        <view class="loading-more" v-if="loading">
          <view class="loading-spinner-dark"></view>
          <text class="loading-text">加载中...</text>
        </view>
        
        <!-- 没有更多 -->
        <view class="no-more" v-if="!hasMore && transactions.length > 0">
          <text class="no-more-text">已经到底啦</text>
        </view>
      </scroll-view>
    </view>
  </view>
</template>

<script>
import transactionApi from '@/api/transaction'

export default {
  data() {
    return {
      selectedTimeRange: '全部',
      timeRanges: ['全部', '今天', '本周', '本月', '本季度', '本年'],
      relationType: '',
      relationTypes: ['全部', '亲戚', '朋友', '同事', '同学', '其他'],
      contactName: '',
      transactions: [],
      page: 1,
      size: 10,
      total: 0,
      loading: false,
      hasMore: true,
      totalAmount: 0
    }
  },

  onLoad() {
    this.loadTransactions()
  },

  methods: {
    onTimeRangeChange(e) {
      this.selectedTimeRange = this.timeRanges[e.detail.value]
    },

    onRelationChange(e) {
      const value = this.relationTypes[e.detail.value]
      this.relationType = value === '全部' ? '' : value
    },
    
    async loadTransactions(reset = true) {
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
          size: this.size
        }
        
        if (this.relationType) {
          params.relationType = this.relationType
        }
        
        if (this.contactName) {
          params.contactName = this.contactName
        }
        
        const res = await transactionApi.getTransactionList(params)
        
        if (res.code === 1) {
          if (reset) {
            this.transactions = res.data.list
          } else {
            this.transactions = [...this.transactions, ...res.data.list]
          }
          
          this.total = res.data.total
          this.hasMore = this.transactions.length < this.total
          
          // 计算总金额
          this.totalAmount = this.transactions.reduce((sum, item) => sum + item.amount, 0)
        }
      } catch (error) {
        console.error('加载账单失败:', error)
      } finally {
        this.loading = false
      }
    },
    
    loadMore() {
      if (this.hasMore && !this.loading) {
        this.page++
        this.loadTransactions(false)
      }
    },
    
    handleSearch() {
      this.loadTransactions(true)
    },
    
    clearSearch() {
      this.contactName = ''
      this.loadTransactions(true)
    },
    
    handleReset() {
      this.selectedTimeRange = '全部'
      this.transactionType = ''
      this.relationType = ''
      this.contactName = ''
      this.loadTransactions(true)
    },
    
    handleExport() {
      uni.showToast({
        title: '导出功能开发中',
        icon: 'none'
      })
    },
    
    handleEdit(item) {
      uni.navigateTo({
        url: `/pages/transaction-detail/transaction-detail?id=${item.id}&edit=true`
      })
    },
    
    handleDelete(item) {
      uni.showModal({
        title: '确认删除',
        content: `确定要删除这条记录吗？`,
        confirmColor: '#EF4444',
        success: async (res) => {
          if (res.confirm) {
            try {
              const deleteRes = await transactionApi.deleteTransaction({ id: item.id })
              
              if (deleteRes.code === 1) {
                uni.showToast({
                  title: '删除成功',
                  icon: 'success'
                })
                
                this.loadTransactions(true)
              }
            } catch (error) {
              console.error('删除失败:', error)
              uni.showToast({
                title: '删除失败',
                icon: 'none'
              })
            }
          }
        }
      })
    },
    
    goToDetail(id) {
      uni.navigateTo({
        url: `/pages/transaction-detail/transaction-detail?id=${id}`
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

// 统计概览
.stats-overview {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40rpx $page-padding 60rpx;
  
  .stat-item {
    flex: 1;
    text-align: center;
  }
  
  .stat-divider {
    width: 2rpx;
    height: 60rpx;
    background: rgba(255, 255, 255, 0.3);
  }
  
  .stat-label {
    font-size: $font-size-sm;
    color: rgba(255, 255, 255, 0.8);
    display: block;
    margin-bottom: 8rpx;
  }
  
  .stat-value {
    font-size: 40rpx;
    font-weight: $font-weight-bold;
    color: #FFFFFF;
  }
}

// 筛选卡片
.filter-card {
  .filter-row {
    display: flex;
    gap: $spacing-md;
    margin-bottom: $spacing-md;
  }
  
  .filter-item {
    flex: 1;
    
    .filter-label {
      font-size: $font-size-sm;
      color: $text-secondary;
      margin-bottom: 8rpx;
      display: block;
    }
    
    .filter-value {
      display: flex;
      align-items: center;
      justify-content: center;
      background: $bg-primary;
      border-radius: $radius-lg;
      padding: 16rpx;
      font-size: $font-size-base;
      color: $text-primary;
      font-weight: $font-weight-medium;
    }
  }
  
  .search-box {
    display: flex;
    align-items: center;
    background: $bg-primary;
    border-radius: $radius-xl;
    padding: 0 $spacing-md;
    height: 80rpx;
    margin-bottom: $spacing-md;
    
    .icon-search {
      font-size: 32rpx;
      color: $text-tertiary;
      margin-right: $spacing-sm;
    }
    
    .search-input {
      flex: 1;
      font-size: $font-size-base;
      color: $text-primary;
      
      &::placeholder {
        color: $text-tertiary;
      }
    }
    
    .clear-btn {
      font-size: 36rpx;
      color: $text-tertiary;
      padding: 0 $spacing-sm;
    }
  }
  
  .filter-actions {
    display: flex;
    gap: $spacing-md;
  }
}

// 账单列表
.bill-list {
  height: calc(100vh - 600rpx);
}

// 账单卡片
.bill-card {
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
  
  .bill-header {
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
    
    .type-tag {
      font-size: 20rpx;
      padding: 4rpx 12rpx;
      border-radius: 20rpx;
      font-weight: $font-weight-medium;
      
      &.wedding {
        background: #FEF3C7;
        color: #D97706;
      }
      
      &.birthday {
        background: #DBEAFE;
        color: #2563EB;
      }
      
      &.festival {
        background: #D1FAE5;
        color: #059669;
      }
      
      &.house {
        background: #FCE7F3;
        color: #DB2777;
      }
      
      &.dinner {
        background: #E0E7FF;
        color: #7C3AED;
      }
      
      &.activity {
        background: #FEE2E2;
        color: #DC2626;
      }
      
      &.default {
        background: $bg-secondary;
        color: $text-secondary;
      }
    }
    
    .bill-date {
      font-size: $font-size-sm;
      color: $text-secondary;
    }
    
    .header-right {
      .bill-amount {
        font-size: $font-size-xl;
        font-weight: $font-weight-bold;
        color: $text-primary;
      }
    }
  }
  
  .bill-remark {
    background: $bg-primary;
    border-radius: $radius-lg;
    padding: $spacing-sm $spacing-md;
    font-size: $font-size-sm;
    color: $text-secondary;
    margin-bottom: $spacing-md;
    display: flex;
    align-items: center;
  }
  
  .bill-actions {
    display: flex;
    justify-content: flex-end;
    gap: $spacing-md;
    padding-top: $spacing-sm;
    border-top: 1rpx solid $bg-secondary;
    
    .action-btn {
      display: flex;
      align-items: center;
      gap: 8rpx;
      padding: 12rpx 24rpx;
      border-radius: $radius-md;
      font-size: $font-size-sm;
      font-weight: $font-weight-medium;
      transition: all 0.3s ease;
      
      .iconfont {
        font-size: 28rpx;
      }
      
      &.edit {
        background: #DBEAFE;
        color: #2563EB;
        
        &:active {
          background: #BFDBFE;
        }
      }
      
      &.delete {
        background: #FEE2E2;
        color: #DC2626;
        
        &:active {
          background: #FECACA;
        }
      }
    }
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
