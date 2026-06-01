# MySQL 主从复制示例项目实施计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 创建一个基于 Spring Boot + MyBatis-Plus + ShardingSphere-JDBC 的 MySQL 主从复制示例项目，包含 Docker Compose 环境配置、完整 CRUD 操作和读写分离演示

**Architecture:** 使用 ShardingSphere-JDBC 作为数据库中间件实现读写分离，Docker Compose 部署 MySQL 主从集群（1主1从），MyBatis-Plus 作为 ORM 框架，提供 REST API 接口展示读写分离效果

**Tech Stack:**
- Spring Boot 3.5.14
- MyBatis-Plus 3.5.9
- ShardingSphere-JDBC 5.8.1
- MySQL 8.4 (Docker)
- Docker Compose

---

## 文件结构总览

### 新建文件：
```
docker-compose.yml                          # Docker Compose 配置
src/main/resources/db/schema.sql            # 数据库建表脚本
src/main/java/com/hfh/api/entity/User.java  # 用户实体类
src/main/java/com/hfh/api/mapper/UserMapper.java  # Mapper 接口
src/main/java/com/hfh/api/service/UserService.java  # 服务接口
src/main/java/com/hfh/api/service/UserServiceImpl.java  # 服务实现
src/main/java/com/hfh/api/controller/UserController.java  # 控制器
src/test/java/com/hfh/api/UserControllerTest.java  # 测试类
```

### 修改文件：
```
pom.xml                                    # 添加依赖
src/main/resources/application.yaml        # 添加数据源配置
```

---

## Task 1: 更新 Maven 依赖配置

**Files:**
- Modify: `pom.xml`

**目标:** 添加 MyBatis-Plus 和 ShardingSphere-JDBC 依赖

- [ ] **Step 1: 在 pom.xml 中添加 MyBatis-Plus 和 ShardingSphere 依赖**

在 `<dependencies>` 标签内添加以下依赖（在 `mysql-connector-j` 之后）：

```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
    <version>3.5.9</version>
</dependency>

<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>shardingsphere-jdbc-core</artifactId>
    <version>5.8.1</version>
</dependency>
```

**验证:** 确保依赖坐标正确，版本号与设计文档一致

---

## Task 2: 创建 Docker Compose 配置

**Files:**
- Create: `docker-compose.yml`

**目标:** 创建 MySQL 主从集群的 Docker Compose 配置文件

- [ ] **Step 1: 编写 docker-compose.yml 文件**

```yaml
version: '3.8'

services:
  mysql-master:
    image: mysql:8.4
    container_name: mysql-master
    environment:
      MYSQL_ROOT_PASSWORD: ${DB_PASSWORD}
      MYSQL_DATABASE: master_slave_db
    ports:
      - "3307:3306"
    volumes:
      - master-data:/var/lib/mysql
      - ./master.cnf:/etc/mysql/conf.d/master.cnf
    command: --server-id=1 --log-bin=mysql-bin --binlog-do-db=master_slave_db
    networks:
      - mysql-network

  mysql-slave:
    image: mysql:8.4
    container_name: mysql-slave
    environment:
      MYSQL_ROOT_PASSWORD: ${DB_PASSWORD}
      MYSQL_DATABASE: master_slave_db
    ports:
      - "3308:3306"
    volumes:
      - slave-data:/var/lib/mysql
      - ./slave.cnf:/etc/mysql/conf.d/slave.cnf
    command: --server-id=2 --relay-log=relay-bin --read-only=1
    networks:
      - mysql-network
    depends_on:
      - mysql-master

volumes:
  master-data:
  slave-data:

networks:
  mysql-network:
    driver: bridge
```

- [ ] **Step 2: 创建 MySQL 主库配置文件 master.cnf**

```ini
[mysqld]
server-id=1
log-bin=mysql-bin
binlog-format=ROW
binlog-do-db=master_slave_db
gtid-mode=ON
enforce-gtid-consistency=ON
```

- [ ] **Step 3: 创建 MySQL 从库配置文件 slave.cnf**

```ini
[mysqld]
server-id=2
relay-log=relay-bin
relay-log-index=relay-bin.index
read-only=1
gtid-mode=ON
enforce-gtid-consistency=ON
```

**验证:** 配置文件包含正确的主从复制参数，端口映射为 3307（主）和 3308（从）

---

## Task 3: 创建数据库初始化脚本

**Files:**
- Create: `src/main/resources/db/schema.sql`

**目标:** 创建 User 表的建表 SQL 脚本

- [ ] **Step 1: 编写 schema.sql 文件**

