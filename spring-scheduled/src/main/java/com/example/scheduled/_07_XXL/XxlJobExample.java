package com.example.scheduled._07_XXL;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

@Component
public class XxlJobExample {
    @XxlJob("myJobHandler")
    public void myJobHandler() {
        System.out.println("XXL-JOB 任务执行：" + System.currentTimeMillis());
    }
}
