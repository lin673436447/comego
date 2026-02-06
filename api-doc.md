# 往来记后端API接口文档

## 1. 接口规范

### 1.1 基础信息
- **接口协议**：HTTP/HTTPS
- **请求方法**：POST
- **请求参数**：JSON格式
- **返回格式**：统一响应格式
- **基础路径**：`/api/v1`

### 1.2 响应格式
```json
{
  "code": 1,           // 状态码：1-成功，0-失败
  "msg": "成功",        // 消息：成功/失败信息
  "data": {}           // 数据：具体返回数据
}
```

## 2. 认证相关接口

### 2.1 用户登录
- **接口路径**：`/user/login`
- **请求参数**：
  ```json
  {
    "phone": "13800138001",     // 手机号
    "password": "123456"         // 密码
  }
  ```
- **返回数据**：
  ```json
  {
    "code": 1,
    "msg": "登录成功",
    "data": {
      "token": "token_1_1743681600000",
      "user": {
        "id": 1,
        "username": "张三",
        "phone": "13800138001",
        "email": "zhangsan@example.com",
        "avatar_url": "https://example.com/avatar1.jpg",
        "member_level": 1,
        "address": "北京市朝阳区",
        "is_active": 1
      }
    }
  }
  ```

### 2.2 用户注册
- **接口路径**：`/user/register`
- **请求参数**：
  ```json
  {
    "username": "张三",           // 用户名
    "phone": "13800138001",     // 手机号
    "password": "123456"         // 密码
  }
  ```
- **返回数据**：
  ```json
  {
    "code": 1,
    "msg": "注册成功",
    "data": {}
  }
  ```

### 2.3 退出登录
- **接口路径**：`/user/logout`
- **请求参数**：无
- **返回数据**：
  ```json
  {
    "code": 1,
    "msg": "退出成功",
    "data": {}
  }
  ```

## 3. 用户相关接口

### 3.1 获取用户信息
- **接口路径**：`/user/info`
- **请求参数**：
  ```json
  {}
  ```
- **返回数据**：
  ```json
  {
    "code": 1,
    "msg": "获取成功",
    "data": {
      "id": 1,
      "username": "张三",
      "phone": "13800138001",
      "email": "zhangsan@example.com",
      "avatar_url": "https://example.com/avatar1.jpg",
      "member_level": 1,
      "address": "北京市朝阳区",
      "is_active": 1,
      "created_at": "2026-01-30 12:00:00"
    }
  }
  ```

### 3.2 更新用户信息
- **接口路径**：`/user/update`
- **请求参数**：
  ```json
  {
    "username": "张三",           // 用户名
    "email": "zhangsan@example.com",  // 邮箱
    "avatar_url": "https://example.com/avatar1.jpg",  // 头像URL
    "address": "北京市朝阳区"    // 地址
  }
  ```
- **返回数据**：
  ```json
  {
    "code": 1,
    "msg": "更新成功",
    "data": {}
  }
  ```

### 3.3 修改密码
- **接口路径**：`/user/change-password`
- **请求参数**：
  ```json
  {
    "old_password": "123456",    // 旧密码
    "new_password": "654321"     // 新密码
  }
  ```
- **返回数据**：
  ```json
  {
    "code": 1,
    "msg": "密码修改成功",
    "data": {}
  }
  ```

## 4. 联系人相关接口

### 4.1 获取联系人列表
- **接口路径**：`/contact/list`
- **请求参数**：
  ```json
  {
    "page": 1,            // 页码
    "size": 10,           // 每页大小
    "name": "",           // 联系人姓名（搜索）
    "relation_type": "",  // 关系类型
    "is_favorite": 0      // 是否常用联系人：0-全部，1-常用
  }
  ```
- **返回数据**：
  ```json
  {
    "code": 1,
    "msg": "获取成功",
    "data": {
      "total": 4,
      "list": [
        {
          "id": 1,
          "name": "张伟",
          "relation_type": "亲戚",
          "avatar_url": "https://example.com/contact1.jpg",
          "phone": "13900139001",
          "email": "zhangwei@example.com",
          "birthday": "1990-05-15",
          "address": "北京市海淀区",
          "remark": "表哥",
          "is_favorite": 1
        }
        // 更多联系人...
      ]
    }
  }
  ```

