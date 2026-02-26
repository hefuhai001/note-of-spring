package _04_12_;

class ThreadpriorityDemo extends Thread {
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println(getName() + ":" + i);
        }
    }
}

public class _10_4 {
    public static void main(String[] args) {
        ThreadpriorityDemo tp1 = new ThreadpriorityDemo();
        ThreadpriorityDemo tp2 = new ThreadpriorityDemo();
        tp1.setName("xian_1");
        tp2.setName("xian_2");
        tp1.setPriority(10);
        tp2.setPriority(1);
        tp1.start();
        tp2.start();
    }
}
