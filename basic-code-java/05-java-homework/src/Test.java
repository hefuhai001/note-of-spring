public class Test {
    private static int x;
    private int y;

    Test(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static void main(String[] args) {
        Test test1 = new Test(1, 2);
        Test test2 = new Test(3, 4);
        System.out.println(test1.x + "," + test2.y);
    }
}
