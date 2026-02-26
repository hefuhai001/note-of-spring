package _01_UDP;

public class _02ShuZu_ {
    public static void main(String[] args) {
        int[] myList = {2, 3, 5, 7, 11, 13, 17};
        reverseOrder(myList);
    }

    public static void reverseOrder(int[] a) {
        int temp = 0;
        int len = a.length;
        for (int i = 0; i < len / 2; i++) {
            temp = a[len - 1 - i];
            a[len - 1 - i] = a[i];
            a[i] = temp;
        }
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + "  ");
        }
    }
}
