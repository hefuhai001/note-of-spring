package _01_JavaJiChu;


public class _10_YunSuanFu {
    /*算术运算符*/
    public static void main(String[] args) {
        int a = 10;
        int b = 20;
        int c = 25;
        int d = 25;
        System.out.println("a + b = " + (a + b));
        System.out.println("a - b = " + (a - b));
        System.out.println("a * b = " + (a * b));
        System.out.println("b / a = " + (b / a));
        System.out.println("b % a = " + (b % a));
        System.out.println("c % a = " + (c % a));
        System.out.println("a++   = " + (a++));
        System.out.println("a--   = " + (a--));
        // 查看  d++ 与 ++d 的不同
        System.out.println("d++   = " + (d++));
        System.out.println("++d   = " + (++d));
    }

    /*自增自减运算符*/
//    public static void main(String[] args) {
//        int a = 3;//定义一个变量；
//        int b = ++a;//自增运算
//        int c = 3;
//        int d = --c;//自减运算
//        System.out.println("进行自增运算后的值等于" + b);
//        System.out.println("进行自减运算后的值等于" + d);
//    }

    /*前缀自增自减法(++a,--a)
     * 后缀自增自减法(a++,a--)*/
//    public static void main(String[] args){
//        int a = 5;//定义一个变量；
//        int b = 5;
//        int x = 2*++a;
//        int y = 2*b++;
//        System.out.println("自增运算符前缀运算后a="+a+",x="+x);
//        System.out.println("自增运算符后缀运算后b="+b+",y="+y);
//    }

    /*关系运算符*/
//    public static void main(String[] args) {
//        int a = 10;
//        int b = 20;
//        System.out.println("a == b = " + (a == b));
//        System.out.println("a != b = " + (a != b));
//        System.out.println("a > b = " + (a > b));
//        System.out.println("a < b = " + (a < b));
//        System.out.println("b >= a = " + (b >= a));
//        System.out.println("b <= a = " + (b <= a));
//    }

    /*位运算符*/
//            ＆	如果相对应位都是1，则结果为1，否则为0	（A＆B），得到12，即0000 1100
//            |	如果相对应位都是 0，则结果为 0，否则为 1	（A | B）得到61，即 0011 1101
//            ^	如果相对应位值相同，则结果为0，否则为1	（A ^ B）得到49，即 0011 0001
//            〜	按位取反运算符翻转操作数的每一位，即0变成1，1变成0。	（〜A）得到-61，即1100 0011
//            <<	按位左移运算符。左操作数按位左移右操作数指定的位数。	A << 2得到240，即 1111 0000
//            >>	按位右移运算符。左操作数按位右移右操作数指定的位数。	A >> 2得到15即 1111
//            >>>	按位右移补零操作符。左操作数的值按右操作数指定的位数右移，移动得到的空位以零填充。	A>>>2得到15即0000 1111

    /*逻辑运算符*/
//    public static void main(String[] args) {
//        boolean a = true;
//        boolean b = false;
//        System.out.println("a && b = " + (a&&b));
//        System.out.println("a || b = " + (a||b) );
//        System.out.println("!(a && b) = " + !(a && b));
//    }

    /*短路逻辑运算符*/
//    public static void main(String[] args){
//        int a = 5;//定义一个变量；
//        boolean b = (a<4)&&(a++<10);
//        System.out.println("使用短路逻辑运算符的结果为"+b);
//        System.out.println("a的结果为"+a);
//    }

    /*赋值运算符*/
//    public static void main(String[] args) {
//        int a = 10;
//        int b = 20;
//        int c = 0;
//        c = a + b;
//        System.out.println("c = a + b = " + c );
//        c += a ;
//        System.out.println("c += a = " + c );
//        c -= a ;
//        System.out.println("c -= a = " + c );
//        c *= a ;
//        System.out.println("c *= a = " + c );
//        a = 10;
//        c = 15;
//        c /= a ;
//        System.out.println("c /= a = " + c );
//        a = 10;
//        c = 15;
//        c %= a ;
//        System.out.println("c %= a = " + c );
//        c <<= 2 ;
//        System.out.println("c <<= 2 = " + c );
//        c >>= 2 ;
//        System.out.println("c >>= 2 = " + c );
//        c >>= 2 ;
//        System.out.println("c >>= 2 = " + c );
//        c &= a ;
//        System.out.println("c &= a = " + c );
//        c ^= a ;
//        System.out.println("c ^= a = " + c );
//        c |= a ;
//        System.out.println("c |= a = " + c );
//    }

    /*条件运算符*/
//    public static void main(String[] args) {
//        int a, b;
//        a = 10;
//        // 如果 a 等于 1 成立，则设置 b 为 20，否则为 30
//        b = (a == 1) ? 20 : 30;
//        System.out.println("Value of b is : " + b);
//
//        // 如果 a 等于 10 成立，则设置 b 为 20，否则为 30
//        b = (a == 10) ? 20 : 30;
//        System.out.println("Value of b is : " + b);
//    }

    /*instanceof 运算符*/
//    该运算符用于操作对象实例，检查该对象是否是一个特定类型（类类型或接口类型）
//    ( Object reference variable ) instanceof  (class/interface type)

//    public class Car extends Vehicle {
//        public static void main(String[] args){
//            Vehicle a = new Car();
//            boolean result =  a instanceof Car;
//            System.out.println( result);
//        }


    /*Java运算符优先级*/
}
