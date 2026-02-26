package _01_JavaJiChu;


//Java 枚举
class FreshJuice {
    enum FreshJuiceSize {SMALL, MEDIUM, LARGE}

    FreshJuiceSize size;
}

public class _02_MeiJu {
    public static void main(String[] args) {
        FreshJuice juice = new FreshJuice();
        juice.size = FreshJuice.FreshJuiceSize.MEDIUM;
    }
}
