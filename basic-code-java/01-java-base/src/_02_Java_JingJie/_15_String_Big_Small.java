package _02_Java_JingJie;

import java.util.Scanner;

public class _15_String_Big_Small {
    public static void main(String[] args) {
        //输入
        Scanner sc = new Scanner(System.in);

        System.out.println("请输入一个字符串:");
        String line = sc.nextLine();

        //统计
        int bigCount = 0;
        int smallCount = 0;
        int numberCount = 0;

        //遍历
        for (int i = 0; i < line.length(); i++) {
            //获取字符
            char ch = line.charAt(i);
            //判断
            if (ch > 'A' && ch < 'Z') {
                bigCount++;
            } else if (ch > 'a' && ch < 'z') {
                smallCount++;
            } else if (ch >= '0' && ch <= '9') {
                numberCount++;
            }
        }
        //输出
        System.out.println("大写字母："+bigCount+"个");
        System.out.println("小写字母："+smallCount+"个");
        System.out.println("数字字符："+numberCount+"个");

    }
}
