package _05_;

import java.io.*;
import java.util.concurrent.*;

public class CopyFileAndCountCharThread implements Runnable {

    private int taskNum;
    private String fileName;
    private String copyName;
    private boolean isCopyFile; // 判断是复制文件还是统计字母

    public CopyFileAndCountCharThread(int taskNum, String fileName, String copyName, boolean isCopyFile) {
        this.taskNum = taskNum;
        this.fileName = fileName;
        this.copyName = copyName;
        this.isCopyFile = isCopyFile;
    }

    public void run() {
        if (isCopyFile) {
            copyFile();
        } else {
            countChar();
        }
    }

    // 复制文件
    public void copyFile() {
        try {
            FileInputStream fis = new FileInputStream(fileName);
            FileOutputStream fos = new FileOutputStream(copyName);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fis.close();
            fos.close();
            System.out.println("复制文件成功！");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    // 统计字母
    public void countChar() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            int[] count = new int[26];
            while ((line = br.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    if (c >= 'a' && c <= 'z') {
                        count[c - 'a']++;
                    } else if (c >= 'A' && c <= 'Z') {
                        count[c - 'A']++;
                    }
                }
            }
            br.close();
            System.out.println("文件中字母出现次数统计结果：");
            for (int i = 0; i < 26; i++) {
                System.out.println((char) ('a' + i) + ": " + count[i]);
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String fileName = "myfile.txt";
        String copyName = "mytest.docx";
        CopyFileAndCountCharThread copyThread = new CopyFileAndCountCharThread(1, "test.docx", copyName, true);
        CopyFileAndCountCharThread countThread = new CopyFileAndCountCharThread(2, fileName, null, false);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<?> copyResult = executor.submit(copyThread);
        Future<?> countResult = executor.submit(countThread);
        copyResult.get();
        countResult.get();
        executor.shutdown();
        System.out.println("程序正常结束！");
    }
}