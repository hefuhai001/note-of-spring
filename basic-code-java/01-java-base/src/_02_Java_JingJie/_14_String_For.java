package _02_Java_JingJie;

import java.util.Scanner;

public class _14_String_For {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);

        //输入
        System.out.println("请输入一个字符串");
        String line=sc.nextLine();

        //遍历
/*        System.out.println(line.charAt(0));
        System.out.println(line.charAt(1));
        System.out.println(line.charAt(2));*/

/*        for (int i=0;i<3;i++){
            System.out.println(line.charAt(i));
        }
        */
        //获取字符串的长度
        System.out.println(line.length());

        for (int i=0;i<line.length();i++){
            System.out.println(line.charAt(i));
        }



    }
}
