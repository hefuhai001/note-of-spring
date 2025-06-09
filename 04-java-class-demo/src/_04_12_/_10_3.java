package _04_12_;


public class _10_3 implements Runnable {

    String s;
    int m, count = 0;

    _10_3(String ss, int mm) {
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

    public static void main(String[] args) {
        _10_3 threadA = new _10_3("A ", 500);
        _10_3 threadB = new _10_3("B ", 600);
        Thread ta = new Thread(threadA);
        Thread tb = new Thread(threadB);
        ta.start();
        tb.start();
    }
}