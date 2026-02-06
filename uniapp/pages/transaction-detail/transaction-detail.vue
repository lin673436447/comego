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
        <text class="nav-title">记录详情</text>
        <view class="nav-right">
          <view class="nav-action" @click="handleDelete">
            <text class="iconfont icon-trash-2"></text>
          </view>
        </view>
      </view>
      

    </view>
    
    <!-- 内容区域 -->
    <view class="content-section">
      <!-- 基本信息 -->
      <view class="card">
        <view class="card-header">
          <view class="header-left">
            <view class="icon-box blue icon-box-sm">
              <text class="iconfont icon-file-text"></text>
            </view>
            <text class="header-title">基本信息</text>
          </view>
        </view>
        
        <view class="info-list">
          <view class="info-item">
            <view class="item-left">
              <view class="item-icon blue">
                <text class="iconfont icon-user"></text>
              </view>
              <text class="item-label">联系人</text>
            </view>
            <view class="item-right">
                  <image class="contact-avatar-sm" :src="transaction.avatarUrl || '/static/default-avatar.png'" mode="aspectFill" @error="transaction.avatarUrl = '/static/default-avatar.png'"></image>
                  <text class="item-value">{{ transaction.contactName }}</text>
                </view>
              </view>

              <view class="info-item">
                <view class="item-left">
                  <view class="item-icon green">
                    <text class="iconfont icon-heart"></text>
                  </view>
                  <text class="item-label">关系</text>
                </view>
                <view class="item-right">
                  <view class="relation-tag" :class="getRelationClass(transaction.relationType)">
                    {{ transaction.relationType }}
                  </view>
                </view>
              </view>

              <view class="info-item">
                <view class="item-left">
                  <view class="item-icon orange">
                    <text class="iconfont icon-calendar"></text>
                  </view>
                  <text class="item-label">日期</text>
                </view>
                <view class="item-right">
                  <text class="item-value">{{ transaction.transactionDate }}</text>
                </view>
              </view>

              <view class="info-item">
                <view class="item-left">
                  <view class="item-icon green">
                    <text class="iconfont icon-exchange"></text>
                  </view>
                  <text class="item-label">收支</text>
                </view>
                <view class="item-right">
                  <view class="direction-tag" :class="transaction.transactionDirection">
                    {{ transaction.transactionDirection === 'income' ? '收入' : '支出' }}
                  </view>
                </view>
              </view>

              <view class="info-item">
                <view class="item-left">
                  <view class="item-icon purple">
                    <text class="iconfont icon-wallet"></text>
                  </view>
                  <text class="item-label">方式</text>
                </view>
                <view class="item-right">
                  <view class="mode-tag" :class="transaction.transactionMode">
                    {{ transaction.transactionMode === 'cash' ? '现金' : '礼物' }}
                  </view>
                </view>
              </view>

              <view class="info-item">
                <view class="item-left">
                  <view class="item-icon red">
                    <text class="iconfont icon-currency"></text>
                  </view>
                  <text class="item-label">金额</text>
                </view>
                <view class="item-right">
                  <text class="item-value amount" :class="{ income: transaction.transactionDirection === 'income', expense: transaction.transactionDirection === 'expense' }">
                    {{ transaction.transactionDirection === 'income' ? '+' : '-' }}¥{{ getAmount(transaction) }}
                  </text>
                </view>
              </view>

              <view class="info-item" v-if="transaction.transactionMode === 'gift' && transaction.giftName">
                <view class="item-left">
                  <view class="item-icon pink">
                    <text class="iconfont icon-gift"></text>
                  </view>
                  <text class="item-label">礼物</text>
                </view>
                <view class="item-right">
                  <text class="item-value">{{ transaction.giftName }}</text>
                </view>
              </view>

              <view class="info-item" v-if="transaction.transactionMode === 'gift' && transaction.giftValue">
                <view class="item-left">
                  <view class="item-icon yellow">
                    <text class="iconfont icon-tag"></text>
                  </view>
                  <text class="item-label">价值</text>
                </view>
                <view class="item-right">
                  <text class="item-value">¥{{ transaction.giftValue }}</text>
                </view>
              </view>
        </view>
      </view>
      
      <!-- 备注信息 -->
      <view class="card" v-if="transaction.remark">
        <view class="card-header">
          <view class="header-left">
            <view class="icon-box purple icon-box-sm">
              <text class="iconfont icon-message-square"></text>
            </view>
            <text class="header-title">备注</text>
          </view>
        </view>
        
        <view class="remark-content">
          <text class="remark-text">{{ transaction.remark }}</text>
        </view>
      </view>
      
      <!-- 记录信息 -->
      <view class="card">
        <view class="card-header">
          <view class="header-left">
            <view class="icon-box orange icon-box-sm">
              <text class="iconfont icon-clock"></text>
            </view>
            <text class="header-title">记录信息</text>
          </view>
        </view>
        
        <view class="info-list">
          <view class="info-item">
            <view class="item-left">
              <view class="item-icon gray">
                <text class="iconfont icon-clock"></text>
              </view>
              <text class="item-label">创建时间</text>
            </view>
            <view class="item-right">
              <text class="item-value text-gray">{{ transaction.createdAt }}</text>
            </view>
          </view>
          
          <view class="info-item">
            <view class="item-left">
              <view class="item-icon gray">
                <text class="iconfont icon-edit"></text>
              </view>
              <text class="item-label">更新时间</text>
            </view>
            <view class="item-right">
              <text class="item-value text-gray">{{ transaction.updatedAt || transaction.createdAt }}</text>
            </view>
          </view>
        </view>
      </view>
      
      <!-- 操作按钮 -->
      <view style="padding: 40rpx 0;" v-if="isEditMode">
        <button class="btn-primary" @click="goToEdit">
          <text class="iconfont icon-edit" style="margin-right: 12rpx;"></text>
          编辑记录
        </button>
      </view>
    </view>
  </view>
