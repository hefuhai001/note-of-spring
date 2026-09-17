package com.example.hfh.service;

import com.example.hfh.vo.TemplateConfig;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.*;

@Service
public class VelocityTemplateService {

    private final VelocityEngine velocityEngine;
    private final Map<String, TemplateConfig> templateConfigs;

    public VelocityTemplateService() {
        this.velocityEngine = new VelocityEngine();
        // 配置Velocity引擎
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADERS, "classpath");
        velocityEngine.setProperty("resource.loader.classpath.class", ClasspathResourceLoader.class.getName());
        velocityEngine.setProperty(RuntimeConstants.RUNTIME_LOG_REFERENCE_LOG_INVALID, true);
        velocityEngine.init();

        // 初始化模板配置
        this.templateConfigs = initializeTemplateConfigs();
    }

    public String mergeTemplate(String templateName, Map<String, Object> variables) {
        try {
            // 根据模板名称确定路径
            String templatePath = getTemplatePath(templateName);
            Template template = velocityEngine.getTemplate(templatePath);
            VelocityContext context = new VelocityContext();

            // 添加所有变量到上下文
            if (variables != null) {
                for (Map.Entry<String, Object> entry : variables.entrySet()) {
                    context.put(entry.getKey(), entry.getValue());
                }
            }

            StringWriter writer = new StringWriter();
            template.merge(context, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException("模板处理失败: " + e.getMessage(), e);
        }
    }

    public List<TemplateConfig> getAvailableTemplates() {
        return new ArrayList<>(templateConfigs.values());
    }

    public TemplateConfig getTemplateConfig(String templateName) {
        return templateConfigs.get(templateName);
    }

    private Map<String, TemplateConfig> initializeTemplateConfigs() {
        Map<String, TemplateConfig> configs = new HashMap<>();

        // Java实体类模板配置
        List<TemplateConfig.ParameterInfo> entityParams = Arrays.asList(
                new TemplateConfig.ParameterInfo("packageName", "String", "包名", true),
                new TemplateConfig.ParameterInfo("className", "String", "类名", true),
                new TemplateConfig.ParameterInfo("tableName", "String", "表名", true),
                new TemplateConfig.ParameterInfo("classComment", "String", "类注释", false),
                new TemplateConfig.ParameterInfo("fields", "List<FieldInfo>", "字段列表", true)
        );
        configs.put("entity.vm", new TemplateConfig("entity.vm", "Java实体类模板", entityParams));

        // Controller模板配置
        List<TemplateConfig.ParameterInfo> controllerParams = Arrays.asList(
                new TemplateConfig.ParameterInfo("packageName", "String", "包名", true),
                new TemplateConfig.ParameterInfo("className", "String", "类名", true),
                new TemplateConfig.ParameterInfo("entityNameLower", "String", "实体名小写", true),
                new TemplateConfig.ParameterInfo("idType", "String", "主键类型", true)
        );
        configs.put("controller.vm", new TemplateConfig("controller.vm", "Controller模板", controllerParams));

        // Service接口模板配置
        List<TemplateConfig.ParameterInfo> serviceParams = Arrays.asList(
                new TemplateConfig.ParameterInfo("packageName", "String", "包名", true),
                new TemplateConfig.ParameterInfo("className", "String", "类名", true),
                new TemplateConfig.ParameterInfo("entityName", "String", "实体类名", true)
        );
        configs.put("service.vm", new TemplateConfig("service.vm", "Service接口模板", serviceParams));

        // Service实现类模板配置
        List<TemplateConfig.ParameterInfo> serviceImplParams = Arrays.asList(
                new TemplateConfig.ParameterInfo("packageName", "String", "包名", true),
                new TemplateConfig.ParameterInfo("className", "String", "类名", true),
                new TemplateConfig.ParameterInfo("entityName", "String", "实体类名", true),
                new TemplateConfig.ParameterInfo("mapperName", "String", "Mapper类名", true)
        );
        configs.put("service-impl.vm", new TemplateConfig("service-impl.vm", "Service实现类模板", serviceImplParams));

        // Mapper接口模板配置
        List<TemplateConfig.ParameterInfo> mapperParams = Arrays.asList(
                new TemplateConfig.ParameterInfo("packageName", "String", "包名", true),
                new TemplateConfig.ParameterInfo("className", "String", "类名", true),
                new TemplateConfig.ParameterInfo("entityName", "String", "实体类名", true)
        );
        configs.put("mapper.vm", new TemplateConfig("mapper.vm", "Mapper接口模板", mapperParams));

        // Mapper XML模板配置
        List<TemplateConfig.ParameterInfo> mapperXmlParams = Arrays.asList(
                new TemplateConfig.ParameterInfo("namespace", "String", "命名空间", true),
                new TemplateConfig.ParameterInfo("className", "String", "实体类名", true),
                new TemplateConfig.ParameterInfo("tableName", "String", "表名", true),
                new TemplateConfig.ParameterInfo("fields", "List<FieldInfo>", "字段列表", true),
                new TemplateConfig.ParameterInfo("primaryKey", "FieldInfo", "主键字段", true)
        );
        configs.put("mapper-xml.vm", new TemplateConfig("mapper-xml.vm", "Mapper XML模板", mapperXmlParams));

        // Request BO模板
        List<TemplateConfig.ParameterInfo> boParams = Arrays.asList(
                new TemplateConfig.ParameterInfo("packageName", "String", "包名", true),
                new TemplateConfig.ParameterInfo("className", "String", "类名", true),
                new TemplateConfig.ParameterInfo("classComment", "String", "类注释", false),
                new TemplateConfig.ParameterInfo("fields", "List<FieldInfo>", "字段列表", true)
        );
        configs.put("bo.vm", new TemplateConfig("bo.vm", "Request BO模板", boParams));

        // VO模板
        List<TemplateConfig.ParameterInfo> voParams = Arrays.asList(
                new TemplateConfig.ParameterInfo("packageName", "String", "包名", true),
                new TemplateConfig.ParameterInfo("className", "String", "类名", true),
                new TemplateConfig.ParameterInfo("classComment", "String", "类注释", false),
                new TemplateConfig.ParameterInfo("fields", "List<FieldInfo>", "字段列表", true)
        );
        configs.put("vo.vm", new TemplateConfig("vo.vm", "VO模板", voParams));

        // TS API模板
        List<TemplateConfig.ParameterInfo> tsApiParams = Arrays.asList(
                new TemplateConfig.ParameterInfo("className", "String", "类名", true),
                new TemplateConfig.ParameterInfo("moduleName", "String", "模块名", true),
                new TemplateConfig.ParameterInfo("businessName", "String", "业务名小写", true),
                new TemplateConfig.ParameterInfo("functionName", "String", "功能名", true),
                new TemplateConfig.ParameterInfo("pkColumn", "Object", "主键列", true)
        );
        configs.put("api.vm", new TemplateConfig("api.vm", "TS API模板", tsApiParams));

        // TS Types模板
        List<TemplateConfig.ParameterInfo> tsTypesParams = Arrays.asList(
                new TemplateConfig.ParameterInfo("className", "String", "类名", true),
                new TemplateConfig.ParameterInfo("columns", "List<Object>", "列信息", true),
                new TemplateConfig.ParameterInfo("table", "Object", "表信息", true),
                new TemplateConfig.ParameterInfo("treeCode", "String", "树编码", false)
        );
        configs.put("types.vm", new TemplateConfig("types.vm", "TS Types模板", tsTypesParams));

        // Vue模板
        List<TemplateConfig.ParameterInfo> vueParams = Arrays.asList(
                new TemplateConfig.ParameterInfo("className", "String", "类名", true),
                new TemplateConfig.ParameterInfo("businessName", "String", "业务名小写", true),
                new TemplateConfig.ParameterInfo("moduleName", "String", "模块名", true),
                new TemplateConfig.ParameterInfo("functionName", "String", "功能名", true),
                new TemplateConfig.ParameterInfo("columns", "List<Object>", "列信息", true),
                new TemplateConfig.ParameterInfo("pkColumn", "Object", "主键列", true),
                new TemplateConfig.ParameterInfo("dicts", "String", "字典", false)
        );
        configs.put("index.vm", new TemplateConfig("index.vm", "Vue模板", vueParams));

        return configs;
    }

    private String getTemplatePath(String templateName) {
        // 如果已经包含了路径信息，则直接使用
        if (templateName.contains("/")) {
            return templateName.startsWith("vm/") ? templateName : "vm/" + templateName;
        }

        // 根据模板名称确定路径
        if (templateName.endsWith(".vm")) {
            if (isJavaTemplate(templateName)) {
                return "vm/java/" + templateName;
            } else if (isXmlTemplate(templateName)) {
                return "vm/xml/" + templateName;
            } else if (isTsTemplate(templateName)) {
                return "vm/ts/" + templateName;
            } else if (isVueTemplate(templateName)) {
                return "vm/vue/" + templateName;
            } else {
                // 其他类型的模板暂时放在vm根目录下
                return "vm/" + templateName;
            }
        }
        // 默认路径
        return "vm/" + templateName;
    }

    private boolean isJavaTemplate(String templateName) {
        return templateName.equals("vo.vm") ||
               templateName.equals("entity.vm") ||
               templateName.equals("controller.vm") ||
               templateName.equals("service.vm") ||
               templateName.equals("service-impl.vm") ||
               templateName.equals("mapper.vm") ||
               templateName.equals("bo.vm");
    }

    private boolean isXmlTemplate(String templateName) {
        return templateName.equals("mapper-xml.vm");
    }

    private boolean isTsTemplate(String templateName) {
        return templateName.equals("api.vm") ||
               templateName.equals("types.vm");
    }

    private boolean isVueTemplate(String templateName) {
        return templateName.equals("index.vm");
    }
}