package _04_12_;

interface _01_ {
    public abstract void test();
}

interface _04_ extends _01_ {
    public abstract void easy();
}

class _02_ implements _01_ {
    @Override
    public void test() {
        System.out.println("_02_test");
    }
}

class _03_ implements _04_ {
    @Override
    public void easy() {
        System.out.println("easy");
    }

    @Override
    public void test() {
        System.out.println("_03_test");
    }
}

public class _07_1_weeks {
    public static void main(String[] args) {
        _02_ a = new _02_();
        _03_ b = new _03_();
        a.test();
        b.easy();
        b.test();

    }
}
