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
        <text class="nav-title">礼簿管理</text>
        <view class="nav-right" @click="handleAdd">
          <text class="iconfont icon-plus"></text>
        </view>
      </view>
      
      <!-- 统计概览 -->
      <view class="stats-overview">
        <view class="stat-item">
          <text class="stat-label">总收入</text>
          <text class="stat-value income">¥{{ (totalIncome || 0).toFixed(2) }}</text>
        </view>
        <view class="stat-divider"></view>
        <view class="stat-item">
          <text class="stat-label">总支出</text>
          <text class="stat-value expense">¥{{ (totalExpense || 0).toFixed(2) }}</text>
        </view>
        <view class="stat-divider"></view>
        <view class="stat-item">
          <text class="stat-label">礼簿数</text>
          <text class="stat-value">{{ total }}个</text>
        </view>
      </view>
    </view>
    
    <!-- 内容区域 -->
    <view class="content-section">
      <!-- 筛选卡片 -->
      <view class="card filter-card">
        <view class="filter-row">
          <view class="filter-item">
            <text class="filter-label">类型</text>
            <picker mode="selector" :range="bookTypes" @change="onTypeChange">
              <view class="filter-value">
                <text>{{ bookType || '全部' }}</text>
                <text class="iconfont icon-arrow-down" style="margin-left: 8rpx; font-size: 24rpx; color: #94A3B8;"></text>
              </view>
            </picker>
          </view>
          
          <view class="filter-item">
            <text class="filter-label">搜索</text>
            <input 
              class="filter-input" 
              type="text" 
              v-model="bookName" 
              placeholder="礼簿名称"
              @confirm="handleSearch"
            />
          </view>
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
      
      <!-- 礼簿列表 -->
      <scroll-view scroll-y class="giftbook-list" @scrolltolower="loadMore">
        <view class="giftbook-card" v-for="(item, index) in giftBooks" :key="index" @click="goToDetail(item.id)">
          <view class="card-header">
            <view class="header-left">
              <view class="book-icon" :class="getTypeClass(item.bookType)">
                <text class="iconfont" :class="getTypeIcon(item.bookType)"></text>
              </view>
              <view class="book-info">
                <text class="book-name">{{ item.bookName }}</text>
                <text class="book-type">{{ getTypeName(item.bookType) }}</text>
              </view>
            </view>
            <view class="header-right">
              <text class="book-date">{{ item.bookDate }}</text>
            </view>
          </view>
          
          <view class="card-stats">
            <view class="stat-row">
              <text class="stat-label">收入</text>
              <text class="stat-value income">¥{{ (parseFloat(item.totalIncome) || 0).toFixed(2) }}</text>
            </view>
            <view class="stat-divider"></view>
            <view class="stat-row">
              <text class="stat-label">支出</text>
              <text class="stat-value expense">¥{{ (parseFloat(item.totalExpense) || 0).toFixed(2) }}</text>
            </view>
            <view class="stat-divider"></view>
            <view class="stat-row">
              <text class="stat-label">结余</text>
              <text class="stat-value" :class="(parseFloat(item.totalAmount) || 0) >= 0 ? 'income' : 'expense'">
                ¥{{ (parseFloat(item.totalAmount) || 0).toFixed(2) }}
              </text>
            </view>
          </view>
          
          <view class="card-remark" v-if="item.description">
            <text class="iconfont icon-file-text" style="margin-right: 8rpx; color: #94A3B8;"></text>
            {{ item.description }}
          </view>
          
          <view class="card-actions">
            <view class="action-btn edit" @click.stop="handleEdit(item)">
              <text class="iconfont icon-edit"></text>
              <text>编辑</text>
            </view>
            <view class="action-btn record" @click.stop="handleAddRecord(item)">
              <text class="iconfont icon-plus"></text>
              <text>记账</text>
            </view>
            <view class="action-btn delete" @click.stop="handleDelete(item)">
              <text class="iconfont icon-trash"></text>
              <text>删除</text>
            </view>
          </view>
        </view>
        
        <!-- 空状态 -->
        <view class="empty-state" v-if="giftBooks.length === 0 && !loading">
          <view class="empty-icon">
            <text class="iconfont icon-book"></text>
          </view>
          <text class="empty-text">暂无礼簿</text>
          <text class="empty-tip">快去创建一个吧</text>
        </view>
        
        <!-- 加载状态 -->
        <view class="loading-more" v-if="loading">
          <view class="loading-spinner-dark"></view>
          <text class="loading-text">加载中...</text>
        </view>
        
        <!-- 没有更多 -->
        <view class="no-more" v-if="!hasMore && giftBooks.length > 0">
          <text class="no-more-text">已经到底啦</text>
        </view>
      </scroll-view>
    </view>
    

  </view>
