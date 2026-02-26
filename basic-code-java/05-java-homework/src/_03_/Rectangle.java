
/*、
 * Rectangle（矩形）的类
 * */
package _03_;

public class Rectangle extends Figure {
    private final double width;
    private final double height;

    public Rectangle(double centerX, double centerY, double width, double height) {
        super(centerX, centerY);
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