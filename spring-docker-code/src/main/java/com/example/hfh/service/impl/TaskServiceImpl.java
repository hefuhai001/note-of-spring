package com.example.hfh.service.impl;

import com.example.hfh.entity.TaskEntity;
import com.example.hfh.mapper.TaskMapper;
import com.example.hfh.service.TaskService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 任务表 服务实现类
 * </p>
 *
 * @author xiaoHe
 * @since 2025-12-07
 */
@AllArgsConstructor
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, TaskEntity> implements TaskService {

    final private TaskMapper taskMapper;

    @Override
    public TaskEntity createTask(TaskEntity task) {
        task.setCreatedAt(new Date());
        task.setUpdatedAt(new Date());
        this.save(task);
        return task;
    }

    @Override
    public TaskEntity updateTask(Long id, TaskEntity task) {
        task.setId(id);
        task.setUpdatedAt(new Date());
        this.updateById(task);
        return task;
    }

    @Override
    public boolean deleteTask(Long id) {
        return this.removeById(id);
    }

    @Override
    public TaskEntity getTaskDetail(Long id) {
        return taskMapper.getTaskDetail(id);
    }

    @Override
    public Page<TaskEntity> getTaskPage(Integer pageNum, Integer pageSize) {
        long total = taskMapper.countTask();
        long offset = (pageNum - 1L) * pageSize;
        List<TaskEntity> records = taskMapper.selectTaskPage(offset, pageSize);

        Page<TaskEntity> page = new Page<>(pageNum, pageSize);
        page.setRecords(records);
        page.setTotal(total);
        return page;
    }

}