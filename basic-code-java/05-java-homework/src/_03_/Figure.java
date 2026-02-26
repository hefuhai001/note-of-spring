
/*
 * Figure抽象类
 * */
package _03_;

public abstract class Figure {
    protected double centerX;
    protected double centerY;

    public Figure(double centerX, double centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public abstract double area();

    public abstract double volume();
}