### 4.2 添加联系人
- **接口路径**：`/contact/add`
- **请求参数**：
  ```json
  {
    "name": "张伟",           // 联系人姓名
    "relation_type": "亲戚",   // 关系类型
    "avatar_url": "https://example.com/contact1.jpg",  // 头像URL
    "phone": "13900139001",   // 手机号
    "email": "zhangwei@example.com",  // 邮箱
    "birthday": "1990-05-15",  // 生日
    "address": "北京市海淀区",  // 地址
    "remark": "表哥",         // 备注
    "is_favorite": 1           // 是否常用联系人
  }
  ```
- **返回数据**：
  ```json
  {
    "code": 1,
    "msg": "添加成功",
    "data": {
      "contact_id": 1
    }
  }
  ```

### 4.3 更新联系人
- **接口路径**：`/contact/update`
- **请求参数**：
  ```json
  {
    "id": 1,                // 联系人ID
    "name": "张伟",           // 联系人姓名
    "relation_type": "亲戚",   // 关系类型
    "avatar_url": "https://example.com/contact1.jpg",  // 头像URL
    "phone": "13900139001",   // 手机号
    "email": "zhangwei@example.com",  // 邮箱
    "birthday": "1990-05-15",  // 生日
    "address": "北京市海淀区",  // 地址
    "remark": "表哥",         // 备注
    "is_favorite": 1           // 是否常用联系人
  }
  ```
- **返回数据**：
  ```json
  {
    "code": 1,
    "msg": "更新成功",
    "data": {}
  }
  ```

### 4.4 删除联系人
- **接口路径**：`/contact/delete`
- **请求参数**：
  ```json
  {
    "id": 1                // 联系人ID
  }
  ```
- **返回数据**：
  ```json
  {
    "code": 1,
    "msg": "删除成功",
    "data": {}
  }
  ```

### 4.5 获取常用联系人
- **接口路径**：`/contact/favorites`
- **请求参数**：
  ```json
  {}
  ```
- **返回数据**：
  ```json
  {
    "code": 1,
    "msg": "获取成功",
    "data": [
      {
        "id": 1,
        "name": "张伟",
        "relation_type": "亲戚",
        "avatar_url": "https://example.com/contact1.jpg"
      }
      // 更多常用联系人...
    ]
  }
  ```

## 5. 人情记录相关接口

### 5.1 添加人情记录
- **接口路径**：`/transaction/add`
- **请求参数**：
  ```json
  {
    "contact_id": 1,              // 联系人ID
    "contact_name": "张伟",       // 联系人姓名
    "transaction_type": "婚礼",   // 类型
    "relation_type": "亲戚",       // 关系类型
    "amount": 500.00,             // 金额
    "transaction_date": "2026-01-15",  // 交易日期
    "remark": "表哥结婚，随礼",    // 备注
    "image_urls": "["https://example.com/img1.jpg"]"  // 图片URL列表
  }
  ```
- **返回数据**：
  ```json
  {
    "code": 1,
    "msg": "添加成功",
    "data": {
      "transaction_id": 1
    }
  }
  ```

### 5.2 获取人情记录列表
- **接口路径**：`/transaction/list`
- **请求参数**：
  ```json
  {
    "page": 1,                    // 页码
    "size": 10,                   // 每页大小
    "start_date": "2026-01-01",   // 开始日期
    "end_date": "2026-01-31",     // 结束日期
    "transaction_type": "",      // 类型
    "relation_type": "",          // 关系类型
    "contact_name": ""            // 联系人姓名
  }
  ```
- **返回数据**：
  ```json
  {
    "code": 1,
    "msg": "获取成功",
    "data": {
      "total": 4,
      "list": [
        {
          "id": 1,
          "contact_id": 1,
          "contact_name": "张伟",
          "transaction_type": "婚礼",
          "relation_type": "亲戚",
          "amount": 500.00,
          "transaction_date": "2026-01-15",
          "remark": "表哥结婚，随礼",
          "image_urls": "["https://example.com/img1.jpg"]",
          "created_at": "2026-01-15 12:00:00"
        }
        // 更多记录...
      ]
    }
  }
  ```

### 5.3 更新人情记录
- **接口路径**：`/transaction/update`
- **请求参数**：
  ```json
  {
    "id": 1,                      // 记录ID
    "contact_id": 1,              // 联系人ID
    "contact_name": "张伟",       // 联系人姓名
    "transaction_type": "婚礼",   // 类型
    "relation_type": "亲戚",       // 关系类型
    "amount": 500.00,             // 金额
    "transaction_date": "2026-01-15",  // 交易日期
    "remark": "表哥结婚，随礼",    // 备注
    "image_urls": "["https://example.com/img1.jpg"]"  // 图片URL列表
  }
  ```
