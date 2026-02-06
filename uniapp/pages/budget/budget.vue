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
        <text class="nav-title">预算管理</text>
        <view class="nav-right"></view>
      </view>
      
      <!-- 预算概览 -->
      <view class="budget-overview">
        <view class="overview-card">
          <view class="overview-header">
            <view class="header-left">
              <view class="icon-box blue">
                <text class="iconfont icon-wallet"></text>
              </view>
              <text class="overview-title">{{ budgetTitle }}</text>
            </view>
            <picker mode="date" :value="selectedDate" :fields="dateFields" @change="onDateChange">
              <view class="date-picker">
                <text class="date-text">{{ selectedDate }}</text>
                <text class="iconfont icon-calendar" style="margin-left: 8rpx; font-size: 24rpx;"></text>
              </view>
            </picker>
          </view>
          
          <view class="amount-display">
            <text class="amount-label">预算金额</text>
            <view class="amount-value">
              <text class="currency">¥</text>
              <input 
                class="amount-input" 
                type="digit" 
                v-model="budgetAmount" 
                placeholder="0.00"
                @input="handleAmountInput"
              />
            </view>
          </view>
        </view>
      </view>
    </view>
    
    <!-- 内容区域 -->
    <view class="content-section">
      <!-- 类型切换标签 -->
      <view class="tab-bar">
        <view 
          class="tab-item" 
          :class="{ active: activeTab === 'monthly' }"
          @click="switchTab('monthly')"
        >
          <text class="iconfont icon-calendar" style="margin-right: 8rpx;"></text>
          <text class="tab-text">月度</text>
        </view>
        <view 
          class="tab-item" 
          :class="{ active: activeTab === 'quarterly' }"
          @click="switchTab('quarterly')"
        >
          <text class="iconfont icon-layers" style="margin-right: 8rpx;"></text>
          <text class="tab-text">季度</text>
        </view>
        <view 
          class="tab-item" 
          :class="{ active: activeTab === 'yearly' }"
          @click="switchTab('yearly')"
        >
          <text class="iconfont icon-bar-chart-2" style="margin-right: 8rpx;"></text>
          <text class="tab-text">年度</text>
        </view>
      </view>
      
      <!-- 预算进度 -->
      <view class="card">
        <view class="card-header">
          <view class="header-left">
            <view class="icon-box blue icon-box-sm">
              <text class="iconfont icon-activity"></text>
            </view>
            <text class="header-title">使用进度</text>
          </view>
          <view class="header-right">
            <text class="percentage-badge" :class="budgetStatus">{{ percentage }}%</text>
          </view>
        </view>
        
        <view class="progress-section">
          <view class="progress-bar-large">
            <view class="progress-fill-large" :class="budgetStatus" :style="{ width: percentage + '%' }"></view>
          </view>
          
          <view class="progress-stats">
            <view class="stat-block">
              <view class="stat-icon blue">
                <text class="iconfont icon-trend-up"></text>
              </view>
              <view class="stat-info">
                <text class="stat-label">已使用</text>
                <text class="stat-value">¥{{ currentAmount }}</text>
              </view>
            </view>
            <view class="stat-divider"></view>
            <view class="stat-block">
              <view class="stat-icon" :class="budgetStatus === 'danger' ? 'red' : 'green'">
                <text class="iconfont" :class="budgetStatus === 'danger' ? 'icon-trend-down' : 'icon-trend-up'"></text>
              </view>
              <view class="stat-info">
                <text class="stat-label">剩余</text>
                <text class="stat-value">¥{{ remainingAmount }}</text>
              </view>
            </view>
          </view>
        </view>
        
        <!-- 状态提示 -->
        <view class="status-card" :class="budgetStatus">
          <view class="status-icon">
            <text class="iconfont" :class="statusIcon"></text>
          </view>
          <view class="status-info">
            <text class="status-title">{{ statusTitle }}</text>
            <text class="status-desc">{{ statusDesc }}</text>
          </view>
        </view>
      </view>
      
      <!-- 预算统计 -->
      <view class="card">
        <view class="card-header">
          <view class="header-left">
            <view class="icon-box green icon-box-sm">
              <text class="iconfont icon-pie-chart"></text>
            </view>
            <text class="header-title">预算统计</text>
          </view>
        </view>
        
        <view class="stats-grid">
          <view class="stat-card blue">
            <view class="stat-icon-bg">
              <text class="iconfont icon-calendar"></text>
            </view>
            <view class="stat-content">
              <text class="stat-label">本月</text>
              <text class="stat-value">¥{{ stats.monthly.used }}</text>
              <view class="stat-progress">
                <view class="stat-progress-bar">
                  <view class="stat-progress-fill" :style="{ width: stats.monthly.percentage + '%' }"></view>
                </view>
                <text class="stat-percent">{{ stats.monthly.percentage }}%</text>
              </view>
            </view>
          </view>
          
          <view class="stat-card purple">
            <view class="stat-icon-bg purple">
              <text class="iconfont icon-layers"></text>
            </view>
            <view class="stat-content">
              <text class="stat-label">本季度</text>
              <text class="stat-value">¥{{ stats.quarterly.used }}</text>
              <view class="stat-progress">
                <view class="stat-progress-bar">
                  <view class="stat-progress-fill purple" :style="{ width: stats.quarterly.percentage + '%' }"></view>
                </view>
                <text class="stat-percent">{{ stats.quarterly.percentage }}%</text>
              </view>
            </view>
          </view>
          
          <view class="stat-card orange">
            <view class="stat-icon-bg orange">
              <text class="iconfont icon-bar-chart-2"></text>
            </view>
            <view class="stat-content">
              <text class="stat-label">本年</text>
              <text class="stat-value">¥{{ stats.yearly.used }}</text>
              <view class="stat-progress">
                <view class="stat-progress-bar">
                  <view class="stat-progress-fill orange" :style="{ width: stats.yearly.percentage + '%' }"></view>
                </view>
                <text class="stat-percent">{{ stats.yearly.percentage }}%</text>
              </view>
            </view>
          </view>
        </view>
      </view>
      
      <!-- 保存按钮 -->
      <view style="padding: 40rpx 0;">
        <button class="btn-primary" :class="{ 'loading': loading }" @click="handleSave" :disabled="loading">
          <text v-if="!loading" class="iconfont icon-save" style="margin-right: 12rpx;"></text>
          <text v-if="!loading">保存预算</text>
          <view v-else class="loading-spinner"></view>
        </button>
      </view>
    </view>
  </view>
