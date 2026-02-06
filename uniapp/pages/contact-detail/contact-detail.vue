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
        <text class="nav-title">{{ contactId ? '编辑联系人' : '添加联系人' }}</text>
        <view class="nav-right">
          <view class="nav-action" @click="handleDelete" v-if="contactId">
            <text class="iconfont icon-trash-2"></text>
          </view>
        </view>
      </view>
      
      <!-- 头像区域 -->
      <view class="avatar-section">
        <view class="avatar-wrapper" @click="chooseAvatar">
          <image class="avatar" :src="formData.avatarUrl || '/static/default-avatar.png'" mode="aspectFill" @error="formData.avatarUrl = '/static/default-avatar.png'"></image>
          <view class="avatar-edit">
            <text class="iconfont icon-camera"></text>
          </view>
        </view>
        <text class="avatar-tip">点击更换头像</text>
      </view>
    </view>
    
    <!-- 内容区域 -->
    <view class="content-section">
      <!-- 基本信息 -->
      <view class="card">
        <view class="card-header">
          <view class="header-left">
            <view class="icon-box blue icon-box-sm">
              <text class="iconfont icon-user"></text>
            </view>
            <text class="header-title">基本信息</text>
          </view>
        </view>
        
        <view class="form-list">
          <view class="form-item">
            <view class="item-label">
              <text class="iconfont icon-user" style="color: #667eea; margin-right: 12rpx;"></text>
              <text>姓名</text>
              <text class="required">*</text>
            </view>
            <input 
              class="item-input" 
              type="text" 
              v-model="formData.name" 
              placeholder="请输入姓名"
              maxlength="20"
            />
          </view>
          
          <view class="form-item">
            <view class="item-label">
              <text class="iconfont icon-heart" style="color: #EF4444; margin-right: 12rpx;"></text>
              <text>关系类型</text>
              <text class="required">*</text>
            </view>
            <picker mode="selector" :range="relationTypes" @change="onRelationChange">
              <view class="item-value" :class="{ placeholder: !formData.relationType }">
                <text>{{ formData.relationType || '请选择关系类型' }}</text>
                <text class="iconfont icon-arrow-right" style="margin-left: 12rpx; color: #94A3B8;"></text>
              </view>
            </picker>
          </view>
        </view>
      </view>
      
      <!-- 联系方式 -->
      <view class="card">
        <view class="card-header">
          <view class="header-left">
            <view class="icon-box green icon-box-sm">
              <text class="iconfont icon-phone"></text>
            </view>
            <text class="header-title">联系方式</text>
          </view>
        </view>
        
        <view class="form-list">
          <view class="form-item">
            <view class="item-label">
              <text class="iconfont icon-phone" style="color: #10B981; margin-right: 12rpx;"></text>
              <text>手机号</text>
            </view>
            <input 
              class="item-input" 
              type="number" 
              v-model="formData.phone" 
              placeholder="请输入手机号"
              maxlength="11"
            />
          </view>
          
          <view class="form-item">
            <view class="item-label">
              <text class="iconfont icon-mail" style="color: #8B5CF6; margin-right: 12rpx;"></text>
              <text>邮箱</text>
            </view>
            <input 
              class="item-input" 
              type="text" 
              v-model="formData.email" 
              placeholder="请输入邮箱"
              maxlength="50"
            />
          </view>
          
          <view class="form-item">
            <view class="item-label">
              <text class="iconfont icon-map-pin" style="color: #F59E0B; margin-right: 12rpx;"></text>
              <text>地址</text>
            </view>
            <input 
              class="item-input" 
              type="text" 
              v-model="formData.address" 
              placeholder="请输入地址"
              maxlength="100"
            />
          </view>
        </view>
      </view>
      
      <!-- 其他信息 -->
      <view class="card">
        <view class="card-header">
          <view class="header-left">
            <view class="icon-box orange icon-box-sm">
              <text class="iconfont icon-info"></text>
            </view>
            <text class="header-title">其他信息</text>
          </view>
        </view>
        
        <view class="form-list">
          <view class="form-item">
            <view class="item-label">
              <text class="iconfont icon-calendar" style="color: #EC4899; margin-right: 12rpx;"></text>
              <text>生日</text>
            </view>
            <picker mode="date" :value="formData.birthday" @change="onBirthdayChange">
              <view class="item-value" :class="{ placeholder: !formData.birthday }">
                <text>{{ formData.birthday || '请选择生日' }}</text>
                <text class="iconfont icon-arrow-right" style="margin-left: 12rpx; color: #94A3B8;"></text>
              </view>
            </picker>
          </view>
          
          <view class="form-item textarea-item">
            <view class="item-label">
              <text class="iconfont icon-file-text" style="color: #64748B; margin-right: 12rpx;"></text>
              <text>备注</text>
            </view>
            <textarea 
              class="item-textarea" 
              v-model="formData.remark" 
              placeholder="请输入备注信息"
              maxlength="200"
            />
            <text class="textarea-count">{{ formData.remark.length }}/200</text>
          </view>
          
          <view class="form-item switch-item">
            <view class="item-label">
              <text class="iconfont icon-star" style="color: #F59E0B; margin-right: 12rpx;"></text>
              <text>设为常用联系人</text>
            </view>
            <switch 
              :checked="formData.isFavorite === 1" 
              @change="onFavoriteChange" 
              color="#667eea" 
            />
          </view>
        </view>
      </view>
      
      <!-- 保存按钮 -->
      <view style="padding: 40rpx 0;">
        <button class="btn-primary" :class="{ 'loading': loading }" @click="handleSave" :disabled="loading">
          <text v-if="!loading" class="iconfont icon-save" style="margin-right: 12rpx;"></text>
          <text v-if="!loading">保存</text>
          <view v-else class="loading-spinner"></view>
        </button>
      </view>
    </view>
  </view>
