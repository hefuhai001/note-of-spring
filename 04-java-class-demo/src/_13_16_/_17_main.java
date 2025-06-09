package _13_16_;

public class _17_main {
    public static void main(String[] args) {
        System.out.println("main1");
        main(new Integer[2]);
    }

    public static void main(Integer[] aaa) {
        System.out.println("main2");
        main(new String[3]);
    }
}
