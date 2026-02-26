package _03_;

//（1）定义接口A，里面包含值为3.14的常量PI和抽象方法double area()。
interface A {
    double PI = 3.14;

    double area();
}

//（2）定义接口B，里面包含抽象方法void setColor(String c)。
interface B {
    void setColor(String c);
}

//（3）定义接口C，该接口继承了接口A和B，里面包含抽象方法 double volume()。
interface C extends A, B {
    void volume();
}

//（4）定义圆柱体类Cylinder实现接口C，该类中包含三个成员变量：
//底圆半径radius、 圆柱体的高height、颜色color。
class Cylinder implements C {
    private int radius; // 底圆半径
    private int height; // 高
    private String color;   // 颜色

    // 构造函数
    public Cylinder(int radius, int height, String color) {
        this.radius = radius;
        this.height = height;
        this.color = color;
    }

    // 实现方法area()求表面积，
    public double area() {
        return (PI * radius * radius * 2) + (2 * PI * radius * height);
    }

    // 颜色
    public void setColor(String c) {
        this.color = c;
        // 测试使用
        System.out.println(this.color);
    }

    // 实现volume()方法求体积。
    public void volume() {
        System.out.println(PI * radius * radius * height);
    }
}

//（5）创建主类来测试类Cylinder。
public class _02_ {
    public static void main(String[] args) {
        Cylinder cl = new Cylinder(2, 2, "红色");
        System.out.println(cl.area());
        cl.setColor("绿色");
        cl.volume();
    }
}

