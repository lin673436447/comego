# 往来记 - 安装部署文档

## 项目概述

**往来记**是一个人情记账管理小程序，用于记录和管理人情往来、礼金收支等财务信息。

### 技术架构

- **后端**：Spring Boot 2.6.13 + MyBatis-Plus + MySQL + Redis
- **前端**：uni-app (Vue 3) + uni-ui
- **部署目标**：
  - 后端：Linux 服务器
  - 前端：微信小程序

---

## 目录

1. [环境要求](#环境要求)
2. [后端部署](#后端部署)
3. [前端部署](#前端部署)
4. [常见问题](#常见问题)

---

## 环境要求

### 服务器环境

| 组件 | 版本要求 | 说明 |
|------|----------|------|
| JDK | 1.8+ | Java 运行环境 |
| MySQL | 5.7+ | 数据库 |
| Redis | 5.0+ | 缓存服务 |
| Nginx | 1.18+ | 反向代理（可选） |

### 开发环境

| 组件 | 版本要求 | 说明 |
|------|----------|------|
| Node.js | 16+ | 前端构建 |
| HBuilderX | 最新版 | uni-app 开发 IDE |
| 微信开发者工具 | 最新版 | 小程序调试 |

---

## 后端部署

### 一、服务器环境准备

#### 1.1 安装 JDK 1.8

```bash
# CentOS/RHEL
yum install -y java-1.8.0-openjdk java-1.8.0-openjdk-devel

# Ubuntu/Debian
apt-get update
apt-get install -y openjdk-8-jdk

# 验证安装
java -version
```

#### 1.2 安装 MySQL 5.7+

```bash
# CentOS 7
wget https://dev.mysql.com/get/mysql57-community-release-el7-11.noarch.rpm
rpm -Uvh mysql57-community-release-el7-11.noarch.rpm
yum install -y mysql-community-server

# 启动 MySQL
systemctl start mysqld
systemctl enable mysqld

# 查看初始密码
grep 'temporary password' /var/log/mysqld.log

# 登录并修改密码
mysql -u root -p
ALTER USER 'root'@'localhost' IDENTIFIED BY 'YourNewPassword@123';
```

#### 1.3 安装 Redis

```bash
# CentOS
yum install -y redis

# Ubuntu
apt-get install -y redis-server

# 启动 Redis
systemctl start redis
systemctl enable redis

# 验证
redis-cli ping
```

### 二、数据库初始化

#### 2.1 创建数据库

```sql
-- 登录 MySQL
mysql -u root -p

-- 创建数据库
CREATE DATABASE come_go CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建应用用户（推荐）
CREATE USER 'comego'@'%' IDENTIFIED BY 'YourPassword@123';
GRANT ALL PRIVILEGES ON come_go.* TO 'comego'@'%';
FLUSH PRIVILEGES;
```

#### 2.2 导入表结构

```bash
# 如果有 SQL 文件
mysql -u comego -p come_go < init.sql
```

### 三、应用部署

#### 3.1 方式一：直接部署 JAR 包

**1. 打包应用**

```bash
# 在项目根目录执行
mvn clean package -DskipTests
```

打包完成后，JAR 文件位于 `target/record-0.0.1-SNAPSHOT.jar`

**2. 上传 JAR 包到服务器**

```bash
scp target/record-0.0.1-SNAPSHOT.jar root@your-server-ip:/opt/comego/
```

**3. 创建配置文件**

在服务器创建 `application-prod.yml`：

```bash
mkdir -p /opt/comego/config
cat > /opt/comego/config/application-prod.yml << 'EOF'
server:
  port: 8080

spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/come_go?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: comego
    password: YourPassword@123
  redis:
    host: localhost
    port: 6379
    password: 
    database: 0
    timeout: 6000ms
    lettuce:
      pool:
        max-active: 8
        max-idle: 8
        min-idle: 0
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8

mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      id-type: auto
      logic-delete-field: isDeleted
      logic-delete-value: 1
      logic-not-delete-value: 0
EOF
```

**4. 创建启动脚本**

```bash
cat > /opt/comego/start.sh << 'EOF'
#!/bin/bash
APP_NAME=record-0.0.1-SNAPSHOT.jar
LOG_FILE=app.log

# 停止已有进程
PID=$(ps -ef | grep $APP_NAME | grep -v grep | awk '{print $2}')
if [ -n "$PID" ]; then
    echo "Stopping existing process: $PID"
    kill -9 $PID
fi

# 启动应用
nohup java -jar -Dspring.profiles.active=prod -Dspring.config.location=./config/ $APP_NAME > $LOG_FILE 2>&1 &
echo "Application started"
EOF

chmod +x /opt/comego/start.sh
```

**5. 启动应用**

```bash
cd /opt/comego
./start.sh

# 查看日志
tail -f app.log
```

#### 3.2 方式二：使用 systemd 服务（推荐）

**1. 创建服务文件**

```bash
cat > /etc/systemd/system/comego.service << 'EOF'
[Unit]
Description=ComeGo Record Application
After=syslog.target network.target mysql.service redis.service

[Service]
User=root
Group=root
WorkingDirectory=/opt/comego
ExecStart=/usr/bin/java -jar -Dspring.profiles.active=prod -Dspring.config.location=./config/ /opt/comego/record-0.0.1-SNAPSHOT.jar
SuccessExitStatus=143
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
EOF
```

**2. 启动服务**

```bash
# 重新加载 systemd
systemctl daemon-reload

# 启动服务
systemctl start comego

# 设置开机自启
systemctl enable comego

# 查看状态
systemctl status comego

# 查看日志
journalctl -u comego -f
```

#### 3.3 方式三：使用 Docker 部署

**1. 创建 Dockerfile**

```dockerfile
FROM openjdk:8-jdk-alpine

VOLUME /tmp

COPY target/record-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
```

**2. 构建并运行**

```bash
# 构建镜像
docker build -t comego-record:latest .

# 运行容器
docker run -d \
  --name comego-record \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/come_go \
  -e SPRING_DATASOURCE_USERNAME=comego \
  -e SPRING_DATASOURCE_PASSWORD=YourPassword@123 \
  -e SPRING_REDIS_HOST=host.docker.internal \
  comego-record:latest
```

### 四、Nginx 反向代理配置（可选）

```bash
cat > /etc/nginx/conf.d/comego.conf << 'EOF'
server {
    listen 80;
    server_name your-domain.com;

    location /api/ {
        proxy_pass http://localhost:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
EOF

# 测试配置并重启
nginx -t
systemctl restart nginx
```

### 五、配置 HTTPS（推荐）

使用 Let's Encrypt 免费证书：

```bash
# 安装 certbot
yum install -y certbot python2-certbot-nginx

# 申请证书
certbot --nginx -d your-domain.com

# 自动续期
echo "0 0,12 * * * root python -c 'import random; import time; time.sleep(random.random() * 3600)' && certbot renew -q" | sudo tee -a /etc/crontab > /dev/null
```

---

## 前端部署

### 一、环境准备

#### 1.1 安装 Node.js

```bash
# 下载安装包
wget https://nodejs.org/dist/v18.20.0/node-v18.20.0-x64.msi

# Windows 直接运行安装程序
# 或使用 nvm-windows 管理版本
```

#### 1.2 安装 HBuilderX

1. 访问官网下载：https://www.dcloud.io/hbuilderx.html
2. 下载对应系统版本
3. 解压后即可使用

#### 1.3 安装微信开发者工具

1. 访问官网下载：https://developers.weixin.qq.com/miniprogram/dev/devtools/download.html
2. 安装并扫码登录

### 二、项目配置

#### 2.1 修改 API 地址

编辑 `uniapp/utils/request.js`，修改 `BASE_URL` 为实际后端地址：

```javascript
// 开发环境
const BASE_URL = 'http://localhost:8080/api/v1'

// 生产环境（部署后）
const BASE_URL = 'https://your-domain.com/api/v1'
```

#### 2.2 配置微信小程序 AppID

编辑 `uniapp/manifest.json`：

```json
{
  "mp-weixin": {
    "appid": "wx your-app-id-here",
    "setting": {
      "urlCheck": false,
      "es6": true,
      "postcss": true,
      "minified": true
    },
    "usingComponents": true
  }
}
```

### 三、构建部署

#### 3.1 开发调试

```bash
cd uniapp

# 安装依赖
npm install

# 运行到微信小程序（开发模式）
npm run dev:mp-weixin
```

#### 3.2 生产构建

```bash
cd uniapp

# 构建微信小程序（生产模式）
npm run build:mp-weixin
```

构建完成后，项目会输出到 `uniapp/dist/build/mp-weixin` 目录。

#### 3.3 上传发布

**方式一：使用 HBuilderX**

1. 打开 HBuilderX
2. 导入项目：`文件` -> `打开目录` -> 选择 `uniapp` 文件夹
3. 点击菜单栏 `发行` -> `小程序-微信`
4. 填写小程序 AppID
5. 点击 `发行`，会自动打开微信开发者工具

**方式二：使用微信开发者工具**

1. 打开微信开发者工具
2. 点击 `+` 新建项目
3. 项目目录选择 `uniapp/dist/build/mp-weixin`
4. 输入 AppID
5. 点击 `确定`
6. 点击右上角 `上传` 按钮
7. 填写版本号和项目备注
8. 上传成功后，登录微信公众平台提交审核

### 四、微信公众平台配置

#### 4.1 配置服务器域名

1. 登录 [微信公众平台](https://mp.weixin.qq.com/)
2. 进入 `开发` -> `开发管理` -> `开发设置`
3. 配置服务器域名：
   - request 合法域名：`https://your-domain.com`
   - uploadFile 合法域名：`https://your-domain.com`
   - downloadFile 合法域名：`https://your-domain.com`

#### 4.2 配置业务域名（H5 页面需要）

1. 进入 `开发` -> `业务域名`
2. 添加业务域名
3. 下载校验文件并上传到服务器根目录

---

## 常见问题

### 后端问题

#### Q1: 启动时报数据库连接错误

**解决方案：**

1. 检查 MySQL 是否启动：`systemctl status mysqld`
2. 检查数据库配置是否正确
3. 检查防火墙是否放行 3306 端口
4. 确认数据库用户权限：
   ```sql
   GRANT ALL PRIVILEGES ON come_go.* TO 'comego'@'%';
   FLUSH PRIVILEGES;
   ```

#### Q2: 端口被占用

```bash
# 查找占用 8080 端口的进程
netstat -tlnp | grep 8080

# 或修改配置文件使用其他端口
server:
  port: 8081
```

#### Q3: 内存不足导致启动失败

```bash
# 调整 JVM 内存参数
java -Xms512m -Xmx1024m -jar record-0.0.1-SNAPSHOT.jar
```

### 前端问题

#### Q1: 请求后端接口报 403 错误

**解决方案：**

1. 检查微信小程序后台是否配置了服务器域名
2. 确保使用 HTTPS 协议
3. 检查后端是否允许跨域访问

#### Q2: 构建失败

```bash
# 清除缓存重新安装依赖
rm -rf node_modules
rm package-lock.json
npm install

# 重新构建
npm run build:mp-weixin
```

#### Q3: 真机调试无法连接后端

1. 确保手机和电脑在同一网络
2. 使用内网穿透工具（如 ngrok）
3. 或者部署到服务器使用真实域名

### 部署检查清单

#### 后端检查项

- [ ] JDK 已安装且版本正确
- [ ] MySQL 已安装且运行正常
- [ ] Redis 已安装且运行正常
- [ ] 数据库已创建且用户权限正确
- [ ] 表结构已初始化
- [ ] 配置文件已修改为生产环境配置
- [ ] 防火墙已放行应用端口
- [ ] 服务已设置为开机自启
- [ ] 日志文件可正常写入

#### 前端检查项

- [ ] API 地址已修改为生产环境地址
- [ ] 微信小程序 AppID 已配置
- [ ] 微信公众平台已配置服务器域名
- [ ] HTTPS 证书已配置且有效
- [ ] 构建产物已上传到微信后台
- [ ] 已提交审核并通过
- [ ] 已发布上线

---

## 附录

### 常用命令速查

```bash
# MySQL
mysql -u root -p                          # 登录 MySQL
systemctl start|stop|restart mysqld       # 启停 MySQL

# Redis
redis-cli ping                            # 测试 Redis
systemctl start|stop|restart redis        # 启停 Redis

# 应用管理
systemctl start|stop|restart comego       # 启停应用服务
journalctl -u comego -f                   # 查看应用日志

# Nginx
nginx -t                                  # 测试配置
systemctl start|stop|restart nginx        # 启停 Nginx

# 防火墙
firewall-cmd --list-ports                 # 查看开放端口
firewall-cmd --add-port=8080/tcp --permanent  # 开放端口
firewall-cmd --reload                     # 重载防火墙
```

### 目录结构

```
/opt/comego/
├── record-0.0.1-SNAPSHOT.jar    # 应用 JAR 包
├── config/
│   └── application-prod.yml     # 生产环境配置
├── start.sh                     # 启动脚本
└── app.log                      # 应用日志
```

---

**文档版本**：v1.0  
**更新日期**：2026-02-03  
**维护人员**：开发团队
