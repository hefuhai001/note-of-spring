package _03_GaoJiJiaoChen;

/**
 * @Description
 * @Author XiaoHe
 * @CreateTime 2023/5/12 17:31
 */

// 文件名 : _17_multithreading_Double.java
public class _17_multithreading_Double {

    public static void main(String[] args) {
        Runnable hello = new _17_DisplayMessage("Hello");
        Thread thread1 = new Thread(hello);
        thread1.setDaemon(true);
        thread1.setName("hello");
        System.out.println("Starting hello thread...");
        thread1.start();

        Runnable bye = new _17_DisplayMessage("Goodbye");
        Thread thread2 = new Thread(bye);
        thread2.setPriority(Thread.MIN_PRIORITY);
        thread2.setDaemon(true);
        System.out.println("Starting goodbye thread...");
        thread2.start();

        System.out.println("Starting thread3...");
        Thread thread3 = new _17_GuessANumber(27);
        thread3.start();
        try {
            thread3.join();
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted.");
        }
        System.out.println("Starting thread4...");
        Thread thread4 = new _17_GuessANumber(75);

        thread4.start();
        System.out.println("main() is ending...");
    }
}

