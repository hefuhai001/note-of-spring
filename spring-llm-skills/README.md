# Spring LLM Skills — 给 Spring Boot 接上大模型的「手脚」

> 基于 OpenAI Function Calling 协议，让 LLM 能调用你写的任何 Java 方法。

## 它解决什么问题

LLM 只会「说话」，不会「做事」。你想让它查数据库、调外部 API、做数学运算——它做不到。

Spring LLM Skills 做了一件事：**把你的 Java 方法注册成 LLM 可调用的 Tool**，LLM 自己判断什么时候该调用哪个方法，拿到结果后继续回答用户。

整个流程全自动：

```
用户提问 → LLM 判断需要调用 Tool → 执行你的 Java 方法 → 结果回传给 LLM → LLM 组织最终回复
```

## 核心架构

```
┌─────────────┐     ┌──────────────┐     ┌──────────────┐
│   用户输入    │ ──▶ │  SkillService │ ──▶ │   LlmService  │
└─────────────┘     │  (Tool循环)   │     │ (WebClient)   │
                    └──────┬───────┘     └──────┬────────┘
                           │                     │
                    ┌──────▼───────┐      ┌──────▼────────┐
                    │ SkillRegistry│ ◀─── │  OpenAI API   │
                    │  (注册中心)   │      │ /兼容接口      │
                    └──────┬───────┘      └───────────────┘
                           │
              ┌────────────┼────────────┐
              ▼            ▼            ▼
        CalculatorSkill  WeatherSkill  TranslateSkill
        (你自己写的任     (想加几个加几个)
         意Skill实现)
```

三个核心角色：

| 组件 | 职责 |
|------|------|
| [`Skill`](src/main/java/com/example/llm/skills/Skill.java) | 接口，定义一个工具的「名字+描述+参数+执行逻辑」 |
| [`SkillRegistry`](src/main/java/com/example/llm/skills/SkillRegistry.java) | 注册中心，管理所有 Skill 实例 |
| [`SkillService`](src/main/java/com/example/llm/service/SkillService.java) | 编排层，处理 LLM 的 Tool Calling 循环（递归，最多5层防死循环） |

## 快速上手

### 1. 加依赖

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>spring-llm-skills</artifactId>
    <version>1.0.0</version>
</dependency>
```

技术栈要求：**Spring Boot 3.2+ / Java 17**

### 2. 配置 LLM

`application.yml`：

```yaml
llm:
  openai:
    api-key: ${OPENAI_API_KEY:your-key}
    base-url: https://api.openai.com       # 或换成任意 OpenAI 兼容接口
    model: gpt-3.5-turbo                   # 或 gpt-4o / qwen-turbo 等
```

也支持阿里云 DashScope（通义千问），改 `base-url` 和 `model` 即可。

### 3. 写一个 Skill

实现 [`Skill`](src/main/java/com/example/llm/skills/Skill.java) 接口，打上 `@Component`：

```java
@Component
public class CalculatorSkill implements Skill {

    @Override
    public String getName() {
        return "calculate";  // LLM 用这个名字来调用你
    }

    @Override
    public String getDescription() {
        return "执行数学计算。当用户需要进行加减乘除运算时调用。";
        // 这段描述直接喂给 LLM，写得越清楚越好
    }

    @Override
    public Map<String, Object> getParametersSchema() {
        // JSON Schema 格式，告诉 LLM 这个工具接受什么参数
        return Map.of(
            "type", "object",
            "properties", Map.of(
                "expression", Map.of(
                    "type", "string",
                    "description", "数学表达式，如 '(100-20)*0.8'"
                )
            ),
            "required", List.of("expression")
        );
    }

    @Override
    public SkillResult execute(Map<String, Object> arguments) {
        String expression = (String) arguments.get("expression");
        double result = doCalculate(expression);  // 你的业务逻辑
        return SkillResult.success(getName(), Map.of("result", result));
    }
}
```

就这三步，启动应用，Skill 会通过 [`SkillAutoConfiguration`](src/main/java/com/example/llm/config/SkillAutoConfiguration.java) 自动注册到 Registry。

### 4. 调用

```bash
# 智能对话 — LLM 自动判断是否需要调用 Skill
POST /api/chat
{"message": "100 打八折是多少？"}
# → LLM 自动调用 calculate Skill → 返回结果

