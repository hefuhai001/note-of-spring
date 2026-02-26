package _02_Java_JingJie;

import java.util.Scanner;

public class _09_PinJunZhi {
    public static void main(String[] args) {
        int[] arr = new int[6];
        Scanner sc = new Scanner(System.in);

        //优化前输入
/*        System.out.println("请输入第1个评委打分");
        arr[0]=sc.nextInt();
        System.out.println("请输入第2个评委打分");
        arr[1]=sc.nextInt();*/

        //优化后输入
        for (int x = 0; x < arr.length; x++) {
            System.out.println("请输入第" + (x + 1) + "个评委打分");
            arr[x] = sc.nextInt();
        }
        //遍历数组 调用方法
        printArray(arr);
        //获取最大值
        int max = getMax(arr);
        //获取最小值
        int min = getMin(arr);
        //获取所有元素的和
        int sum = getSum(arr);
        //计算
        int avg = (sum - min - max) / (arr.length - 2);
        System.out.println("选手的最终得分是：" + avg);

    }

    /*两个明确：
        返回值类型 int
        返回参数 itn[] arr */
    public static int getSum(int[] arr) {
        int sum = 0;
        for (int x = 0; x < arr.length; x++) {
            sum += arr[x];
        }
        return sum;
    }

    /*两个明确：
        返回值类型 int
        返回参数 itn[] arr */
    public static int getMin(int[] arr) {
        int min = arr[0];
        for (int x = 1; x < arr.length; x++) {
            if (arr[x] < min) {
                min = arr[x];
            }
        }
        return min;

    }

    /*两个明确：
    返回值类型 int
    返回参数 itn[] arr */
    public static int getMax(int[] arr) {
        int max = arr[0];
        for (int x = 1; x < arr.length; x++) {
            if (arr[x] > max) {
                max = arr[x];
            }
        }
        return max;
    }

    /*两个明确：
    返回值类型 void
    返回参数 itn[] arr */
    public static void printArray(int[] arr) {
        System.out.print('[');
        for (int x = 0; x < arr.length; x++) {
            if (x == arr.length - 1) {
                System.out.print(arr[x]);
            } else {
                System.out.print(arr[x] + ",");
            }
        }
        System.out.println("]");
    }
}
