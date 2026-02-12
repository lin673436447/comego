<template>
  <view class="agreement-container">
    <!-- 顶部导航 -->
    <view class="header">
      <view class="back-btn" @click="goBack">
        <text class="iconfont icon-arrow-left"></text>
      </view>
      <text class="header-title">隐私政策</text>
      <view class="placeholder"></view>
    </view>
    
    <!-- 协议内容 -->
    <scroll-view class="content-scroll" scroll-y>
      <view class="content-wrapper">
        <view class="title">隐私政策</view>
        <view class="effective-date">生效日期：2025年2月9日</view>
        
        <view class="intro">
          <text class="intro-text">"往来记"（以下简称"我们"或"本小程序"）非常重视用户的隐私保护。本隐私政策旨在向您说明我们如何收集、使用、存储和保护您的个人信息。请您仔细阅读并理解本政策。</text>
        </view>
        
        <view class="section">
          <view class="section-title">一、信息收集范围</view>
          <view class="section-content">
            <text class="paragraph">1.1 账号注册信息</text>
            <text class="list-item">• 用户名：用于标识您的身份</text>
            <text class="list-item">• 手机号：用于账号注册、登录和身份验证</text>
            <text class="list-item">• 密码：用于账号安全验证</text>
            <text class="list-item">• 邮箱（可选）：用于找回密码和接收通知</text>
            
            <text class="paragraph">1.2 使用数据信息</text>
            <text class="list-item">• 记账数据：您录入的人情往来账目信息</text>
            <text class="list-item">• 礼簿数据：您创建的礼簿及相关记录</text>
            <text class="list-item">• 联系人信息：您添加的联系人资料</text>
            <text class="list-item">• 预算设置：您设置的预算相关信息</text>
            
            <text class="paragraph">1.3 设备和技术信息</text>
            <text class="list-item">• 设备型号、操作系统版本</text>
            <text class="list-item">• 微信版本号、小程序版本号</text>
            <text class="list-item">• IP地址、网络类型</text>
            <text class="list-item">• 操作日志和错误信息</text>
          </view>
        </view>
        
        <view class="section">
          <view class="section-title">二、信息使用目的</view>
          <view class="section-content">
            <text class="paragraph">2.1 我们收集您的信息主要用于以下目的：</text>
            <text class="list-item">• 提供账号注册、登录和身份验证服务</text>
            <text class="list-item">• 存储和同步您的记账数据</text>
            <text class="list-item">• 生成数据统计和分析报告</text>
            <text class="list-item">• 发送服务通知和提醒</text>
            <text class="list-item">• 改进和优化产品功能</text>
            <text class="list-item">• 保障账号和数据安全</text>
            <text class="list-item">• 处理用户反馈和投诉</text>
            
            <text class="paragraph">2.2 我们不会将您的个人信息用于本政策未载明的其他用途。</text>
          </view>
        </view>
        
        <view class="section">
          <view class="section-title">三、信息存储与保护</view>
          <view class="section-content">
            <text class="paragraph">3.1 信息存储</text>
            <text class="list-item">• 您的数据存储在安全的服务器上</text>
            <text class="list-item">• 采用加密技术保护敏感信息</text>
            <text class="list-item">• 定期进行数据备份</text>
            
            <text class="paragraph">3.2 安全措施</text>
            <text class="list-item">• 采用SSL加密传输数据</text>
            <text class="list-item">• 实施访问控制和权限管理</text>
            <text class="list-item">• 定期进行安全审计</text>
            <text class="list-item">• 建立应急响应机制</text>
            
            <text class="paragraph">3.3 尽管我们采取了合理的安全措施，但互联网环境并非绝对安全，我们无法保证您的信息绝对安全。</text>
          </view>
        </view>
        
        <view class="section">
          <view class="section-title">四、信息共享与披露</view>
          <view class="section-content">
            <text class="paragraph">4.1 我们不会向第三方出售、出租或交易您的个人信息。</text>
            
            <text class="paragraph">4.2 在以下情况下，我们可能会披露您的信息：</text>
            <text class="list-item">• 获得您的明确同意</text>
            <text class="list-item">• 根据法律法规要求</text>
            <text class="list-item">• 响应司法机关或政府部门的合法要求</text>
            <text class="list-item">• 保护我们的合法权益</text>
            <text class="list-item">• 防止或调查可能的违法行为</text>
            
            <text class="paragraph">4.3 我们可能与第三方服务提供商合作，这些提供商仅在为我们提供服务所必需的范围内访问您的信息，并受保密义务约束。</text>
          </view>
        </view>
        
        <view class="section">
          <view class="section-title">五、您的权利</view>
          <view class="section-content">
            <text class="paragraph">5.1 访问和修改</text>
            <text class="list-item">• 您可以在个人中心查看和修改您的个人信息</text>
            <text class="list-item">• 您可以随时修改或删除您的记账数据</text>
            
            <text class="paragraph">5.2 数据导出</text>
            <text class="list-item">• 您可以将您的数据导出到本地设备</text>
            
            <text class="paragraph">5.3 账号注销</text>
            <text class="list-item">• 您可以申请注销账号，注销后我们将删除您的个人信息</text>
            <text class="list-item">• 法律法规要求保留的信息除外</text>
            
            <text class="paragraph">5.4 撤回同意</text>
            <text class="list-item">• 您可以随时撤回对我们收集使用您信息的同意</text>
            <text class="list-item">• 撤回同意后，我们可能无法继续为您提供某些服务</text>
          </view>
        </view>
        
        <view class="section">
          <view class="section-title">六、Cookie和类似技术</view>
          <view class="section-content">
            <text class="paragraph">6.1 本小程序可能使用Cookie或类似技术来：</text>
            <text class="list-item">• 保持您的登录状态</text>
            <text class="list-item">• 记住您的偏好设置</text>
            <text class="list-item">• 分析和改进服务</text>
            
            <text class="paragraph">6.2 您可以通过浏览器或设备设置管理Cookie。</text>
          </view>
        </view>
        
        <view class="section">
          <view class="section-title">七、未成年人保护</view>
          <view class="section-content">
            <text class="paragraph">7.1 本小程序主要面向成年用户。</text>
            <text class="paragraph">7.2 如果您是未成年人，请在监护人指导下使用本小程序。</text>
            <text class="paragraph">7.3 如果我们发现未经监护人同意收集了未成年人的个人信息，将尽快删除相关数据。</text>
          </view>
        </view>
        
        <view class="section">
          <view class="section-title">八、隐私政策更新</view>
          <view class="section-content">
            <text class="paragraph">8.1 我们可能会不时更新本隐私政策。</text>
            <text class="paragraph">8.2 更新后的政策将在本小程序内公布，重大变更会通过适当方式通知您。</text>
            <text class="paragraph">8.3 继续使用本服务即表示您同意更新后的政策。</text>
          </view>
        </view>
        
        <view class="section">
          <view class="section-title">九、联系我们</view>
          <view class="section-content">
            <text class="paragraph">如您对本隐私政策有任何疑问、意见或建议，请通过以下方式联系我们：</text>
            <text class="list-item">• 邮箱：support@wanglaiji.com</text>
            <text class="list-item">• 客服电话：400-XXX-XXXX</text>
            <text class="list-item">• 工作时间：周一至周五 9:00-18:00</text>
            
            <text class="paragraph">我们将在收到您的反馈后15个工作日内予以回复。</text>
          </view>
        </view>
        
        <view class="footer">
          <text>往来记 保留所有权利</text>
        </view>
      </view>
    </scroll-view>
  </view>
