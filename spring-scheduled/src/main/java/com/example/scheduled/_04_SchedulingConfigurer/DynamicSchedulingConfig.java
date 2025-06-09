package com.example.scheduled._04_SchedulingConfigurer;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.time.Instant;
import java.util.Date;

@Configuration
public class DynamicSchedulingConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.initialize();

        taskRegistrar.addTriggerTask(
                () -> System.out.println("动态任务执行：" + new Date()),
                new Trigger() {
                    @Override
                    public Date nextExecutionTime(TriggerContext triggerContext) {
                        return new CronTrigger("0/5 * * * * ?").nextExecutionTime(triggerContext);
                    }

                    @Override
                    public Instant nextExecution(TriggerContext triggerContext) {
                        return null;
                    }
                }
        );
    }
}