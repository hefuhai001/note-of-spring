package _03_other;

import _03_.Figure;
import _03_.Rectangle;
import _03_.Solid;

/**
 * @Description
 * @Author XiaoHe
 * @CreateTime 2023/4/2 17:34
 */
public class Main {
    public static void main(String[] args) {
        _03_.Figure fr = new Rectangle(0, 0, 10, 5);
        System.out.println("Rectangle area: " + fr.area());
        Figure fs = new Solid(0, 0, 10, 5, 3);
        System.out.println("Solid area: " + fs.area());
        System.out.println("Solid volume: " + fs.volume());
    }
}