</template>

<script>
import transactionApi from '@/api/transaction'

export default {
  data() {
    return {
      transactionId: null,
      isEditMode: false,
      transaction: {
        amount: '0.00',
        contactName: '',
        avatarUrl: '',
        relationType: '',
        transactionDate: '',
        remark: '',
        createdAt: '',
        updatedAt: '',
        transactionDirection: 'expense',
        transactionMode: 'cash',
        giftName: '',
        giftValue: ''
      }
    }
  },
  
  onLoad(options) {
    if (options.id) {
      this.transactionId = options.id
    }
    
    if (options.edit === 'true') {
      this.isEditMode = true
    }
    
    if (this.transactionId) {
      this.loadTransactionDetail()
    }
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
    
    formatDateTime(dateStr) {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      const seconds = String(date.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    },

    async loadTransactionDetail() {
      try {
        const res = await transactionApi.getTransactionDetail({ id: this.transactionId })

        if (res.code === 1 && res.data) {
          const transaction = res.data
          this.transaction = {
            amount: transaction.amount ? transaction.amount.toFixed(2) : '0.00',
            contactName: transaction.contactName,
            avatarUrl: transaction.avatarUrl,
            relationType: transaction.relationType,
            transactionDate: transaction.transactionDate,
            remark: transaction.remark || '',
            createdAt: this.formatDateTime(transaction.createdAt),
            updatedAt: this.formatDateTime(transaction.updatedAt),
            transactionDirection: transaction.transactionDirection || 'expense',
            transactionMode: transaction.transactionMode || 'cash',
            giftName: transaction.giftName || '',
            giftValue: transaction.giftValue ? transaction.giftValue.toFixed(2) : '0.00'
          }
        }
      } catch (error) {
        console.error('加载记录详情失败:', error)
      }
    },
    
    getRelationClass(type) {
      const classMap = {
        '亲戚': 'family',
        '朋友': 'friend',
        '同事': 'colleague',
        '同学': 'classmate',
        '其他': 'other'
      }
      return classMap[type] || 'other'
    },
    
    getAmount(transaction) {
      if (transaction.transactionMode === 'cash') {
        return transaction.amount || '0.00'
      } else {
        return transaction.giftValue || '0.00'
      }
    },
    
    goToEdit() {
      uni.navigateTo({
        url: `/pages/record/record?edit=true&id=${this.transactionId}`
      })
    },
    
    handleDelete() {
      uni.showModal({
        title: '确认删除',
        content: '确定要删除这条记录吗？删除后无法恢复',
        confirmColor: '#EF4444',
        success: async (res) => {
          if (res.confirm) {
            try {
              const deleteRes = await transactionApi.deleteTransaction({ id: this.transactionId })
                
              if (deleteRes.code === 1) {
                uni.showToast({
                  title: '删除成功',
                  icon: 'success'
                })
                
                setTimeout(() => {
                  uni.navigateBack()
                }, 1500)
              }
            } catch (error) {
              console.error('删除失败:', error)
            }
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

// 信息列表
.info-list {
  .info-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: $spacing-md 0;
    border-bottom: 2rpx solid $bg-secondary;
    
    &:last-child {
      border-bottom: none;
    }
    
    .item-left {
      display: flex;
      align-items: center;
      gap: $spacing-sm;
      
      .item-icon {
        width: 48rpx;
        height: 48rpx;
        border-radius: $radius-md;
        display: flex;
        align-items: center;
        justify-content: center;
        
        .iconfont {
          font-size: 28rpx;
          color: #FFFFFF;
        }
        
        &.blue {
          background: $primary-gradient;
        }
        
        &.green {
          background: $success-gradient;
        }
        
        &.orange {
          background: $warning-gradient;
        }
        
        &.purple {
          background: $purple-gradient;
        }
        
        &.gray {
          background: linear-gradient(135deg, #94A3B8 0%, #64748B 100%);
        }
        
        &.green {
          background: $success-gradient;
        }
        
        &.purple {
          background: $purple-gradient;
        }
        
        &.pink {
          background: linear-gradient(135deg, #EC4899 0%, #DB2777 100%);
        }
        
        &.yellow {
          background: $warning-gradient;
        }
      }
      
      .item-label {
        font-size: $font-size-base;
        color: $text-primary;
        font-weight: $font-weight-medium;
      }
    }
    
    .item-right {
      display: flex;
      align-items: center;
      gap: $spacing-sm;
      
      .contact-avatar-sm {
        width: 48rpx;
        height: 48rpx;
        border-radius: $radius-full;
        background: $bg-secondary;
      }
      
      .item-value {
        font-size: $font-size-base;
        color: $text-primary;
        font-weight: $font-weight-medium;

        &.text-gray {
          color: $text-tertiary;
        }

        &.amount {
          font-size: 36rpx;
          font-weight: $font-weight-bold;

          &.income {
            color: #EF4444;
          }

          &.expense {
            color: #10B981;
          }
        }
      }
      
      .relation-tag {
        padding: 8rpx 20rpx;
        border-radius: $radius-full;
        font-size: $font-size-xs;
        font-weight: $font-weight-medium;
        
        &.family {
          background: rgba(102, 126, 234, 0.1);
          color: $primary-color;
        }
        
        &.friend {
          background: rgba(16, 185, 129, 0.1);
          color: $success-color;
        }
        
        &.colleague {
          background: rgba(245, 158, 11, 0.1);
          color: $warning-dark;
        }
        
        &.classmate {
          background: rgba(139, 92, 246, 0.1);
          color: $purple-color;
        }
        
        &.other {
          background: rgba(100, 116, 139, 0.1);
          color: #64748B;
        }
      }
      
      .direction-tag {
        padding: 8rpx 20rpx;
        border-radius: $radius-full;
        font-size: $font-size-xs;
        font-weight: $font-weight-medium;
        
        &.income {
          background: rgba(16, 185, 129, 0.1);
          color: $success-color;
        }
        
        &.expense {
          background: rgba(239, 68, 68, 0.1);
          color: $danger-color;
        }
      }
      
      .mode-tag {
        padding: 8rpx 20rpx;
        border-radius: $radius-full;
        font-size: $font-size-xs;
        font-weight: $font-weight-medium;
        
        &.cash {
          background: rgba(102, 126, 234, 0.1);
          color: $primary-color;
        }
        
        &.gift {
          background: rgba(236, 72, 153, 0.1);
          color: #EC4899;
        }
      }
    }
  }
}

// 备注内容
.remark-content {
  padding: $spacing-md;
  background: $bg-secondary;
  border-radius: $radius-lg;
  
  .remark-text {
    font-size: $font-size-base;
    color: $text-secondary;
    line-height: 1.6;
  }
}

// 导航操作按钮
.nav-action {
  width: 64rpx;
  height: 64rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: $radius-full;
  background: rgba(239, 68, 68, 0.2);
  
  .iconfont {
    font-size: 32rpx;
    color: #FFFFFF;
  }
}
</style>
