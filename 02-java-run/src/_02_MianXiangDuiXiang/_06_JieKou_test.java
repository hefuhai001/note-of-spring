package _02_MianXiangDuiXiang;

/**
 * @Description
 * @Author XiaoHe
 * @CreateTime 2023/5/12 15:58
 */

public class _06_JieKou_test implements _06_JieKou {

    public void eat() {
        System.out.println("Mammal eats");
    }

    public void travel() {
        System.out.println("Mammal travels");
    }

    public int noOfLegs() {
        return 0;
    }

    public static void main(String args[]) {
        _06_JieKou_test m = new _06_JieKou_test();
        m.eat();
        m.travel();
    }
}
