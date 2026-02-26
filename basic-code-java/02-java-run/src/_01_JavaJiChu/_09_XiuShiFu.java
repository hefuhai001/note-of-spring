package _01_JavaJiChu;

public class _09_XiuShiFu {
    /*访问控制修饰符*/
    //    default (即默认，什么也不写）: 在同一包内可见，不使用任何修饰符。使用对象：类、接口、变量、方法。
    //    private : 在同一类内可见。使用对象：变量、方法。 注意：不能修饰类（外部类）
    //    public : 对所有类可见。使用对象：类、接口、变量、方法
    //    protected : 对同一包内的类和所有子类可见。使用对象：变量、方法。 注意：不能修饰类（外部类）。

/*访问控制和继承
请注意以下方法继承的规则：
父类中声明为 public 的方法在子类中也必须为 public。
父类中声明为 protected 的方法在子类中要么声明为 protected，要么声明为 public，不能声明为 private。
父类中声明为 private 的方法，不能够被子类继承。*/

//    默认访问修饰符-不使用任何关键字
//    私有访问修饰符-private
//    公有访问修饰符-public
//    受保护的访问修饰符-protected

    /*非访问修饰符*/
    //    static
    private static int numInstances = 0;

    protected static int getCount() {
        return numInstances;
    }

    private static void addInstance() {
        numInstances++;
    }

    _09_XiuShiFu() {
        _09_XiuShiFu.addInstance();
    }

    public static void main(String[] arguments) {
        System.out.println("Starting with " +
                _09_XiuShiFu.getCount() + " instances");
        for (int i = 0; i < 500; ++i) {
            new _09_XiuShiFu();
        }
        System.out.println("Created " +
                _09_XiuShiFu.getCount() + " instances");
    }

    //final
    //final 类不能被继承，没有类能够继承 final 类的任何特性。
    final int value = 10;
    // 下面是声明常量的实例
    public static final int BOXWIDTH = 6;
    static final String TITLE = "Manager";

    public void changeValue() {
//        value = 12; //将输出一个错误
    }

    //abstract
    public abstract class SuperClass {
        abstract void m(); //抽象方法
    }

    class SubClass extends SuperClass {
        //实现抽象方法
        void m() {
//          .........
        }
    }

    //    transient 修饰符
    public synchronized void showDetails() {
//.......
    }

    //    transient 修饰符
    public transient int limit = 55;   // 不会持久化
    public int b; // 持久化

//     volatile 修饰符
    //    volatile 修饰的成员变量在每次被线程访问时，都强制从共享内存中重新读取该成员变量的值。而且，当成员变量发生变化时，会强制线程将变化值回写到共享内存。这样在任何时刻，两个不同的线程总是看到某个成员变量的同一个值。
    //    一个 volatile 对象引用可能是 null。
    public class MyRunnable implements Runnable {
        private volatile boolean active;

        public void run() {
            active = true;
            while (active) // 第一行
            {
                // 代码
            }
        }

        public void stop() {
            active = false; // 第二行
        }
    }
}
