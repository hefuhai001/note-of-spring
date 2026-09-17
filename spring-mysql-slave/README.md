# 🚀 Spring Boot + MySQL 主从复制实战指南

> **从零搭建生产级读写分离架构** | ShardingSphere-JDBC + MyBatis-Plus + Docker

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.14-green.svg)](https://spring.io/projects/spring-boot)
[![MyBatis-Plus](https://img.shields.io/badge/MyBatis--Plus-3.5.9-blue.svg)](https://baomidou.com/)
[![ShardingSphere](https://img.shields.io/badge/ShardingSphere-5.8.1-orange.svg)](https://shardingsphere.apache.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.4-blue.svg)](https://www.mysql.com/)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED.svg)](https://www.docker.com/)

---

## ✨ 为什么需要主从复制？

在实际生产环境中，随着用户量增长，数据库往往会成为系统瓶颈：

**❌ 单库问题：**
- 读写在同一个数据库，性能瓶颈明显
- 无法横向扩展，升级硬件成本高昂
- 单点故障风险，数据安全性差

**✅ 主从复制优势：**
- **读写分离**：写操作走主库，读操作走从库，性能提升 **3-5 倍**
- **负载均衡**：多个从库分担读取压力
- **数据备份**：从库实时同步，容灾能力强
- **高可用性**：主库故障可快速切换到从库

---

## 📖 本项目能学到什么？

通过这个实战项目，你将掌握：

- 🔧 **Docker Compose** 部署 MySQL 主从集群（1主1从）
- ⚙️ **ShardingSphere-JDBC** 实现透明化读写分离
- 🎯 **MyBatis-Plus** 简化 CRUD 开发
- 🧪 **集成测试** 验证路由正确性
- 📊 **日志监控** 观察 SQL 实际执行路径

**适合人群：** Java后端开发、架构师、技术爱好者

---

## 🏗️ 架构设计

### 整体架构图

```
┌─────────────────────────────────────────────────────────────┐
│                    Spring Boot Application                  │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│   ┌──────────────┐    ┌──────────────┐    ┌──────────────┐  │
│   │ Controller   │───▶│   Service    │───▶│    Mapper    │  │
│   │ (REST API)   │    │ (Business)   │    │ (MyBatis+)   │  │
│   └──────────────┘    └──────────────┘    └──────┬───────┘  │
│                                               │             │
│                    ┌──────────────────────────┘             │
│                    ▼                                        │
│          ┌─────────────────────┐                            │
│          │  ShardingSphere-JDBC │ ◄── 读写分离中间件          │
│          │  (智能SQL路由引擎)    │                            │
│          └──────────┬──────────┘                            │
│                     │                                       │
└─────────────────────┼───────────────────────────────────────┘
                      │
           ┌──────────┴──────────┐
           ▼                     ▼
   ┌──────────────┐      ┌──────────────┐
   │  Master DB   │      │  Slave DB    │
   │  (主库:3307) │◄────▶│  (从库:3308)  │
   │              │ 复制  │              │
   │  ✓ 写操作    │──────▶│  ✓ 读操作     │
   │  ✓ INSERT    │      │  ✓ SELECT    │
   │  ✓ UPDATE    │      │              │
   │  ✓ DELETE    │      │  (只读模式)   │
   └──────────────┘      └──────────────┘
```

### 数据流向说明

| 操作类型 | SQL 示例 | 路由目标 | 说明 |
|---------|---------|---------|------|
| **写入** | `INSERT / UPDATE / DELETE` | → Master (3307) | 保证数据一致性 |
| **查询** | `SELECT * FROM user` | → Slave (3308) | 减轻主库压力 |
| **事务** | `@Transactional` | → Master (3307) | 事务内强制走主库 |

---

## 🛠️ 技术栈详解

### 核心组件

| 组件 | 版本 | 作用 |
|------|------|------|
| **Spring Boot** | 3.5.14 | 应用框架，自动配置 |
| **MyBatis-Plus** | 3.5.9 | ORM 框架，简化数据库操作 |
| **ShardingSphere-JDBC** | 5.8.1 | 数据库中间件，实现读写分离 |
| **MySQL** | 8.4 | 关系型数据库 |
| **Docker Compose** | - | 容器编排，快速部署环境 |
| **Lombok** | - | 减少样板代码 |

### 为什么选择 ShardingSphere？

市面上主流的读写分离方案对比：

| 方案 | 优点 | 缺点 | 适用场景 |
|------|------|------|---------|
| **应用层手动切换** | 简单直接 | 代码侵入性强，维护成本高 | 小项目 |
| **MySQL Proxy** | 透明代理 | 性能有损耗，单点故障 | 中小项目 |
| **ShardingSphere-JDBC** ✅ | **零侵入、高性能、功能全** | 学习曲线稍陡 | **生产环境推荐** |
| **MyCat** | 功能强大 | 配置复杂，社区活跃度下降 | 大型集群 |

**本项目选择 ShardingSphere 的原因：**
1. **Jar包形式嵌入应用**，无需额外部署代理服务
2. **对业务代码零侵入**，无需修改 DAO 层代码
3. **支持多种分片策略**，未来扩展方便
4. **Apache 顶级项目**，社区活跃，文档完善

---

## 🚀 快速开始（5分钟上手）

### 前置条件

确保你的开发环境已安装：

- ☑️ **JDK 17+** （推荐 JDK 21）
- ☑️ **Maven 3.6+**
- ☑️ **Docker Desktop** （用于运行 MySQL）
- ☑️ **Git** （可选）

### 第一步：克隆项目

```bash
git clone <your-repo-url>
cd spring-mysql-slave
```

### 第二步：启动 MySQL 主从集群 ⏱️ 30秒

```bash
# 启动 Docker 容器（主库 + 从库）
docker-compose up -d

# 查看容器状态
docker-compose ps
```

**预期输出：**
```
Name           Command             State           Ports
----------------------------------------------------------------------
mysql-master   docker-entrypoint.sh mysqld ... Up     0.0.0.0:3307->3306/tcp
mysql-slave    docker-entrypoint.sh mysqld ... Up     0.0.0.0:3308->3306/tcp
```

🎉 **恭喜！** 你的 MySQL 主从环境已经启动成功！

### 第三步：配置主从复制关系 🔗

这是最关键的一步，让从库能够同步主库的数据：

#### 3.1 在主库创建复制用户

```bash
docker exec -it mysql-master mysql -uroot -p'${DB_PASSWORD}' -e "
CREATE USER 'repl'@'%' IDENTIFIED BY 'repl@123456';
GRANT REPLICATION SLAVE ON *.* TO 'repl'@'%';
FLUSH PRIVILEGES;
SHOW MASTER STATUS;
"
```

**输出示例（记住 File 和 Position 的值）：**
```
+------------------+----------+--------------+------------------+
| File             | Position | Binlog_Do_DB | Binlog_Ignore_DB |
+------------------+----------+--------------+------------------+
| mysql-bin.000003 |      1234| master_slave_db|                  |
+------------------+----------+--------------+------------------+
```

> ⚠️ **重要：** 请记录下 `File` 和 `Position` 的值，下一步要用！

#### 3.2 在从库配置复制源

将下面的命令中的 `mysql-bin.000003` 和 `1234` 替换成你上一步看到的实际值：

```bash
docker exec -it mysql-slave mysql -uroot -p'${DB_PASSWORD}' -e "
CHANGE MASTER TO
  MASTER_HOST='mysql-master',
  MASTER_USER='repl',
  MASTER_PASSWORD='repl@123456',
  MASTER_LOG_FILE='mysql-bin.000003',
  MASTER_LOG_POS=1234,
  GET_MASTER_PUBLIC_KEY=1;

START SLAVE;
SHOW SLAVE STATUS\G
"
```

**验证复制是否成功：**

查看 `Slave_IO_Running` 和 `Slave_SQL_Running` 是否都为 `Yes`：
```
*************************** 1. row ***************************
               Slave_IO_Running: Yes
              Slave_SQL_Running: Yes
...
             Seconds_Behind_Master: 0  ← 这个值应该是 0 或很小的数
```

如果看到两个 **Yes**，恭喜！🎉 主从复制已经配置成功了！

### 第四步：初始化数据库表结构

```bash
# 在主库执行建表脚本（会自动同步到从库）
docker exec -i mysql-master mysql -uroot -p'${DB_PASSWORD}' master_slave_db < src/main/resources/db/schema.sql
```

**验证表是否创建成功：**
```bash
docker exec -it mysql-master mysql -uroot -p'${DB_PASSWORD}' -e "USE master_slave_db; SHOW TABLES; DESC user;"
```

### 第五步：启动 Spring Boot 应用

```bash
# 方式一：Maven 命令启动
mvn spring-boot:run

# 方式二：打包后运行
mvn clean package -DskipTests
java -jar target/spring-mysql-slave-0.0.1-SNAPSHOT.jar
```

**看到以下日志表示启动成功：**
```
Started ApiApplication in 3.456 seconds
```

默认端口：**http://localhost:8080**

---

## 💡 实战演示：验证读写分离效果

现在让我们通过实际的 HTTP 请求来验证读写分离是否生效！

### 测试工具准备

你可以使用以下任一工具：
- **curl** （命令行）
- **Postman** （GUI 客户端）
- **IDEA HTTP Client** （推荐）

### 场景 1：创建用户（写操作 → 应该走主库）

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "zhangsan",
    "email": "zhangsan@example.com",
    "phone": "13800138000"
  }'
```

**返回结果：**
```json
{
  "id": 1,
  "username": "zhangsan",
  "email": "zhangsan@example.com",
  "phone": "13800138000",
  "createTime": "2024-01-15T10:30:00",
  "updateTime": "2024-01-15T10:30:00"
}
```

**观察控制台日志：** 👀
```
Actual SQL: INSERT INTO user ( username, email, phone ) VALUES ( ?, ?, ? )
>>> Logic SQL: INSERT INTO user ...
>>> SQL Route: dataSourceName: master  ← 这里显示走了 master 数据源！
```

### 场景 2：查询用户（读操作 → 应该走从库）

```bash
# 根据 ID 查询
curl http://localhost:8080/api/users/1

# 查询所有用户
curl http://localhost:8080/api/users
```

**返回结果：**
```json
{
  "id": 1,
  "username": "zhangsan",
  "email": "zhangsan@example.com",
  "phone": "13800138000",
  "createTime": "2024-01-15T10:30:00",
  "updateTime": "2024-01-15T10:30:00"
}
```

**观察控制台日志：** 👀
```
Actual SQL: SELECT id,username,email,phone,create_time,update_time FROM user WHERE id=?
>>> Logic SQL: SELECT ...
>>> SQL Route: dataSourceName: slave  ← 这里显示走了 slave 数据源！
```

### 场景 3：更新用户（写操作 → 应该走主库）

```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "username": "zhangsan",
    "email": "newemail@example.com",
    "phone": "13900139000"
  }'
```

**控制台日志应显示：** `dataSourceName: master`

### 场景 4：删除用户（写操作 → 应该走主库）

```bash
curl -X DELETE http://localhost:8080/api/users/1
```

**控制台日志应显示：** `dataSourceName: master`

### 批量测试脚本

如果你想一次性测试所有接口，可以使用这个脚本：

```bash
#!/bin/bash
BASE_URL="http://localhost:8080/api/users"

echo "=== 1. 创建用户 ==="
curl -s -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d '{"username":"lisi","email":"lisi@test.com","phone":"13700137000"}' | jq .

echo -e "\n=== 2. 查询用户列表 ==="
curl -s $BASE_URL | jq .

echo -e "\n=== 3. 更新用户 ==="
curl -s -X PUT "$BASE_URL/2" \
  -H "Content-Type: application/json" \
  -d '{"username":"lisi","email":"updated@test.com"}' | jq .

echo -e "\n=== 4. 删除用户 ==="
curl -s -X DELETE "$BASE_URL/2" | jq .
```

保存为 `test-api.sh` 并运行：`chmod +x test-api.sh && ./test-api.sh`

---

## 🔍 核心原理深度解析

### ShardingSphere 如何实现读写分离？

#### 1. SQL 解析与路由流程

```
应用程序发送 SQL
       ↓
┌─────────────────────────────────────────┐
│         ShardingSphere-JDBC            │
│                                         │
│  Step 1: SQL 解析                       │
│  ├── 词法分析（Lexer）                   │
│  ├── 语法分析（Parser）                  │
│  └── 生成抽象语法树（AST）               │
│                                         │
│  Step 2: SQL 路由                       │
│  ├── 判断 SQL 类型（SELECT vs 其他）     │
│  ├── 检查事务上下文                     │
│  └── 应用路由规则                       │
│                                         │
│  Step 3: SQL 改写                       │
│  ├── 添加数据源标识                     │
│  └── 参数替换                           │
│                                         │
│  Step 4: SQL 执行                       │
│  └── 连接对应的数据源执行               │
│                                         │
│  Step 5: 结果合并                       │
│  └── 返回统一的结果集                   │
└─────────────────────────────────────────┘
       ↓
   目标数据库（Master 或 Slave）
```

#### 2. 关键配置解读

查看 [application.yaml](src/main/resources/application.yaml)：

```yaml
spring:
  shardingsphere:
    datasource:
      names: master,slave          # 定义数据源名称
      master:                      # 主库配置
        type: com.zaxxer.hikari.HikariDataSource  # 连接池类型
        url: jdbc:mysql://localhost:3307/master_slave_db
        username: root
        password: ${DB_PASSWORD}
      slave:                       # 从库配置
        url: jdbc:mysql://localhost:3308/master_slave_db
        # ... 其他配置同上

    rules:
      readwrite-splitting:         # 读写分离规则
        data-sources:
          ds:                      # 逻辑数据源名称
            write-data-source-name: master      # 写操作数据源
            read-data-source-names:            # 读操作数据源列表
              - slave
            load-balancer-name: round-robin    # 负载均衡策略名称
        load-balancers:
          round-robin:             # 轮询策略定义
            type: ROUND_ROBIN      # 多个从库时轮询分配请求

    props:
      sql-show: true               # 打印 SQL 日志（调试时开启）
```

#### 3. 负载均衡策略说明

当有多个从库时，ShardingSphere 支持：

| 策略类型 | 说明 | 适用场景 |
|---------|------|---------|
| **ROUND_ROBIN** ✅ | 轮询分配 | 从库性能相近，均匀分流 |
| **RANDOM** | 随机分配 | 简单场景 |
| **WEIGHT** | 按权重分配 | 从库性能不均，强者多承担 |

当前项目只有 1 个从库，所以策略影响不大。但如果你扩展到 3 个从库，轮询策略会让每个从库承担 1/3 的读流量。

---

## 📁 项目结构说明

```
spring-mysql-slave/
├── docker-compose.yml                 # Docker 编排文件（MySQL 主从集群）
├── master.cnf                         # 主库 MySQL 配置（开启 binlog）
├── slave.cnf                          # 从库 MySQL 配置（只读模式）
├── pom.xml                            # Maven 依赖配置
│
├── src/main/
│   ├── java/com/hfh/api/
│   │   ├── ApiApplication.java        # Spring Boot 启动类
│   │   │
│   │   ├── config/                    # 配置类（预留）
│   │   │
│   │   ├── entity/
│   │   │   └── User.java              # 用户实体类（MyBatis-Plus 注解）
│   │   │
│   │   ├── mapper/
│   │   │   └── UserMapper.java        # 数据访问层（继承 BaseMapper）
│   │   │
│   │   ├── service/
│   │   │   ├── UserService.java       # 服务接口（继承 IService）
│   │   │   └── UserServiceImpl.java   # 服务实现（继承 ServiceImpl）
│   │   │
│   │   └── controller/
│   │       └── UserController.java    # REST 控制器（CRUD 接口）
│   │
│   └── resources/
│       ├── application.yaml           # 应用配置（ShardingSphere 核心）
│       └── db/
│           └── schema.sql             # 数据库初始化脚本
│
├── src/test/java/com/hfh/api/
│   └── UserControllerTest.java        # 集成测试（验证读写分离）
│
└── docs/
    └── superpowers/
        ├── specs/                     # 设计文档
        └── plans/                     # 实施计划
```

### 各层职责说明

| 层级 | 类名 | 职责 | 关键注解/继承 |
|------|------|------|---------------|
| **Controller** | UserController | 接收HTTP请求，参数校验 | `@RestController`, `@RequestMapping` |
| **Service** | UserServiceImpl | 业务逻辑处理 | 继承 `ServiceImpl` |
| **Mapper** | UserMapper | 数据库CRUD操作 | 继承 `BaseMapper`, `@Mapper` |
| **Entity** | User | 数据表映射 | `@TableName`, `@TableId`, `@Data` |

---

## 🧪 运行测试

### 单元测试 & 集成测试

```bash
# 运行所有测试
mvn test

# 运行指定测试类
mvn test -Dtest=UserControllerTest

# 运行指定测试方法
mvn test -Dtest=UserControllerTest#testCreateUser
```

### 测试覆盖范围

[UserControllerTest.java](src/test/java/com/example/hfh/UserControllerTest.java) 包含：

| 测试方法 | 验证点 | 预期行为 |
|---------|--------|---------|
| `testCreateUser()` | POST 创建用户 | 写入主库，返回带ID的用户对象 |
| `testGetUserById()` | GET 根据ID查询 | 从从库读取，数据一致 |
| `testListUsers()` | GET 查询列表 | 从从库读取多条记录 |
| `testUpdateUser()` | PUT 更新用户 | 写入主库，数据更新成功 |

---

## ❓ 常见问题 FAQ

### Q1: 启动时报错 "Connection refused"？

**原因：** MySQL 容器未启动或端口未就绪

**解决方案：**
```bash
# 1. 检查容器状态
docker-compose ps

# 2. 如果容器未启动
docker-compose up -d

# 3. 等待 10 秒让 MySQL 完全初始化
sleep 10

# 4. 再次启动应用
mvn spring-boot:run
```

### Q2: 主从复制报错 "Last_IO_Error: ..."？

**常见原因及解决：**

1. **防火墙问题：** 确保 Docker 网络正常
   ```bash
   docker network ls
   docker network inspect mysql-network
   ```

2. **密码特殊字符：** 密码中包含 `@` 或 `!` 时需用单引号包裹
   ```bash
   # 正确 ✅
   -p'${DB_PASSWORD}'
   
   # 错误 ❌
   -p${DB_PASSWORD}
   ```

3. **binlog 位置错误：** 重新获取 Master Status 并配置
   ```bash
   docker exec -it mysql-master mysql -uroot -p'${DB_PASSWORD}' -e "SHOW MASTER STATUS;"
   ```

### Q3: 如何确认读写分离真的生效了？

**方法 1：查看应用日志**

在 [application.yaml](src/main/resources/application.yaml) 中已配置：
```yaml
props:
  sql-show: true  # 开启 SQL 日志
```

启动后会打印类似这样的日志：
```
Actual SQL: SELECT ... FROM user
Logic SQL: SELECT ... FROM user
SQL Route: dataSourceName: slave  ← 看这里！
```

**方法 2：分别查看主从库的数据**

```bash
# 查看主库数据
docker exec -it mysql-master mysql -uroot -p'${DB_PASSWORD}' -e "SELECT * FROM master_slave_db.user;"

# 查看从库数据（应该相同）
docker exec -it mysql-slave mysql -uroot -p'${DB_PASSWORD}' -e "SELECT * FROM master_slave_db.user;"
```

**方法 3：监控数据库连接数**

```bash
# 主库应该有更多的写连接
docker exec -it mysql-master mysql -uroot -p'${DB_PASSWORD}' -e "SHOW PROCESSLIST;" | grep -E "INSERT|UPDATE|DELETE"

# 从库应该有更多的读连接
docker exec -it mysql-slave mysql -uroot -p'${DB_PASSWORD}' -e "SHOW PROCESSLIST;" | grep SELECT
```

### Q4: 可以添加多个从库吗？

**当然可以！** 修改 [docker-compose.yml](docker-compose.yml) 和 [application.yaml](src/main/resources/application.yaml) 即可：

**1. docker-compose.yml 添加新的从库：**
```yaml
  mysql-slave-2:
    image: mysql:8.4
    container_name: mysql-slave-2
    environment:
      MYSQL_ROOT_PASSWORD: ${DB_PASSWORD}
      MYSQL_DATABASE: master_slave_db
    ports:
      - "3309:3306"  # 新端口
    volumes:
      - slave-data-2:/var/lib/mysql
      - ./slave.cnf:/etc/mysql/conf.d/slave.cnf
    command: --server-id=3 --relay-log=relay-bin --read-only=1
    networks:
      - mysql-network
    depends_on:
      - mysql-master
```

**2. application.yaml 添加从库数据源：**
```yaml
datasource:
  names: master,slave,slave-2  # 新增 slave-2
  
  slave-2:                     # 新增从库配置
    type: com.zaxxer.hikari.HikariDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3309/master_slave_db?...
    username: root
    password: ${DB_PASSWORD}

rules:
  readwrite-splitting:
    data-sources:
      ds:
        read-data-source-names:
          - slave
          - slave-2    # 新增
```

这样 ShardingSphere 会自动在 `slave` 和 `slave-2` 之间轮询分配读请求！

### Q5: 生产环境需要注意什么？

| 方面 | 建议 |
|------|------|
| **安全性** | 不要在配置文件中使用明文密码，建议使用 Jasypt 加密或 Vault |
| **连接池** | 调优 HikariCP 参数（最大连接数、超时时间等） |
| **监控** | 集成 Prometheus + Grafana 监控数据库指标 |
| **备份** | 定期备份主库，并验证备份可恢复性 |
| **故障转移** | 使用 MHA 或 Orchestrator 实现自动故障转移 |
| **延迟监控** | 监控 `Seconds_Behind_Master`，超过阈值告警 |

---

## 🎯 进阶玩法

### 1. 添加更多实体表

参照 User 实体的模式，可以轻松添加 Order、Product 等表：

```java
// src/main/java/com/hfh/api/entity/Order.java
@Data
@TableName("order")
public class Order {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    private String orderNo;
    private BigDecimal amount;
    // ...
}
```

### 2. 自定义 SQL 查询

虽然 BaseMapper 提供了常用方法，但你也可以自定义复杂查询：

```java
// UserMapper.java
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    @Select("SELECT * FROM user WHERE email = #{email}")
    User findByEmail(@Param("email") String email);
    
    @Select("SELECT COUNT(*) FROM user WHERE create_time > #{startTime}")
    long countUsersAfter(@Param("startTime") LocalDateTime startTime);
}
```

### 3. 分页查询

MyBatis-Plus 内置分页插件，非常方便：

```java
// UserServiceImpl.java
public IPage<User> pageQuery(int current, int size) {
    Page<User> page = new Page<>(current, size);
    return this.page(page);
}

// Controller
@GetMapping("/page")
public IPage<User> page(
    @RequestParam(defaultValue = "1") int current,
    @RequestParam(defaultValue = "10") int size) {
    return userService.pageQuery(current, size);
}
```

访问：`GET /api/users/page?current=1&size=10`

### 4. 逻辑删除

在实际项目中，通常不会物理删除数据，而是使用逻辑删除：

```java
// User.java
@TableLogic
private Integer deleted;  // 0-未删除 1-已删除

// application.yaml
mybatis-plus:
  global-config:
    db-config:
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0
```

这样调用 `removeById()` 时，实际执行的 SQL 是：
```sql
UPDATE user SET deleted = 1 WHERE id = ?
```

而不是真正的 DELETE！

---

## 📊 性能优化建议

### 1. 连接池调优

```yaml
spring:
  shardingsphere:
    datasource:
      master:
        hikari:                        # HikariCP 配置
          minimum-idle: 5              # 最小空闲连接
          maximum-pool-size: 20        # 最大连接数
          idle-timeout: 30000          # 空闲超时（毫秒）
          max-lifetime: 1800000        # 连接最大存活时间（30分钟）
          connection-timeout: 30000    # 连接超时时间
```

### 2. SQL 日志优化

**开发环境**（开启详细日志）：
```yaml
mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

**生产环境**（关闭或降级）：
```yaml
mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.nologging.NoLoggingImpl  # 关闭日志
    
spring:
  shardingsphere:
    props:
      sql-show: false  # 关闭 SQL 显示
```

### 3. 索引优化

确保你的查询字段都有适当的索引：

```sql
-- 为常用查询字段添加索引
ALTER TABLE user ADD INDEX idx_email (email);
ALTER TABLE user ADD INDEX idx_phone (phone);
ALTER TABLE user ADD INDEX idx_create_time (create_time);

-- 覆盖索引（避免回表）
ALTER TABLE user ADD INDEX idx_username_email (username, email);
```

---

## 🌟 总结

通过本项目的学习，你已经掌握了：

✅ **理论层面：**
- 理解主从复制的原理和应用场景
- 掌握读写分离的设计思路
- 了解 ShardingSphere 的核心机制

✅ **实践层面：**
- 能够独立搭建 MySQL 主从环境
- 能够配置 ShardingSphere-JDBC 实现读写分离
- 能够使用 MyBatis-Plus 进行高效开发
- 能够编写集成测试验证功能正确性

✅ **进阶能力：**
- 具备扩展为多从库的能力
- 了解生产环境的优化方向
- 掌握常见问题的排查方法

---

## 📚 参考资源

### 官方文档
- [ShardingSphere 官方文档](https://shardingsphere.apache.org/document/current/cn/overview/)
- [MyBatis-Plus 官方文档](https://baomidou.com/pages/24112f/)
- [Spring Boot 官方文档](https://docs.spring.io/spring-boot/docs/current/reference/html/)

### 推荐阅读
- 《高性能MySQL》- Baron Schwartz 等
- 《深入理解MySQL主从原理》- 徐阳炀
- [MySQL官方主从复制文档](https://dev.mysql.com/doc/refman/8.0/en/replication.html)

### 相关项目
- [ShardingSphere Examples](https://github.com/apache/shardingsphere-example)
- [MyBatis-Plus Samples](https://github.com/baomidou/mybatis-plus-samples)

---

## 🤝 贡献指南

欢迎贡献代码、报告 Bug 或提出改进建议！

### 如何贡献？

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

### 代码规范

- 遵循阿里巴巴 Java 开发手册
- 保持代码简洁，注释清晰
- 新增功能请附带单元测试

---

## 📄 许可证

本项目采用 [MIT License](LICENSE) 开源协议。

---

## ⭐ 如果这个项目对你有帮助

别忘记给个 Star ⭐ 支持一下！

你的支持是我持续更新的动力 💪

---

**作者：** [你的名字]  
**邮箱：** [your.email@example.com]  
**博客：** [your-blog-url]  
**最后更新：** 2024年1月

---

<div align="center">

**感谢阅读！如有问题欢迎提 Issue 或联系我 😊**

[回到顶部 ↑](#-spring-boot--mysql-主从复制实战指南)

</div>
