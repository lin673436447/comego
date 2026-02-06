# GitHub自动部署配置说明

## 项目概述

本项目使用GitHub Actions实现后端服务的自动构建和部署。当代码推送到GitHub仓库的main分支时，会自动执行构建、测试和部署流程。

## 配置步骤

### 1. 配置GitHub仓库密钥

在GitHub仓库的 `Settings > Secrets and variables > Actions` 中添加以下密钥：

| 密钥名称 | 说明 | 值示例 |
|---------|------|--------|
| `SSH_PRIVATE_KEY` | 服务器SSH私钥 | `-----BEGIN OPENSSH PRIVATE KEY-----...` |
| `REMOTE_HOST` | 服务器IP地址 | `192.168.1.100` |
| `REMOTE_USER` | 服务器用户名 | `root` |
| `DEPLOY_TARGET` | 部署目标目录 | `/opt/comego` |

### 2. 服务器准备工作

#### 2.1 安装必要软件

```bash
# 安装JDK 1.8（Ubuntu示例）
apt-get update && apt-get install -y openjdk-8-jdk

# 安装MySQL和Redis
apt-get install -y mysql-server redis-server

# 启动服务
systemctl start mysql
systemctl start redis
systemctl enable mysql
systemctl enable redis
```

#### 2.2 创建部署目录结构

```bash
# 创建部署目录
mkdir -p /opt/comego/config

# 创建生产环境配置文件
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

#### 2.3 配置数据库

```bash
# 登录MySQL
mysql -u root -p

# 创建数据库
CREATE DATABASE come_go CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 创建用户并授权
CREATE USER 'comego'@'%' IDENTIFIED BY 'YourPassword@123';
GRANT ALL PRIVILEGES ON come_go.* TO 'comego'@'%';
FLUSH PRIVILEGES;

# 导入表结构
mysql -u comego -p come_go < /opt/comego/init.sql
```

### 3. SSH密钥配置

#### 3.1 生成SSH密钥对

```bash
# 生成密钥对
ssh-keygen -t rsa -b 4096 -C "github-actions"

# 查看公钥
cat ~/.ssh/id_rsa.pub

# 查看私钥（用于GitHub Secrets）
cat ~/.ssh/id_rsa
```

#### 3.2 将公钥添加到服务器

```bash
# 将公钥添加到服务器的authorized_keys
ssh-copy-id -i ~/.ssh/id_rsa.pub root@your-server-ip

# 测试连接
ssh root@your-server-ip
```

## 部署流程

### 1. 触发部署

当向GitHub仓库的main分支推送代码时，会自动触发部署流程：

```bash
# 推送代码到GitHub
git add .
git commit -m "Update code"
git push origin main
```

### 2. 查看部署状态

在GitHub仓库的 `Actions` 标签页中可以查看部署进度和日志：

1. 点击 `Actions` 标签
2. 选择 `Deploy Backend Application` 工作流
3. 查看最新的运行记录
4. 点击进入查看详细日志

### 3. 验证服务状态

部署完成后，可以在服务器上验证服务运行状态：

```bash
# 查看进程
ps -ef | grep record-0.0.1-SNAPSHOT.jar

# 查看日志
tail -f /opt/comego/app.log

