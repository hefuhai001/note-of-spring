package com.example.demo.controller;

import com.example.demo.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    /**
     * 创建定时任务
     *
     * @param time    触发时间，格式：yyyy-MM-dd HH:mm:ss
     * @param message 任务消息
     * @return 任务ID
     */
    @PostMapping("/create")
    public String createTask(@RequestParam String time,
                             @RequestParam(required = false, defaultValue = "默认任务") String message) {
        return scheduleService.createTask(time, message);
    }

    /**
     * 取消定时任务
     *
     * @param taskId 任务ID
     * @return 操作结果
     */
    @PostMapping("/cancel")
    public String cancelTask(@RequestParam String taskId) {
        return scheduleService.cancelTask(taskId);
    }

    /**
     * 获取所有定时任务
     *
     * @return 任务列表
     */
    @GetMapping("/list")
    public Map<String, String> listTasks() {
        return scheduleService.listTasks();
    }
}