```sql
CREATE DATABASE IF NOT EXISTS master_slave_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE master_slave_db;

CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `email` VARCHAR(100) COMMENT '邮箱',
    `phone` VARCHAR(20) COMMENT '手机号',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
```

**验证:** 表结构包含必要的字段，使用 utf8mb4 字符集，有适当的索引

---

## Task 4: 配置应用数据源

**Files:**
- Modify: `src/main/resources/application.yaml`

**目标:** 配置 ShardingSphere-JDBC 的读写分离规则

- [ ] **Step 1: 更新 application.yaml 配置**

```yaml
spring:
  application:
    name: spring-mysql-slave
  shardingsphere:
    datasource:
      names: master,slave
      master:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://localhost:3307/master_slave_db?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
        username: root
        password: ${DB_PASSWORD}
      slave:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://localhost:3308/master_slave_db?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
        username: root
        password: ${DB_PASSWORD}
    rules:
      readwrite-splitting:
        data-sources:
          ds:
            write-data-source-name: master
            read-data-source-names:
              - slave
            load-balancer-name: round-robin
        load-balancers:
          round-robin:
            type: ROUND_ROBIN
    props:
      sql-show: true

mybatis-plus:
  mapper-locations: classpath*:/mapper/**/*.xml
  type-aliases-package: com.hfh.api.entity
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

**验证:** 配置包含主从数据源、读写分离规则、负载均衡策略和 MyBatis-Plus 配置

---

## Task 5: 创建实体类

**Files:**
- Create: `src/main/java/com/hfh/api/entity/User.java`

**目标:** 创建 User 实体类，映射到 user 表

- [ ] **Step 1: 编写 User 实体类**

```java
package com.hfh.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String email;

    private String phone;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
```

**验证:** 使用 MyBatis-Plus 注解，字段与表结构匹配，包含 Lombok 注解简化代码

---

## Task 6: 创建 Mapper 接口

**Files:**
- Create: `src/main/java/com/hfh/api/mapper/UserMapper.java`

**目标:** 继承 BaseMapper 提供 CRUD 操作

- [ ] **Step 1: 编写 UserMapper 接口**

```java
package com.hfh.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hfh.api.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
```

**验证:** 继承 BaseMapper<User>，添加 @Mapper 注解

---

## Task 7: 创建 Service 层

**Files:**
- Create: `src/main/java/com/hfh/api/service/UserService.java`
- Create: `src/main/java/com/hfh/api/service/UserServiceImpl.java`

**目标:** 实现业务逻辑层

- [ ] **Step 1: 编写 UserService 接口**

```java
package com.hfh.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hfh.api.entity.User;

public interface UserService extends IService<User> {
}
```

- [ ] **Step 2: 编写 UserServiceImpl 实现类**

```java
package com.hfh.api.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hfh.api.entity.User;
import com.hfh.api.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
```

**验证:** 继承 IService 和 ServiceImpl，使用标准 MyBatis-Plus 服务层模式

---

## Task 8: 创建 Controller 层

**Files:**
- Create: `src/main/java/com/hfh/api/controller/UserController.java`

**目标:** 提供 REST API 接口用于 CRUD 操作和测试读写分离

- [ ] **Step 1: 编写 UserController 类**

```java
package com.hfh.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hfh.api.entity.User;
import com.hfh.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public User create(@RequestBody User user) {
        userService.save(user);
        return user;
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @GetMapping
    public List<User> list() {
        return userService.list(new QueryWrapper<>());
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        userService.updateById(user);
        return user;
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return userService.removeById(id);
    }
}
```

**验证:** 包含完整的 CRUD 接口，使用 RESTful 风格，注入 UserService

---

## Task 9: 创建集成测试

**Files:**
- Create: `src/test/java/com/hfh/api/UserControllerTest.java`

**目标:** 测试 CRUD 操作并验证读写分离效果

- [ ] **Step 1: 编写集成测试类**

```java
package com.hfh.api;

