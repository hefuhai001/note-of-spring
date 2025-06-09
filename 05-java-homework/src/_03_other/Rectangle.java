package _03_other;

/**
 * @Description
 * @Author XiaoHe
 * @CreateTime 2023/4/2 17:31
 */
public class Rectangle extends Figure {

    private final double width;
    private final double height;

    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public double area() {
        return width * height;
    }

    @Override
    public double volume() {
        return 0;
    }
}
