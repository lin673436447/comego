-- 往来记数据库表结构设计
-- 创建时间: 2026-01-30
-- 更新时间: 2026-02-01
-- 说明: 根据产品需求文档和页面原型设计的数据库表结构

-- 用户信息表
CREATE TABLE user_info (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  username VARCHAR(50) NOT NULL COMMENT '用户名',
  phone VARCHAR(20) NOT NULL COMMENT '手机号',
  email VARCHAR(100) COMMENT '邮箱',
  password VARCHAR(100) NOT NULL COMMENT '密码（加密存储）',
  avatar_url VARCHAR(500) COMMENT '头像URL',
  member_level TINYINT(1) NOT NULL DEFAULT 1 COMMENT '会员等级：1-普通会员，2-高级会员',
  address VARCHAR(200) COMMENT '地址',
  is_active TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否激活：0-否，1-是',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删，1-已删',
  PRIMARY KEY (id),
  UNIQUE KEY uk_phone (phone),
  KEY idx_username (username),
  KEY idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

-- 联系人表
CREATE TABLE contact_info (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '联系人ID',
  user_id BIGINT NOT NULL COMMENT '用户ID',
  name VARCHAR(50) NOT NULL COMMENT '联系人姓名',
  relation_type VARCHAR(20) NOT NULL COMMENT '关系类型：亲戚、朋友、同事、同学、其他',
  avatar_url VARCHAR(500) COMMENT '头像URL',
  phone VARCHAR(20) COMMENT '手机号',
  email VARCHAR(100) COMMENT '邮箱',
  birthday DATE COMMENT '生日',
  address VARCHAR(200) COMMENT '地址',
  remark VARCHAR(500) COMMENT '备注',
  is_favorite TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否常用联系人：0-否，1-是',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删，1-已删',
  PRIMARY KEY (id),
  KEY idx_user_id (user_id),
  KEY idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='联系人表';

-- 礼簿表
CREATE TABLE gift_book (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '礼簿ID',
  user_id BIGINT NOT NULL COMMENT '用户ID',
  book_name VARCHAR(100) NOT NULL COMMENT '礼簿名称',
  book_type VARCHAR(20) NOT NULL COMMENT '礼簿类型：wedding、birthday、funeral、other',
  book_date DATE COMMENT '礼簿日期',
  description VARCHAR(500) COMMENT '礼簿描述',
  total_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '总金额',
  total_income DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '总收入',
  total_expense DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '总支出',
  is_active TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否激活：0-否，1-是',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删，1-已删',
  PRIMARY KEY (id),
  KEY idx_user_id (user_id),
  KEY idx_book_type (book_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='礼簿表';

-- 人情记录表
CREATE TABLE transaction_record (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  user_id BIGINT NOT NULL COMMENT '用户ID',
  gift_book_id BIGINT COMMENT '礼簿ID（关联礼簿表）',
  gift_book_name VARCHAR(100) COMMENT '礼簿名称（冗余字段）',
  contact_id BIGINT NOT NULL COMMENT '联系人ID',
  contact_name VARCHAR(50) NOT NULL COMMENT '联系人姓名（冗余字段）',
  relation_type VARCHAR(20) NOT NULL COMMENT '关系类型：亲戚、朋友、同事、同学、其他',
  transaction_direction VARCHAR(10) NOT NULL COMMENT '收支方向：income-收入，expense-支出',
  transaction_mode VARCHAR(10) NOT NULL COMMENT '支付方式：cash-现金，gift-礼物',
  amount DECIMAL(10,2) COMMENT '金额（现金方式时必填）',
  gift_name VARCHAR(200) COMMENT '礼物名称（礼物方式时必填）',
  gift_value DECIMAL(10,2) COMMENT '礼物价值（礼物方式时选填）',
  transaction_date DATE NOT NULL COMMENT '交易日期',
  remark VARCHAR(500) COMMENT '备注',
  image_urls TEXT COMMENT '图片URL列表，JSON格式',
  is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删，1-已删',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  KEY idx_user_id (user_id),
  KEY idx_gift_book_id (gift_book_id),
  KEY idx_contact_id (contact_id),
  KEY idx_transaction_direction (transaction_direction)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人情记录表';

-- 预算设置表
CREATE TABLE budget_setting (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '预算ID',
  user_id BIGINT NOT NULL COMMENT '用户ID',
  budget_type VARCHAR(20) NOT NULL COMMENT '预算类型：monthly、quarterly、yearly',
  budget_amount DECIMAL(10,2) NOT NULL COMMENT '预算金额',
  current_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '已使用金额',
  budget_year INT NOT NULL COMMENT '预算年份',
  budget_month TINYINT(2) COMMENT '预算月份（月度预算时使用）',
  budget_quarter TINYINT(1) COMMENT '预算季度（季度预算时使用）',
  is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删，1-已删',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预算设置表';

-- 余额记录表
CREATE TABLE balance_record (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '余额ID',
  user_id BIGINT NOT NULL COMMENT '用户ID',
  contact_id BIGINT NOT NULL COMMENT '联系人ID',
  contact_name VARCHAR(50) NOT NULL COMMENT '联系人姓名（冗余字段）',
  balance DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '余额（正数表示对方欠我，负数表示我欠对方）',
  last_transaction_date DATE COMMENT '最后交易日期',
  last_transaction_amount DECIMAL(10,2) COMMENT '最后交易金额',
  is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删，1-已删',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  KEY idx_user_id (user_id),
  KEY idx_contact_id (contact_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='余额记录表';

-- 通知提醒表
CREATE TABLE notification_reminder (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '提醒ID',
  user_id BIGINT NOT NULL COMMENT '用户ID',
  contact_id BIGINT COMMENT '联系人ID',
  contact_name VARCHAR(50) COMMENT '联系人姓名',
  reminder_type VARCHAR(20) NOT NULL COMMENT '提醒类型：birthday、anniversary、custom',
  reminder_title VARCHAR(100) NOT NULL COMMENT '提醒标题',
  reminder_content VARCHAR(500) COMMENT '提醒内容',
  reminder_date DATE NOT NULL COMMENT '提醒日期',
  reminder_time TIME COMMENT '提醒时间',
  is_repeat TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否重复提醒：0-否，1-是',
  repeat_type VARCHAR(20) COMMENT '重复类型：yearly、monthly、weekly',
  is_notified TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已通知：0-否，1-是',
  is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删，1-已删',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  KEY idx_user_id (user_id),
  KEY idx_contact_id (contact_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知提醒表';

-- ============================================
-- 测试数据
-- ============================================

-- 用户信息表测试数据
INSERT INTO user_info (id, username, phone, email, password, avatar_url, member_level, address, is_active) VALUES
(1, '张三', '13800138001', 'zhangsan@example.com', 'e10adc3949ba59abbe56e057f20f883e', 'https://example.com/avatar1.jpg', 1, '北京市朝阳区', 1),
(2, '李四', '13800138002', 'lisi@example.com', 'e10adc3949ba59abbe56e057f20f883e', 'https://example.com/avatar2.jpg', 2, '上海市浦东新区', 1),
(3, '王五', '13800138003', 'wangwu@example.com', 'e10adc3949ba59abbe56e057f20f883e', 'https://example.com/avatar3.jpg', 1, '广州市天河区', 1),
(4, '赵六', '13800138004', 'zhaoliu@example.com', 'e10adc3949ba59abbe56e057f20f883e', 'https://example.com/avatar4.jpg', 1, '深圳市南山区', 1);

-- 联系人表测试数据
INSERT INTO contact_info (id, user_id, name, relation_type, avatar_url, phone, email, birthday, address, remark, is_favorite) VALUES
(1, 1, '张伟', '亲戚', 'https://example.com/contact1.jpg', '13900139001', 'zhangwei@example.com', '1990-05-15', '北京市海淀区', '表哥', 1),
(2, 1, '李娜', '朋友', 'https://example.com/contact2.jpg', '13900139002', 'lina@example.com', '1992-08-20', '北京市西城区', '大学同学', 1),
(3, 1, '王强', '同事', 'https://example.com/contact3.jpg', '13900139003', 'wangqiang@example.com', '1988-03-10', '北京市东城区', '部门同事', 0),
(4, 1, '刘芳', '同学', 'https://example.com/contact4.jpg', '13900139004', 'liufang@example.com', '1991-11-25', '北京市丰台区', '高中同学', 0),
(5, 2, '陈明', '亲戚', 'https://example.com/contact5.jpg', '13900139005', 'chenming@example.com', '1985-07-08', '上海市黄浦区', '舅舅', 1),
(6, 2, '杨丽', '朋友', 'https://example.com/contact6.jpg', '13900139006', 'yangli@example.com', '1993-12-30', '上海市徐汇区', '闺蜜', 1),
(7, 2, '周杰', '同事', 'https://example.com/contact7.jpg', '13900139007', 'zhoujie@example.com', '1987-09-18', '上海市静安区', '项目经理', 0),
(8, 3, '吴刚', '亲戚', 'https://example.com/contact8.jpg', '13900139008', 'wugang@example.com', '1986-04-22', '广州市越秀区', '堂弟', 1),
(9, 3, '郑红', '朋友', 'https://example.com/contact9.jpg', '13900139009', 'zhenghong@example.com', '1994-06-12', '广州市海珠区', '大学室友', 0),
(10, 4, '孙磊', '同事', 'https://example.com/contact10.jpg', '13900139010', 'sunlei@example.com', '1989-02-14', '深圳市福田区', '技术总监', 1);

-- 礼簿表测试数据
INSERT INTO gift_book (id, user_id, book_name, book_type, book_date, description, total_amount, total_income, total_expense) VALUES
(1, 1, '张伟婚礼礼簿', 'wedding', '2026-01-15', '表哥张伟的婚礼', 5000.00, 5000.00, 0.00),
(2, 1, '2026年春节礼簿', 'other', '2026-01-28', '2026年春节拜年礼簿', 400.00, 0.00, 400.00),
(3, 2, '杨丽婚礼礼簿', 'wedding', '2026-01-18', '闺蜜杨丽的婚礼', 600.00, 0.00, 600.00),
(4, 3, '郑红生日礼簿', 'birthday', '2026-01-26', '大学室友郑红的生日聚会', 250.00, 0.00, 250.00);

-- 人情记录表测试数据
INSERT INTO transaction_record (id, user_id, gift_book_id, gift_book_name, contact_id, contact_name, relation_type, transaction_direction, transaction_mode, amount, gift_name, gift_value, transaction_date, remark, image_urls) VALUES
(1, 1, 1, '张伟婚礼礼簿', 1, '张伟', '亲戚', 'income', 'cash', 500.00, NULL, NULL, '2026-01-15', '表哥结婚，随礼', NULL),
(2, 1, NULL, NULL, 2, '李娜', '朋友', 'expense', 'gift', NULL, '生日蛋糕', 300.00, '2026-01-20', '生日礼物', NULL),
(3, 1, NULL, NULL, 3, '王强', '同事', 'expense', 'cash', 200.00, NULL, NULL, '2026-01-25', '部门聚餐AA制', NULL),
(4, 1, 2, '2026年春节礼簿', 4, '刘芳', '同学', 'expense', 'cash', 400.00, NULL, NULL, '2026-01-28', '春节拜年', NULL),
(5, 2, NULL, NULL, 5, '陈明', '亲戚', 'expense', 'gift', NULL, '空气净化器', 800.00, '2026-01-10', '舅舅搬家', NULL),
(6, 2, 3, '杨丽婚礼礼簿', 6, '杨丽', '朋友', 'expense', 'cash', 600.00, NULL, NULL, '2026-01-18', '闺蜜结婚', NULL),
(7, 2, NULL, NULL, 7, '周杰', '同事', 'expense', 'cash', 150.00, NULL, NULL, '2026-01-22', '团建活动', NULL),
(8, 3, NULL, NULL, 8, '吴刚', '亲戚', 'expense', 'gift', NULL, '茶叶礼盒', 350.00, '2026-01-12', '堂弟生日', NULL),
(9, 3, 4, '郑红生日礼簿', 9, '郑红', '朋友', 'expense', 'cash', 250.00, NULL, NULL, '2026-01-26', '同学聚会', NULL),
(10, 4, NULL, NULL, 10, '孙磊', '同事', 'expense', 'cash', 500.00, NULL, NULL, '2026-01-30', '春节红包', NULL);

-- 预算设置表测试数据
INSERT INTO budget_setting (id, user_id, budget_type, budget_amount, current_amount, budget_year, budget_month, budget_quarter) VALUES
(1, 1, 'monthly', 2000.00, 1450.00, 2026, 1, NULL),
(2, 1, 'quarterly', 5000.00, 1450.00, 2026, NULL, 1),
(3, 1, 'yearly', 20000.00, 1450.00, 2026, NULL, NULL),
(4, 2, 'monthly', 3000.00, 1550.00, 2026, 1, NULL),
(5, 2, 'quarterly', 8000.00, 1550.00, 2026, NULL, 1),
(6, 3, 'monthly', 1500.00, 600.00, 2026, 1, NULL),
(7, 3, 'yearly', 15000.00, 600.00, 2026, NULL, NULL),
(8, 4, 'monthly', 2500.00, 500.00, 2026, 1, NULL),
(9, 4, 'quarterly', 6000.00, 500.00, 2026, NULL, 1);

-- 余额记录表测试数据
INSERT INTO balance_record (id, user_id, contact_id, contact_name, balance, last_transaction_date, last_transaction_amount) VALUES
(1, 1, 1, '张伟', 200.00, '2026-01-15', 500.00),
(2, 1, 2, '李娜', -100.00, '2026-01-20', 300.00),
(3, 1, 3, '王强', 0.00, '2026-01-25', 200.00),
(4, 2, 5, '陈明', 300.00, '2026-01-10', 800.00),
(5, 2, 6, '杨丽', -200.00, '2026-01-18', 600.00),
(6, 3, 8, '吴刚', 150.00, '2026-01-12', 350.00),
(7, 3, 9, '郑红', 0.00, '2026-01-26', 250.00),
(8, 4, 10, '孙磊', 100.00, '2026-01-30', 500.00);

-- 通知提醒表测试数据
INSERT INTO notification_reminder (id, user_id, contact_id, contact_name, reminder_type, reminder_title, reminder_content, reminder_date, reminder_time, is_repeat, repeat_type, is_notified) VALUES
(1, 1, 1, '张伟', 'birthday', '张伟生日提醒', '表哥张伟的生日是5月15日，记得准备礼物', '2026-05-15', '09:00:00', 1, 'yearly', 0),
(2, 1, 2, '李娜', 'birthday', '李娜生日提醒', '朋友李娜的生日是8月20日，记得准备礼物', '2026-08-20', '09:00:00', 1, 'yearly', 0),
(3, 1, 3, '王强', 'custom', '部门聚餐提醒', '部门聚餐时间：1月25日18:00，地点：XX餐厅', '2026-01-25', '18:00:00', 0, NULL, 1),
(4, 2, 5, '陈明', 'birthday', '陈明生日提醒', '舅舅陈明的生日是7月8日，记得准备礼物', '2026-07-08', '09:00:00', 1, 'yearly', 0),
(5, 2, 6, '杨丽', 'anniversary', '杨丽结婚纪念日', '闺蜜杨丽的结婚纪念日是1月18日', '2026-01-18', '09:00:00', 1, 'yearly', 1),
(6, 3, 8, '吴刚', 'birthday', '吴刚生日提醒', '堂弟吴刚的生日是4月22日，记得准备礼物', '2026-04-22', '09:00:00', 1, 'yearly', 0),
(7, 3, 9, '郑红', 'custom', '同学聚会提醒', '高中同学聚会时间：1月26日14:00，地点：XX酒店', '2026-01-26', '14:00:00', 0, NULL, 1),
(8, 4, 10, '孙磊', 'birthday', '孙磊生日提醒', '同事孙磊的生日是2月14日，记得准备礼物', '2026-02-14', '09:00:00', 1, 'yearly', 0);