</template>

<script>
export default {
  methods: {
    goBack() {
      uni.navigateBack()
    }
  }
}
</script>

<style lang="scss" scoped>
.agreement-container {
  min-height: 100vh;
  background: #F8F9FA;
}

/* 顶部导航 */
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 40rpx 30rpx 20rpx;
  background: #FFFFFF;
  border-bottom: 1rpx solid #F0F0F0;
  
  .back-btn {
    width: 60rpx;
    height: 60rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    
    .iconfont {
      font-size: 40rpx;
      color: #333333;
    }
  }
  
  .header-title {
    font-size: 36rpx;
    font-weight: bold;
    color: #333333;
  }
  
  .placeholder {
    width: 60rpx;
  }
}

/* 内容区域 */
.content-scroll {
  height: calc(100vh - 120rpx);
}

.content-wrapper {
  padding: 40rpx 30rpx;
}

.title {
  font-size: 44rpx;
  font-weight: bold;
  color: #333333;
  text-align: center;
  margin-bottom: 16rpx;
}

.effective-date {
  font-size: 26rpx;
  color: #999999;
  text-align: center;
  margin-bottom: 40rpx;
}

.intro {
  background: #EEF2FF;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 40rpx;
  border-left: 8rpx solid #667eea;
  
  .intro-text {
    font-size: 28rpx;
    color: #4B5563;
    line-height: 1.8;
    text-align: justify;
  }
}

.section {
  margin-bottom: 40rpx;
  background: #FFFFFF;
  border-radius: 16rpx;
  padding: 30rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.04);
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333333;
  margin-bottom: 20rpx;
  padding-bottom: 16rpx;
  border-bottom: 2rpx solid #F0F0F0;
}

.section-content {
  display: flex;
  flex-direction: column;
}

.paragraph {
  font-size: 28rpx;
  color: #333333;
  font-weight: 500;
  line-height: 1.8;
  margin-bottom: 12rpx;
  margin-top: 16rpx;
  
  &:first-child {
    margin-top: 0;
  }
}

.list-item {
  font-size: 28rpx;
  color: #666666;
  line-height: 1.8;
  margin-bottom: 12rpx;
  padding-left: 20rpx;
  
  &:last-child {
    margin-bottom: 0;
  }
}

.footer {
  text-align: center;
  padding: 40rpx 0 60rpx;
  
  text {
    font-size: 24rpx;
    color: #999999;
  }
}
</style>
