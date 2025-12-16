package com.example.gcc;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class RunController {

    @Autowired
    private DockerClient docker;

    @Value("${docker.container-name}")
    private String CONTAINER_NAME;

    @PostMapping("/run")
    public String runC(@RequestBody Map<String, Object> req) throws Exception {
        String source = (String) req.get("code");

        /* 1. 生成唯一可执行名 */
        String baseName = UUID.randomUUID().toString();
        String srcFile = baseName + ".c";
        String exeFile = baseName;

        /* 2. 打成 tar */
        byte[] tarBytes = tarBytes(srcFile, source);
        try (ByteArrayInputStream tarIn = new ByteArrayInputStream(tarBytes)) {
            docker.copyArchiveToContainerCmd(containerId())
                    .withTarInputStream(tarIn)
                    .withRemotePath("/workspace")
                    .exec();
        }

        /* 3. 拼装命令行参数 */
        List<?> rawArgs = (List<?>) req.get("args");
        String argStr = rawArgs == null ? "" :
                rawArgs.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(" "));

        String cmd = String.format(
                "cd /workspace && " +
                        "gcc -Wall -O2 %s -o %s && " +
                        "timeout 3s su nobody -s /bin/sh -c './%s %s' 2>&1",
                srcFile, exeFile, exeFile, argStr);      // ← 用 %s 把参数塞进去

        /* 4. 执行 */
        ExecCreateCmdResponse exec = docker.execCreateCmd(containerId())
                .withCmd("sh", "-c", cmd)
                .withAttachStdout(true)
                .withAttachStderr(true)
                .exec();
        String log = execAndGet(exec);

        /* 5. 返回 */
        if (log.contains("error:") || log.contains("ERROR")) {
            return "编译/运行错误:\n" + log;
        }
        return String.format("编译通过(%s) \n%s", exeFile, log);
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