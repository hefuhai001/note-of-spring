package com.example.hfh.service.impl;

import com.example.hfh.bo.CreateTaskRequest;
import com.example.hfh.core.DockerCodeRunner;
import com.example.hfh.core.LanguageConfig;
import com.example.hfh.entity.TaskEntity;
import com.example.hfh.service.CodeRunService;
import com.example.hfh.service.TaskService;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 代码执行服务实现类
 * </p>
 *
 * @author Qwen
 * @since 2025-12-21
 */
@Service
public class CodeRunServiceImpl implements CodeRunService {

    @Autowired
    @Lazy  // 避免循环依赖
    private CodeRunService self;  // 注入自身代理

    @Autowired
    private DockerCodeRunner codeRunner;

    @Autowired
    private TaskService taskService;

    @Override
    public Map<String, Object> createAndExecuteTask(CreateTaskRequest req) {
        String source = req.getCode();
        String language = req.getLanguage() != null ? req.getLanguage() : "c";

        // 校验语言是否支持
        LanguageConfig config = LanguageConfig.fromLanguage(language);
        if (config == null) {
            return SaResult.data(Map.of(
                    "success", false,
                    "message", "不支持的语言: " + language
            ));
        }

        // 1. 生成唯一可执行名
        String baseName = UUID.randomUUID().toString();

        // 2. 创建任务记录
        TaskEntity task = new TaskEntity();
        task.setExeName(baseName);
        task.setLanguage(language);
        task.setCode(source);
        task.setArgs(req.getArgs());
        task.setStatus("CREATED");
        task.setCreatedAt(new Date(System.currentTimeMillis()));
        task.setUpdatedAt(new Date(System.currentTimeMillis()));

        // 3. 保存任务
        taskService.save(task);

        // 4. 异步执行任务
        self.executeTaskAsync(task);

        return SaResult.data(Map.of(
                "success", true,
                "exeName", baseName,
                "language", language,
                "message", "任务创建成功"
        ));
    }

    @Override
    public Map<String, Object> getTaskResult(String exeName) {
        TaskEntity task = taskService.getOne(new QueryWrapper<TaskEntity>()
                .eq("exe_name", exeName));

        if (task == null) {
            return SaResult.data(Map.of(
                    "success", false,
                    "message", "任务不存在"
            ));
        }

        return SaResult.data(Map.of(
                "success", true,
                "exeName", task.getExeName(),
                "language", task.getLanguage(),
                "status", task.getStatus(),
                "result", task.getResult(),
                "createTime", task.getCreatedAt(),
                "updateTime", task.getUpdatedAt()
        ));
    }

    @Override
    @Async
    public void executeTaskAsync(TaskEntity task) {
        try {
            // 更新任务状态为RUNNING
            task.setStatus("RUNNING");
            task.setUpdatedAt(new Date(System.currentTimeMillis()));
            taskService.updateById(task);

            // 使用核心库执行代码
            String language = task.getLanguage() != null ? task.getLanguage() : "c";
            DockerCodeRunner.RunResult result = codeRunner.run(
                    language, task.getExeName(), task.getCode(), task.getArgs());

            // 更新任务结果
            if (result.isSuccess()) {
                task.setResult(result.getOutput());
                task.setStatus("COMPLETED");
            } else {
                task.setResult(result.getOutput());
                task.setStatus("ERROR");
            }
        } catch (Exception e) {
            task.setResult("执行异常: " + e.getMessage());
            task.setStatus("ERROR");
        }

        task.setUpdatedAt(new Date(System.currentTimeMillis()));
        taskService.updateById(task);
    }
}
