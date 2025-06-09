package _03_other;

/**
 * @Description
 * @Author XiaoHe
 * @CreateTime 2023/4/2 17:33
 */
public class Solid extends Figure {
    private final double length;
    private final double width;
    private final double height;

    public Solid(double length, double width, double height) {
        this.length = length;
        this.width = width;
        this.height = height;
    }

    @Override
    public double area() {
        return 2 * (length * width + width * height + height * length);
    }

    @Override
    public double volume() {
        return length * width * height;
    }
}
