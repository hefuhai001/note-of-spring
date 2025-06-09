package _01_JavaJiChu;

public class _21_FangFa {

    /*
     *    * 修饰符 返回值类型 方法名(参数类型 参数名){
     *    ...
     *    方法体
     *    ...
     *    return 返回值;
     *    }
     * */
    public static void main(String[] args) {
        printGrade(78.5);
    }

    public static void printGrade(double score) {
        if (score >= 90.0) {
            System.out.println('A');
        } else if (score >= 80.0) {
            System.out.println('B');
        } else if (score >= 70.0) {
            System.out.println('C');
        } else if (score >= 60.0) {
            System.out.println('D');
        } else {
            System.out.println('F');
        }
    }

    /*
     * 通过值传递参数
     * */
//    public static void main(String[] args) {
//        int num1 = 1;
//        int num2 = 2;
//
//        System.out.println("交换前 num1 的值为：" +
//                num1 + " ，num2 的值为：" + num2);
//
//        // 调用swap方法
//        swap(num1, num2);
//        System.out.println("交换后 num1 的值为：" +
//                num1 + " ，num2 的值为：" + num2);
//    }
//    /** 交换两个变量的方法 */
//    public static void swap(int n1, int n2) {
//        System.out.println("\t进入 swap 方法");
//        System.out.println("\t\t交换前 n1 的值为：" + n1
//                + "，n2 的值：" + n2);
//        // 交换 n1 与 n2的值
//        int temp = n1;
//        n1 = n2;
//        n2 = temp;
//
//        System.out.println("\t\t交换后 n1 的值为 " + n1
//                + "，n2 的值：" + n2);
//    }

    /*
     *可变参数
     * */
//    public static void main(String[] args) {
//        // 调用可变参数的方法
//        printMax(34, 3, 3, 2, 56.5);
//        printMax(new double[]{1, 2, 3});
//    }
//
//    public static void printMax( double... numbers) {
//        if (numbers.length == 0) {
//            System.out.println("No argument passed");
//            return;
//        }
//
//        double result = numbers[0];
//
//        for (int i = 1; i <  numbers.length; i++){
//            if (numbers[i] >  result) {
//                result = numbers[i];
//            }
//        }
//        System.out.println("The max value is " + result);
//    }

    /*
     * finalize() 方法
     * */
//    public static void main(String[] args) {
//        Cake c1 = new Cake(1);
//        Cake c2 = new Cake(2);
//        Cake c3 = new Cake(3);
//
//        c2 = c3 = null;
//        System.gc(); //调用Java垃圾收集器
//    }
//}
//
//class Cake extends Object {
//    private int id;
//
//    public Cake(int id) {
//        this.id = id;
//        System.out.println("Cake Object " + id + "is created");
//    }
//
//    protected void finalize() throws java.lang.Throwable {
//        super.finalize();
//        System.out.println("Cake Object " + id + "is disposed");
//    }
}