</template>

<script>
import contactApi from '@/api/contact'

export default {
  data() {
    return {
      contactId: null,
      formData: {
        name: '',
        relationType: '',
        avatarUrl: '',
        phone: '',
        email: '',
        birthday: '',
        address: '',
        remark: '',
        isFavorite: 0
      },
      relationTypes: ['亲戚', '朋友', '同事', '同学', '其他'],
      loading: false
    }
  },
  
  onLoad(options) {
    if (options.id) {
      this.contactId = options.id
      this.loadContactDetail()
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
    
    async loadContactDetail() {
      try {
        const res = await contactApi.getContactDetail({ id: this.contactId })

        if (res.code === 1 && res.data) {
          const contact = res.data
          this.formData = {
            name: contact.name,
            relationType: contact.relationType,
            avatarUrl: contact.avatarUrl,
            phone: contact.phone,
            email: contact.email,
            birthday: contact.birthday,
            address: contact.address,
            remark: contact.remark,
            isFavorite: contact.isFavorite
          }
        }
      } catch (error) {
        console.error('加载联系人详情失败:', error)
      }
    },
    
    onRelationChange(e) {
      this.formData.relationType = this.relationTypes[e.detail.value]
    },
    
    onBirthdayChange(e) {
      this.formData.birthday = e.detail.value
    },
    
    onFavoriteChange(e) {
      this.formData.isFavorite = e.detail.value ? 1 : 0
    },
    
    chooseAvatar() {
      uni.chooseImage({
        count: 1,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: (res) => {
          this.formData.avatarUrl = res.tempFilePaths[0]
        }
      })
    },
    
    validateForm() {
      if (!this.formData.name) {
        uni.showToast({
          title: '请输入姓名',
          icon: 'none'
        })
        return false
      }
      
      if (!this.formData.relationType) {
        uni.showToast({
          title: '请选择关系类型',
          icon: 'none'
        })
        return false
      }
      
      if (this.formData.phone && !/^1[3-9]\d{9}$/.test(this.formData.phone)) {
        uni.showToast({
          title: '手机号格式不正确',
          icon: 'none'
        })
        return false
      }
      
      if (this.formData.email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.formData.email)) {
        uni.showToast({
          title: '邮箱格式不正确',
          icon: 'none'
        })
        return false
      }
      
      return true
    },
    
    async handleSave() {
      if (!this.validateForm()) {
        return
      }
      
      this.loading = true
      
      try {
        const data = { ...this.formData }
        
        if (this.contactId) {
          data.id = this.contactId
          await contactApi.updateContact(data)
        } else {
          await contactApi.addContact(data)
        }
        
        uni.showToast({
          title: '保存成功',
          icon: 'success'
        })
        
        setTimeout(() => {
          uni.navigateBack()
        }, 1500)
      } catch (error) {
        console.error('保存失败:', error)
      } finally {
        this.loading = false
      }
    },
    
    handleDelete() {
      uni.showModal({
        title: '确认删除',
        content: '确定要删除这个联系人吗？删除后无法恢复',
        confirmColor: '#EF4444',
        success: async (res) => {
          if (res.confirm) {
            try {
              const deleteRes = await contactApi.deleteContact({ id: this.contactId })
              
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

// 头像区域
.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40rpx 0 60rpx;
  
  .avatar-wrapper {
    position: relative;
    width: 180rpx;
    height: 180rpx;
    margin-bottom: $spacing-md;
    
    .avatar {
      width: 100%;
      height: 100%;
      border-radius: $radius-full;
      background: $bg-secondary;
      border: 6rpx solid rgba(255, 255, 255, 0.3);
      box-shadow: $shadow-md;
    }
    
    .avatar-edit {
      position: absolute;
      bottom: 0;
      right: 0;
      width: 64rpx;
      height: 64rpx;
      background: $primary-gradient;
      border-radius: $radius-full;
      display: flex;
      align-items: center;
      justify-content: center;
      border: 4rpx solid #FFFFFF;
      box-shadow: $shadow-sm;
      
      .iconfont {
        font-size: 32rpx;
        color: #FFFFFF;
      }
    }
  }
  
  .avatar-tip {
    font-size: $font-size-sm;
    color: rgba(255, 255, 255, 0.8);
  }
}

// 表单列表
.form-list {
  .form-item {
    display: flex;
    align-items: center;
    padding: $spacing-md 0;
    border-bottom: 2rpx solid $bg-secondary;
    
    &:last-child {
      border-bottom: none;
    }
    
    &.textarea-item {
      flex-direction: column;
      align-items: flex-start;
      
      .item-label {
        margin-bottom: $spacing-sm;
      }
      
      .item-textarea {
        width: 100%;
        min-height: 160rpx;
        background: $bg-secondary;
        border-radius: $radius-lg;
        padding: $spacing-md;
        font-size: $font-size-base;
        color: $text-primary;
      }
      
      .textarea-count {
        align-self: flex-end;
        font-size: $font-size-xs;
        color: $text-tertiary;
        margin-top: $spacing-xs;
      }
    }
    
    &.switch-item {
      justify-content: space-between;
      
      .item-label {
        margin-bottom: 0;
      }
    }
    
    .item-label {
      width: 200rpx;
      font-size: $font-size-base;
      color: $text-primary;
      font-weight: $font-weight-medium;
      display: flex;
      align-items: center;
      flex-shrink: 0;
      
      .required {
        color: $danger-color;
        margin-left: 4rpx;
      }
    }
    
    .item-input {
      flex: 1;
      font-size: $font-size-base;
      color: $text-primary;
      text-align: right;
      height: 48rpx;
      line-height: 48rpx;
    }
    
    .item-value {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: flex-end;
      font-size: $font-size-base;
      color: $text-primary;
      
      &.placeholder {
        color: $text-tertiary;
      }
    }
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
