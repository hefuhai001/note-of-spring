package com.example.codegenerator.service;

import com.example.codegenerator.model.TemplateConfig;
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
            Template template = velocityEngine.getTemplate("templates/" + templateName);
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
                new TemplateConfig.ParameterInfo("classComment", "String", "类注释", false),
                new TemplateConfig.ParameterInfo("fields", "List<FieldInfo>", "字段列表", true)
        );
        configs.put("java-class.vm", new TemplateConfig("java-class.vm", "Java实体类模板", entityParams));

        // Controller模板配置
        List<TemplateConfig.ParameterInfo> controllerParams = Arrays.asList(
                new TemplateConfig.ParameterInfo("packageName", "String", "包名", true),
                new TemplateConfig.ParameterInfo("className", "String", "类名", true),
                new TemplateConfig.ParameterInfo("basePath", "String", "基础路径", true),
                new TemplateConfig.ParameterInfo("entityName", "String", "实体类名", true),
                new TemplateConfig.ParameterInfo("serviceName", "String", "Service类名", true),
                new TemplateConfig.ParameterInfo("primaryKeyType", "String", "主键类型", true)
        );
        configs.put("controller.vm", new TemplateConfig("controller.vm", "Controller模板", controllerParams));

        // Service接口模板配置
        List<TemplateConfig.ParameterInfo> serviceParams = Arrays.asList(
                new TemplateConfig.ParameterInfo("packageName", "String", "包名", true),
                new TemplateConfig.ParameterInfo("className", "String", "类名", true),
                new TemplateConfig.ParameterInfo("entityName", "String", "实体类名", true),
                new TemplateConfig.ParameterInfo("primaryKeyType", "String", "主键类型", true)
        );
        configs.put("service.vm", new TemplateConfig("service.vm", "Service接口模板", serviceParams));

        // Service实现类模板配置
        List<TemplateConfig.ParameterInfo> serviceImplParams = Arrays.asList(
                new TemplateConfig.ParameterInfo("packageName", "String", "包名", true),
                new TemplateConfig.ParameterInfo("className", "String", "类名", true),
                new TemplateConfig.ParameterInfo("entityName", "String", "实体类名", true),
                new TemplateConfig.ParameterInfo("primaryKeyType", "String", "主键类型", true),
                new TemplateConfig.ParameterInfo("mapperName", "String", "Mapper类名", true)
        );
        configs.put("service-impl.vm", new TemplateConfig("service-impl.vm", "Service实现类模板", serviceImplParams));

        // Mapper接口模板配置
        List<TemplateConfig.ParameterInfo> mapperParams = Arrays.asList(
                new TemplateConfig.ParameterInfo("packageName", "String", "包名", true),
                new TemplateConfig.ParameterInfo("className", "String", "类名", true),
                new TemplateConfig.ParameterInfo("entityName", "String", "实体类名", true),
                new TemplateConfig.ParameterInfo("primaryKeyType", "String", "主键类型", true)
        );
        configs.put("mapper.vm", new TemplateConfig("mapper.vm", "Mapper接口模板", mapperParams));

        // Mapper XML模板配置
        List<TemplateConfig.ParameterInfo> mapperXmlParams = Arrays.asList(
                new TemplateConfig.ParameterInfo("namespace", "String", "命名空间", true),
                new TemplateConfig.ParameterInfo("entityName", "String", "实体类名", true),
                new TemplateConfig.ParameterInfo("tableName", "String", "表名", true),
                new TemplateConfig.ParameterInfo("fields", "List<FieldInfo>", "字段列表", true),
                new TemplateConfig.ParameterInfo("primaryKey", "FieldInfo", "主键字段", true)
        );
        configs.put("mapper-xml.vm", new TemplateConfig("mapper-xml.vm", "Mapper XML模板", mapperXmlParams));

        // Request DTO模板配置
        List<TemplateConfig.ParameterInfo> dtoParams = Arrays.asList(
                new TemplateConfig.ParameterInfo("packageName", "String", "包名", true),
                new TemplateConfig.ParameterInfo("className", "String", "类名", true),
                new TemplateConfig.ParameterInfo("classComment", "String", "类注释", false),
                new TemplateConfig.ParameterInfo("fields", "List<FieldInfo>", "字段列表", true)
        );
        configs.put("request-dto.vm", new TemplateConfig("request-dto.vm", "Request DTO模板", dtoParams));

        return configs;
    }
}