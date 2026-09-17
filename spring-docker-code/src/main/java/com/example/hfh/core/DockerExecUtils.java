package com.example.hfh.core;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.StreamType;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Docker底层操作工具类，提供与Docker容器交互的通用方法
 */
public final class DockerExecUtils {

    private DockerExecUtils() {}

    /**
     * 将源代码内容打包为tar字节数组
     *
     * @param fileName 文件名（如 Main.java, main.py）
     * @param content  文件内容
     * @return tar格式的字节数组
     */
    public static byte[] tarBytes(String fileName, String content) throws Exception {
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

    /**
     * 将多个文件打包为tar字节数组
     *
     * @param files 文件名->内容的映射
     * @return tar格式的字节数组
     */
    public static byte[] tarBytes(Map<String, String> files) throws Exception {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        try (TarArchiveOutputStream to = new TarArchiveOutputStream(bo)) {
            for (Map.Entry<String, String> e : files.entrySet()) {
                TarArchiveEntry entry = new TarArchiveEntry(e.getKey());
                byte[] bytes = e.getValue().getBytes(StandardCharsets.UTF_8);
                entry.setSize(bytes.length);
                to.putArchiveEntry(entry);
                to.write(bytes);
                to.closeArchiveEntry();
            }
        }
        return bo.toByteArray();
    }

    /**
     * 将tar文件拷贝到容器指定路径
     *
     * @param docker      Docker客户端
     * @param containerId 容器ID
     * @param tarBytes    tar字节数组
     * @param remotePath  容器内目标路径
     */
    public static void copyToContainer(DockerClient docker, String containerId,
                                       byte[] tarBytes, String remotePath) throws Exception {
        try (ByteArrayInputStream tarIn = new ByteArrayInputStream(tarBytes)) {
            docker.copyArchiveToContainerCmd(containerId)
                    .withTarInputStream(tarIn)
                    .withRemotePath(remotePath)
                    .exec();
        }
    }

    /**
     * 在容器中执行命令并获取输出
     *
     * @param docker      Docker客户端
     * @param containerId 容器ID
     * @param cmd         要执行的命令
     * @return 命令的标准输出+标准错误
     */
    public static String execCommand(DockerClient docker, String containerId, String cmd) throws Exception {
        ExecCreateCmdResponse exec = docker.execCreateCmd(containerId)
                .withCmd("sh", "-c", cmd)
                .withAttachStdout(true)
                .withAttachStderr(true)
                .exec();
        return execAndGet(docker, exec);
    }

    /**
     * 启动已创建的exec并获取输出
     */
    public static String execAndGet(DockerClient docker, ExecCreateCmdResponse exec) throws Exception {
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

    /**
     * 根据容器名称获取容器ID
     *
     * @param docker         Docker客户端
     * @param containerName  容器名称
     * @return 容器ID
     */
    public static String getContainerId(DockerClient docker, String containerName) {
        return docker.listContainersCmd()
                .withNameFilter(List.of(containerName))
                .exec().get(0).getId();
    }
}
