package com.example.hfh.service.impl;

import com.example.hfh.service.GccService;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.StreamType;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GccServiceImpl implements GccService {

    @Autowired
    private DockerClient docker;

    @Value("${docker.container-name}")
    private String CONTAINER_NAME;

    @Override
    public String compile(String code) throws Exception {
        String baseName = UUID.randomUUID().toString();
        String srcFile = baseName + ".c";
        String exeFile = baseName;

        byte[] tarBytes = tarBytes(srcFile, code);
        try (ByteArrayInputStream tarIn = new ByteArrayInputStream(tarBytes)) {
            docker.copyArchiveToContainerCmd(containerId())
                    .withTarInputStream(tarIn)
                    .withRemotePath("/workspace")
                    .exec();
        }

        String cmd = String.format(
                "cd /workspace && gcc -Wall -O2 %s -o %s 2>&1",
                srcFile, exeFile);

        ExecCreateCmdResponse exec = docker.execCreateCmd(containerId())
                .withCmd("sh", "-c", cmd)
                .withAttachStdout(true)
                .withAttachStderr(true)
                .exec();
        String log = execAndGet(exec);

        if (log.contains("error:") || log.contains("ERROR")) {
            return "编译错误:\n" + log;
        }
        return baseName;
    }

    @Override
    public String run(String taskId, List<String> args) throws Exception {
        String exeFile = taskId;
        String argStr = args == null ? "" :
                args.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(" "));

        String cmd = String.format(
                "cd /workspace && timeout 3s su nobody -s /bin/sh -c './%s %s' 2>&1",
                exeFile, argStr);

        ExecCreateCmdResponse exec = docker.execCreateCmd(containerId())
                .withCmd("sh", "-c", cmd)
                .withAttachStdout(true)
                .withAttachStderr(true)
                .exec();

        return execAndGet(exec);
    }

    @Override
    public String compileAndRun(String code, List<String> args) throws Exception {
        String taskId = compile(code);
        if (taskId.startsWith("编译错误")) {
            return taskId;
        }

        String result = run(taskId, args);

        return String.format("编译通过(%s) \n%s", taskId, result);
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
                .exec().getFirst().getId();
    }
}
