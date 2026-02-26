package _01_UDP;

import java.util.Scanner;

public class _03_ {
    public static void main(String[] args) {
        while (true) {
            System.out.println("-->输入位数(输入0退出系统)：");
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
        double a = Math.pow(10, n);
        int i = (int) a;
        System.out.println(i);

    }
}



