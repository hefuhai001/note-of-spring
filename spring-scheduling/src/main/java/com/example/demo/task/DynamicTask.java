package com.example.demo.task;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Component
public class DynamicTask implements SchedulingConfigurer {

    private final TaskScheduler taskScheduler;
    private final Map<String, ScheduledFuture<?>> taskFutures = new ConcurrentHashMap<>();
    private final Map<String, String> taskMessages = new ConcurrentHashMap<>();

    public DynamicTask(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        // 不需要在这里配置固定任务
    }

    /**
     * 创建定时任务
     *
     * @param taskId      任务ID
     * @param triggerTime 触发时间
     * @param message     任务消息
     */
    public void createTask(String taskId, LocalDateTime triggerTime, String message) {
        // 取消已存在的任务（如果有）
        cancelTask(taskId);

        // 计算延迟时间（毫秒）
        long delay = triggerTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - System.currentTimeMillis();

        if (delay < 0) {
            throw new IllegalArgumentException("触发时间不能是过去的时间");
        }

        // 创建并调度任务
        ScheduledFuture<?> future = taskScheduler.schedule(() -> {
            // 任务执行逻辑
            System.out.println("执行定时任务: " + message + "，时间: " + LocalDateTime.now());

            // 任务执行完成后移除
            taskFutures.remove(taskId);
            taskMessages.remove(taskId);
        }, new Date(System.currentTimeMillis() + delay));

        // 保存任务引用
        taskFutures.put(taskId, future);
        taskMessages.put(taskId, message + " | 触发时间: " + triggerTime);
    }

    /**
     * 取消定时任务
     *
     * @param taskId 任务ID
     */
    public void cancelTask(String taskId) {
        ScheduledFuture<?> future = taskFutures.remove(taskId);
        if (future != null) {
            future.cancel(true);
        }
        taskMessages.remove(taskId);
    }

    /**
     * 获取所有任务列表
     *
     * @return 任务列表
     */
    public Map<String, String> listTasks() {
        return new HashMap<>(taskMessages);
    }
}