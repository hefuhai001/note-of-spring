package _04_12_;

//jicheng thread jiekou
class MyThreadDemo extends Thread {
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(getName() + ":" + i);
        }
    }
}

public class _10_1 {

    public static void main(String[] args) {
//        System.out.println("_10_");
        MyThreadDemo my1 = new MyThreadDemo();
        MyThreadDemo my2 = new MyThreadDemo();
        my1.start();
        my2.start();
    }
}