</template>

<script>
import budgetApi from '@/api/budget'

export default {
  data() {
    return {
      activeTab: 'monthly',
      selectedDate: '',
      budgetAmount: '',
      currentAmount: '0.00',
      budgetTotal: '2000.00',
      loading: false,
      stats: {
        monthly: { budget: 2000, used: 0, percentage: 0 },
        quarterly: { budget: 5000, used: 0, percentage: 0 },
        yearly: { budget: 20000, used: 0, percentage: 0 }
      }
    }
  },
  
  computed: {
    budgetTitle() {
      const titles = {
        monthly: '月度预算',
        quarterly: '季度预算',
        yearly: '年度预算'
      }
      return titles[this.activeTab]
    },
    
    dateFields() {
      const fields = {
        monthly: 'month',
        quarterly: 'month',
        yearly: 'year'
      }
      return fields[this.activeTab]
    },
    
    percentage() {
      if (!this.budgetTotal || parseFloat(this.budgetTotal) === 0) {
        return 0
      }
      const pct = ((parseFloat(this.currentAmount) / parseFloat(this.budgetTotal)) * 100).toFixed(1)
      return parseFloat(pct) > 100 ? 100 : pct
    },
    
    remainingAmount() {
      const remaining = parseFloat(this.budgetTotal) - parseFloat(this.currentAmount)
      return remaining > 0 ? remaining.toFixed(2) : '0.00'
    },
    
    budgetStatus() {
      const pct = parseFloat(this.percentage)
      if (pct >= 100) return 'danger'
      if (pct >= 80) return 'warning'
      return 'normal'
    },
    
    statusIcon() {
      const status = this.budgetStatus
      const icons = {
        normal: 'icon-check-circle',
        warning: 'icon-alert-triangle',
        danger: 'icon-x-circle'
      }
      return icons[status]
    },
    
    statusTitle() {
      const status = this.budgetStatus
      const texts = {
        normal: '预算使用正常',
        warning: '即将超支',
        danger: '已超支'
      }
      return texts[status]
    },
    
    statusDesc() {
      const status = this.budgetStatus
      const texts = {
        normal: '您的预算控制得很好，继续保持！',
        warning: '注意控制支出，即将超出预算',
        danger: '已经超出预算，请合理规划支出'
      }
      return texts[status]
    }
  },
  
  onLoad() {
    this.initDate()
    this.loadBudget()
    this.loadStatistics()
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
    
    initDate() {
      const now = new Date()
      const year = now.getFullYear()
      const month = String(now.getMonth() + 1).padStart(2, '0')
      this.selectedDate = `${year}-${month}`
    },
    
    switchTab(tab) {
      this.activeTab = tab
      this.loadBudget()
    },
    
    onDateChange(e) {
      this.selectedDate = e.detail.value
      this.loadBudget()
    },
    
    handleAmountInput(e) {
      let value = e.detail.value
      // 限制小数点后两位
      if (value.includes('.')) {
        const parts = value.split('.')
        if (parts[1] && parts[1].length > 2) {
          value = parts[0] + '.' + parts[1].slice(0, 2)
          this.budgetAmount = value
        }
      }
    },
    
    async loadBudget() {
      try {
        const [year, month] = this.selectedDate.split('-')
        
        const params = {
          budgetType: this.activeTab,
          budgetYear: parseInt(year)
        }
        
        if (this.activeTab === 'monthly') {
          params.budgetMonth = parseInt(month)
        } else if (this.activeTab === 'quarterly') {
          params.budgetQuarter = Math.ceil(parseInt(month) / 3)
        }
        
        const res = await budgetApi.getBudget(params)
        
        if (res.code === 1 && res.data) {
          this.budgetAmount = res.data.budgetAmount.toString()
          this.currentAmount = res.data.currentAmount.toFixed(2)
          this.budgetTotal = res.data.budgetAmount.toFixed(2)
        }
      } catch (error) {
        console.error('加载预算失败:', error)
      }
    },
    
    async loadStatistics() {
      try {
        const now = new Date()
        const res = await budgetApi.getBudgetStatistics({
          budgetYear: now.getFullYear()
        })
        
        if (res.code === 1) {
          this.stats = res.data
        }
      } catch (error) {
        console.error('加载统计失败:', error)
      }
    },
    
    async handleSave() {
      if (!this.budgetAmount) {
        uni.showToast({
          title: '请输入预算金额',
          icon: 'none'
        })
        return
      }
      
      if (parseFloat(this.budgetAmount) <= 0) {
        uni.showToast({
          title: '预算金额必须大于0',
          icon: 'none'
        })
        return
      }
      
      this.loading = true
      
      try {
        const [year, month] = this.selectedDate.split('-')
        
        const params = {
          budgetType: this.activeTab,
          budgetAmount: parseFloat(this.budgetAmount),
          budgetYear: parseInt(year)
        }
        
        if (this.activeTab === 'monthly') {
          params.budgetMonth = parseInt(month)
        } else if (this.activeTab === 'quarterly') {
          params.budgetQuarter = Math.ceil(parseInt(month) / 3)
        }
        
        const res = await budgetApi.setBudget(params)
        
        if (res.code === 1) {
          uni.showToast({
            title: '保存成功',
            icon: 'success'
          })
          
          this.budgetTotal = this.budgetAmount
          this.loadStatistics()
        }
      } catch (error) {
        console.error('保存预算失败:', error)
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/styles/theme.scss';
@import '@/styles/components.scss';

// 预算概览
.budget-overview {
  padding: 0 $page-padding 30rpx;
  
  .overview-card {
    background: rgba(255, 255, 255, 0.95);
    border-radius: $radius-xl;
    padding: $spacing-lg;
    box-shadow: $shadow-md;
    backdrop-filter: blur(10rpx);
    
    .overview-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: $spacing-lg;
      
      .header-left {
        display: flex;
        align-items: center;
        gap: $spacing-sm;
      }
      
      .overview-title {
        font-size: $font-size-lg;
        font-weight: $font-weight-bold;
        color: $text-primary;
      }
      
      .date-picker {
        display: flex;
        align-items: center;
        background: $bg-secondary;
        padding: 16rpx 24rpx;
        border-radius: $radius-lg;
        
        .date-text {
          font-size: $font-size-sm;
          color: $text-primary;
          font-weight: $font-weight-medium;
        }
      }
    }
    
    .amount-display {
      .amount-label {
        font-size: $font-size-sm;
        color: $text-tertiary;
        margin-bottom: $spacing-xs;
        display: block;
      }
      
      .amount-value {
        display: flex;
        align-items: baseline;
        
        .currency {
          font-size: $font-size-lg;
          font-weight: $font-weight-bold;
          color: $primary-color;
          margin-right: 12rpx;
        }
        
        .amount-input {
          flex: 1;
          font-size: 72rpx;
          font-weight: $font-weight-bold;
          color: $text-primary;
          height: 90rpx;
          line-height: 90rpx;
        }
      }
    }
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
    
    &.active {
      background: $primary-gradient;
      color: #FFFFFF;
      font-weight: $font-weight-medium;
      box-shadow: 0 4rpx 12rpx rgba(102, 126, 234, 0.4);
    }
  }
}

// 进度区域
.progress-section {
  .progress-bar-large {
    width: 100%;
    height: 20rpx;
    background: $bg-secondary;
    border-radius: 10rpx;
    overflow: hidden;
    margin-bottom: $spacing-lg;
    
    .progress-fill-large {
      height: 100%;
      border-radius: 10rpx;
      transition: all 0.3s ease;
      
      &.normal {
        background: linear-gradient(90deg, #10B981 0%, #059669 100%);
      }
      
      &.warning {
        background: linear-gradient(90deg, #F59E0B 0%, #D97706 100%);
      }
      
      &.danger {
        background: linear-gradient(90deg, #EF4444 0%, #DC2626 100%);
      }
    }
  }
  
  .progress-stats {
    display: flex;
    align-items: center;
    gap: $spacing-lg;
    
    .stat-block {
      flex: 1;
      display: flex;
      align-items: center;
      gap: $spacing-sm;
      
      .stat-icon {
        width: 72rpx;
        height: 72rpx;
        border-radius: $radius-full;
        display: flex;
        align-items: center;
        justify-content: center;
        
        &.blue {
          background: rgba(102, 126, 234, 0.1);
          
          .iconfont {
            color: $primary-color;
          }
        }
        
        &.green {
          background: rgba(16, 185, 129, 0.1);
          
          .iconfont {
            color: $success-color;
          }
        }
        
        &.red {
          background: rgba(239, 68, 68, 0.1);
          
          .iconfont {
            color: $danger-color;
          }
        }
        
        .iconfont {
          font-size: 36rpx;
        }
      }
      
      .stat-info {
        flex: 1;
        
        .stat-label {
          font-size: $font-size-xs;
          color: $text-tertiary;
          display: block;
          margin-bottom: 4rpx;
        }
        
        .stat-value {
          font-size: $font-size-md;
          font-weight: $font-weight-bold;
          color: $text-primary;
        }
      }
    }
    
    .stat-divider {
      width: 2rpx;
      height: 60rpx;
      background: $bg-secondary;
    }
  }
}

// 百分比徽章
.percentage-badge {
  font-size: $font-size-sm;
  font-weight: $font-weight-bold;
  padding: 8rpx 20rpx;
  border-radius: $radius-full;
  
  &.normal {
    background: rgba(16, 185, 129, 0.1);
    color: $success-color;
  }
  
  &.warning {
    background: rgba(245, 158, 11, 0.1);
    color: $warning-dark;
  }
  
  &.danger {
    background: rgba(239, 68, 68, 0.1);
    color: $danger-color;
  }
}

// 状态卡片
.status-card {
  display: flex;
  align-items: center;
  gap: $spacing-md;
  padding: $spacing-lg;
  border-radius: $radius-lg;
  margin-top: $spacing-lg;
  
  &.normal {
    background: linear-gradient(135deg, rgba(16, 185, 129, 0.1) 0%, rgba(5, 150, 105, 0.1) 100%);
    
    .status-icon {
      background: $success-color;
    }
    
    .status-title {
      color: $success-dark;
    }
  }
  
  &.warning {
    background: linear-gradient(135deg, rgba(245, 158, 11, 0.1) 0%, rgba(217, 119, 6, 0.1) 100%);
    
    .status-icon {
      background: $warning-color;
    }
    
    .status-title {
      color: $warning-dark;
    }
  }
  
  &.danger {
    background: linear-gradient(135deg, rgba(239, 68, 68, 0.1) 0%, rgba(220, 38, 38, 0.1) 100%);
    
    .status-icon {
      background: $danger-color;
    }
    
    .status-title {
      color: $danger-dark;
    }
  }
  
  .status-icon {
    width: 80rpx;
    height: 80rpx;
    border-radius: $radius-full;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
    
    .iconfont {
      font-size: 40rpx;
      color: #FFFFFF;
    }
  }
  
  .status-info {
    flex: 1;
    
    .status-title {
      font-size: $font-size-base;
      font-weight: $font-weight-bold;
      margin-bottom: 4rpx;
      display: block;
    }
    
    .status-desc {
      font-size: $font-size-sm;
      color: $text-tertiary;
    }
  }
}

// 统计网格
.stats-grid {
  display: flex;
  flex-direction: column;
  gap: $spacing-md;
  
  .stat-card {
    display: flex;
    align-items: center;
    gap: $spacing-md;
    padding: $spacing-lg;
    border-radius: $radius-lg;
    background: linear-gradient(135deg, rgba(102, 126, 234, 0.05) 0%, rgba(79, 70, 229, 0.05) 100%);
    border: 2rpx solid rgba(102, 126, 234, 0.1);
    
    &.purple {
      background: linear-gradient(135deg, rgba(139, 92, 246, 0.05) 0%, rgba(124, 58, 237, 0.05) 100%);
      border-color: rgba(139, 92, 246, 0.1);
      
      .stat-icon-bg {
        background: $purple-gradient;
      }
    }
    
    &.orange {
      background: linear-gradient(135deg, rgba(245, 158, 11, 0.05) 0%, rgba(217, 119, 6, 0.05) 100%);
      border-color: rgba(245, 158, 11, 0.1);
      
      .stat-icon-bg {
        background: $warning-gradient;
      }
    }
    
    .stat-icon-bg {
      width: 88rpx;
      height: 88rpx;
      border-radius: $radius-full;
      background: $primary-gradient;
      display: flex;
      align-items: center;
      justify-content: center;
      flex-shrink: 0;
      
      .iconfont {
        font-size: 40rpx;
        color: #FFFFFF;
      }
      
      &.purple {
        background: $purple-gradient;
      }
      
      &.orange {
        background: $warning-gradient;
      }
    }
    
    .stat-content {
      flex: 1;
      
      .stat-label {
        font-size: $font-size-xs;
        color: $text-tertiary;
        margin-bottom: 4rpx;
        display: block;
      }
      
      .stat-value {
        font-size: $font-size-lg;
        font-weight: $font-weight-bold;
        color: $text-primary;
        margin-bottom: $spacing-xs;
        display: block;
      }
      
      .stat-progress {
        display: flex;
        align-items: center;
        gap: $spacing-sm;
        
        .stat-progress-bar {
          flex: 1;
          height: 8rpx;
          background: rgba(0, 0, 0, 0.05);
          border-radius: 4rpx;
          overflow: hidden;
          
          .stat-progress-fill {
            height: 100%;
            background: $primary-gradient;
            border-radius: 4rpx;
            
            &.purple {
              background: $purple-gradient;
            }
            
            &.orange {
              background: $warning-gradient;
            }
          }
        }
        
        .stat-percent {
          font-size: $font-size-xs;
          color: $primary-color;
          font-weight: $font-weight-medium;
          min-width: 60rpx;
          text-align: right;
        }
      }
    }
  }
}
</style>