- **返回数据**：
  ```json
  {
    "code": 1,
    "msg": "更新成功",
    "data": {}
  }
  ```

### 5.4 删除人情记录
- **接口路径**：`/transaction/delete`
- **请求参数**：
  ```json
  {
    "id": 1                      // 记录ID
  }
  ```
- **返回数据**：
  ```json
  {
    "code": 1,
    "msg": "删除成功",
    "data": {}
  }
  ```

### 5.5 获取最近记账记录
- **接口路径**：`/transaction/recent`
- **请求参数**：
  ```json
  {
    "limit": 3                   // 限制数量
  }
  ```
- **返回数据**：
  ```json
  {
    "code": 1,
    "msg": "获取成功",
    "data": [
      {
        "id": 1,
        "contact_name": "张伟",
        "transaction_type": "婚礼",
        "amount": 500.00,
        "transaction_date": "2026-01-15"
      }
      // 更多记录...
    ]
  }
  ```

### 5.6 导出人情记录
- **接口路径**：`/transaction/export`
- **请求参数**：
  ```json
  {
    "start_date": "2026-01-01",   // 开始日期
    "end_date": "2026-01-31",     // 结束日期
    "transaction_type": "",      // 类型
    "relation_type": ""           // 关系类型
  }
  ```
- **返回数据**：
  ```json
  {
    "code": 1,
    "msg": "导出成功",
    "data": {
      "file_url": "https://example.com/export/transactions_20260131.xlsx"
    }
  }
  ```

## 6. 预算设置相关接口

### 6.1 获取预算设置
- **接口路径**：`/budget/get`
- **请求参数**：
  ```json
  {
    "budget_type": "monthly",  // 预算类型：monthly、quarterly、yearly
    "budget_year": 2026,        // 预算年份
    "budget_month": 1,          // 预算月份（月度预算时使用）
    "budget_quarter": 1          // 预算季度（季度预算时使用）
  }
  ```
- **返回数据**：
  ```json
  {
    "code": 1,
    "msg": "获取成功",
    "data": {
      "id": 1,
      "budget_type": "monthly",
      "budget_amount": 2000.00,
      "current_amount": 1450.00,
      "budget_year": 2026,
      "budget_month": 1,
      "budget_quarter": null
    }
  }
  ```

### 6.2 设置预算
- **接口路径**：`/budget/set`
- **请求参数**：
  ```json
  {
    "budget_type": "monthly",  // 预算类型：monthly、quarterly、yearly
    "budget_amount": 2000.00,   // 预算金额
    "budget_year": 2026,        // 预算年份
    "budget_month": 1,          // 预算月份（月度预算时使用）
    "budget_quarter": 1          // 预算季度（季度预算时使用）
  }
  ```
- **返回数据**：
  ```json
  {
    "code": 1,
    "msg": "设置成功",
    "data": {
      "budget_id": 1
    }
  }
  ```

### 6.3 获取预算统计
- **接口路径**：`/budget/statistics`
- **请求参数**：
  ```json
  {
    "budget_year": 2026         // 预算年份
  }
  ```
- **返回数据**：
  ```json
  {
    "code": 1,
    "msg": "获取成功",
    "data": {
      "monthly": {
        "budget": 2000.00,
        "used": 1450.00,
        "percentage": 72.5
      },
      "quarterly": {
        "budget": 5000.00,
        "used": 1450.00,
        "percentage": 29
      },
      "yearly": {
        "budget": 20000.00,
        "used": 1450.00,
        "percentage": 7.25
      }
    }
  }
  ```

## 7. 余额记录相关接口

### 7.1 获取余额记录列表
- **接口路径**：`/balance/list`
- **请求参数**：
  ```json
  {
    "page": 1,                  // 页码
    "size": 10,                 // 每页大小
    "min_balance": 0,           // 最小余额
    "max_balance": 1000         // 最大余额
  }
  ```
- **返回数据**：
  ```json
  {
    "code": 1,
    "msg": "获取成功",
    "data": {
      "total": 3,
      "list": [
        {
          "id": 1,
          "contact_id": 1,
          "contact_name": "张伟",
          "balance": 200.00,
          "last_transaction_date": "2026-01-15",
          "last_transaction_amount": 500.00
        }
        // 更多余额记录...
      ]
    }
  }
  ```

