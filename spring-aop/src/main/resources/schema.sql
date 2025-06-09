CREATE TABLE sys_oper_log
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    operation   VARCHAR(255) NOT NULL COMMENT '操作内容',
    method      VARCHAR(255) COMMENT '方法名',
    params      TEXT COMMENT '请求参数',
    ip          VARCHAR(50) COMMENT '请求IP',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);