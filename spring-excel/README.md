# Spring Boot + EasyExcel 实战

基于 Spring Boot 3.x 的 Excel 导入导出完整方案，集成阿里云 OSS、Knife4j 文档。

## 技术栈

| 库 | 版本 | 用途 |
|---|---|---|
| [EasyExcel](https://github.com/alibaba/easyexcel) | 3.1.3 | Excel 读写核心 |
| [Knife4j](https://doc.xiaominfo.com/) | 4.1.0 | Swagger 增强 UI |
| MyBatis | 3.0.0 | ORM 持久层 |
| 阿里云 OSS | 3.16.1 | 文件存储 |

## 核心依赖

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>easyexcel</artifactId>
    <version>3.1.3</version>
</dependency>
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-openapi3-jakarta-spring-boot-starter</artifactId>
    <version>4.1.0</version>
</dependency>
```

---

## 一、EasyExcel 导出

### 1. 实体注解映射

```java
@Data
public class Emp {
    @ExcelProperty("ID")
    private int id;

    @ExcelProperty("用户名")
    private String names;

    @ExcelProperty("工资")
    private double salary;

    @ColumnWidth(20)
    @ExcelProperty("头像")
    private String photo;
}
```

**关键注解**：
- `@ExcelProperty("列名")` — 映射表头
- `@ColumnWidth(20)` — 设置列宽
- `@DateTimeFormat` / `@NumberFormat` — 格式化日期/数字

### 2. 导出接口

```java
@GetMapping("/exportExcel")
public void exportData(HttpServletResponse response) throws IOException {
    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    response.setCharacterEncoding("utf-8");
    String fileName = URLEncoder.encode("用户表", StandardCharsets.UTF_8).replaceAll("\\+", "%20");

    List<Emp> data = excelMapper.selectEmpAll();
    response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

    EasyExcel.write(response.getOutputStream(), Emp.class)
             .sheet("用户表")
             .doWrite(data);
}
```

**一行代码完成导出**：`EasyExcel.write(输出流, 实体类).sheet(名称).doWrite(数据)`

---

## 二、EasyExcel 导入

### 同步读取（小文件）

```java
@PostMapping("/importExcel")
public Integer importData(MultipartFile file) {
    InputStream inputStream = file.getInputStream();
    List<Emp> list = EasyExcel.read(inputStream)
            .head(Emp.class)           // 对应实体类
            .sheet(0)                  // 第几个 sheet（从0开始）
            .headRowNumber(1)          // 表头占几行
            .doReadSync();             // 返回 List<T>

    for (Emp emp : list) {
        // 入库逻辑...
    }
    return 1;
}
```

### 异步监听器（大文件 + 校验）

```java
public class UserListener extends AnalysisEventListener<Emp> {

    @Override
    public void invoke(Emp data, AnalysisContext context) {
        // 每解析一行回调，可做数据校验
        if (StrUtil.isBlank(data.getNames())) {
            throw new RuntimeException("第" + (context.readRowHolder().getRowIndex() + 1) + "行名称为空");
        }
        // 逐条入库或批量收集后入库
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) {
        if (exception instanceof ExcelDataConvertException e) {
            int col = e.getColumnIndex() + 1;
            int row = e.getRowIndex() + 1;
            throw new RuntimeException("第" + row + "行第" + col + "列数据格式错误");
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 全部解析完毕回调
    }
}

// 使用方式
EasyExcel.read(inputStream)
        .head(Emp.class)
        .registerReadListener(new UserListener())   // 注册监听器
        .sheet(0)
        .doRead();                                  // 异步模式用 doRead()
```

**同步 vs 异步选择**：
| 场景 | 推荐方式 |
|---|---|
| 数据量 < 1万行 | `doReadSync()` 简单直接 |
| 数据量大 / 需要校验 | `registerReadListener()` + `doRead()` |

---

## 三、Knife4j 接口文档

### 配置

```yaml
knife4j:
  enable: true
  setting:
    language: zh_cn
```

### 注解使用

```java
@RestController
public class ExcelController {

    @Operation(summary = "导出Excel")
    @GetMapping("/exportExcel")
    public void exportData(HttpServletResponse response) { ... }

    @Operation(summary = "导入Excel")
    @PostMapping("/importExcel")
    public Integer importData(MultipartFile file) { ... }
}
```

访问地址：
- Swagger UI: `http://localhost:9090/swagger-ui/index.html`
- Knife4j 增强版: `http://localhost:9090/doc.html`

---

## 四、阿里云 OSS 文件上传

```java
public static String uploadImage(MultipartFile file) throws IOException {
    String endpoint = "http://oss-cn-guangzhou.aliyuncs.com";
    String accessKeyId = "your-access-key-id";
    String accessKeySecret = "your-access-key-secret";

    OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

    String fileName = UUID.randomUUID().toString().replace("-", "") +
                      "." + FilenameUtils.getExtension(file.getOriginalFilename());

    ossClient.putObject("bucket-name", fileName, file.getInputStream());
    ossClient.shutdown();

    return "https://your-domain.oss-cn-guangzhou.aliyuncs.com/" + fileName;
}
```

---

## 项目结构

```
src/main/java/com/example/
├── common/                  # 公共模块
│   ├── config/              # 全局异常处理
│   └── resp/                # 统一返回 Result
├── springboot/
│   ├── controller/          # 控制层（Excel 导入导出接口）
│   ├── entity/              # 实体类（带 @ExcelProperty 注解）
│   ├── dto/                 # 参数接收对象
│   ├── mapper/              # MyBatis Mapper
│   └── utils/               # 工具类
│       ├── UserListener.java    # EasyExcel 监听器
│       └── uploadUtil.java      # OSS 上传工具
```

## 启动方式

```bash
# 1. 修改 application.yml 中的数据库连接信息
# 2. 执行 resources/db_ssmbuild.sql 初始化数据库
# 3. 启动 ExportExcelApplication
# 4. 访问 http://localhost:9090/doc.html 查看接口文档
```
