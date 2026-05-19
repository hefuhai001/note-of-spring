package asia.hfh.code.service.impl;

import asia.hfh.code.bo.CreateTaskRequest;
import asia.hfh.code.entity.TaskEntity;
import asia.hfh.code.service.CodeRunService;
import asia.hfh.code.service.TaskService;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.StreamType;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class CodeRunRunServiceImpl implements CodeRunService {

    @Autowired
    @Lazy  // 避免循环依赖
    private CodeRunService self;  // 注入自身代理

    @Autowired
    private DockerClient docker;

    @Autowired
    private TaskService taskService;

    @Value("${docker.container-name}")
    private String CONTAINER_NAME;

    @Override
    public Map<String, Object> createAndExecuteTask(CreateTaskRequest req) {
        String source = req.getCode();

        // 1. 生成唯一可执行名
        String baseName = UUID.randomUUID().toString();
        String srcFile = baseName + ".c";
        String exeFile = baseName;

        // 2. 创建任务记录
        TaskEntity task = new TaskEntity();
        task.setExeName(exeFile);
        task.setCode(source);

        // 3. 处理参数
        JsonNode args = req.getArgs();
        task.setArgs(args);

        // 4. 设置任务状态
        task.setStatus("CREATED");
        task.setCreatedAt(new Date(System.currentTimeMillis()));
        task.setUpdatedAt(new Date(System.currentTimeMillis()));

        // 5. 保存任务
        taskService.save(task);

        // 6. 异步执行任务
        self.executeTaskAsync(task);

        return SaResult.data(Map.of(
                "success", true,
                "exeName", exeFile,
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

            String source = task.getCode();
            String baseName = task.getExeName();
            String srcFile = baseName + ".c";
            String exeFile = baseName;

            // 1. 打成 tar
            byte[] tarBytes = tarBytes(srcFile, source);
            try (ByteArrayInputStream tarIn = new ByteArrayInputStream(tarBytes)) {
                docker.copyArchiveToContainerCmd(containerId())
                        .withTarInputStream(tarIn)
                        .withRemotePath("/workspace")
                        .exec();
            }

            // 2. 拼装命令行参数
            List<String> rawArgs = flatten(task.getArgs());
            String argStr = String.join(" ", rawArgs);

            String cmd = String.format(
                    "cd /workspace && " +
                            "gcc -Wall -O2 %s -o %s && " +
                            "timeout 3s su nobody -s /bin/sh -c './%s %s' 2>&1",
                    srcFile, exeFile, exeFile, argStr);

            // 3. 执行
            ExecCreateCmdResponse exec = docker.execCreateCmd(containerId())
                    .withCmd("sh", "-c", cmd)
                    .withAttachStdout(true)
                    .withAttachStderr(true)
                    .exec();
            String log = execAndGet(exec);

            // 4. 更新任务结果
            if (log.contains("error:") || log.contains("ERROR")) {
                task.setResult("编译/运行错误:\n" + log);
                task.setStatus("ERROR");
            } else {
                task.setResult(String.format("%s", log));
                task.setStatus("COMPLETED");
            }
        } catch (Exception e) {
            task.setResult("执行异常: " + e.getMessage());
            task.setStatus("ERROR");
        }

        task.setUpdatedAt(new Date(System.currentTimeMillis()));
        taskService.updateById(task);
    }

    private List<String> flatten(JsonNode node) {
        List<String> list = new ArrayList<>();
        if (node.isTextual()) {
            list.add(node.asText());
        } else if (node.isArray()) {
            node.forEach(n -> list.addAll(flatten(n)));
        }
        return list;
    }

    private byte[] tarBytes(String fileName, String content) throws Exception {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        try (TarArchiveOutputStream to = new TarArchiveOutputStream(bo)) {
            TarArchiveEntry entry = new TarArchiveEntry(fileName);
            byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
            entry.setSize(bytes.length);
            to.putArchiveEntry(entry);
            to.write(bytes);
            to.closeArchiveEntry();
        }
        return bo.toByteArray();
    }

    private String execAndGet(ExecCreateCmdResponse exec) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        try (ResultCallback.Adapter<Frame> callback = docker.execStartCmd(exec.getId())
                .exec(new ResultCallback.Adapter<>() {
                    @Override
                    public void onNext(Frame frame) {
                        try {
                            (frame.getStreamType() == StreamType.STDOUT ? out : err).write(frame.getPayload());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                })) {
            callback.awaitCompletion();
        }
        return (out.toString() + err).trim();
    }

    private String containerId() {
        return docker.listContainersCmd()
                .withNameFilter(List.of(CONTAINER_NAME))
                .exec().get(0).getId();
    }
}