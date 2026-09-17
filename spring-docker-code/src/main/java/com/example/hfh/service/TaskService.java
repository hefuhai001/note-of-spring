package com.example.hfh.service;

import com.example.hfh.entity.TaskEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 任务表 服务类
 * </p>
 *
 * @author xiaoHe
 * @since 2025-12-07
 */
public interface TaskService extends IService<TaskEntity> {

    /**
     * 创建Task
     *
     * @param task Task信息
     * @return 创建后的Task信息
     */
    TaskEntity createTask(TaskEntity task);

    /**
     * 更新Task
     *
     * @param id   TaskID
     * @param task 更新的Task信息
     * @return 更新后的Task信息
     */
    TaskEntity updateTask(Long id, TaskEntity task);

    /**
     * 删除Task
     *
     * @param id TaskID
     * @return 是否删除成功
     */
    boolean deleteTask(Long id);

    /**
     * 获取Task详情
     *
     * @param id TaskID
     * @return Task详情
     */
    TaskEntity getTaskDetail(Long id);

    /**
     * 分页查询Task列表
     *
     * @param pageNum  当前页码
     * @param pageSize 每页条数
     * @return Task分页列表
     */
    Page<TaskEntity> getTaskPage(Integer pageNum, Integer pageSize);

}