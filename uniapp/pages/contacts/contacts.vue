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
        <text class="nav-title">{{ selectMode ? '选择联系人' : '联系人' }}</text>
        <view class="nav-right" @click="goToAdd">
          <text class="iconfont icon-plus"></text>
        </view>
      </view>
      
      <!-- 搜索框 -->
      <view class="search-section">
        <view class="search-box">
          <text class="iconfont icon-search"></text>
          <input 
            class="search-input" 
            type="text" 
            v-model="searchKeyword" 
            placeholder="搜索联系人姓名"
            @confirm="handleSearch"
          />
          <text class="clear-btn" v-if="searchKeyword" @click="clearSearch">×</text>
        </view>
        <view class="search-btn" @click="handleSearch">
          <text>查询</text>
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
          <view class="tab-badge" v-if="total > 0">{{ total }}</view>
        </view>
        <view 
          class="tab-item" 
          :class="{ active: activeTab === 'favorite' }"
          @click="switchTab('favorite')"
        >
          <text class="tab-text">常用</text>
          <text class="iconfont icon-star-fill" style="font-size: 24rpx; margin-left: 8rpx;"></text>
        </view>
      </view>
      
      <!-- 联系人列表 -->
      <scroll-view scroll-y class="contact-list" @scrolltolower="loadMore">
        <view class="contact-card" v-for="(item, index) in contacts" :key="index" @click="goToDetail(item.id)">
          <view class="card-main">
            <image class="contact-avatar" :src="item.avatarUrl || '/static/default-avatar.png'" mode="aspectFill" @error="item.avatarUrl = '/static/default-avatar.png'"></image>
            <view class="contact-info">
              <view class="info-header">
                <text class="contact-name">{{ item.name }}</text>
                <view class="relation-tag" :class="getRelationClass(item.relationType)">
                  {{ item.relationType }}
                </view>
              </view>
              <view class="info-detail">
                <text class="detail-item" v-if="item.phone">
                  <text class="iconfont icon-phone" style="font-size: 24rpx; margin-right: 8rpx;"></text>
                  {{ item.phone }}
                </text>
                <text class="detail-item" v-if="item.birthday">
                  <text class="iconfont icon-calendar" style="font-size: 24rpx; margin-right: 8rpx;"></text>
                  {{ item.birthday }}
                </text>
              </view>
              <text class="contact-remark" v-if="item.remark">{{ item.remark }}</text>
            </view>
          </view>
          <view class="card-actions">
            <view class="action-btn favorite" :class="{ active: item.isFavorite }" @click.stop="toggleFavorite(item)">
              <text class="iconfont" :class="item.isFavorite ? 'icon-star-fill' : 'icon-star'"></text>
            </view>
            <view class="action-btn more" @click.stop="showMoreActions(item)">
              <text class="iconfont icon-more"></text>
            </view>
          </view>
        </view>
        
        <!-- 空状态 -->
        <view class="empty-state" v-if="contacts.length === 0 && !loading">
          <view class="empty-icon">
            <text class="iconfont icon-users"></text>
          </view>
          <text class="empty-text">暂无联系人</text>
          <text class="empty-tip">点击右上角添加您的第一个联系人</text>
          <button class="btn-primary btn-sm" style="margin-top: 40rpx; width: 240rpx;" @click="goToAdd">
            <text class="iconfont icon-plus" style="margin-right: 8rpx;"></text>
            添加联系人
          </button>
        </view>
        
        <!-- 加载状态 -->
        <view class="loading-more" v-if="loading">
          <view class="loading-spinner-dark"></view>
          <text class="loading-text">加载中...</text>
        </view>
        
        <!-- 没有更多 -->
        <view class="no-more" v-if="!hasMore && contacts.length > 0">
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
import contactApi from '@/api/contact'

