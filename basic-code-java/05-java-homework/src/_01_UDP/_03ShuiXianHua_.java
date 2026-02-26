package _01_UDP;

import java.util.Scanner;

public class _03ShuiXianHua_ {
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
        int l = 1, r = 1;
        for (int i = 1; i < n; i++) l *= 10;//获取位次最小值
        for (int i = 1; i <= n; i++) r *= 10;//获取位次最大值
        for (int i = l; i < r; i++) {//最小值到最大值
            int t = i, s = 0;
            while (t > 0) {
                int temp = 1;
                for (int j = 0; j < n; j++) temp *= t % 10;//t为当前区间计算值 循环n次 取余相乘相加
                s += temp;
                t /= 10;//当前循环值除以10获得次方数 取尽循环结束
            }
            if (s == i) System.out.println(s + "是" + n + "位" + n + "次方的水仙花数");
        }
    }
}



