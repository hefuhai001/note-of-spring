class Father {
    protected int x, y;

    Father(int a, int b) {
        x = a;
        y = b;
    }
}

public class A extends Father {
    private int a;

    A(int a, int b, int c) {
        super(b, a);
        this.a = c;
    }

    public static void main(String[] args) {
        A ma = new A(1, 2, 3);
        System.out.println(ma.a + "\t" + ma.y);
    }
}
