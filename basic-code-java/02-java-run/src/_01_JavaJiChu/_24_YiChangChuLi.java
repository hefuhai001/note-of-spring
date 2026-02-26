package _01_JavaJiChu;

public class _24_YiChangChuLi {
    /*
     * Java 异常处理
     * 用户输入了非法数据。
     * 要打开的文件不存在。
     * 网络通信时连接中断，或者JVM内存溢出。
     * */

    /*
     * Exception 类的层次
     * 所有的异常类是从 java.lang.Exception 类继承的子类。
     * Exception 类是 Throwable 类的子类。除了Exception类外，Throwable还有一个子类Error 。
     * Java 程序通常不捕获错误。错误一般发生在严重故障时，它们在Java程序处理的范畴之外。
     * Error 用来指示运行时环境发生的错误。
     * 例如，JVM 内存溢出。一般地，程序不会从错误中恢复。
     * 异常类有两个主要的子类：IOException 类和 RuntimeException 类。
     * */

    /*
     * Java 内置异常类
     * Java 语言定义了一些异常类在 java.lang 标准包中。
     * 标准运行时异常类的子类是最常见的异常类。由于 java.lang 包是默认加载到所有的 Java 程序的，所以大部分从运行时异常类继承而来的异常都可以直接使用。
     * Java 根据各个类库也定义了一些其他的异常，下面的表中列出了 Java 的非检查性异常。
     * 异常	描述
     * ArithmeticException	当出现异常的运算条件时，抛出此异常。例如，一个整数"除以零"时，抛出此类的一个实例。
     * ArrayIndexOutOfBoundsException	用非法索引访问数组时抛出的异常。如果索引为负或大于等于数组大小，则该索引为非法索引。
     * ArrayStoreException	试图将错误类型的对象存储到一个对象数组时抛出的异常。
     * ClassCastException	当试图将对象强制转换为不是实例的子类时，抛出该异常。
     * IllegalArgumentException	抛出的异常表明向方法传递了一个不合法或不正确的参数。
     * IllegalMonitorStateException	抛出的异常表明某一线程已经试图等待对象的监视器，或者试图通知其他正在等待对象的监视器而本身没有指定监视器的线程。
     * IllegalStateException	在非法或不适当的时间调用方法时产生的信号。换句话说，即 Java 环境或 Java 应用程序没有处于请求操作所要求的适当状态下。
     * IllegalThreadStateException	线程没有处于请求操作所要求的适当状态时抛出的异常。
     * IndexOutOfBoundsException	指示某排序索引（例如对数组、字符串或向量的排序）超出范围时抛出。
     * NegativeArraySizeException	如果应用程序试图创建大小为负的数组，则抛出该异常。
     * NullPointerException	当应用程序试图在需要对象的地方使用 null 时，抛出该异常
     * NumberFormatException	当应用程序试图将字符串转换成一种数值类型，但该字符串不能转换为适当格式时，抛出该异常。
     * SecurityException	由安全管理器抛出的异常，指示存在安全侵犯。
     * StringIndexOutOfBoundsException	此异常由 String 方法抛出，指示索引或者为负，或者超出字符串的大小。
     * UnsupportedOperationException	当不支持请求的操作时，抛出该异常。
     * 下面的表中列出了 Java 定义在 java.lang 包中的检查性异常类。
     * 异常	描述
     * ClassNotFoundException	应用程序试图加载类时，找不到相应的类，抛出该异常。
     * CloneNotSupportedException	当调用 Object 类中的 clone 方法克隆对象，但该对象的类无法实现 Cloneable 接口时，抛出该异常。
     * IllegalAccessException	拒绝访问一个类的时候，抛出该异常。
     * InstantiationException	当试图使用 Class 类中的 newInstance 方法创建一个类的实例，而指定的类对象因为是一个接口或是一个抽象类而无法实例化时，抛出该异常。
     * InterruptedException	一个线程被另一个线程中断，抛出该异常。
     * NoSuchFieldException	请求的变量不存在
     * NoSuchMethodException	请求的方法不存在
     * 异常方法
     * 下面的列表是 Throwable 类的主要方法:
     * 序号	方法及说明
     * 1	public String getMessage()
     * 返回关于发生的异常的详细信息。这个消息在Throwable 类的构造函数中初始化了。
     * 2	public Throwable getCause()
     * 返回一个 Throwable 对象代表异常原因。
     * 3	public String toString()
     * 返回此 Throwable 的简短描述。
     * 4	public void printStackTrace()
     * 将此 Throwable 及其回溯打印到标准错误流。。
     * 5	public StackTraceElement [] getStackTrace()
     * 返回一个包含堆栈层次的数组。下标为0的元素代表栈顶，最后一个元素代表方法调用堆栈的栈底。
     * 6	public Throwable fillInStackTrace()
     * 用当前的调用栈层次填充Throwable 对象栈层次，添加到栈层次任何先前信息中。
     * */

