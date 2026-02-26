package _03_other;

/**
 * @Description
 * @Author XiaoHe
 * @CreateTime 2023/4/2 17:31
 */
public abstract class Figure {

    protected double centerX;
    protected double centerY;

    public double getCenterX() {
        return centerX;
    }

    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
    }

    public abstract double area();

    public abstract double volume();
}
