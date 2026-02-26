
/*
 * Soild（长方体）的类
 * */
package _03_;

public class Solid extends Figure {
    private final double length;
    private final double width;
    private final double height;

    public Solid(double centerX, double centerY, double length, double width, double height) {
        super(centerX, centerY);
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