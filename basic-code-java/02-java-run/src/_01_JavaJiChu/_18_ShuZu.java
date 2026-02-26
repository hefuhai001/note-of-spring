package _01_JavaJiChu;

public class _18_ShuZu {
    /*
     * Java 数组
     * 数组对于每一门编程语言来说都是重要的数据结构之一，当然不同语言对数组的实现及处理也不尽相同。
     * Java 语言中提供的数组是用来存储固定大小的同类型元素。
     * 你可以声明一个数组变量，如 numbers[100] 来代替直接声明 100 个独立变量 number0，number1，....，number99。
     * 本教程将为大家介绍 Java 数组的声明、创建和初始化，并给出其对应的代码。
     * */

    /*
     * 声明数组变量
     * */

//    dataType[] arrayRefVar;   // 首选的方法
//    dataType arrayRefVar[];  // 效果相同，但不是首选方法

    /*
     * 注意: 建议使用 dataType[] arrayRefVar 的声明风格声明数组变量。
     * dataType arrayRefVar[] 风格是来自 C/C++ 语言 ，在Java中采用是为了让 C/C++ 程序员能够快速理解java语言。
     * */

//    public static void main(String[] args) {
//        // 数组大小
//        int size = 10;
//        // 定义数组
//        double[] myList = new double[size];
//        myList[0] = 5.6;
//        myList[1] = 4.5;
//        myList[2] = 3.3;
//        myList[3] = 13.2;
//        myList[4] = 4.0;
//        myList[5] = 34.33;
//        myList[6] = 34.0;
//        myList[7] = 45.45;
//        myList[8] = 99.993;
//        myList[9] = 11123;
//        // 计算所有元素的总和
//        double total = 0;
//        for (int i = 0; i < size; i++) {
//            total += myList[i];
//        }
//        System.out.println("总和为： " + total);
//    }

    /*
     * 处理数组
     * 数组的元素类型和数组的大小都是确定的，所以当处理数组元素时候，我们通常使用基本循环或者 For-Each 循环。
     * */

//    public static void main(String[] args) {
//        double[] myList = {1.9, 2.9, 3.4, 3.5};
//
//        // 打印所有数组元素
//        for (int i = 0; i < myList.length; i++) {
//            System.out.println(myList[i] + " ");
//        }
//        // 计算所有元素的总和
//        double total = 0;
//        for (int i = 0; i < myList.length; i++) {
//            total += myList[i];
//        }
//        System.out.println("Total is " + total);
//        // 查找最大元素
//        double max = myList[0];
//        for (int i = 1; i < myList.length; i++) {
//            if (myList[i] > max) max = myList[i];
//        }
//        System.out.println("Max is " + max);
//    }

    /*
     * For-Each 循环
     * */
//    public static void main(String[] args) {
//        double[] myList = {1.9, 2.9, 3.4, 3.5};
//
//        // 打印所有数组元素
//        for (double element: myList) {
//            System.out.println(element);
//        }
//    }

    /*
     * 数组作为函数的参数
     * */

    public static void main(String[] args) {
        int[] myList = {1, 2, 3, 4, 5};
        printArray(myList);
        reverse(myList);
    }

    public static void printArray(int[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
        }
    }

    /*
     * 数组作为函数的返回值
     * */
    public static int[] reverse(int[] list) {
        int[] result = new int[list.length];

        for (int i = 0, j = result.length - 1; i < list.length; i++, j--) {
            result[j] = list[i];
        }
        System.out.println(result);
        return result;
    }

    /*
     * 多维数组
     * */
    String[][] str = new String[3][4];
    String[][] s = new String[2][];
//    s[0] = new String[2];
//    s[1] = new String[3];
//    s[0][0] = new String("Good");
//    s[0][1] = new String("Luck");
//    s[1][0] = new String("to");
//    s[1][1] = new String("you");
//    s[1][2] = new String("!");

    /*
     * Arrays 类
     * java.util.Arrays 类能方便地操作数组，它提供的所有方法都是静态的。
     * 具有以下功能：
     * 给数组赋值：通过 fill 方法。
     * 对数组排序：通过 sort 方法,按升序。
     * 比较数组：通过 equals 方法比较数组中元素值是否相等。
     * 查找数组元素：通过 binarySearch 方法能对排序好的数组进行二分查找法操作。
     * */

    /*
     * 序号	方法和说明
     * 1	public static int binarySearch(Object[] a, Object key)
     * 用二分查找算法在给定数组中搜索给定值的对象(Byte,Int,double等)。数组在调用前必须排序好的。如果查找值包含在数组中，则返回搜索键的索引；否则返回 (-(插入点) - 1)。
     * 2	public static boolean equals(long[] a, long[] a2)
     * 如果两个指定的 long 型数组彼此相等，则返回 true。如果两个数组包含相同数量的元素，并且两个数组中的所有相应元素对都是相等的，则认为这两个数组是相等的。换句话说，如果两个数组以相同顺序包含相同的元素，则两个数组是相等的。同样的方法适用于所有的其他基本数据类型（Byte，short，Int等）。
     * 3	public static void fill(int[] a, int val)
     * 将指定的 int 值分配给指定 int 型数组指定范围中的每个元素。同样的方法适用于所有的其他基本数据类型（Byte，short，Int等）。
     * 4	public static void sort(Object[] a)
     * 对指定对象数组根据其元素的自然顺序进行升序排列。同样的方法适用于所有的其他基本数据类型（Byte，short，Int等）。
     *
     * */


}
