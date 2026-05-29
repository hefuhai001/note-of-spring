# Spring Boot 3 + MyBatis + Oracle 整合示例

> 技术栈：Spring Boot 3.1.4 / Java 21 / MyBatis 3.0.3 / Oracle JDBC 23.3 / HikariCP / Knife4j 4.1

---

## 一、项目依赖一览

| 库 | 版本 | 用途 |
|---|---|---|
| `mybatis-spring-boot-starter` | 3.0.3 | ORM 映射 |
| `ojdbc8` | 23.3.0.23.09 | Oracle 驱动（Oracle 19c+） |
| `druid` | 1.2.22 | 监控/连接池备选 |
| `knife4j-openapi3-jakarta` | 4.1.0 | OpenAPI 3 文档 UI |
| `lombok` | 1.18.32 | 省掉 getter/setter/toString |

---

## 二、MyBatis 整合要点

### 2.1 Mapper 接口 + XML 分离写法

接口标注 `@Mapper`，XML 放在 `src/main/java/com/ali/dao/` 同目录下：

```java
@Mapper
public interface OrderMapper {
    List<Order> queryOrder(Map<String, Object> param);
    List<Order> getAll();
}
```

```xml
<mapper namespace="com.ali.dao.OrderMapper">
    <select id="queryOrder" parameterType="java.util.Map" resultType="com.ali.entity.Order">
        SELECT * FROM boot_order WHERE isDelete = 0 ORDER BY id LIMIT #{start}, #{size}
    </select>
</mapper>
```

### 2.2 关键配置：让 Maven 同时扫描 java 和 resources 下的 XML

MyBatis 默认只扫 `resources` 目录。如果 XML 和 Mapper 接口放一起（java 目录），必须在 `pom.xml` 显式声明：

```xml
<resources>
    <resource>
        <directory>src/main/java</directory>
        <includes>
            <include>**/*.xml</include>
        </includes>
    </resource>
    <resource>
        <directory>src/main/resources</directory>
        <includes>
            <include>**/*.xml</include>
        </includes>
    </resource>
</resources>
```

否则启动报错 `Invalid bound statement`。

### 2.3 SQL 日志打印

```yaml
mybatis:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

开发阶段必开，生产环境换 `Slf4jImpl` 或直接关掉。

---

## 三、Oracle 驱动选型

| Oracle 版本 | 推荐依赖 | groupId |
|---|---|---|
| **19c / 21c / 23c** | `ojdbc8` / `ojdbc11` | `com.oracle.database.jdbc` |
| **11g** | `ojdbc6` | `com.oracle` |

本项目用的 `ojdbc8:23.3.0.23.09`，兼容 Oracle 12c+，**不支持 11g**。如果连 11g，取消 pom 里注释的那段 `ojdbc6` 依赖即可。

> 注意：老版本 11g 驱动的 groupId 是 `com.oracle`，新版本统一为 `com.oracle.database.jdbc`，别混用。

---

## 四、连接池：HikariCP vs Druid

本项目配置文件里实际生效的是 **HikariCP**（Spring Boot 2.x/3.x 默认连接池）：

```yaml
spring:
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    hikari:
      maximum-pool-size: 12
      minimum-idle: 5
      max-lifetime: 400000
      idle-timeout: 30000
      connection-test-query: SELECT 1 FROM DUAL   # Oracle 必须用 DUAL
      pool-name: MyHikariCP
```

**为什么配了 Druid 却没用？** 因为 `spring.datasource.type` 指向了 HikariCP。要切换到 Druid 只需改这一行：

```yaml
type: com.alibaba.druid.pool.DruidDataSource
```

两者对比：

| | HikariCP | Druid |
|---|---|---|
| 性能 | 更快 | 略慢 |
| 监控 | 无内置 Web 页面 | 自带 `druid-spring-boot-starter` 监控台 |
| SQL 防注入 | 无 | 内置 WallFilter |
| 适用场景 | 追求性能 | 需要 SQL 监控/防火墙 |

---

## 五、Knife4j（Swagger 增强）

依赖引入后访问 `http://localhost:{port}/doc.html` 即可看到增强版 API 文档。

Spring Boot 3 使用 Jakarta 命名空间，artifactId 必须 带 `-jakarta`：

```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-openapi3-jakarta-spring-boot-starter</artifactId>
    <version>4.1.0</version>
</dependency>
```

Spring Boot 2.x 则用 `knife4j-openapi3-spring-boot-starter`（不带 jakarta），**版本号不能混**。

---

## 六、多环境配置

```yaml
# application.yml
spring:
  profiles:
    active: dev
```

启动时加载 `application-dev.yml`。切 prod 就把 active 改成 `prod`，或启动参数覆盖：

```bash
java -jar app.jar --spring.profiles.active=prod
```

---

## 七、项目结构

```
src/main/java/com/ali/
├── Main.java                    # 启动类
├── controller/
│   └── OrderController.java     # REST 接口
├── dao/
│   ├── OrderMapper.java         # Mapper 接口
│   └── OrderMapper.xml           # SQL 映射
└── entity/
    └── Order.java               # 实体类（Lombok @Data）
sql/
└── schema.sql                   # 建表语句
```

接口：`GET /order/list` → 查询全部订单

---

## 八、常见坑

1. **Oracle 大小写问题**：Oracle 默认将未加引号的标识符转大写。`BOOT_ORDER` 和 `boot_order` 是同一个表，但 `boot_order` 带引号就是另一个。
2. **HikariCP + Oracle 必须配 `connection-test-query`**：MySQL 可以不配，Oracle 不配会报验证失败，值固定为 `SELECT 1 FROM DUAL`。
3. **MyBatis XML 找不到**：检查 pom 的 `<resources>` 是否包含 `**/*.xml`，这是最常见的新手坑。
4. **Knife4j 404**：确认 artifactId 带 `-jakarta`（Spring Boot 3），且 Controller 方法上有 `@GetMapping` / `@PostMapping` 等注解。
