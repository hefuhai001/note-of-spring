package com.example.scheduled._03_quartz;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(MyJob.class)
                .withIdentity("myJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger jobTrigger(JobDetail jobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("myTrigger")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(5)
                        .repeatForever())
                .build();
    }
}
