# 往来记 - uniapp前端项目

## 项目简介

往来记是一款专注于人情记账管理的小程序，帮助用户轻松管理人情往来记录。

## 技术栈

- **框架**: uniapp (Vue 3)
- **状态管理**: Vuex 4
- **样式**: SCSS
- **构建工具**: Vite

## 项目结构

```
uniapp/
├── api/                    # API接口封装
│   ├── auth.js            # 认证相关接口
│   ├── user.js            # 用户相关接口
│   ├── contact.js         # 联系人相关接口
│   ├── transaction.js     # 交易记录相关接口
│   ├── budget.js          # 预算相关接口
│   └── analytics.js       # 统计分析相关接口
├── pages/                  # 页面
│   ├── login/             # 登录页
│   ├── register/          # 注册页
│   ├── index/            # 首页
│   ├── record/           # 记账页
│   ├── bill/             # 账单页
│   ├── stats/            # 统计页
│   ├── contacts/         # 联系人列表页
│   ├── contact-detail/    # 联系人详情页
│   ├── profile/          # 个人中心页
│   ├── budget/           # 预算设置页
│   ├── notifications/     # 通知提醒页
│   └── transaction-detail/ # 交易详情页
├── store/                  # Vuex状态管理
│   ├── index.js           # Store入口
│   └── modules/
│       └── user.js       # 用户模块
├── styles/                 # 全局样式
│   ├── variables.scss     # 变量定义
│   └── index.scss        # 样式入口
├── utils/                  # 工具类
│   └── request.js        # 请求封装
├── static/                 # 静态资源
├── App.vue                 # 应用入口
├── main.js                 # 主入口文件
├── manifest.json           # 应用配置
├── pages.json             # 页面配置
├── package.json           # 项目依赖
├── tsconfig.json         # TypeScript配置
└── vite.config.ts       # Vite配置
```

## 开发指南

### 安装依赖

```bash
npm install
```

### 运行项目

```bash
# 微信小程序
npm run dev:mp-weixin

# H5
npm run dev:h5
```

### 构建项目

```bash
# 微信小程序
npm run build:mp-weixin

# H5
npm run build:h5
```

## 页面说明

### 登录/注册
- [login/login.vue](pages/login/login.vue) - 用户登录
- [register/register.vue](pages/register/register.vue) - 用户注册

### 主要功能
- [index/index.vue](pages/index/index.vue) - 首页，展示概览信息
- [record/record.vue](pages/record/record.vue) - 快速记账
- [bill/bill.vue](pages/bill/bill.vue) - 账单管理
- [stats/stats.vue](pages/stats/stats.vue) - 统计分析
- [contacts/contacts.vue](pages/contacts/contacts.vue) - 联系人管理

### 详情页面
- [contact-detail/contact-detail.vue](pages/contact-detail/contact-detail.vue) - 联系人详情
- [transaction-detail/transaction-detail.vue](pages/transaction-detail/transaction-detail.vue) - 交易详情

### 设置页面
- [profile/profile.vue](pages/profile/profile.vue) - 个人中心
- [budget/budget.vue](pages/budget/budget.vue) - 预算设置
- [notifications/notifications.vue](pages/notifications/notifications.vue) - 通知提醒

## API接口

所有API接口都在 `api/` 目录下统一管理，使用统一的请求封装。

### 请求示例

```javascript
import transactionApi from '@/api/transaction'

// 获取交易列表
const res = await transactionApi.getTransactionList({
  page: 1,
  size: 10
})

// 添加交易
const res = await transactionApi.addTransaction({
  contact_id: 1,
  contact_name: '张三',
  transaction_type: '婚礼',
  relation_type: '亲戚',
  amount: 500.00,
  transaction_date: '2026-01-15',
  remark: '表哥结婚，随礼'
})
```

## 样式规范

### 颜色变量

所有颜色值统一定义在 `styles/variables.scss` 中，禁止在页面中直接使用色值。

```scss
// 使用示例
color: $primary;      // 主色调
background: $light;  // 浅色背景
```

### 工具类

项目中预定义了常用的工具类，可以直接使用。

```html
<view class="flex justify-between items-center">
  <text class="text-primary font-bold">标题</text>
</view>
```

## 状态管理

使用 Vuex 进行状态管理，用户信息存储在 `store/modules/user.js` 中。

```javascript
// 获取用户信息
import { mapGetters } from 'vuex'

computed: {
  ...mapGetters('user', ['username', 'avatar'])
}

// 调用用户操作
import { mapActions } from 'vuex'

methods: {
  ...mapActions('user', ['login', 'logout'])
}
```

## 注意事项

1. 所有颜色值必须使用 CSS 变量或 SCSS 变量，禁止硬编码
2. 图标优先使用 emoji 或微信小程序内置图标
3. API 请求统一使用封装好的 request 工具
4. 页面跳转使用 uni.navigateTo 或 uni.switchTab
5. 数据持久化使用 uni.setStorageSync 和 uni.getStorageSync

## 开发规范

### 命名规范
- 文件名：kebab-case（如：user-info.vue）
- 组件名：PascalCase（如：UserInfo）
- 变量名：camelCase（如：userName）
- 常量名：UPPER_SNAKE_CASE（如：API_BASE_URL）

### 代码风格
- 使用 2 空格缩进
- 使用单引号
- 每行最大长度 120 字符
- 函数名与括号之间不加空格

## 版本信息

当前版本：v1.0.0