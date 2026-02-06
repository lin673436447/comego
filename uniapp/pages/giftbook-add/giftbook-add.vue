<template>
  <view class="page-container">
    <!-- 顶部渐变头部 -->
    <view class="header-gradient">
      <!-- 导航栏 -->
      <view class="navbar">
        <view class="nav-back" @click="goBack">
          <text class="iconfont icon-arrow-left"></text>
        </view>
        <text class="nav-title">{{ isEdit ? '编辑礼簿' : '添加礼簿' }}</text>
        <view class="nav-right"></view>
      </view>
    </view>
    
    <!-- 内容区域 -->
    <view class="content-section">
      <view class="card">
        <view class="card-header">
          <view class="header-left">
            <view class="icon-box blue icon-box-sm">
              <text class="iconfont icon-book"></text>
            </view>
            <text class="header-title">礼簿信息</text>
          </view>
        </view>
        
        <view class="form-item">
          <view class="item-label">
            <text class="iconfont icon-book" style="color: #667eea; margin-right: 12rpx;"></text>
            <text>礼簿名称</text>
          </view>
          <input 
            class="item-input" 
            type="text" 
            v-model="formData.bookName" 
            placeholder="请输入礼簿名称"
          />
        </view>
        
        <view class="form-item">
          <view class="item-label">
            <text class="iconfont icon-tag" style="color: #10B981; margin-right: 12rpx;"></text>
            <text>礼簿类型</text>
          </view>
          <picker mode="selector" :range="bookTypes" @change="onBookTypeChange">
            <view class="item-value" :class="{ placeholder: !formData.bookType }">
              <text>{{ formData.bookType || '请选择礼簿类型' }}</text>
              <text class="iconfont icon-arrow-right" style="margin-left: 12rpx; color: #94A3B8;"></text>
            </view>
          </picker>
        </view>
        
        <view class="form-item">
          <view class="item-label">
            <text class="iconfont icon-calendar" style="color: #F59E0B; margin-right: 12rpx;"></text>
            <text>礼簿日期</text>
          </view>
          <picker mode="date" :value="formData.bookDate" @change="onDateChange">
            <view class="item-value" :class="{ placeholder: !formData.bookDate }">
              <text>{{ formData.bookDate || '请选择日期' }}</text>
              <text class="iconfont icon-arrow-right" style="margin-left: 12rpx; color: #94A3B8;"></text>
            </view>
          </picker>
        </view>
        
        <view class="form-item textarea-item">
          <view class="item-label">
            <text class="iconfont icon-file-text" style="color: #8B5CF6; margin-right: 12rpx;"></text>
            <text>描述</text>
          </view>
          <textarea 
            class="remark-textarea" 
            v-model="formData.description" 
            placeholder="请输入描述（选填）"
            maxlength="200"
          />
          <text class="textarea-count">{{ formData.description.length }}/200</text>
        </view>
      </view>
      
      <!-- 提交按钮 -->
      <view class="submit-section">
        <button class="btn-primary btn-lg" @click="handleSubmit">
          <text class="iconfont icon-check" style="margin-right: 12rpx;"></text>
          {{ isEdit ? '保存修改' : '创建礼簿' }}
        </button>
      </view>
    </view>
  </view>
</template>

<script>
import giftbookApi from '@/api/giftbook'