### 7.2 获取余额提醒
- **接口路径**：`/balance/reminder`
- **请求参数**：
  ```json
  {}
  ```
- **返回数据**：
  ```json
  {
    "code": 1,
    "msg": "获取成功",
    "data": [
      {
        "contact_id": 1,
        "contact_name": "张伟",
        "balance": 200.00,
        "last_transaction_date": "2026-01-15",
        "last_transaction_amount": 500.00,
        "status": "建议回礼"
      }
      // 更多余额提醒...
    ]
  }
  ```

## 8. 通知提醒相关接口

### 8.1 获取通知提醒列表
- **接口路径**：`/notification/list`
- **请求参数**：
  ```json
  {
    "page": 1,                  // 页码
    "size": 10,                 // 每页大小
    "reminder_type": "",       // 提醒类型
    "is_notified": 0            // 是否已通知：0-全部，1-已通知，2-未通知
  }
  ```
- **返回数据**：
  ```json
  {
    "code": 1,
    "msg": "获取成功",
    "data": {
      "total": 3,
      "list": [
        {
          "id": 1,
          "contact_id": 1,
          "contact_name": "张伟",
          "reminder_type": "birthday",
          "reminder_title": "张伟生日提醒",
          "reminder_content": "表哥张伟的生日是5月15日，记得准备礼物",
          "reminder_date": "2026-05-15",
          "reminder_time": "09:00:00",
          "is_repeat": 1,
          "repeat_type": "yearly",
          "is_notified": 0
        }
        // 更多通知提醒...
      ]
    }
  }
  ```

### 8.2 添加通知提醒
- **接口路径**：`/notification/add`
- **请求参数**：
  ```json
  {
    "contact_id": 1,              // 联系人ID
    "contact_name": "张伟",       // 联系人姓名
    "reminder_type": "birthday",  // 提醒类型
    "reminder_title": "张伟生日提醒",  // 提醒标题
    "reminder_content": "表哥张伟的生日是5月15日，记得准备礼物",  // 提醒内容
    "reminder_date": "2026-05-15",  // 提醒日期
    "reminder_time": "09:00:00",   // 提醒时间
    "is_repeat": 1,               // 是否重复提醒
    "repeat_type": "yearly"       // 重复类型
  }
  ```
- **返回数据**：
  ```json
  {
    "code": 1,
    "msg": "添加成功",
    "data": {
      "notification_id": 1
    }
  }
  ```

### 8.3 更新通知提醒状态
- **接口路径**：`/notification/update-status`
- **请求参数**：
  ```json
  {
    "id": 1,                    // 提醒ID
    "is_notified": 1            // 是否已通知：1-已通知
  }
  ```
- **返回数据**：
  ```json
  {
    "code": 1,
    "msg": "更新成功",
    "data": {}
  }
  ```

### 8.4 删除通知提醒
- **接口路径**：`/notification/delete`
- **请求参数**：
  ```json
  {
    "id": 1                    // 提醒ID
  }
  ```
- **返回数据**：
  ```json
  {
    "code": 1,
    "msg": "删除成功",
    "data": {}
  }
  ```

## 9. 数据分析相关接口

### 9.1 获取支出统计
- **接口路径**：`/analytics/expense`
- **请求参数**：
  ```json
  {
    "year": 2026,                // 年份
    "month": 1                   // 月份
  }
  ```
- **返回数据**：
  ```json
  {
    "code": 1,
    "msg": "获取成功",
    "data": {
      "current_month": 2600.00,   // 本月支出
      "last_month": 1800.00,      // 上月支出
      "growth_rate": 44.4,        // 同比增长
      "yearly_total": 2600.00,    // 本年累计
      "monthly_data": [           // 月度数据（12个月）
        {"month": 1, "amount": 2600.00},
        {"month": 2, "amount": 0.00},
        // 更多月份...
        {"month": 12, "amount": 0.00}
      ]
    }
  }
  ```

### 9.2 获取分类分析
- **接口路径**：`/analytics/category`
- **请求参数**：
  ```json
  {
    "start_date": "2026-01-01",   // 开始日期
    "end_date": "2026-01-31"      // 结束日期
  }
  ```