</template>

<script>
import giftbookApi from '@/api/giftbook'

export default {
  data() {
    return {
      bookType: '',
      bookTypes: ['全部', '婚礼', '生日', '葬礼', '其他'],
      bookName: '',
      giftBooks: [],
      page: 1,
      size: 10,
      total: 0,
      loading: false,
      hasMore: true,
      totalIncome: 0.00,
      totalExpense: 0.00
    }
  },
  
  onLoad() {
    this.loadGiftBooks()
  },

  onShow() {
    // 每次页面显示时刷新数据
    this.loadGiftBooks()
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
    
    onTypeChange(e) {
      const value = this.bookTypes[e.detail.value]
      this.bookType = value === '全部' ? '' : value
    },
    
    async loadGiftBooks(reset = true) {
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
        
        if (this.bookType) {
          params.bookType = this.bookType
        }
        
        if (this.bookName) {
          params.bookName = this.bookName
        }
        
        const res = await giftbookApi.getGiftBookList(params)
        
        if (res.code === 1) {
          if (reset) {
            this.giftBooks = res.data.list
          } else {
            this.giftBooks = [...this.giftBooks, ...res.data.list]
          }
          
          this.total = Number(res.data.total) || 0
          this.hasMore = this.giftBooks.length < this.total

          // 计算总收入和总支出（确保转换为数字）
          this.totalIncome = this.giftBooks.reduce((sum, item) => sum + (parseFloat(item.totalIncome) || 0), 0)
          this.totalExpense = this.giftBooks.reduce((sum, item) => sum + (parseFloat(item.totalExpense) || 0), 0)
        }
      } catch (error) {
        console.error('加载礼簿失败:', error)
      } finally {
        this.loading = false
      }
    },
    
    loadMore() {
      if (this.hasMore && !this.loading) {
        this.page++
        this.loadGiftBooks(false)
      }
    },
    
    handleSearch() {
      this.loadGiftBooks(true)
    },
    
    handleReset() {
      this.bookType = ''
      this.bookName = ''
      this.loadGiftBooks(true)
    },
    
    handleAdd() {
      uni.navigateTo({
        url: '/pages/giftbook-add/giftbook-add'
      })
    },
    
    handleEdit(item) {
      uni.navigateTo({
        url: `/pages/giftbook-add/giftbook-add?id=${item.id}`
      })
    },
    
    handleAddRecord(item) {
      // 将礼簿ID和来源页面存储到本地存储，然后跳转到记账页面
      uni.setStorageSync('selectedGiftBookId', item.id)
      uni.setStorageSync('selectedGiftBook', item)
      uni.setStorageSync('selectedFromPage', 'giftbook')
      uni.navigateTo({
        url: '/pages/record/record?from=giftbook'
      })
    },
    
    handleDelete(item) {
      uni.showModal({
        title: '确认删除',
        content: `确定要删除"${item.bookName}"礼簿吗？`,
        confirmColor: '#EF4444',
        success: async (res) => {
          if (res.confirm) {
            try {
              const deleteRes = await giftbookApi.deleteGiftBook({ id: item.id })
              
              if (deleteRes.code === 1) {
                uni.showToast({
                  title: '删除成功',
                  icon: 'success'
                })
                
                this.loadGiftBooks(true)
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
        url: `/pages/giftbook-detail/giftbook-detail?id=${id}`
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

    &.income {
      color: #EF4444;
    }

    &.expense {
      color: #10B981;
    }
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
      padding: 0 16rpx;         // 只保留左右 padding，上下由 line-height 或默认居中控制
      font-size: $font-size-base;
      height: 76rpx;
      line-height: 76rpx;       // ✅ 让 line-height = height，实现垂直居中
      box-sizing: border-box;
    }

    .filter-input {
      background: $bg-primary;
      border-radius: $radius-lg;
      padding: 0 16rpx;         // 只保留左右 padding，上下由 line-height 或默认居中控制
      font-size: $font-size-base;
      color: $text-primary;
      height: 76rpx;
      line-height: 76rpx;       // ✅ 让 line-height = height，实现垂直居中
      box-sizing: border-box;
    }
  }
  
  .filter-actions {
    display: flex;
    gap: $spacing-md;
  }
}

// 礼簿列表
.giftbook-list {
  height: calc(100vh - 600rpx);
}

// 礼簿卡片
.giftbook-card {
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
  
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: $spacing-md;
    
    .header-left {
      flex: 1;
      display: flex;
      align-items: center;
    }
    
    .book-icon {
      width: 80rpx;
      height: 80rpx;
      border-radius: $radius-lg;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: $spacing-md;
      
      .iconfont {
        font-size: 40rpx;
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
    
    .book-info {
      flex: 1;
    }
    
    .book-name {
      font-size: $font-size-md;
      font-weight: $font-weight-bold;
      color: $text-primary;
      display: block;
      margin-bottom: 6rpx;
    }
    
    .book-type {
      font-size: $font-size-sm;
      color: $text-secondary;
    }
    
    .header-right {
      .book-date {
        font-size: $font-size-sm;
        color: $text-secondary;
      }
    }
  }
  
  .card-stats {
    display: flex;
    align-items: center;
    background: $bg-primary;
    border-radius: $radius-lg;
    padding: $spacing-sm $spacing-md;
    margin-bottom: $spacing-sm;
    
    .stat-row {
      flex: 1;
      text-align: center;
    }
    
    .stat-divider {
      width: 2rpx;
      height: 40rpx;
      background: $bg-secondary;
    }
    
    .stat-label {
      font-size: $font-size-xs;
      color: $text-secondary;
      display: block;
      margin-bottom: 4rpx;
    }
    
    .stat-value {
      font-size: $font-size-base;
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
  
  .card-remark {
    background: $bg-primary;
    border-radius: $radius-lg;
    padding: $spacing-sm $spacing-md;
    font-size: $font-size-sm;
    color: $text-secondary;
    margin-bottom: $spacing-md;
    display: flex;
    align-items: center;
  }
  
  .card-actions {
    display: flex;
    justify-content: flex-end;
    gap: $spacing-sm;
    padding-top: $spacing-sm;
    border-top: 1rpx solid $bg-secondary;
    
    .action-btn {
      display: flex;
      align-items: center;
      gap: 6rpx;
      padding: 10rpx 20rpx;
      border-radius: $radius-md;
      font-size: $font-size-sm;
      font-weight: $font-weight-medium;
      transition: all 0.3s ease;
      
      .iconfont {
        font-size: 24rpx;
      }
      
      &.edit {
        background: #DBEAFE;
        color: #2563EB;
        
        &:active {
          background: #BFDBFE;
        }
      }
      
      &.record {
        background: #D1FAE5;
        color: #059669;
        
        &:active {
          background: #A7F3D0;
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

// 弹窗
.popup-content {
  background: #FFFFFF;
  border-radius: $radius-xl;
  width: 600rpx;
  max-height: 80vh;
  overflow-y: auto;
  
  .popup-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: $spacing-md;
    border-bottom: 1rpx solid $bg-secondary;
    
    .popup-title {
      font-size: $font-size-lg;
      font-weight: $font-weight-bold;
      color: $text-primary;
    }
    
    .popup-close {
      font-size: 48rpx;
      color: $text-tertiary;
      line-height: 1;
    }
  }
  
  .popup-body {
    padding: $spacing-md;
  }
  
  .form-item {
    margin-bottom: $spacing-md;
    
    .form-label {
      font-size: $font-size-sm;
      color: $text-secondary;
      margin-bottom: 8rpx;
      display: block;
    }
    
    .form-input {
      background: $bg-primary;
      border-radius: $radius-lg;
      padding: 16rpx;
      font-size: $font-size-base;
      color: $text-primary;
    }
    
    .form-picker {
      display: flex;
      justify-content: space-between;
      align-items: center;
      background: $bg-primary;
      border-radius: $radius-lg;
      padding: 16rpx;
      font-size: $font-size-base;
      color: $text-primary;
    }
    
    .form-textarea {
      background: $bg-primary;
      border-radius: $radius-lg;
      padding: 16rpx;
      font-size: $font-size-base;
      color: $text-primary;
      min-height: 120rpx;
    }
  }
  
  .popup-footer {
    display: flex;
    gap: $spacing-md;
    padding: $spacing-md;
    border-top: 1rpx solid $bg-secondary;
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
