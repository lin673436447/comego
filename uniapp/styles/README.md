# 往来记 - 样式系统使用指南

## 文件说明

### 1. theme.scss - 主题变量
包含所有颜色、间距、圆角、阴影等设计变量和混合器。

### 2. components.scss - 组件样式
包含可直接使用的组件类名，如按钮、卡片、输入框等。

## 使用方法

### 在页面中引入

```scss
<style lang="scss" scoped>
@import '@/styles/theme.scss';
@import '@/styles/components.scss';

// 使用变量
.my-component {
  color: $primary-color;
  padding: $spacing-md;
  border-radius: $radius-lg;
}

// 使用混合器
.my-button {
  @include btn-gradient($primary-gradient, $btn-shadow-primary);
}
</style>
```

### 直接使用组件类名

```vue
<template>
  <view class="page-container">
    <!-- 顶部渐变头部 -->
    <view class="header-gradient">
      <view class="decoration-circle circle-1"></view>
      <view class="decoration-circle circle-2"></view>
      
      <!-- 导航栏 -->
      <view class="navbar">
        <view class="nav-back">
          <text class="iconfont icon-arrow-left"></text>
        </view>
        <text class="nav-title">页面标题</text>
        <view class="nav-right">
          <text class="iconfont icon-more"></text>
        </view>
      </view>
      
      <!-- 统计卡片 -->
      <view class="stat-card">
        <text class="stat-label">本月支出</text>
        <text class="stat-value">¥1,234.00</text>
        <view class="stat-compare">
          <view class="compare-badge up">
            <text>↑ 12%</text>
          </view>
          <text class="compare-text">较上月</text>
        </view>
      </view>
    </view>
    
    <!-- 内容区域 -->
    <view class="content-section">
      <!-- 卡片 -->
      <view class="card">
        <view class="card-header">
          <view class="header-left">
            <view class="icon-box blue icon-box-sm">
              <text class="iconfont icon-list"></text>
            </view>
            <text class="header-title">卡片标题</text>
          </view>
          <text class="header-more">查看更多</text>
        </view>
        
        <!-- 列表项 -->
        <view class="list-item">
          <view class="item-icon wedding">
            <text class="iconfont">💒</text>
          </view>
          <view class="item-content">
            <text class="item-title">项目名称</text>
            <text class="item-subtitle">描述信息</text>
          </view>
          <view class="item-right">
            <text class="item-value">¥500</text>
          </view>
          <view class="item-arrow">
            <text class="iconfont icon-arrow-right"></text>
          </view>
        </view>
      </view>
      
      <!-- 快捷操作 -->
      <view class="quick-actions">
        <view class="action-item">
          <view class="icon-box blue">
            <text class="iconfont icon-edit"></text>
          </view>
          <text class="action-label">记账</text>
        </view>
        <view class="action-item">
          <view class="icon-box green">
            <text class="iconfont icon-users"></text>
          </view>
          <text class="action-label">联系人</text>
        </view>
      </view>
      
      <!-- 按钮 -->
      <button class="btn-primary">主要按钮</button>
      <button class="btn-success">成功按钮</button>
      <button class="btn-secondary">次要按钮</button>
      
      <!-- 输入框 -->
      <view class="input-group">
        <view class="input-item">
          <view class="input-prefix">
            <text class="iconfont icon-user"></text>
          </view>
          <input class="input-field" placeholder="请输入用户名" />
        </view>
      </view>
      
      <!-- 进度条 -->
      <view class="progress-bar">
        <view class="progress-fill success" style="width: 60%"></view>
      </view>
      
      <!-- 状态标签 -->
      <text class="status-tag success">正常</text>
      <text class="status-tag warning">警告</text>
      <text class="status-tag danger">异常</text>
      
      <!-- 空状态 -->
      <view class="empty-state">
        <view class="empty-icon">
          <text class="iconfont icon-inbox"></text>
        </view>
        <text class="empty-text">暂无数据</text>
        <text class="empty-tip">点击添加按钮开始</text>
      </view>
    </view>
  </view>
</template>
```

## 颜色系统

### 主色调
- `$primary-color`: #667eea
- `$primary-gradient`: linear-gradient(135deg, #667eea 0%, #764ba2 100%)

### 状态色
- `$success-color`: #10B981 (成功)
- `$warning-color`: #F59E0B (警告)
- `$danger-color`: #EF4444 (危险)
- `$purple-color`: #8B5CF6 (紫色)

### 文字色
- `$text-primary`: #1E293B (主要文字)
- `$text-secondary`: #64748B (次要文字)
- `$text-tertiary`: #94A3B8 (辅助文字)

### 背景色
- `$bg-primary`: #F8FAFC (页面背景)
- `$bg-secondary`: #F1F5F9 (卡片背景)
- `$bg-tertiary`: #E2E8F0 (分隔线)

## 间距系统

- `$spacing-xs`: 8rpx
- `$spacing-sm`: 16rpx
- `$spacing-md`: 24rpx
- `$spacing-lg`: 32rpx
- `$spacing-xl`: 40rpx
- `$spacing-2xl`: 60rpx

## 圆角系统

- `$radius-sm`: 8rpx
- `$radius-md`: 12rpx
- `$radius-lg`: 16rpx
- `$radius-xl`: 24rpx
- `$radius-2xl`: 32rpx
- `$radius-full`: 50%

## 阴影系统

- `$shadow-sm`: 小阴影
- `$shadow-md`: 中等阴影（卡片默认）
- `$shadow-lg`: 大阴影
- `$shadow-xl`: 超大阴影

## 最佳实践

1. **始终使用变量**：不要硬编码颜色或间距值
2. **使用组件类名**：优先使用预定义的组件类名
3. **保持一致性**：相同类型的元素使用相同的样式
4. **响应式设计**：使用 rpx 单位确保在不同设备上显示一致
5. **避免重复**：使用混合器（mixin）封装可复用的样式模式
