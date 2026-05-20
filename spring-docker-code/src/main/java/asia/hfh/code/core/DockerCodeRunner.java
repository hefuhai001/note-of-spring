package asia.hfh.code.core;

import com.github.dockerjava.api.DockerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Docker代码运行器 - 核心编排组件
 * <p>
 * 将源代码拷贝到容器、编译（如需）、运行、收集输出，一站式完成。
 * 不依赖任何业务实体，可被任意Service复用。
 * </p>
 */
@Component
public class DockerCodeRunner {

    @Autowired
    private DockerClient docker;

    @Value("${docker.container-name}")
    private String containerName;

    /** 默认超时秒数 */
    private static final int DEFAULT_TIMEOUT = 3;

    /**
     * 在Docker容器中执行代码
     *
     * @param lang     语言标识（如 "c", "java", "python"）
     * @param baseName 基础文件名（不含扩展名），同时作为可执行文件名
     * @param source   源代码内容
     * @param args     命令行参数列表
     * @return 执行结果
     */
    public RunResult run(String lang, String baseName, String source, List<String> args) {
        return run(lang, baseName, source, args, DEFAULT_TIMEOUT);
    }

    /**
     * 在Docker容器中执行代码
     *
     * @param lang     语言标识
     * @param baseName 基础文件名
     * @param source   源代码内容
     * @param args     命令行参数列表
     * @param timeout  超时秒数
     * @return 执行结果
     */
    public RunResult run(String lang, String baseName, String source, List<String> args, int timeout) {
        LanguageConfig config = LanguageConfig.fromLanguage(lang);
        if (config == null) {
            return RunResult.error("不支持的语言: " + lang);
        }

        try {
            String containerId = DockerExecUtils.getContainerId(docker, containerName);

            // 1. 如果需要子目录，先创建
            if (config.isUseSubDir()) {
                DockerExecUtils.execCommand(docker, containerId,
                        "mkdir -p " + config.getRemotePath(baseName));
            }

            // 2. 将源代码拷贝到容器（Java等使用子目录，源文件固定为Main.java）
            String srcFileName = config.getSrcFileName(baseName);
            String remotePath = config.getRemotePath(baseName);
            byte[] tarBytes = DockerExecUtils.tarBytes(srcFileName, source);
            DockerExecUtils.copyToContainer(docker, containerId, tarBytes, remotePath);

            // 3. 构建并执行命令
            String argStr = args != null ? String.join(" ", args) : "";
            String cmd = config.buildCommand(baseName, argStr, timeout);
            String output = DockerExecUtils.execCommand(docker, containerId, cmd);

            // 3. 判断结果
            if (config.hasError(output)) {
                return RunResult.error("编译/运行错误:\n" + output);
            }
            return RunResult.success(output);

        } catch (Exception e) {
            return RunResult.error("执行异常: " + e.getMessage());
        }
    }

    /**
     * 执行结果
     */
    public static class RunResult {
        private final boolean success;
        private final String output;

        private RunResult(boolean success, String output) {
            this.success = success;
            this.output = output;
        }

        public static RunResult success(String output) { return new RunResult(true, output); }
        public static RunResult error(String output) { return new RunResult(false, output); }

        public boolean isSuccess() { return success; }
        public String getOutput() { return output; }
    }
}
