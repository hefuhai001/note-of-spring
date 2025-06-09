package _01_UDP;

import java.util.Scanner;

public class _01PI_ {
    public static void main(String[] args) {
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("-->请输入一个数字(输入0退出系统)：");
            int n = sc.nextInt();
            if (n == 0) {
                System.out.println("退出系统成功!");
                System.exit(0);
            } else {
                pai(n);
            }
        }
    }

    public static void pai(int n) {
        double a, b, c;
        b = 0;
        for (int i = 1; i <= n; i++) {  //从一相加 加到n 小于每一个n的值
            c = (i % 2 == 0) ? -1 : 1;  //不断输出1,-1,1,-1
            b = b + c * (1.0 / (2 * i - 1));  //正负1不断输出乘以 1/2n-1,1万循环完,2万,接着
        }
        a = 4 * b;
        System.out.println("PI=" + a);
    }
}
