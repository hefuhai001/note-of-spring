package _02_Java_JingJie;

import java.util.Scanner;

public class _21_String_FanZhuan_PinJie {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3};

        String s = myConnect(arr);

        System.out.println("s:" + s);
        //

        Scanner sc = new Scanner(System.in);
        System.out.println("请输入一个字符串：");
        String line = sc.nextLine();

        String ss = myReverse(line);
        System.out.println("ss:" + ss);
    }

    public static String myConnect(int[] arr) {
        StringBuilder sb = new StringBuilder();

        sb.append("[");

        for (int i = 0; i < arr.length; i++) {
            if (i == arr.length - 1) {
                sb.append(arr[i]);
            } else {
                sb.append(arr[i]).append(",");
            }
        }
        sb.append("]");

        String s = sb.toString();
        return s;
    }

    //
    public static String myReverse(String h) {
        //String---StringBuilder---Reverse()---toString
//        StringBuilder sh=new StringBuilder(h);
//        sh.reverse();
//        String ss = sh.toString();
//        return ss;

        return new StringBuilder(h).reverse().toString();
    }
}
