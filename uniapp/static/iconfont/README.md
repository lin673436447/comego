# Iconfont 图标使用指南

## 下载步骤

### 1. 访问 Iconfont 网站
打开 [阿里巴巴矢量图标库](https://www.iconfont.cn/collections/detail?cid=29)

### 2. 选择图标
在图标库页面，点击"批量操作"按钮，然后选择以下图标：

#### 导航类（5个）
- 左箭头 (arrow-left)
- 右箭头 (arrow-right)
- 下箭头 (arrow-down)
- 添加 (plus)
- 更多 (more)

#### 用户相关（5个）
- 用户 (user)
- 群组 (users)
- 电话 (phone)
- 信封 (mail)
- 地标 (map-pin/location)

#### 功能操作（8个）
- 搜索 (search)
- 设置 (settings)
- 提醒 (bell)
- 修改 (edit)
- 删除 (delete/trash)
- 保存 (save)
- 相机 (camera)
- 刷新 (refresh)

#### 财务/统计（10个）
- 钱 (wallet)
- 赚取流量 (bar-chart)
- 分类 (pie-chart)
- 流量 (activity)
- 上箭头 (trend-up)
- 下箭头 (trend-down)
- 下载 (download)
- 文档 (file-text)
- 清单 (inbox)

#### 关系/标签（7个）
- 爱心 (heart)
- 加星收藏 (star)
- 加星收藏_填充 (star-fill)
- 标签 (tag)
- 日历 (calendar)
- 时钟 (clock)
- 对话信息 (message)

#### 安全/系统（9个）
- 安全 (lock)
- 对勾 (check)
- 正确提示 (check-circle)
- 一般提示 (info)
- 帮助 (help)
- 分享 (log-out)
- 流量包 (database)
- 上传 (cloud-upload)
- 套餐 (layers)

#### 状态图标（2个）
- 禁止 (bell-off)
- 灯泡 (fire)

### 3. 添加到项目
1. 选择完图标后，点击页面右下角的购物车图标
2. 点击"添加至项目"
3. 如果没有项目，点击"新建项目"，输入项目名称（如：come-go-icons）
4. 选择项目后点击"确定"

### 4. 下载图标
1. 进入项目管理页面
2. 点击"下载至本地"按钮
3. 解压下载的压缩包

### 5. 替换项目文件
将解压后的文件复制到 `uniapp/static/iconfont/` 目录下：
```
uniapp/static/iconfont/
├── iconfont.css      (替换现有的模板文件)
├── iconfont.ttf
├── iconfont.woff
└── iconfont.woff2
```

### 6. 更新 CSS 文件
下载的 `iconfont.css` 中会自动生成正确的 `content` 编码，请使用下载的文件替换现有的模板文件。

## 图标使用方式

在模板中使用：
```vue
<text class="iconfont icon-user"></text>
<text class="iconfont icon-settings" style="color: #667eea;"></text>
```

## 颜色规范

| 颜色值 | 使用场景 |
|--------|---------|
| `#FFFFFF` | 白色背景上的图标、按钮内图标 |
| `#1F2937` | 主要文字、标题 |
| `#94A3B8` | 提示文字、箭头、占位符 |
| `#667eea` | 主题色（主色调） |
| `#10B981` | 成功、收入、正向状态 |
| `#EF4444` | 错误、支出、删除、危险操作 |
| `#F59E0B` | 警告、提醒、收藏 |
| `#8B5CF6` | 紫色强调、邮件、备份 |

## 注意事项

1. 下载后请确保字体文件路径正确
2. 如果图标显示为方框或空白，请检查字体文件是否正确引入
3. 微信小程序中使用时，字体文件需要小于 40KB，否则可能无法显示
4. 建议只选择项目中实际使用的图标，以减小文件体积
