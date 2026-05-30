# Spring Skills — 用 Spring AI 打造你的智能技能引擎

> 当 AI 遇上 Spring Boot，技能不再是代码里的硬编码，而是一个个即插即用的文件夹。

---

## 这是什么？

Spring Skills 是一个基于 **Spring Boot 3.5 + Spring AI** 构建的智能技能框架。它的核心理念很简单：

**把 AI 的能力封装成「技能」，像插件一样即插即用。**

你不需要写一行 Java 代码，只需要在 `resources/skills/` 下新建一个文件夹，放两个文件——`skill.yaml` 和 `prompt.md`，应用启动时会自动发现并注册这个技能。

更酷的是，技能还可以**自动调用外部 API**，把获取到的数据丢给 AI 处理，实现「数据获取 → AI 加工」的完整链路。

---

## 架构一览

```
┌─────────────────────────────────────────────────────┐
│                    REST API 层                       │
│              GET/POST /api/skills/**                 │
└──────────────────────┬──────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────┐
│                  SkillService                        │
│     ┌───────────┐  ┌──────────────┐                 │
│     │ API 调用  │→│ Prompt 模板   │                 │
│     │ (可选)    │  │ 变量注入     │                 │
│     └───────────┘  └──────┬───────┘                 │
└───────────────────────────┼──────────────────────────┘
                            │
┌───────────────────────────▼──────────────────────────┐
│              Spring AI ChatClient                     │
│           (DeepSeek / OpenAI / DashScope)             │
└───────────────────────────┬──────────────────────────┘
                            │
┌───────────────────────────▼──────────────────────────┐
│               SkillRegistry                          │
│     启动时自动扫描 classpath:/skills/ 下所有子目录     │
│     解析 skill.yaml → 读取 prompt.md → 注册到内存     │
└──────────────────────────────────────────────────────┘
```

---

## 快速开始

### 1. 环境要求

- JDK 25+
- Maven 3.8+
- 一个 AI 服务的 API Key（DeepSeek / OpenAI / 阿里云 DashScope 均可）

### 2. 配置 AI 服务

在 `application.yaml` 中配置你的 AI 提供商。框架使用 Spring AI 的 OpenAI 兼容协议，所以 DeepSeek、OpenAI、甚至阿里云的兼容模式都可以直接对接：

**DeepSeek（推荐，性价比高）：**

```yaml
spring:
  ai:
    openai:
      api-key: ${DEEPSEEK_API_KEY:sk-your-key}
      base-url: https://api.deepseek.com
      chat:
        options:
          model: deepseek-chat
          temperature: 0.7
```

**OpenAI：**

```yaml
spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY:sk-your-key}
      chat:
        options:
          model: gpt-4o
          temperature: 0.7
```

### 3. 启动应用

```bash
mvn spring-boot:run
```

启动后你会看到：

```
=================================
  Spring Skills Application Started
  Skills API: http://localhost:8080/api/skills
=================================
```

### 4. 查看已注册的技能

```bash
curl http://localhost:8080/api/skills
```

---

## 技能的标准结构

每个技能就是一个文件夹，包含两个文件：

```
resources/skills/
├── translator/          ← 技能文件夹（名称随意）
│   ├── skill.yaml       ← 技能元数据
│   └── prompt.md        ← 提示词模板
├── summarizer/
│   ├── skill.yaml
│   └── prompt.md
├── coder/
│   ├── skill.yaml
│   └── prompt.md
└── 60s-news/
    ├── skill.yaml
    └── prompt.md
```

### skill.yaml — 技能元数据

```yaml
name: translator                  # 技能唯一标识
description: 多语言翻译技能        # 技能描述
version: 1.0.0                    # 版本号
author: hfh                       # 作者
category: language                # 分类
parameters:                       # 参数定义（用于 prompt 模板变量）
  source_language: 源语言
  target_language: 目标语言
```

### prompt.md — 提示词模板

支持 `{{变量名}}` 占位符，运行时会自动替换：

```markdown
你是一个专业的多语言翻译专家。你的任务是将用户提供的文本从{{source_language}}翻译为{{target_language}}。

翻译要求：
1. 保持原文的语义和语气
2. 使用地道自然的目标语言表达
3. 对于专业术语，提供准确的翻译

请翻译以下内容：
```

### 带外部 API 的技能

如果你的技能需要先获取外部数据再交给 AI 处理，可以在 `skill.yaml` 中添加 `api` 配置：

```yaml
name: 60s-news
description: 每日60秒新闻速览，自动获取最新新闻并由AI整理摘要
version: 1.0.0
author: hfh
category: news
parameters:
  format: 输出格式
api:
  url: https://60s.viki.moe/v2/60s    # API 地址
  method: GET                           # HTTP 方法
  response_key: data                    # 从 JSON 中提取指定字段（可选）
  headers:                              # 自定义请求头（可选）
    Authorization: Bearer xxx
  body: '{"key":"value"}'              # 请求体（可选，POST 时使用）
```

