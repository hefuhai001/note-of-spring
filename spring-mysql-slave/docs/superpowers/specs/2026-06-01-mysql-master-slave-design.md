# MySQL 主从复制示例项目设计文档

## 项目概述

**项目名称**: spring-mysql-slave
**目标**: 创建一个基于 Spring Boot + MyBatis-Plus + ShardingSphere-JDBC 的 MySQL 主从复制示例项目
**目的**: 既要清晰展示主从复制和读写分离的原理，又要具备实际应用的参考价值

## 技术栈

- **框架**: Spring Boot 3.5.14
- **ORM**: MyBatis-Plus 3.5.9
- **数据库中间件**: ShardingSphere-JDBC 5.8.1
- **数据库**: MySQL 8.4 (Docker)
- **连接池**: HikariCP (Spring Boot 默认)

## 架构设计

### 整体架构

```
┌─────────────────────────────────────────┐
│           Spring Boot Application        │
├─────────────────────────────────────────┤
│         Controller Layer                 │
│    └── UserController                   │
├─────────────────────────────────────────┤
│         Service Layer                    │
│    └── UserService                      │
├─────────────────────────────────────────┤
│         Mapper Layer (MyBatis-Plus)      │
│    └── UserMapper                       │
├─────────────────────────────────────────┤
│     ShardingSphere-JDBC (读写分离)       │
│    ├── Master DataSource (写操作)        │
│    └── Slave DataSource  (读操作)        │
└─────────────────────────────────────────┘
          │                │
          ▼                ▼
   ┌────────────┐   ┌────────────┐
   │ Master DB  │◄──│ Slave DB   │
   │ :3307      │   │ :3308      │
   └────────────┘   └────────────┘
```

### 数据库配置

**主库 (Master)**:
- 端口: 3307
- 用途: 处理所有写操作（INSERT, UPDATE, DELETE）
- 角色: 数据源，向从库同步数据

**从库 (Slave)**:
- 端口: 3308
- 用途: 处理所有读操作（SELECT）
- 角色: 接收主库的数据复制

## 核心功能模块

### 1. Docker Compose 配置
- **mysql-master**: 主库实例，开启 binlog
- **mysql-slave**: 从库实例，配置主从复制
- 自动配置主从复制关系

### 2. 数据源配置 (ShardingSphere-JDBC)
- 配置主从数据源
- 设置读写分离规则
- 负载均衡策略: 轮询（Round-Robin）

### 3. 实体与表结构
**User 表**:
```sql
CREATE TABLE user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 4. CRUD 操作示例
- **Create**: 写入主库
- **Read**: 从从库读取
- **Update**: 更新主库
- **Delete**: 删除主库

### 5. 测试验证
- 验证写入操作路由到主库
- 验证读取操作路由到从库
- 验证数据同步延迟情况

## 项目文件结构

```
spring-mysql-slave/
├── docker-compose.yml              # Docker Compose 配置
├── pom.xml                         # Maven 依赖
├── src/main/java/com/hfh/api/
│   ├── ApiApplication.java         # 启动类
│   ├── config/
│   │   └── DataSourceConfig.java   # 数据源配置（可选）
│   ├── controller/
│   │   └── UserController.java     # REST API 控制器
│   ├── entity/
│   │   └── User.java               # 用户实体类
│   ├── mapper/
│   │   └── UserMapper.java         # MyBatis-Plus Mapper
│   └── service/
│       ├── UserService.java        # 服务接口
│       └── UserServiceImpl.java    # 服务实现
├── src/main/resources/
│   ├── application.yaml            # 应用配置
│   └── db/                         # SQL 脚本
│       └── schema.sql              # 建表语句
└── src/test/java/com/hfh/api/
    └── UserControllerTest.java     # 集成测试
```

## 关键依赖

```xml
<!-- MyBatis-Plus -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
    <version>3.5.9</version>
</dependency>

<!-- ShardingSphere JDBC -->
<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>shardingsphere-jdbc-core</artifactId>
    <version>5.8.1</version>
</dependency>
```

## 使用流程

1. **启动环境**: `docker-compose up -d` 启动 MySQL 主从集群
2. **验证复制**: 检查主从复制状态是否正常
3. **启动应用**: 运行 Spring Boot 应用
4. **测试接口**: 通过 REST API 验证读写分离效果
5. **观察日志**: 查看 SQL 路由到主库还是从库

## 设计原则

1. **简洁性**: 代码清晰易懂，适合学习理解
2. **实用性**: 包含生产环境的最佳实践
3. **可扩展性**: 易于扩展为多从库架构
4. **可观测性**: 提供日志和监控接口查看路由情况

## 成功标准

- ✅ Docker Compose 能成功启动主从 MySQL 环境
- ✅ 主从复制正常工作，数据能实时同步
- ✅ 写操作自动路由到主库
- ✅ 读操作自动路由到从库
- ✅ 提供完整的 CRUD 示例和测试用例
