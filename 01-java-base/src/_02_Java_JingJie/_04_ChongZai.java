package _02_Java_JingJie;

public class _04_ChongZai {
    public static void main(String[] args) {
        // 方法重载 方法会根据传入的参数类型主动选择合适的方法执行
        System.out.println(compare((int) 10, (int) 20));
        System.out.println(compare(10.0, 20.0));
        //强转类型
        System.out.println(compare((byte) 10, (byte) 20));
        System.out.println(compare((short) 10, (short) 20));
        System.out.println(compare((float) 10, (float) 20));
        System.out.println(compare(10L, 20L));
    }
    /*两个明确：
        返回值类型 boolean
        返回参数 int a||b */

    //方法重载
    public static boolean compare(int a, int b) {
        System.out.println("~~~int~~~");
        return a == b;
    }

    public static boolean compare(double a, double b) {
        System.out.println("~~~double~~~");
        return a == b;
    }

    public static boolean compare(float a, float b) {
        System.out.println("~~~float~~~");
        return a == b;
    }

    public static boolean compare(byte a, byte b) {
        System.out.println("~~~byte~~~");
        return a == b;
    }

    public static boolean compare(short a, short b) {
        System.out.println("~~~short~~~");
        return a == b;
    }

    public static boolean compare(long a, long b) {
        System.out.println("~~~long~~~");
        return a == b;
    }
}
