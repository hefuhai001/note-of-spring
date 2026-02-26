package _01_JavaJiChu;

public class _11_XunHuanJieGou {
    /*while 循环*/
    public static void main(String[] args) {
        int x = 10;
        while (x < 20) {
            System.out.print("value of x : " + x);
            x++;
            System.out.print("\n");
        }
    }

    /*do…while 循环*/
//    public static void main(String[] args){
//        int x = 10;
//
//        do{
//            System.out.print("value of x : " + x );
//            x++;
//            System.out.print("\n");
//        }while( x < 20 );
//    }

    /*for循环*/
//    public static void main(String[] args) {
//
//        for(int x = 10; x < 20; x = x+1) {
//            System.out.print("value of x : " + x );
//            System.out.print("\n");
//        }
//    }

    /*Java 增强 for 循环*/
    /*声明语句：声明新的局部变量，该变量的类型必须和数组元素的类型匹配。其作用域限定在循环语句块，其值与此时数组元素的值相等。
      表达式：表达式是要访问的数组名，或者是返回值为数组的方法。*/
//    public static void main(String[] args){
//        int [] numbers = {10, 20, 30, 40, 50};
//
//        for(int x : numbers ){
//            System.out.print( x );
//            System.out.print(",");
//        }
//        System.out.print("\n");
//        String [] names ={"James", "Larry", "Tom", "Lacy"};
//        for( String name : names ) {
//            System.out.print( name );
//            System.out.print(",");
//        }
//    }

    /*break 关键字*/
//    public static void main(String[] args) {
//        int [] numbers = {10, 20, 30, 40, 50};
//
//        for(int x : numbers ) {
//            // x 等于 30 时跳出循环
//            if( x == 30 ) {
//                break;
//            }
//            System.out.print( x );
//            System.out.print("\n");
//        }
//    }

    /*continue 关键字*/
    /*continue 适用于任何循环控制结构中。作用是让程序立刻跳转到下一次循环的迭代。
      在 for 循环中，continue 语句使程序立即跳转到更新语句。
      在 while 或者 do…while 循环中，程序立即跳转到布尔表达式的判断语句。*/

//    public static void main(String[] args) {
//        int [] numbers = {10, 20, 30, 40, 50};
//
//        for(int x : numbers ) {
//            if( x == 30 ) {
//                continue;
//            }
//            System.out.print( x );
//            System.out.print("\n");
//        }
//    }

}
