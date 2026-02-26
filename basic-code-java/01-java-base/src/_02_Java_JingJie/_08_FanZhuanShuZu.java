package _02_Java_JingJie;

//import java.util.Scanner;

public class _08_FanZhuanShuZu {
    public static void main(String[] args) {

        //反转数组
        int[] arr = {1111, 22, 33, 44, 55};
        //交换数组
        /*for (int start = 0, end = arr.length - 1; start <= end; start++, end--) {
            int temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
        }*/

        //调用反转的方法
        reverse(arr);
        //遍历数组 调用方法
        printArray(arr);
    }

    /*两个明确：
        返回值类型 void
        返回参数 int[] arr */
    public static void reverse(int[] arr) {
        for (int start = 0, end = arr.length - 1; start <= end; start++, end--) {
            int temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
        }
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
