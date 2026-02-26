package _04_;

import java.io.*;

public class FileCopyThread extends Thread {
    private final String srcFile;
    private final String destFile;

    public FileCopyThread(String srcFile, String destFile) {
        this.srcFile = srcFile;
        this.destFile = destFile;
    }

    public void run() {
        try {
            FileInputStream in = new FileInputStream(srcFile);
            FileOutputStream out = new FileOutputStream(destFile);
            byte[] buffer = new byte[4096];
            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            System.out.println("文件复制失败：" + e.getMessage());
        }
    }
}

