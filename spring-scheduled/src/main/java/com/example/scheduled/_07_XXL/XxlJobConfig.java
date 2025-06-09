package com.example.scheduled._07_XXL;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XxlJobConfig {
    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses("http://127.0.0.1:8080/xxl-job-admin");
        xxlJobSpringExecutor.setAppname("xxl-job-executor-sample");
        xxlJobSpringExecutor.setIp("127.0.0.1");
        xxlJobSpringExecutor.setPort(9999);
        return xxlJobSpringExecutor;
    }
}
