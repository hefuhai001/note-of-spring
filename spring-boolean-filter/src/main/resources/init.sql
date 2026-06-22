-- 商品订单表
CREATE DATABASE IF NOT EXISTS spring_bool_filter
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE spring_bool_filter;

DROP TABLE IF EXISTS product_order;
CREATE TABLE product_order (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    order_no      VARCHAR(64)    NOT NULL COMMENT '订单号',
    product_name  VARCHAR(128)   NOT NULL COMMENT '商品名称',
    quantity      INT            NOT NULL DEFAULT 1 COMMENT '数量',
    unit_price    DECIMAL(10, 2) NOT NULL COMMENT '单价',
    total_amount  DECIMAL(12, 2) NOT NULL COMMENT '总金额',
    customer_name VARCHAR(64)    NOT NULL COMMENT '客户名称',
    status        TINYINT        NOT NULL DEFAULT 0 COMMENT '订单状态：0-待支付 1-已支付 2-已发货 3-已完成 4-已取消',
    deleted       TINYINT        NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    create_time   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_order_no (order_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品订单表';

-- 插入测试数据
INSERT INTO product_order (order_no, product_name, quantity, unit_price, total_amount, customer_name, status) VALUES
('ORD-20240601-001', '华为Mate 60 Pro',       1, 6999.00, 6999.00,  '张三', 1),
('ORD-20240601-002', 'Apple AirPods Pro',      2, 1899.00, 3798.00,  '李四', 2),
('ORD-20240602-001', '机械革命蛟龙16 Pro',      1, 7999.00, 7999.00,  '王五', 0),
('ORD-20240602-002', '罗技G502鼠标',            3, 299.00,  897.00,   '赵六', 3),
('ORD-20240603-001', 'Dell 27寸4K显示器',       1, 3499.00, 3499.00,  '张三', 1);