export default {
  data() {
    return {
      isEdit: false,
      bookTypes: ['婚礼', '生日', '葬礼', '其他'],
      formData: {
        id: null,
        bookName: '',
        bookType: '',
        bookDate: '',
        description: ''
      }
    }
  },
  
  onLoad(options) {
    if (options.id) {
      this.isEdit = true
      this.formData.id = options.id
      this.loadGiftBookDetail(options.id)
    } else {
      // 设置默认日期为今天
      const now = new Date()
      const year = now.getFullYear()
      const month = String(now.getMonth() + 1).padStart(2, '0')
      const day = String(now.getDate()).padStart(2, '0')
      this.formData.bookDate = `${year}-${month}-${day}`
    }
  },
  
  methods: {
    async loadGiftBookDetail(id) {
      try {
        const res = await giftbookApi.getGiftBookDetail({ id })
        if (res.code === 1) {
          const data = res.data
          this.formData.bookName = data.bookName
          this.formData.bookType = data.bookType
          this.formData.bookDate = data.bookDate
          this.formData.description = data.description
        }
      } catch (error) {
        console.error('加载礼簿详情失败:', error)
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        })
      }
    },
    
    onBookTypeChange(e) {
      this.formData.bookType = this.bookTypes[e.detail.value]
    },
    
    onDateChange(e) {
      this.formData.bookDate = e.detail.value
    },
    
    async handleSubmit() {
      if (!this.formData.bookName.trim()) {
        uni.showToast({
          title: '请输入礼簿名称',
          icon: 'none'
        })
        return
      }
      
      if (!this.formData.bookType) {
        uni.showToast({
          title: '请选择礼簿类型',
          icon: 'none'
        })
        return
      }
      
      if (!this.formData.bookDate) {
        uni.showToast({
          title: '请选择礼簿日期',
          icon: 'none'
        })
        return
      }
      
      try {
        let res
        if (this.isEdit) {
          res = await giftbookApi.updateGiftBook(this.formData)
        } else {
          res = await giftbookApi.addGiftBook(this.formData)
        }
        
        if (res.code === 1) {
          uni.showToast({
            title: this.isEdit ? '修改成功' : '创建成功',
            icon: 'success'
          })
          
          // 延迟返回
          setTimeout(() => {
            uni.navigateBack()
          }, 1500)
        } else {
          uni.showToast({
            title: res.msg || '操作失败',
            icon: 'none'
          })
        }
      } catch (error) {
        console.error('保存礼簿失败:', error)
        uni.showToast({
          title: '操作失败',
          icon: 'none'
        })
      }
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

.page-container {
  background: #F8FAFC;
}

/* 内容区域 */
.content-section {
  padding: 0 30rpx 40rpx;
  margin-top: -60rpx;
  position: relative;
  z-index: 2;
}

/* 卡片 */
.card {
  background: #FFFFFF;
  border-radius: 24rpx;
  padding: 30rpx;
  margin-bottom: 24rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
}

.card-header {
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

.icon-box {
  width: 48rpx;
  height: 48rpx;
  border-radius: 12rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.icon-box.blue {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.icon-box .iconfont {
  font-size: 28rpx;
  color: #FFFFFF;
}

.header-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #1E293B;
}

/* 表单样式 */
.form-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx 0;
  border-bottom: 1rpx solid #F1F5F9;
}

.form-item:last-child {
  border-bottom: none;
}

.form-item.textarea-item {
  flex-direction: column;
  align-items: flex-start;
  gap: 16rpx;
}

.item-label {
  display: flex;
  align-items: center;
  font-size: 28rpx;
  color: #1E293B;
  font-weight: 500;
}

.item-value {
  display: flex;
  align-items: center;
  font-size: 28rpx;
  color: #1E293B;
}

.item-value.placeholder {
  color: #94A3B8;
}

.item-input {
  flex: 1;
  text-align: right;
  font-size: 28rpx;
  color: #1E293B;
}

.remark-textarea {
  width: 100%;
  height: 160rpx;
  background: #F8FAFC;
  border-radius: 12rpx;
  padding: 20rpx;
  font-size: 28rpx;
  color: #1E293B;
  box-sizing: border-box;
}

.textarea-count {
  align-self: flex-end;
  font-size: 24rpx;
  color: #94A3B8;
}

/* 提交按钮 */
.submit-section {
  margin-top: 40rpx;
}

.btn-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #FFFFFF;
  border: none;
  border-radius: 16rpx;
  font-size: 32rpx;
  font-weight: 500;
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-primary::after {
  border: none;
}

.btn-lg {
  width: 100%;
  height: 96rpx;
}
</style>