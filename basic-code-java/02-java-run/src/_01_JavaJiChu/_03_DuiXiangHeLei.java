package _01_JavaJiChu;

public class _03_DuiXiangHeLei {
    public static void main(String[] args) {
        /* 使用构造器创建两个对象 */
        _03_Lei empOne = new _03_Lei("RUNOOB1");
        _03_Lei empTwo = new _03_Lei("RUNOOB2");

        // 调用这两个对象的成员方法
        empOne.empAge(26);
        empOne.empDesignation("高级程序员");
        empOne.empSalary(1000);
        empOne.printEmployee();

        empTwo.empAge(21);
        empTwo.empDesignation("菜鸟程序员");
        empTwo.empSalary(500);
        empTwo.printEmployee();
    }
}
