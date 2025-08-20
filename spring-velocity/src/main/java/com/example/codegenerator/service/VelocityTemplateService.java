package com.example.codegenerator.service;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.Map;

@Service
public class VelocityTemplateService {

    private final VelocityEngine velocityEngine;

    public VelocityTemplateService() {
        this.velocityEngine = new VelocityEngine();
        // 配置Velocity引擎
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADERS, "classpath");
        velocityEngine.setProperty("resource.loader.classpath.class", ClasspathResourceLoader.class.getName());
        velocityEngine.setProperty(RuntimeConstants.RUNTIME_LOG_REFERENCE_LOG_INVALID, true);
        velocityEngine.init();
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
}