# 测试服务是否可访问
curl http://localhost:8080/actuator/health
```

## 部署配置详解

### GitHub Actions工作流配置

工作流文件位于 `.github/workflows/deploy.yml`，包含以下步骤：

1. **检出代码**：从GitHub仓库检出最新代码
2. **设置JDK环境**：配置Java 1.8环境
3. **构建项目**：使用Maven编译并打包项目
4. **运行测试**：执行单元测试确保代码质量
5. **部署到服务器**：通过SSH将构建产物部署到目标服务器

### 部署脚本说明

部署脚本会执行以下操作：

1. **停止旧服务**：查找并终止正在运行的旧服务进程
2. **启动新服务**：使用生产环境配置启动新的服务实例
3. **重定向日志**：将服务日志重定向到app.log文件

## 常见问题与解决方案

### 1. SSH连接失败

**问题**：部署时SSH连接失败

**解决方案**：
- 检查服务器防火墙是否放行SSH端口（默认22）
- 确保SSH私钥正确配置，且具有正确的格式
- 验证服务器IP地址和用户名是否正确

### 2. 构建失败

**问题**：Maven构建失败

**解决方案**：
- 检查项目依赖是否完整
- 确保Java版本为1.8
- 检查代码是否有编译错误

### 3. 部署后服务未启动

**问题**：部署完成后服务未运行

**解决方案**：
- 查看服务器上的app.log日志文件
- 检查端口是否被占用
- 验证配置文件是否正确
- 检查数据库连接是否正常

### 4. 测试失败

**问题**：测试阶段失败

**解决方案**：
- 检查测试用例是否正确
- 确保测试环境配置正确
- 修复测试失败的代码问题

## 高级配置

### 1. 多环境部署

可以通过修改工作流配置，实现多环境部署：

```yaml
# 示例：多环境部署
jobs:
  build:
    # 构建步骤...
    
  deploy-staging:
    needs: build
    if: github.ref == 'refs/heads/staging'
    # 部署到测试环境...
    
  deploy-production:
    needs: build
    if: github.ref == 'refs/heads/main'
    # 部署到生产环境...
```

### 2. 使用Docker部署

如果服务器支持Docker，可以使用Docker容器化部署：

1. **创建Dockerfile**：

```dockerfile
FROM openjdk:8-jdk-alpine

VOLUME /tmp

COPY target/record-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
```

2. **修改Actions配置**：

```yaml
# 构建Docker镜像
- name: Build Docker Image
  run: docker build -t comego-record:latest .

# 推送镜像到Docker Hub
- name: Push to Docker Hub
  run: |
    echo ${{ secrets.DOCKER_HUB_PASSWORD }} | docker login -u ${{ secrets.DOCKER_HUB_USERNAME }} --password-stdin
    docker push comego-record:latest

# 在服务器上拉取并运行
- name: Deploy with Docker
  run: |
    ssh -i ~/.ssh/id_rsa root@${{ secrets.REMOTE_HOST }} "docker pull comego-record:latest && docker run -d --name comego-record -p 8080:8080 comego-record:latest"
```

### 3. 添加监控告警

可以集成监控系统，实现部署和运行状态的监控：

1. **集成Prometheus + Grafana**：监控应用性能和资源使用情况
2. **配置健康检查**：使用Spring Boot Actuator提供健康检查端点
3. **设置告警通知**：当服务异常时发送邮件或短信通知

## 注意事项

1. **安全配置**：
   - 敏感信息必须通过GitHub Secrets传递，不得硬编码在代码中
   - 服务器SSH私钥需要妥善保管
   - 生产环境配置文件中的密码等敏感信息应使用环境变量或配置中心

2. **部署环境**：
   - 确保服务器有足够的内存和CPU资源
   - 网络连接稳定，支持SSH访问
   - 防火墙已放行必要的端口（SSH 22、应用端口8080等）

3. **回滚策略**：
   - 保留历史构建产物，以便快速回滚
   - 部署前备份当前运行的应用
   - 建立自动化回滚机制，当部署失败时自动回滚

4. **测试覆盖**：
   - 确保单元测试覆盖率足够
   - 考虑添加集成测试和端到端测试
   - 部署前进行代码质量检查

## 总结

通过GitHub Actions实现自动部署，可以：

- **提高开发效率**：减少手动部署的时间和错误
- **保证部署一致性**：每次部署使用相同的流程和配置
- **实现持续交付**：代码变更后快速部署到生产环境
- **增强可追溯性**：所有部署操作都有详细的日志记录

此配置适用于小型到中型项目的自动部署需求，可根据实际情况进行调整和扩展。