package _02_Java_JingJie;

public class _01_JiChu<z> {
    public static void main(String[] args) {

        System.out.println("hello java");
        //变量 比较大小
        int a = 10;
        int b = 20;
        int maxab = a > b ? a : b;
        System.out.println("maxab:" + maxab);
        System.out.println("~~~~~~~~~1~~~~~~~~~~~");

        int FirstName = 10;
        //类型转换
        //        System.out.println(compare((byte) 10,(byte) 20));

        //算数运算符
        //字符操作
        //赋值运算符
        //自增自减运算
        //关系运算
        //逻辑运算
        //短路运算
        //三元运算
        //输入
        //流程控制 顺序 条件 循环
        //跳转控制语句
        //Random
        //数组

        int[] arr = new int[3];
        int[] arr1 = arr;
        //地址
        System.out.println(arr);
        System.out.println(arr1);
        //arr
        System.out.println(arr[0]);
        System.out.println(arr[1]);
        System.out.println(arr[2]);
        arr[0] = 100;
        System.out.println(arr[0]);
        System.out.println(arr[1]);
        System.out.println(arr[2]);
        System.out.println("~~~~~~~~2~~~~~~~~~~~~");

        //静态初始化
        int[] arr2 = {11, 22, 33};
        //遍历
        for (int x = 0; x < arr.length; x++) {
            System.out.println(arr2[x]);
        }
        //最大值
        int[] arrmax = {111, 222, 33, 444, 555};
        int max = arrmax[0];
        for (int z = 1; z < arrmax.length; z++) {
            if (arrmax[z] > max) {
                max = arrmax[z];
            }
        }
        System.out.println("max:" + max);


        System.out.println("~~~~~~3~~~~~~~~~~~~~~");
    }


}