对应的 `prompt.md` 中使用 `{{api_response}}` 接收 API 返回的数据：

```markdown
你是一个专业的新闻编辑助手。

以下是今日获取的原始新闻数据：
{{api_response}}

请按照以下要求整理新闻：
1. 将每条新闻提炼为简洁明了的标题
2. 按重要性排序
3. 对重要新闻添加简短点评
```

**执行流程：**

```
调用技能 → 请求外部 API → 获取响应 → 注入 {{api_response}} → AI 处理 → 返回结果
```

---

## API 接口

| 方法 | 路径 | 说明 |
|------|------|------|
| `GET` | `/api/skills` | 列出所有已注册的技能 |
| `GET` | `/api/skills/{name}` | 获取指定技能的详情 |
| `GET` | `/api/skills/{name}/prompt` | 获取指定技能的提示词 |
| `POST` | `/api/skills/{name}/execute` | 执行指定技能 |
| `POST` | `/api/skills/execute` | 通过请求体指定技能执行 |

---

## 使用示例

### 翻译技能

```bash
curl -X POST http://localhost:8080/api/skills/translator/execute \
  -H "Content-Type: application/json" \
  -d '{
    "variables": {
      "source_language": "中文",
      "target_language": "英文"
    },
    "userInput": "今天天气真好"
  }'
```

响应：

```json
{
  "skillName": "translator",
  "result": "The weather is really nice today.",
  "success": true,
  "errorMessage": null
}
```

### 文本摘要技能

```bash
curl -X POST http://localhost:8080/api/skills/summarizer/execute \
  -H "Content-Type: application/json" \
  -d '{
    "variables": {
      "summary_length": "简短",
      "focus_area": "核心观点"
    },
    "userInput": "Spring AI 是一个用于构建 AI 应用的框架..."
  }'
```

### 代码生成技能

```bash
curl -X POST http://localhost:8080/api/skills/coder/execute \
  -H "Content-Type: application/json" \
  -d '{
    "variables": {
      "language": "Java",
      "framework": "Spring Boot"
    },
    "userInput": "实现一个用户注册接口，包含参数校验"
  }'
```

### 60秒新闻技能（带 API 调用）

```bash
curl -X POST http://localhost:8080/api/skills/60s-news/execute \
  -H "Content-Type: application/json" \
  -d '{}'
```

不需要传 `userInput`，技能会自动请求新闻 API，把数据交给 AI 整理。

---

## 内置技能

| 技能 | 分类 | 说明 | 是否调用 API |
|------|------|------|:---:|
| `translator` | language | 多语言翻译 | ❌ |
| `summarizer` | text-processing | 文本摘要 | ❌ |
| `coder` | development | 代码生成 | ❌ |
| `60s-news` | news | 每日新闻速览 | ✅ |

---

## 如何新增一个技能？

只需 3 步：

**1.** 在 `src/main/resources/skills/` 下新建文件夹，比如 `my-skill/`

**2.** 创建 `skill.yaml`：

```yaml
name: my-skill
description: 我的自定义技能
version: 1.0.0
author: you
category: custom
parameters:
  key1: 参数1说明
  key2: 参数2说明
```

**3.** 创建 `prompt.md`：

```markdown
你是一个{{key1}}专家。请根据以下内容进行{{key2}}：

{{user_input}}
```

重启应用，新技能自动注册。就是这么简单。

如果需要调用外部 API，在 `skill.yaml` 中加上 `api` 配置即可。

---

## 项目结构

```
src/main/java/com/hfh/api/
├── APIApplication.java                          # 启动类
└── skill/
    ├── model/
    │   ├── Skill.java                           # 技能元数据模型
    │   ├── SkillApiConfig.java                  # API 配置模型
    │   ├── SkillExecutionRequest.java           # 执行请求
    │   └── SkillExecutionResponse.java          # 执行响应
    ├── config/
    │   ├── SkillProperties.java                 # 配置属性
    │   └── SkillAutoConfiguration.java          # ChatClient 自动配置
    ├── registry/
    │   └── SkillRegistry.java                   # 技能自动发现与注册
    ├── executor/
    │   └── SkillApiExecutor.java                # 外部 API 请求执行器
    ├── service/
    │   └── SkillService.java                    # 技能执行服务
    └── controller/
        └── SkillController.java                 # REST API
```

---

## 技术栈

- **Spring Boot** 3.5.14
- **Spring AI** 1.1.7（OpenAI 兼容协议）
- **DeepSeek** / OpenAI / DashScope（可切换）
- **SnakeYAML** 解析技能元数据
- **RestTemplate** 调用外部 API

---

## License

MIT
