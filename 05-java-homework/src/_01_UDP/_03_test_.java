package _01_UDP;

import java.util.Scanner;


public class _03_test_ {
    //        Scanner sc = new Scanner(System.in);
//        int i = sc.nextInt();
//        int h = i / 100;
//        int t = i % 100 / 10;
//        int s = i % 10;
//        System.out.println(h);
//        System.out.println(t);
//        System.out.println(s);
//        Scanner sc = new Scanner(System.in);
//        System.out.println("输入a");
//        int a = sc.nextInt();
//        System.out.println("输入b");
//        int b = sc.nextInt();
//
//        System.out.println(Math.pow(a, b));
//        System.out.println(Math.abs(a));
//        System.out.println();

    public static void main(String[] args) {
        while (true) {
            System.out.println("-->输入一个数子(输入0退出系统)：");
            Scanner sc = new Scanner(System.in);
            int n = sc.nextInt();
            if (n == 0) {
                System.out.println("退出系统成功!");
                System.exit(0);
            } else {
                daffodils(n);
            }
        }
    }

    public static void daffodils(int n) {
        for (int i = 0; i < n; i++) {
            int h = i / 100;
            int t = i % 100 / 10;
            int s = i % 10;
            if (h * h * h + t * t * t + s * s * s == i) {
                System.out.println(i + "是水仙花数");
            }
        }

    }


}