import com.hfh.api.entity.User;
import com.hfh.api.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserMapper userMapper;

    private User testUser;

    @BeforeEach
    void setUp() {
        userMapper.delete(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>());
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.phone = "13800138000";
    }

    @Test
    void testCreateUser() {
        ResponseEntity<User> response = restTemplate.postForEntity("/api/users", testUser, User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("testuser", response.getBody().getUsername());
    }

    @Test
    void testGetUserById() {
        userMapper.insert(testUser);

        ResponseEntity<User> response = restTemplate.getForEntity("/api/users/{id}", User.class, testUser.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("testuser", response.getBody().getUsername());
    }

    @Test
    void testListUsers() {
        userMapper.insert(testUser);

        User anotherUser = new User();
        anotherUser.setUsername("another");
        anotherUser.setEmail("another@test.com");
        userMapper.insert(anotherUser);

        ResponseEntity<User[]> response = restTemplate.getForEntity("/api/users", User[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length >= 2);
    }

    @Test
    void testUpdateUser() {
        userMapper.insert(testUser);
        testUser.setEmail("updated@example.com");

        HttpEntity<User> requestEntity = new HttpEntity<>(testUser);
        ResponseEntity<User> response = restTemplate.exchange(
                "/api/users/" + testUser.getId(),
                HttpMethod.PUT,
                requestEntity,
                User.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("updated@example.com", response.getBody().getEmail());
    }

    @Test
    void testDeleteUser() {
        userMapper.insert(testUser);

        ResponseEntity<Boolean> response = restTemplate.exchange(
                "/api/users/" + testUser.getId(),
                HttpMethod.DELETE,
                null,
                Boolean.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());

        User deleted = userMapper.selectById(testUser.getId());
        assertNull(deleted);
    }
}
```

**验证:** 测试覆盖所有 CRUD 操作，使用 TestRestTemplate 进行 HTTP 测试

---

## Task 10: 添加 Lombok 依赖（如果需要）

**Files:**
- Modify: `pom.xml`

**目标:** 确保 Lombok 依赖存在以支持实体类的注解

- [ ] **Step 1: 检查并添加 Lombok 依赖**

在 pom.xml 中确认或添加：

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```

**注意:** Spring Boot Starter Parent 通常已包含 Lombok，此步骤为可选检查

---

## 执行顺序建议

1. **Task 1** → **Task 2** → **Task 3** (基础设施：依赖和环境)
2. **Task 4** → **Task 5** → **Task 6** → **Task 7** → **Task 8** (核心代码：配置到控制器)
3. **Task 9** → **Task 10** (测试和完善)

每个 Task 可以独立提交，便于代码审查和回滚。

---

## 启动和测试流程

### 1. 启动 Docker 环境
```bash
docker-compose up -d
docker exec mysql-master mysql -uroot -p'${DB_PASSWORD}' -e "CREATE USER IF NOT EXISTS 'repl'@'%' IDENTIFIED BY 'repl'; GRANT REPLICATION SLAVE ON *.* TO 'repl'@'%'; FLUSH PRIVILEGES;"
docker exec mysql-slave mysql -uroot -p'${DB_PASSWORD}' -e "CHANGE MASTER TO MASTER_HOST='mysql-master', MASTER_USER='repl', MASTER_PASSWORD='repl', MASTER_AUTO_POSITION=1; START SLAVE; SHOW SLAVE STATUS\G"
```

### 2. 初始化数据库
```bash
docker exec -i mysql-master mysql -uroot -p'${DB_PASSWORD}' < src/main/resources/db/schema.sql
```

### 3. 启动应用
```bash
mvn spring-boot:run
```

### 4. 测试接口
```bash
# 创建用户 (应该路由到主库)
curl -X POST http://localhost:8080/api/users -H "Content-Type: application/json" -d '{"username":"zhangsan","email":"zhangsan@test.com","phone":"13800138000"}'

# 查询用户 (应该路由到从库)
curl http://localhost:8080/api/users/1

# 列出所有用户 (应该路由到从库)
curl http://localhost:8080/api/users

# 更新用户 (应该路由到主库)
curl -X PUT http://localhost:8080/api/users/1 -H "Content-Type: application/json" -d '{"id":1,"username":"zhangsan","email":"newemail@test.com","phone":"13800138000"}'

# 删除用户 (应该路由到主库)
curl -X DELETE http://localhost:8080/api/users/1
```

### 5. 观察日志
查看控制台输出中的 SQL 日志，ShardingSphere 会显示实际执行的数据源名称（master 或 slave）

---

## 成功标准验证清单

- [ ] `docker-compose up -d` 成功启动两个 MySQL 容器
- [ ] 主从复制状态正常（SHOW SLAVE STATUS 显示 Slave_IO_Running 和 Slave_SQL_Running 都是 Yes）
- [ ] 应用启动无错误
- [ ] POST /api/users 返回成功，日志显示写入 master
- [ ] GET /api/users/{id} 返回成功，日志显示查询 slave
- [ ] GET /api/users 返回成功，日志显示查询 slave
- [ ] PUT /api/users/{id} 返回成功，日志显示更新 master
- [ ] DELETE /api/users/{id} 返回成功，日志显示删除 master
- [ ] 所有单元测试通过
