package com.example.hfh.controller;

import com.example.hfh.entity.TaskEntity;
import com.example.hfh.mapper.TaskMapper;
import com.example.hfh.service.TaskService;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    /**
     * 创建Task
     *
     * @param task Task信息
     * @return 创建结果
     */
    @PostMapping("/create")
    public SaResult createTask(@RequestBody TaskEntity task) {
        TaskEntity createdTask = taskService.createTask(task);
        return SaResult.data(createdTask);
    }

    /**
     * 根据ID更新Task
     *
     * @param id   TaskID
     * @param task 更新的Task信息
     * @return 更新结果
     */
    @PutMapping("/update/{id}")
    public SaResult updateTask(@PathVariable Long id, @RequestBody TaskEntity task) {
        TaskEntity updatedTask = taskService.updateTask(id, task);
        return SaResult.data(updatedTask);
    }

    /**
     * 根据ID删除Task
     *
     * @param id TaskID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{id}")
    public SaResult deleteTask(@PathVariable Long id) {
        boolean result = taskService.deleteTask(id);
        if (result) {
            return SaResult.ok();
        } else {
            return SaResult.error("删除失败");
        }
    }

    /**
     * 根据ID获取Task详情
     *
     * @param id TaskID
     * @return Task详情
     */
    @GetMapping("/detail/{id}")
    public SaResult getTaskDetail(@PathVariable Long id) {
        TaskEntity task = taskService.getTaskDetail(id);
        return SaResult.data(task);
    }

    /**
     * 分页查询Task列表
     *
     * @param pageNum  当前页码
     * @param pageSize 每页条数
     * @return Task分页列表
     */
    @GetMapping("/page")
    public SaResult getTaskPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<TaskEntity> result = taskService.getTaskPage(pageNum, pageSize);
        return SaResult.data(result);
    }

}