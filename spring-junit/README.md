# Spring JUnit 测试实战

## 技术栈总览

| 库 | 版本 | 用途 |
|---|---|---|
| JUnit 5 | Jupiter | 测试引擎和注解 |
| Mockito | mockito-core | Mock 对象 |
| AssertJ | assertj-core | 流式断言 |
| Spring Boot Test | 3.1.10 | 集成测试上下文 |

---

## 一、JUnit 5 核心用法

### 1. 基础注解

```java
@Test  // 测试方法
@BeforeEach  // 每个测试前执行
@AfterEach  // 每个测试后执行
@BeforeAll  // 所有测试前执行（static）
@AfterAll  // 所有测试后执行（static）
@Disabled  // 跳过测试
@DisplayName("自定义测试名称")  // 显示名
```

### 2. 断言方式对比

```java
// ❌ JUnit 原生断言（可读性差）
assertEquals(expected, actual);
assertTrue(list.size() > 0);
assertNull(result);

// ✅ AssertJ 流式断言（推荐）
assertThat(actual).isEqualTo(expected);
assertThat(list).isNotEmpty().hasSize(3);
assertThat(result).isNull();
assertThat(string).contains("hello").startsWith("prefix");
```

---

## 二、Mockito 实战技巧

### 1. 注解式 Mock（推荐）

```java
@SpringBootTest
class MyServiceTest {
    @Mock
    private AnotherService anotherService;  // Mock 依赖
    
    @InjectMocks
    private MyService myService;  // 自动注入 Mock 对象
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // 初始化注解
    }
}
```

### 2. Mock 行为定义

```java
// 固定返回值
when(anotherService.processInput("test")).thenReturn("processed");

// 抛异常
when(service.method()).thenThrow(new RuntimeException());

// 无返回值方法
doNothing().when(service).voidMethod();

// 真实调用（部分 Mock）
when(service.method()).thenCallRealMethod();
```

### 3. 参数匹配器

```java
// 任意参数
when(service.method(anyString())).thenReturn("result");

// 具体条件
when(service.method(argThat(s -> s.length() > 5))).thenReturn("ok");

// 多参数混用（要么全用匹配器，要么全不用）
when(service.method(eq("id"), anyInt())).thenReturn("result");
```

### 4. 验证调用

```java
// 验证是否调用
verify(anotherService).processInput("test");

// 验证调用次数
verify(anotherService, times(1)).processInput(anyString());
verify(never()).method();  // 从未调用
verify(atLeast(2)).method();  // 至少2次

// 验证顺序
InOrder inOrder = inOrder(service);
inOrder.verify(service).method1();
inOrder.verify(service).method2();
```

---

## 三、Spring Boot 测试策略

### 1. 单元测试 vs 集成测试

```java
// ✅ 单元测试（快，不启动容器）
@ExtendWith(MockitoExtension.class)
class MyServiceTest { ... }

// ⚠️ 集成测试（慢，启动完整上下文）
@SpringBootTest
class MyServiceIntegrationTest { ... }

// 🚀 切片测试（只加载部分上下文）
@WebMvcTest(Controller.class)  // 测试 Controller 层
@DataJpaTest  // 测试 Repository 层
```

### 2. 本项目示例解析

```java
@SpringBootTest  // 启动完整 Spring 上下文
class MyServiceTest {
    @Mock
    private AnotherService anotherService;  // Mock 依赖服务
    
    @InjectMocks
    private MyService myService;  // 被测对象
    
    @Test
    void testDoSomething() {
        // Arrange - 准备数据
        String input = "test";
        when(anotherService.processInput(input)).thenReturn("test processed");
        
        // Act - 执行操作
        String result = myService.doSomething(input);
        
        // Assert - 验证结果
        assertThat(result).isEqualTo("Result: test processed");
    }
}
```

---

## 四、AssertJ 高频用法速查

```java
// 字符串
assertThat(str).isEqualTo("expected")
              .contains("sub")
              .startsWith("pre")
              .hasSize(10)
              .matches("\\d+");

// 集合
assertThat(list).isNotEmpty()
               .hasSize(3)
               .containsExactly("a", "b", "c")
               .extracting("name")
               .contains("Alice");

// 对象
assertThat(obj).isNotNull()
              .isEqualToComparingFieldByField(expected)
              .usingRecursiveComparison()  // 递归比较所有字段
              .isEqualTo(expected);

// 异常
assertThatThrownBy(() -> service.method())
    .isInstanceOf(RuntimeException.class)
    .hasMessage("error")
    .hasNoCause();
```

---

## 五、最佳实践清单

- [ ] **优先使用 AssertJ** 替代 JUnit 原生断言
- [ ] **单元测试用 `@ExtendWith(MockitoExtension.class)`**，别用 `@SpringBootTest`
- [ ] **遵循 AAA 模式**：Arrange → Act → Assert
- [ ] **测试方法命名**：`test + 方法名 + 场景 + 预期结果`
- [ ] **每个测试只验证一件事**
- [ ] **Mock 外部依赖**（数据库、HTTP、文件系统）
- [ ] **集成测试才启动 Spring 容器**

---

## 六、Maven 依赖配置要点

```xml
<!-- 排除旧版 JUnit 4 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
    <exclusions>
        <exclusion>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
        </exclusion>
    </exclusions>
</dependency>

<!-- 显式引入 JUnit 5 -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-engine</artifactId>
    <scope>test</scope>
</dependency>

<!-- Mockito 和 AssertJ -->
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-core</artifactId>
    <scope>test</scope>
</dependency>
```

---

## 运行测试

```bash
# 执行所有测试
mvn test

# 执行指定测试类
mvn test -Dtest=MyServiceTest

# 并行执行（pom.xml 配置 surefire 插件）
mvn test -Djunit.jupiter.execution.parallel.enabled=true
```

---

**核心原则**：测试代码也是代码，保持简洁、可读、可靠。
