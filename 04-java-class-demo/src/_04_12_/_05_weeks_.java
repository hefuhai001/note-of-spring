package _04_12_;

import java.util.Scanner;

public class _05_weeks_ {
    public static void main(String[] args) {
        while (true) {
            System.out.println("************");
            System.out.println("****0退出****");
            System.out.println("****1求面积****");
            System.out.println("****2计算PI****");
            System.out.println("****3求体积****");
            Scanner sc = new Scanner(System.in);
            int menu = sc.nextInt();
            switch (menu) {
                case 0:
                    tuichu();
                case 1:
                    qiumianji();
                case 2:
                    qiuPi();
                case 3:
                    qiutiji();
                default:
                    System.out.println("无");
            }
        }
    }

    private static void qiutiji() {
        System.out.println("输入一个数字：");
        Scanner sc = new Scanner(System.in);
        int num = sc.nextInt();
        System.out.println("你输入的是：" + num);
        for (int i = 1; i <= num; i++) {
            i = ++i;
            System.out.println(i);
        }
    }

    private static void qiuPi() {
        System.out.println("输入一个数字：");
        Scanner sc = new Scanner(System.in);
        int num = sc.nextInt();
        System.out.println("你输入的是：" + num);
        //
        int a = 0;
        double b = 0;
        double c = 0;
        double d = 0;
        double e = 0;
        for (int i = 0; i < num; i++) {
            if (i % 2 == 0) a = 1;
            else a = -1;
            b = 2 * i + 1;
            c = 1 / b;
            d = a * c;
            e = e + d;
        }
        System.out.println("PI的值为：" + 4 * e);
    }

    private static void qiumianji() {
        System.out.println("请输入长:");
        Scanner sc = new Scanner(System.in);
        double ch = sc.nextDouble();
        System.out.println(ch);

        System.out.println("请输入宽:");
        double ku = sc.nextDouble();
        System.out.println(ku);

        System.out.println("面积为：" + ch * ku);
    }

    private static void tuichu() {
        System.out.println("退出成功");
        System.exit(0);
    }
}
