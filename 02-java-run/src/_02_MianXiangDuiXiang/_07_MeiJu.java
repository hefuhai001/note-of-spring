package _02_MianXiangDuiXiang;

/**
 * @Description
 * @Author XiaoHe
 * @CreateTime 2023/5/12 15:59
 */

enum Color {
    RED, GREEN, BLUE;
}

public class _07_MeiJu {
    // 执行输出结果
    public static void main(String[] args) {
        Color c1 = Color.RED;
        System.out.println(c1);
    }
}

//枚举类 Color 转化在内部类实现：
/*class Color
{
    public static final Color RED = new Color();
    public static final Color BLUE = new Color();
    public static final Color GREEN = new Color();
}*/