export default {
  data() {
    return {
      searchKeyword: '',
      activeTab: 'all',
      contacts: [],
      page: 1,
      size: 20,
      total: 0,
      loading: false,
      hasMore: true,
      selectMode: false,
      batchMode: false
    }
  },
  
  onLoad(options) {
    if (options.select === 'true') {
      this.selectMode = true
    }
    if (options.batch === 'true') {
      this.batchMode = true
    }
    this.loadContacts()
  },
  
  onShow() {
    if (this.page === 1) {
      this.loadContacts()
    }
  },
  
  methods: {
    getRelationClass(relation) {
      const map = {
        '亲戚': 'family',
        '朋友': 'friend',
        '同事': 'colleague',
        '同学': 'classmate',
        '邻居': 'neighbor',
        '其他': 'other'
      }
      return map[relation] || 'default'
    },
    
    switchTab(tab) {
      this.activeTab = tab
      this.loadContacts(true)
    },
    
    async loadContacts(reset = true) {
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
        
        if (this.searchKeyword) {
          params.name = this.searchKeyword
        }
        
        if (this.activeTab === 'favorite') {
          params.isFavorite = 1
        }
        
        const res = await contactApi.getContactList(params)
        
        if (res.code === 1) {
          if (reset) {
            this.contacts = res.data.list
          } else {
            this.contacts = [...this.contacts, ...res.data.list]
          }
          
          this.total = res.data.total
          this.hasMore = this.contacts.length < this.total
        }
      } catch (error) {
        console.error('加载联系人失败:', error)
      } finally {
        this.loading = false
      }
    },
    
    loadMore() {
      if (this.hasMore && !this.loading) {
        this.page++
        this.loadContacts(false)
      }
    },
    
    handleSearch() {
      this.loadContacts(true)
    },
    
    clearSearch() {
      this.searchKeyword = ''
      this.loadContacts(true)
    },
    
    async toggleFavorite(contact) {
      try {
        const res = await contactApi.updateContact({
          id: contact.id,
          name: contact.name,
          relationType: contact.relationType,
          avatarUrl: contact.avatarUrl,
          phone: contact.phone,
          email: contact.email,
          birthday: contact.birthday,
          address: contact.address,
          remark: contact.remark,
          isFavorite: contact.isFavorite ? 0 : 1
        })
        
        if (res.code === 1) {
          contact.isFavorite = contact.isFavorite ? 0 : 1
          
          uni.showToast({
            title: contact.isFavorite ? '已设为常用' : '已取消常用',
            icon: 'none'
          })
        }
      } catch (error) {
        console.error('操作失败:', error)
      }
    },
    
    showMoreActions(item) {
      uni.showActionSheet({
        itemList: ['编辑', '删除'],
        success: (res) => {
          if (res.tapIndex === 0) {
            uni.navigateTo({
              url: `/pages/contact-detail/contact-detail?id=${item.id}`
            })
          } else if (res.tapIndex === 1) {
            this.deleteContact(item)
          }
        }
      })
    },
    
    deleteContact(item) {
      uni.showModal({
        title: '确认删除',
        content: `确定要删除联系人 "${item.name}" 吗？`,
        confirmColor: '#EF4444',
        success: (res) => {
          if (res.confirm) {
            contactApi.deleteContact(item.id).then(() => {
              uni.showToast({ title: '删除成功', icon: 'success' })
              this.loadContacts(true)
            }).catch(() => {
              uni.showToast({ title: '删除失败', icon: 'none' })
            })
          }
        }
      })
    },
    
    goToAdd() {
      uni.navigateTo({
        url: '/pages/contact-detail/contact-detail'
      })
    },
    
    goToDetail(id) {
      if (this.selectMode) {
        const pages = getCurrentPages()
        const prevPage = pages[pages.length - 2]
        const contact = this.contacts.find(c => c.id === id)
        
        if (this.batchMode && prevPage && prevPage.$vm && prevPage.$vm.handleSelectBatchContact) {
          // 批量记账模式
          prevPage.$vm.handleSelectBatchContact(contact)
          uni.navigateBack()
        } else if (prevPage && prevPage.$vm && prevPage.$vm.handleSelectContact) {
          // 单条记账模式
          prevPage.$vm.handleSelectContact(contact)
          uni.navigateBack()
        }
      } else {
        uni.navigateTo({
          url: `/pages/contact-detail/contact-detail?id=${id}`
        })
      }
    },
    
    goBack() {
      const pages = getCurrentPages()
      if (pages.length > 1) {
        uni.navigateBack()
      } else {
        uni.switchTab({
          url: '/pages/index/index'
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/styles/theme.scss';
@import '@/styles/components.scss';

// 搜索区域
.search-section {
  padding: 0 $page-padding $spacing-lg;
  display: flex;
  align-items: center;
  gap: $spacing-sm;
  
  .search-box {
    flex: 1;
    display: flex;
    align-items: center;
    background: rgba(255, 255, 255, 0.2);
    border-radius: $radius-xl;
    padding: 0 $spacing-md;
    height: 88rpx;
    backdrop-filter: blur(10rpx);
    
    .icon-search {
      font-size: 36rpx;
      color: rgba(255, 255, 255, 0.8);
      margin-right: $spacing-sm;
    }
    
    .search-input {
      flex: 1;
      font-size: $font-size-base;
      color: #FFFFFF;
      
      &::placeholder {
        color: rgba(255, 255, 255, 0.6);
      }
    }
    
    .clear-btn {
      font-size: 40rpx;
      color: rgba(255, 255, 255, 0.8);
      padding: 0 $spacing-sm;
    }
  }
  
  .search-btn {
    display: flex;
    align-items: center;
    justify-content: center;
    background: #FFFFFF;
    border-radius: $radius-xl;
    height: 88rpx;
    padding: 0 $spacing-lg;
    
    text {
      font-size: $font-size-base;
      color: #667eea;
      font-weight: 600;
    }
  }
}

// 标签栏
.tab-bar {
  display: flex;
  background: #FFFFFF;
  border-radius: $radius-xl;
  padding: 8rpx;
  margin-bottom: $spacing-md;
  box-shadow: $shadow-sm;
  
  .tab-item {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 20rpx 0;
    border-radius: $radius-lg;
    transition: all 0.3s ease;
    
    .tab-text {
      font-size: $font-size-base;
      color: $text-secondary;
      font-weight: $font-weight-medium;
    }
    
    .tab-badge {
      background: $primary-color;
      color: #FFFFFF;
      font-size: 20rpx;
      padding: 4rpx 12rpx;
      border-radius: 20rpx;
      margin-left: 8rpx;
    }
    
    &.active {
      background: $primary-gradient;
      
      .tab-text {
        color: #FFFFFF;
      }
    }
  }
}

// 联系人列表
.contact-list {
  height: calc(100vh - 400rpx);
}

// 联系人卡片
.contact-card {
  background: #FFFFFF;
  border-radius: $radius-xl;
  padding: $spacing-md;
  margin-bottom: $spacing-md;
  box-shadow: $shadow-sm;
  display: flex;
  align-items: center;
  transition: all 0.3s ease;
  
  &:active {
    transform: scale(0.98);
    box-shadow: $shadow-md;
  }
  
  .card-main {
    flex: 1;
    display: flex;
    align-items: center;
  }
  
  .contact-avatar {
    width: 100rpx;
    height: 100rpx;
    border-radius: $radius-full;
    margin-right: $spacing-md;
    border: 4rpx solid $bg-secondary;
  }
  
  .contact-info {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 8rpx;
  }
  
  .info-header {
    display: flex;
    align-items: center;
    gap: $spacing-sm;
  }
  
  .contact-name {
    font-size: $font-size-lg;
    font-weight: $font-weight-bold;
    color: $text-primary;
  }
  
  .relation-tag {
    font-size: 22rpx;
    padding: 4rpx 12rpx;
    border-radius: 20rpx;
    font-weight: $font-weight-medium;
    
    &.family {
      background: #FEF3C7;
      color: #D97706;
    }
    
    &.friend {
      background: #DBEAFE;
      color: #2563EB;
    }
    
    &.colleague {
      background: #D1FAE5;
      color: #059669;
    }
    
    &.classmate {
      background: #FCE7F3;
      color: #DB2777;
    }
    
    &.neighbor {
      background: #E0E7FF;
      color: #7C3AED;
    }
    
    &.other, &.default {
      background: $bg-secondary;
      color: $text-secondary;
    }
  }
  
  .info-detail {
    display: flex;
    align-items: center;
    gap: $spacing-md;
  }
  
  .detail-item {
    font-size: $font-size-sm;
    color: $text-secondary;
    display: flex;
    align-items: center;
  }
  
  .contact-remark {
    font-size: $font-size-sm;
    color: $text-tertiary;
  }
  
  .card-actions {
    display: flex;
    flex-direction: column;
    gap: $spacing-sm;
    margin-left: $spacing-sm;
  }
  
  .action-btn {
    width: 64rpx;
    height: 64rpx;
    border-radius: $radius-lg;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.3s ease;
    
    &.favorite {
      background: $bg-secondary;
      
      .iconfont {
        font-size: 32rpx;
        color: $text-tertiary;
      }
      
      &.active {
        background: #FEF3C7;
        
        .iconfont {
          color: #F59E0B;
        }
      }
    }
    
    &.more {
      background: $bg-secondary;
      
      .iconfont {
        font-size: 32rpx;
        color: $text-tertiary;
      }
    }
    
    &:active {
      transform: scale(0.9);
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

// 悬浮按钮
.fab-btn {
  position: fixed;
  bottom: 160rpx;
  right: 40rpx;
  width: 120rpx;
  height: 120rpx;
  border-radius: $radius-full;
  background: $primary-gradient;
  box-shadow: 0 12rpx 40rpx rgba(102, 126, 234, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 100;
  transition: all 0.3s ease;
  
  &:active {
    transform: scale(0.95);
  }
  
  .iconfont {
    font-size: 48rpx;
    color: #FFFFFF;
  }
}
</style>