# 直接指定 Skill（调试用）
POST /api/skill/calculate
{"expression": "100 * 0.8"}

# 查看所有已注册的 Skill
GET /api/skills
```

## 关键设计细节

### Tool Calling 循环机制

[`SkillService.processWithSkills()`](src/main/java/com/example/llm/service/SkillService.java#L35-L37) 是整个框架的核心：

1. 把用户消息 + 所有 Skill 定义发给 LLM
2. 如果 LLM 返回了 `tool_call`，执行对应的 Skill
3. 把 Skill 结果追加到消息历史，再次发给 LLM
4. 重复直到 LLM 返回纯文本（不再调用工具）
5. **深度限制 5 层**，防止 LLM 陷入死循环

```java
// 伪代码展示核心逻辑
callWithToolLoop(messages, depth):
  response = llmService.chat(messages, allTools)
  
  if response 有 tool_call:
    result = skillRegistry.execute(toolName, args)
    messages += [assistant的toolCall, tool的结果]
    return callWithToolLoop(messages, depth + 1)  // 递归
  
  return response.content  // 最终答案
```

### 为什么用 WebFlux

[`LlmService`](src/main/java/com/example/llm/service/LlmService.java) 基于 `WebClient`（非阻塞 HTTP 客户端），返回 `Mono<ChatResponse>`。原因很简单——**等 LLM 响应可能要好几秒**，阻塞线程纯属浪费。Skill 执行也被扔到了 `boundedElastic` 调度器上，不阻塞事件循环。

### 多 Provider 支持

配置里同时留了 OpenAI 和 DashScope 的模板，本质是同一个协议（OpenAI Chat Completions API）。只要接口兼容，换 `base-url` 就能切换模型提供商。

## 内置示例 Skill

| Skill | 功能 | 触发场景 |
|-------|------|----------|
| [`CalculatorSkill`](src/main/java/com/example/llm/skills/impl/CalculatorSkill.java) | 数学计算 | 「帮我算一下...」 |
| [`WeatherSkill`](src/main/java/com/example/llm/skills/impl/WeatherSkill.java) | 天气查询 | 「今天天气怎么样」 |
| [`TranslateSkill`](src/main/java/com/example/llm/skills/impl/TranslateSkill.java) | 翻译 | 「翻译成英文」 |

这些只是演示，生产环境替换成真实的业务逻辑即可（接数据库、调外部 API、读写文件……随你）。

## 项目结构一览

```
spring-llm-skills/
├── config/
│   ├── LlmConfig.java           # LLM 连接配置（@ConfigurationProperties）
│   └── SkillAutoConfiguration.java  # 启动时自动注册所有 Skill Bean
├── controller/
│   └── SkillController.java     # REST API：/api/chat, /api/skills
├── dto/
│   ├── ChatRequest.java         # OpenAI 协议请求体（含 Tool 定义）
│   ├── ChatResponse.java        # OpenAI 协议响应体
│   └── SkillResult.java         # Skill 执行结果封装
├── service/
│   ├── LlmService.java          # 封装 LLM API 调用（WebClient）
│   └── SkillService.java        # 核心：Tool Calling 循环编排
└── skills/
    ├── Skill.java               # 接口定义（必须实现的4个方法）
    ├── SkillRegistry.java       # 注册中心（ConcurrentHashMap）
    └── impl/                    # 内置示例 Skill 实现
```

## 适用场景

- **智能客服**：LLM 回答 + 查订单 / 查物流 / 退换货
- **数据分析助手**：自然语言 → 调用 SQL / 调用 Python 脚本 → 返回结果
- **运维 Copilot**：「重启 xxx 服务」→ 调用 Kubernetes API
- **任何需要 LLM + 业务系统联动的场景**
