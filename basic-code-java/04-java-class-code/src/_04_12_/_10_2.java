package _04_12_;

class Thread_demo implements Runnable {
    String s;
    int m, count = 0;

    Thread_demo(String ss, int mm) {
        s = ss;
        m = mm;
    }

    public void run() {
        try {
            do {
                System.out.print(s);
                Thread.sleep(m);
                count++;
            } while (count < 20);
            System.out.println("[The Thread " + s + "finished!]");
        } catch (InterruptedException e) {
            return;
        }
    }
}

public class _10_2 {
    public static void main(String[] args) {
        Thread_demo threadA = new Thread_demo("A ", 500);
        Thread_demo threadB = new Thread_demo("B ", 600);
        Thread ta = new Thread(threadA);
        Thread tb = new Thread(threadB);
        ta.start();
        tb.start();
    }
}