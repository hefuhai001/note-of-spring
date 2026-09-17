package com.example.hfh.controller;

import com.example.hfh.bo.CreateTaskRequest;
import com.example.hfh.service.CodeRunService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class CodeRunController {

    final private CodeRunService codeRunService;

    /**
     * 创建任务接口
     *
     * @param req 请求参数，包含代码和参数
     * @return 任务创建结果，包含唯一可执行名
     */
    @PostMapping("/task/create")
    public Map<String, Object> createTask(@RequestBody CreateTaskRequest req) {
        return codeRunService.createAndExecuteTask(req);
    }

    /**
     * 查询任务结果接口
     *
     * @param exeName 可执行文件名
     * @return 任务执行结果
     */
    @GetMapping("/task/result/{exeName}")
    public Map<String, Object> getTaskResult(@PathVariable String exeName) {
        return codeRunService.getTaskResult(exeName);
    }
}