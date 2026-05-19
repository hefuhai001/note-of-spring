package asia.hfh.code.service;

import asia.hfh.code.bo.CreateTaskRequest;
import asia.hfh.code.entity.TaskEntity;

import java.util.Map;

/**
 * <p>
 * 代码执行服务接口
 * </p>
 *
 * @author Qwen
 * @since 2025-12-21
 */
public interface CodeRunService {

    /**
     * 创建并执行代码任务
     *
     * @param req 创建任务请求参数
     * @return 任务创建结果
     */
    Map<String, Object> createAndExecuteTask(CreateTaskRequest req);

    /**
     * 获取任务执行结果
     *
     * @param exeName 可执行文件名
     * @return 任务执行结果
     */
    Map<String, Object> getTaskResult(String exeName);

    /**
     * 异步执行任务
     *
     * @param task 任务实体
     */
    void executeTaskAsync(TaskEntity task);
}