- **返回数据**：
  ```json
  {
    "code": 1,
    "msg": "获取成功",
    "data": {
      "categories": [
        {
          "name": "节日",
          "amount": 1000.00,
          "percentage": 38.5
        },
        {
          "name": "乔迁",
          "amount": 800.00,
          "percentage": 30.8
        },
        {
          "name": "生日",
          "amount": 500.00,
          "percentage": 19.2
        },
        {
          "name": "聚餐",
          "amount": 300.00,
          "percentage": 11.5
        }
      ]
    }
  }
  ```

### 9.3 获取关系图谱数据
- **接口路径**：`/analytics/relation`
- **请求参数**：
  ```json
  {
    "limit": 5                  // 限制数量
  }
  ```
- **返回数据**：
  ```json
  {
    "code": 1,
    "msg": "获取成功",
    "data": {
      "relation_distribution": [  // 关系分布
        {"name": "亲戚", "count": 2},
        {"name": "朋友", "count": 2},
        {"name": "同事", "count": 1}
      ],
      "interaction_heat": [      // 往来热度
        {
          "contact_id": 1,
          "contact_name": "赵六",
          "heat": 90
        },
        {
          "contact_id": 2,
          "contact_name": "王五",
          "heat": 75
        },
        {
          "contact_id": 3,
          "contact_name": "李四",
          "heat": 60
        }
      ]
    }
  }
  ```

## 10. 数据管理相关接口

### 10.1 导出全部数据
- **接口路径**：`/data/export`
- **请求参数**：无
- **返回数据**：
  ```json
  {
    "code": 1,
    "msg": "导出成功",
    "data": {
      "file_url": "https://example.com/export/all_data_20260131.zip"
    }
  }
  ```

### 10.2 备份数据
- **接口路径**：`/data/backup`
- **请求参数**：无
- **返回数据**：
  ```json
  {
    "code": 1,
    "msg": "备份成功",
    "data": {
      "backup_id": "20260131120000",
      "backup_time": "2026-01-31 12:00:00"
    }
  }
  ```

### 10.3 恢复数据
- **接口路径**：`/data/restore`
- **请求参数**：
  ```json
  {
    "backup_id": "20260131120000"  // 备份ID
  }
  ```
- **返回数据**：
  ```json
  {
    "code": 1,
    "msg": "恢复成功",
    "data": {}
  }
  ```

### 10.4 清空数据
- **接口路径**：`/data/clear`
- **请求参数**：
  ```json
  {
    "confirm": true            // 确认清空
  }
  ```
- **返回数据**：
  ```json
  {
    "code": 1,
    "msg": "清空成功",
    "data": {}
  }
  ```

## 11. 错误码说明

| 错误码 | 错误信息 | 说明 |
|--------|----------|------|
| 001 | 参数错误 | 请求参数格式或值错误 |
| 002 | 未授权 | 用户未登录或登录已过期 |
| 003 | 无权限 | 用户无权限执行此操作 |
| 004 | 资源不存在 | 请求的资源不存在 |
| 005 | 操作失败 | 操作执行失败 |
| 006 | 系统错误 | 系统内部错误 |

## 12. 接口调用示例

### 12.1 添加人情记录示例
**请求**：
```bash
POST /api/v1/transaction/add
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

{
  "contact_id": 1,
  "contact_name": "张伟",
  "transaction_type": "婚礼",
  "relation_type": "亲戚",
  "amount": 500.00,
  "transaction_date": "2026-01-15",
  "remark": "表哥结婚，随礼"
}
```

**响应**：
```json
{
  "code": 1,
  "msg": "添加成功",
  "data": {
    "transaction_id": 1
  }
}
```

### 12.2 获取账单列表示例
**请求**：
```bash
POST /api/v1/transaction/list
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

{
  "page": 1,
  "size": 10,
  "start_date": "2026-01-01",
  "end_date": "2026-01-31"
}
```

**响应**：
```json
{
  "code": 1,
  "msg": "获取成功",
  "data": {
    "total": 4,
    "list": [
      {
        "id": 1,
        "contact_id": 1,
        "contact_name": "张伟",
        "transaction_type": "婚礼",
        "relation_type": "亲戚",
        "amount": 500.00,
        "transaction_date": "2026-01-15",
        "remark": "表哥结婚，随礼",
        "created_at": "2026-01-15 12:00:00"
      },
      // 更多记录...
    ]
  }
}
```