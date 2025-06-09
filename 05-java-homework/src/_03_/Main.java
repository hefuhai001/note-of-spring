
/*
 * 主方法测试
 * */
package _03_;
public class Main {
    public static void main(String[] args) {
        Figure fr = new Rectangle(0, 0, 10, 2);
        System.out.println("Rectangle area: " + fr.area());
        Figure fs = new Solid(0, 0, 10, 2, 3);
        System.out.println("Solid area: " + fs.area());
        System.out.println("Solid volume: " + fs.volume());
    }
}
