package _02_MianXiangDuiXiang;

/**
 * @Description
 * @Author XiaoHe
 * @CreateTime 2023/5/12 15:54
 */

public class _05_FenZhuang_test {
    public static void main(String args[]) {
        _05_FenZhuang encap = new _05_FenZhuang();
        encap.setName("James");
        encap.setAge(20);
        encap.setIdNum("12343ms");

        System.out.print("Name : " + encap.getName() +
                " Age : " + encap.getAge());
    }
}
