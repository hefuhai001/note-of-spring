-- 用户表
CREATE TABLE t_user
(
    id       BIGINT PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

-- 角色表
CREATE TABLE t_role
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(50) NOT NULL UNIQUE
);

-- 权限表（资源+操作）
CREATE TABLE t_permission
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    permission_code VARCHAR(100) NOT NULL UNIQUE COMMENT '例如 user:add'
);

-- 用户↔角色 多对多
CREATE TABLE t_user_role
(
    user_id BIGINT,
    role_id BIGINT,
    PRIMARY KEY (user_id, role_id)
);

-- 角色↔权限 多对多
CREATE TABLE t_role_permission
(
    role_id       BIGINT,
    permission_id BIGINT,
    PRIMARY KEY (role_id, permission_id)
);