    /*
     * 捕获异常
     * try
     * {
     *    // 程序代码
     * }catch(ExceptionName e1)
     * {
     *    //Catch 块
     * }
     * */
    public static void main(String[] args) {
        try {
            int[] a = new int[2];
            System.out.println("Access element three :" + a[3]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Exception thrown  :" + e);
        }
        System.out.println("Out of the block");
    }

    /*
     * 多重捕获块
     * try{
     *    // 程序代码
     * }catch(异常类型1 异常的变量名1){
     *   // 程序代码
     * }catch(异常类型2 异常的变量名2){
     *   // 程序代码
     * }catch(异常类型3 异常的变量名3){
     *  // 程序代码
     * }
     * */
//    public static void main(String[] args) {
//        try {
//            file = new FileInputStream(fileName);
//            x = (byte) file.read();
//        } catch(FileNotFoundException f) { // Not valid!
//            f.printStackTrace();
//            return -1;
//        } catch(IOException i) {
//            i.printStackTrace();
//            return -1;
//        }
//    }

    /*
     * throws/throw 关键字：
     * */
//    public void deposit(double amount) throws RemoteException {
//        // Method implementation
//        throw new RemoteException();
//    }
    //Remainder of class definition

    /*
     * finally关键字
     * */
//    public static void main(String[] args) {
//        int[] a = new int[2];
//        try {
//            System.out.println("Access element three :" + a[3]);
//        } catch (ArrayIndexOutOfBoundsException e) {
//            System.out.println("Exception thrown  :" + e);
//        } finally {
//            a[0] = 6;
//            System.out.println("First element value: " + a[0]);
//            System.out.println("The finally statement is executed");
//        }
//    }

    /*
     *try-with-resources
     * try (resource declaration) {
     *   // 使用的资源
     * } catch (ExceptionType e1) {
     *   // 异常块
     * }
     * */
//    public static void main(String[] args) {
//        String line;
//        try (BufferedReader br = new BufferedReader(new FileReader("test.txt"))) {
//            while ((line = br.readLine()) != null) {
//                System.out.println("Line =>" + line);
//            }
//        } catch (IOException e) {
//            System.out.println("IOException in try block =>" + e.getMessage());
//        }
//    }

    /*
     *以上实例中，我们实例一个 BufferedReader 对象从 test.txt 文件中读取数据。
     * 在 try-with-resources 语句中声明和实例化 BufferedReader 对象，执行完毕后实例资源，不需要考虑 try 语句是正常执行还是抛出异常。
     * 如果发生异常，可以使用 catch 来处理异常。
     * 再看下不使用 try-with-resources 而改成 finally 来关闭资源，整体代码量多了很多，而且更复杂繁琐了：
     * */
//    public static void main(String[] args) {
//        BufferedReader br = null;
//        String line;
//
//        try {
//            System.out.println("Entering try block");
//            br = new BufferedReader(new FileReader("test.txt"));
//            while ((line = br.readLine()) != null) {
//                System.out.println("Line =>" + line);
//            }
//        } catch (IOException e) {
//            System.out.println("IOException in try block =>" + e.getMessage());
//        } finally {
//            System.out.println("Entering finally block");
//            try {
//                if (br != null) {
//                    br.close();
//                }
//            } catch (IOException e) {
//                System.out.println("IOException in finally block =>" + e.getMessage());
//            }
//        }
//    }

    /*
     * try-with-resources 处理多个资源
     * try-with-resources 语句中可以声明多个资源，方法是使用分号 ; 分隔各个资源：
     *
     * 以上实例使用 Scanner 对象从 testRead.txt 文件中读取一行并将其写入新的 testWrite.txt 文件中。
     * 多个声明资源时，try-with-resources 语句以相反的顺序关闭这些资源。 在本例中，PrintWriter 对象先关闭，然后 Scanner 对象关闭。
     * */
//    public static void main(String[] args) throws IOException {
//        try (Scanner scanner = new Scanner(new File("testRead.txt"));
//             PrintWriter writer = new PrintWriter(new File("testWrite.txt"))) {
//            while (scanner.hasNext()) {
//                writer.print(scanner.nextLine());
//            }
//        }
//    }


    /*
     * 声明自定义异常
     * 在 Java 中你可以自定义异常。编写自己的异常类时需要记住下面的几点。
     * 所有异常都必须是 Throwable 的子类。
     * 如果希望写一个检查性异常类，则需要继承 Exception 类。
     * 如果你想写一个运行时异常类，那么需要继承 RuntimeException 类。
     * 可以像下面这样定义自己的异常类：
     * class MyException extends Exception{
     * }
     * 只继承Exception 类来创建的异常类是检查性异常类。
     * 下面的 InsufficientFundsException 类是用户定义的异常类，它继承自 Exception。
     * 一个异常类和其它任何类一样，包含有变量和方法。
     * */
    //自定义异常类，继承Exception类
//    public class InsufficientFundsException extends Exception {
//        //此处的amount用来储存当出现异常（取出钱多于余额时）所缺乏的钱
//        private double amount;
//
//        public InsufficientFundsException(double amount) {
//            this.amount = amount;
//        }
//
//        public double getAmount() {
//            return amount;
//        }
//    }

    //balance为余额，number为卡号
//    private double balance;
//    private int number;
//
//    public void CheckingAccount(int number) {
//        this.number = number;
//    }
//
//    //方法：存钱
//    public void deposit(double amount) {
//        balance += amount;
//    }
//
//    //方法：取钱
//    public void withdraw(double amount) throws
//            InsufficientFundsException {
//        if (amount <= balance) {
//            balance -= amount;
//        } else {
//            double needs = amount - balance;
//            throw new InsufficientFundsException(needs);
//        }
//    }
//
//    //方法：返回余额
//    public double getBalance() {
//        return balance;
//    }
//
//    //方法：返回卡号
//    public int getNumber() {
//        return number;
//    }

//    public static void main(String [] args)
//    {
//        CheckingAccount c = new CheckingAccount(101);
//        System.out.println("Depositing $500...");
//        c.deposit(500.00);
//        try
//        {
//            System.out.println("\nWithdrawing $100...");
//            c.withdraw(100.00);
//            System.out.println("\nWithdrawing $600...");
//            c.withdraw(600.00);
//        }catch(InsufficientFundsException e)
//        {
//            System.out.println("Sorry, but you are short $"
//                    + e.getAmount());
//            e.printStackTrace();
//        }
//    }

    /*
     * 编译上面三个文件，并运行程序 BankDemo
     * */



    /*
     * 通用异常
     * 在Java中定义了两种类型的异常和错误。
     * JVM(Java虚拟机) 异常：由 JVM 抛出的异常或错误。例如：NullPointerException 类，ArrayIndexOutOfBoundsException 类，ClassCastException 类。
     * 程序级异常：由程序或者API程序抛出的异常。例如 IllegalArgumentException 类，IllegalStateException 类。
     * */

}
