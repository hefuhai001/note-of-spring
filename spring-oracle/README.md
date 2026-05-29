# Spring Boot + Oracle 数据库连接

基于 **Spring Boot 3.4.0** + **Oracle JDBC** 的数据库连接示例。

## 技术栈

| 组件 | 版本 |
|------|------|
| Spring Boot | 3.4.0 |
| Java | 17 |
| Oracle JDBC (ojdbc8) | 19.3.0.0 |

## 核心依赖

```xml
<!-- Oracle JDBC 驱动 -->
<dependency>
    <groupId>com.oracle.database.jdbc</groupId>
    <artifactId>ojdbc8</artifactId>
    <version>19.3.0.0</version>
</dependency>

<!-- JDBC 操作（生产环境建议加为正式依赖） -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
```

## 版本选择指南

**ojdbc8 vs ojdbc11 vs ojdbc10：**

| 驱动 | 适用 JDK | 说明 |
|------|---------|------|
| `ojdbc8` | JDK 8 - JDK 11 | **推荐**，兼容性最好 |
| `ojdbc10` | JDK 10+ | 较新特性支持 |
| `ojdbc11` | JDK 11+ | 最新版，仅支持 JDK 11+ |

**Oracle 版本与驱动对应关系：**

| Oracle 数据库版本 | 推荐驱动版本 |
|------------------|-------------|
| 12c R2 (12.2) | 12.2.0.1+ |
| 18c / 19c | 19.x (本项目使用) |
| 21c | 21.x |

> ⚠️ **注意**：驱动版本应 ≥ 数据库版本，否则可能遇到兼容性问题。

## 配置文件

### application.properties

```properties
# 数据源配置
spring.datasource.url=jdbc:oracle:thin:@//localhost:1521/ORCL
spring.datasource.username=你的用户名
spring.datasource.password=你的密码
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

# JPA 配置（可选）
spring.jpa.database-platform=org.hibernate.dialect.Oracle12cDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### URL 格式说明

```
jdbc:oracle:thin:@//主机:端口/SID
jdbc:oracle:thin:@//主机:端口/服务名   -- 推荐
jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=主机)(PORT=1521))(CONNECT_DATA=(SERVICE_NAME=服务名)))  -- TNS 格式
```

## 快速验证

运行测试类验证连接：

```bash
mvn test -Dtest=DatabaseConnectionTest
```

或直接执行 [DatabaseConnectionTest.java](src/test/java/com/example/oracle/DatabaseConnectionTest.java)：

```java
@Test
public void testDatabaseConnection() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    String sql = "SELECT 1 FROM DUAL";
    Map<String, Object> result = jdbcTemplate.queryForMap(sql);
    System.out.println(result); // {1=1} 表示连接成功
}
```

## 常见问题

### 1. ORA-28040: No matching authentication protocol

**原因**：Oracle 12c+ 默认禁用了旧版认证协议

**解决**：在 Oracle 服务器端修改 `sqlnet.ora`：
```sql
SQLNET.ALLOWED_LOGON_VERSION_SERVER=8
```

### 2. ORA-12505: Listener refused connection

**原因**：SID 或服务名错误

**排查**：
```sql
-- 查询服务名
SELECT name, value FROM v$parameter WHERE name LIKE '%service%';
-- 或查询实例名
SELECT instance_name FROM v$instance;
```

### 3. 连接超时

**解决**：添加连接池参数
```properties
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
```

### 4. Maven 依赖下载失败

Oracle JDBC 在 Maven Central 可用，如遇问题可手动安装：

```bash
mvn install:install-file \
  -Dfile=ojdbc8.jar \
  -DgroupId=com.oracle.database.jdbc \
  -DartifactId=ojdbc8 \
  -Dversion=19.3.0.0 \
  -Dpackaging=jar
```

## 项目结构

```
spring-oracle/
├── src/main/java/com/example/oracle/
│   └── SpringOracleApplication.java      # 启动类
├── src/main/resources/
│   ├── application.properties             # 主配置
│   ├── application-dev.properties         # 开发环境
│   └── application-test.properties        # 测试环境
└── src/test/java/com/example/oracle/
    ├── DatabaseConnectionTest.java        # 连接测试
    └── SpringOracleApplicationTests.java  # 启动测试
```

## 参考链接

- [Oracle JDBC 官方文档](https://docs.oracle.com/en/database/oracle/oracle-database/)
- [Spring Boot Data Access 文档](https://docs.spring.io/spring-boot/docs/current/reference/html/data.html)

---

**License**: MIT
