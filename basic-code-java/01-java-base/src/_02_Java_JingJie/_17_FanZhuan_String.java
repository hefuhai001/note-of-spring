package _02_Java_JingJie;

import java.util.Scanner;

public class _17_FanZhuan_String {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入一个字符串");
        String line = sc.nextLine();
        String s = reverse(line);
        System.out.println("s:" + s);
    }

    //定义方法 字符串反转
    /*返回值类型Striing 返回i参数String s*/
    public static String reverse(String s) {
        String ss = "";
        for (int i = s.length() - 1; i >= 0; i--) {
            ss += s.charAt(i);
        }

        return ss;

    }
}
