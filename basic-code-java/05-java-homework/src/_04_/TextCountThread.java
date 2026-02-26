package _04_;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TextCountThread extends Thread {
    private final String textFile;

    public TextCountThread(String textFile) {
        this.textFile = textFile;
    }

    public void run() {
        int[] counts = new int[26];
        try {
            BufferedReader reader = new BufferedReader(new FileReader(textFile));
            String line;
            while ((line = reader.readLine()) != null) {
                for (char c : line.toLowerCase().toCharArray()) {
                    if (c >= 'a' && c <= 'z') {
                        counts[c - 'a']++;
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("文本统计失败：" + e.getMessage());
        }
        System.out.println("文本文件中各个字母出现次数为：");
        for (int i = 0; i < counts.length; i++) {
            System.out.println((char) ('a' + i) + ": " + counts[i]);
        }
    }
}
