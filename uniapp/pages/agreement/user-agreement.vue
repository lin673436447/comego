<template>
  <view class="agreement-container">
    <!-- 顶部导航 -->
    <view class="header">
      <view class="back-btn" @click="goBack">
        <text class="iconfont icon-arrow-left"></text>
      </view>
      <text class="header-title">用户服务协议</text>
      <view class="placeholder"></view>
    </view>
    
    <!-- 协议内容 -->
    <scroll-view class="content-scroll" scroll-y>
      <view class="content-wrapper">
        <view class="title">用户服务协议</view>
        <view class="effective-date">生效日期：2025年2月9日</view>
        
        <view class="section">
          <view class="section-title">一、协议的范围</view>
          <view class="section-content">
            <text class="paragraph">1.1 本协议是您与"往来记"（以下简称"我们"或"本小程序"）之间关于您使用本小程序服务所订立的协议。</text>
            <text class="paragraph">1.2 本小程序是一款专注于人情往来记账管理的工具类应用，帮助用户记录和管理人情往来账目。</text>
            <text class="paragraph">1.3 您使用本小程序服务，即表示您已阅读、理解并同意接受本协议的约束。如果您不同意本协议的任何内容，请勿使用本小程序服务。</text>
          </view>
        </view>
        
        <view class="section">
          <view class="section-title">二、账号注册与管理</view>
          <view class="section-content">
            <text class="paragraph">2.1 您需要注册账号才能使用本小程序的完整功能。注册时，您应当提供真实、准确、完整的个人信息。</text>
            <text class="paragraph">2.2 您有责任妥善保管您的账号和密码，并对您账号下的所有行为承担法律责任。</text>
            <text class="paragraph">2.3 您不得将账号转让、出借或以任何方式允许他人使用。如发现账号异常，请立即联系我们。</text>
            <text class="paragraph">2.4 我们有权对违反法律法规或本协议的用户账号采取警告、限制功能、暂停使用或注销等措施。</text>
          </view>
        </view>
        
        <view class="section">
          <view class="section-title">三、服务内容与使用规范</view>
          <view class="section-content">
            <text class="paragraph">3.1 本小程序提供以下主要服务：</text>
            <text class="list-item">• 人情往来账目记录与管理</text>
            <text class="list-item">• 礼簿创建与管理</text>
            <text class="list-item">• 联系人管理</text>
            <text class="list-item">• 数据统计与分析</text>
            <text class="list-item">• 数据备份与导出</text>
            <text class="paragraph">3.2 您在使用本小程序服务时，应当遵守法律法规，不得从事以下行为：</text>
            <text class="list-item">• 发布或传播违法违规信息</text>
            <text class="list-item">• 侵犯他人合法权益</text>
            <text class="list-item">• 干扰或破坏本小程序的正常运行</text>
            <text class="list-item">• 未经授权访问本小程序系统或数据</text>
            <text class="list-item">• 其他违反法律法规或公序良俗的行为</text>
          </view>
        </view>
        
        <view class="section">
          <view class="section-title">四、用户数据与隐私保护</view>
          <view class="section-content">
            <text class="paragraph">4.1 我们重视您的隐私保护，具体隐私政策请参见《隐私政策》。</text>
            <text class="paragraph">4.2 您使用本小程序所产生的数据归您所有，您有权随时导出、修改或删除您的数据。</text>
            <text class="paragraph">4.3 我们会采取合理的技术措施保护您的数据安全，但无法保证绝对安全。</text>
          </view>
        </view>
        
        <view class="section">
          <view class="section-title">五、知识产权</view>
          <view class="section-content">
            <text class="paragraph">5.1 本小程序的所有内容，包括但不限于文字、图片、图标、界面设计、代码等，均受知识产权法律法规保护。</text>
            <text class="paragraph">5.2 未经我们书面许可，您不得复制、修改、传播、出售或用于任何商业目的。</text>
            <text class="paragraph">5.3 您在使用本小程序过程中产生的数据内容，您享有相应的权利，同时您授予我们在提供服务范围内使用这些内容的许可。</text>
          </view>
        </view>
        
        <view class="section">
          <view class="section-title">六、免责声明</view>
          <view class="section-content">
            <text class="paragraph">6.1 本小程序按"现状"提供，我们不提供任何明示或暗示的担保。</text>
            <text class="paragraph">6.2 因不可抗力、系统维护、网络故障等原因导致服务中断或数据丢失，我们不承担责任。</text>
            <text class="paragraph">6.3 您理解并同意，使用本小程序所产生的风险由您自行承担。</text>
          </view>
        </view>
        
        <view class="section">
          <view class="section-title">七、协议的修改与终止</view>
          <view class="section-content">
            <text class="paragraph">7.1 我们有权根据需要不时修改本协议，修改后的协议将在本小程序内公布。</text>
            <text class="paragraph">7.2 如您不同意修改后的协议，应当停止使用本小程序服务。</text>
            <text class="paragraph">7.3 我们有权在任何时候因任何原因终止向您提供服务，而无需事先通知。</text>
          </view>
        </view>
        
        <view class="section">
          <view class="section-title">八、法律适用与争议解决</view>
          <view class="section-content">
            <text class="paragraph">8.1 本协议的订立、执行和解释均适用中华人民共和国法律。</text>
            <text class="paragraph">8.2 因本协议引起的或与本协议有关的任何争议，双方应友好协商解决；协商不成的，任何一方均可向有管辖权的人民法院提起诉讼。</text>
          </view>
        </view>
        
        <view class="section">
          <view class="section-title">九、联系我们</view>
          <view class="section-content">
            <text class="paragraph">如您对本协议有任何疑问，请通过以下方式联系我们：</text>
            <text class="list-item">• 邮箱：support@wanglaiji.com</text>
            <text class="list-item">• 客服电话：400-XXX-XXXX</text>
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
  margin-bottom: 50rpx;
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
  color: #666666;
  line-height: 1.8;
  margin-bottom: 16rpx;
  text-align: justify;
  
  &:last-child {
    margin-bottom: 0;
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
