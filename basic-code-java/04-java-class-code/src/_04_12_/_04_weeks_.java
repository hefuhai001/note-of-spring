package _04_12_;

import java.util.Arrays;
import java.util.Scanner;

public class _04_weeks_ {
    public static void main(String[] args) {
        forArr();
        Scanner();
        other();
    }

    public static void forArr() {
        int[] arr = new int[10];
        int[] arrs = {1, 2, 3, 4, 5};
        System.out.println(Arrays.toString(arr));
        System.out.println(Arrays.toString(arrs));
        for (int i = 0; i < arrs.length; i++) {
            System.out.println(arrs[i]);
        }
    }

    public static void Scanner() {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入一个字符串");
        String s = sc.nextLine();
        System.out.println(s);
    }

    public static void other() { //接受
        int n = 7;
        long[] a = new long[n + 1];
        a[0] = 7;
        a[n] = 9;
        double[] arr = {79, 84.5, 63, 90, 98};
        System.out.print(arr[2]);

    }

}