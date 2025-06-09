package _01_JavaJiChu;

public class _14_Number$Math_Lei {
    public static void main(String[] args) {
        Integer x = 5;
        x = x + 10;
        System.out.println(x);
    }

    /*Java Math 类*/
//    public static void main (String []args)
//    {
//        System.out.println("90 度的正弦值：" + Math.sin(Math.PI/2));
//        System.out.println("0度的余弦值：" + Math.cos(0));
//        System.out.println("60度的正切值：" + Math.tan(Math.PI/3));
//        System.out.println("1的反正切值： " + Math.atan(1));
//        System.out.println("π/2的角度值：" + Math.toDegrees(Math.PI/2));
//        System.out.println(Math.PI);
//    }

    /*序号	方法与描述
      1	xxxValue()
      将 Number 对象转换为xxx数据类型的值并返回。
      2	compareTo()
      将number对象与参数比较。
      3	equals()
      判断number对象是否与参数相等。
      4	valueOf()
      返回一个 Number 对象指定的内置数据类型
      5	toString()
      以字符串形式返回值。
      6	parseInt()
      将字符串解析为int类型。
      7	abs()
      返回参数的绝对值。
      8	ceil()
      返回大于等于( >= )给定参数的的最小整数，类型为双精度浮点型。
      9	floor()
      返回小于等于（<=）给定参数的最大整数 。
      10	rint()
      返回与参数最接近的整数。返回类型为double。
      11	round()
      它表示四舍五入，算法为 Math.floor(x+0.5)，即将原来的数字加上 0.5 后再向下取整，所以，Math.round(11.5) 的结果为12，Math.round(-11.5) 的结果为-11。
      12	min()
      返回两个参数中的最小值。
      13	max()
      返回两个参数中的最大值。
      14	exp()
      返回自然数底数e的参数次方。
      15	log()
      返回参数的自然数底数的对数值。
      16	pow()
      返回第一个参数的第二个参数次方。
      17	sqrt()
      求参数的算术平方根。
      18	sin()
      求指定double类型参数的正弦值。
      19	cos()
      求指定double类型参数的余弦值。
      20	tan()
      求指定double类型参数的正切值。
      21	asin()
      求指定double类型参数的反正弦值。
      22	acos()
      求指定double类型参数的反余弦值。
      23	atan()
      求指定double类型参数的反正切值。
      24	atan2()
      将笛卡尔坐标转换为极坐标，并返回极坐标的角度值。
      25	toDegrees()
      将参数转化为角度。
      26	toRadians()
      将角度转换为弧度。
      27	random()
      返回一个随机数。
    * */

    /*floor,round 和 ceil 实例：*/
//    public static void main(String[] args) {
//        double[] nums = { 1.4, 1.5, 1.6, -1.4, -1.5, -1.6 };
//        for (double num : nums) {
//            test(num);
//        }
//    }
//
//    private static void test(double num) {
//        System.out.println("Math.floor(" + num + ")=" + Math.floor(num));
//        System.out.println("Math.round(" + num + ")=" + Math.round(num));
//        System.out.println("Math.ceil(" + num + ")=" + Math.ceil(num));
//    }
}
