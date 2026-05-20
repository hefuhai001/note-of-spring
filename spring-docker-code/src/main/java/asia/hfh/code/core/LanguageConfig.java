package asia.hfh.code.core;

import java.util.List;

/**
 * 语言配置枚举，定义每种语言的编译/运行模板
 * <p>
 * 命令模板中的占位符:
 *   {srcFile}  - 源文件名（含扩展名）
 *   {exeFile}  - 可执行文件名（不含扩展名）
 *   {args}     - 命令行参数
 *   {timeout}  - 超时秒数
 *   {subDir}   - 子目录路径（用于需要独立工作目录的语言，如Java）
 * </p>
 */
public enum LanguageConfig {

    C(
            "c", ".c", false,
            "gcc -Wall -O2 {srcFile} -o {exeFile}",
            "su nobody -s /bin/sh -c './{exeFile} {args}'",
            List.of("error:", "ERROR")
    ),

    CPP(
            "cpp", ".cpp", false,
            "g++ -Wall -O2 {srcFile} -o {exeFile}",
            "su nobody -s /bin/sh -c './{exeFile} {args}'",
            List.of("error:", "ERROR")
    ),

    JAVA(
            "java", ".java", true,
            "javac Main.java",
            "su nobody -s /bin/sh -c 'java Main {args}'",
            List.of("error:", "ERROR", "Exception")
    ),

    PYTHON(
            "python", ".py", false,
            null,
            "su nobody -s /bin/sh -c 'python3 {srcFile} {args}'",
            List.of("Error:", "Traceback")
    ),

    JAVASCRIPT(
            "javascript", ".js", false,
            null,
            "su nobody -s /bin/sh -c 'node {srcFile} {args}'",
            List.of("Error:", "ERROR")
    ),

    RUST(
            "rust", ".rs", false,
            "rustc {srcFile} -o {exeFile}",
            "su nobody -s /bin/sh -c './{exeFile} {args}'",
            List.of("error:", "ERROR")
    ),

    DOTNET(
            "dotnet", ".cs", false,
            null,
            "su nobody -s /bin/sh -c 'dotnet-script {srcFile} {args}'",
            List.of("error:", "Error:", "ERROR")
    ),

    GO(
            "go", ".go", false,
            "go build -o {exeFile} {srcFile}",
            "su nobody -s /bin/sh -c './{exeFile} {args}'",
            List.of("error:", "ERROR")
    );

    /** 语言标识（小写） */
    private final String language;

    /** 源文件扩展名（含点号） */
    private final String extension;

    /** 是否使用子目录（Java需要，因为public class名必须与文件名一致） */
    private final boolean useSubDir;

    /** 编译命令模板，null表示解释型语言无需编译 */
    private final String compileTemplate;

    /** 运行命令模板 */
    private final String runTemplate;

    /** 输出中标识错误的关键词 */
    private final List<String> errorKeywords;

    LanguageConfig(String language, String extension, boolean useSubDir,
                   String compileTemplate, String runTemplate,
                   List<String> errorKeywords) {
        this.language = language;
        this.extension = extension;
        this.useSubDir = useSubDir;
        this.compileTemplate = compileTemplate;
        this.runTemplate = runTemplate;
        this.errorKeywords = errorKeywords;
    }

    public String getLanguage() { return language; }
    public String getExtension() { return extension; }
    public boolean isUseSubDir() { return useSubDir; }
    public String getCompileTemplate() { return compileTemplate; }
    public String getRunTemplate() { return runTemplate; }
    public List<String> getErrorKeywords() { return errorKeywords; }

    /** 是否需要编译 */
    public boolean isCompiled() { return compileTemplate != null; }

    /**
     * 获取源文件名
     * Java使用子目录，源文件固定为 Main.java；其他语言使用 {baseName}{extension}
     */
    public String getSrcFileName(String baseName) {
        return useSubDir ? "Main" + extension : baseName + extension;
    }

    /**
     * 获取容器内拷贝目标路径
     * 使用子目录的语言拷贝到 /workspace/{baseName}/，其他拷贝到 /workspace/
     */
    public String getRemotePath(String baseName) {
        return useSubDir ? "/workspace/" + baseName : "/workspace";
    }

    /**
     * 根据语言标识获取配置
     */
    public static LanguageConfig fromLanguage(String lang) {
        if (lang == null) return null;
        for (LanguageConfig cfg : values()) {
            if (cfg.language.equalsIgnoreCase(lang)) {
                return cfg;
            }
        }
        return null;
    }

    /**
     * 构建完整的执行命令（编译+运行）
     *
     * @param baseName 基础文件名（不含扩展名）
     * @param args     命令行参数字符串
     * @param timeout  超时秒数
     * @return 完整的shell命令
     */
    public String buildCommand(String baseName, String args, int timeout) {
        String srcFile = getSrcFileName(baseName);
        String cdPath = useSubDir ? "/workspace/" + baseName : "/workspace";

        StringBuilder cmd = new StringBuilder("cd ").append(cdPath).append(" && ");

        if (isCompiled()) {
            String compile = compileTemplate
                    .replace("{srcFile}", srcFile)
                    .replace("{exeFile}", baseName);
            cmd.append(compile).append(" && ");
        }

        String run = runTemplate
                .replace("{srcFile}", srcFile)
                .replace("{exeFile}", baseName)
                .replace("{args}", args);
        cmd.append(String.format("timeout %ds %s 2>&1", timeout, run));

        return cmd.toString();
    }

    /**
     * 判断输出是否包含错误
     */
    public boolean hasError(String output) {
        for (String keyword : errorKeywords) {
            if (output.contains(keyword)) return true;
        }
        return false;
    }
}
