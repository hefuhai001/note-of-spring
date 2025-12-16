package com.example.demo.service;

import com.example.demo.task.DynamicTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

@Service
public class ScheduleService {

    @Autowired
    private DynamicTask dynamicTask;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public String createTask(String time, String message) {
        try {
            // 解析时间
            LocalDateTime triggerTime = LocalDateTime.parse(time, FORMATTER);

            // 生成任务ID
            String taskId = UUID.randomUUID().toString();

            // 创建任务
            dynamicTask.createTask(taskId, triggerTime, message);

            return "任务创建成功，任务ID: " + taskId;
        } catch (Exception e) {
            return "任务创建失败: " + e.getMessage();
        }
    }

    public String cancelTask(String taskId) {
        try {
            dynamicTask.cancelTask(taskId);
            return "任务取消成功";
        } catch (Exception e) {
            return "任务取消失败: " + e.getMessage();
        }
    }

    public Map<String, String> listTasks() {
        return dynamicTask.listTasks();
    }
}