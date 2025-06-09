package com.example.upload;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.util.Properties;

@SpringBootApplication
public class ImageUploadApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImageUploadApplication.class, args);
        YamlPropertiesFactoryBean yamlProFb = new YamlPropertiesFactoryBean();
        yamlProFb.setResources(new ClassPathResource("application.yml"));
        Properties properties = yamlProFb.getObject();
        System.out.println("项目启动在" + properties.get("server.port") + "端口：http://localhost:" + properties.get("server.port") + "/doc.html#/home");
        System.out.println("图片回显" + properties.get("server.port") + "端口：http://localhost:" + properties.get("server.port") + "/");
    }

}
