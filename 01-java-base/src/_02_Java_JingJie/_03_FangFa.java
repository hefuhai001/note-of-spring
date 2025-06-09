package _02_Java_JingJie;

public class _03_FangFa {
    public static void main(String[] args) {
        //方法调用
        isEvenNumber();
        //带参数的方法
        int num1 = 10;
        int num2 = 20;
        getMax(num1, num2);
        //带返回值的方法
        boolean flag = isBoolean(10);
        System.out.println("flag: " + flag);
        //isBoolean(10);
        int result = isgetMax(100, 200);
        System.out.println("result: " + result);
    }

    //定义方法
    public static void isEvenNumber() {
        int num = 10;
        if (num % 2 == 0) {
            System.out.println(true);
        } else {
            System.out.println(false);
        }
//        System.out.println(num % 2 == 0);
    }

    //带参数的方法
    public static void getMax(int num1, int num2) {
        if (num1 > num2) {
            System.out.println(num1);
        } else {
            System.out.println(num2);
        }
//        System.out.println(Math.max(num1, num2));
    }

    //带返回值的方法
    public static boolean isBoolean(int bool) {
        if (bool % 2 == 0) {
            return true;
        } else {
            return false;
        }
//        return bool % 2 == 0;
    }

    public static int isgetMax(int num3, int num4) {
        if (num3 > num4) {
            return num3;
        } else {
            return num4;
        }
//        return Math.max(num3, num4);
    }

}
