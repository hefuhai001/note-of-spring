package _03_LeiDingYi;
/*
   测试类
*/
public class PhoneDemo {
    public static void main(String[] args) {
        //创建对象
        Phone p1=new Phone();
        //使用成员变量
/*        System.out.println(p1.brand);
        System.out.println(p1.price);*/
        p1.brand="xiaomi";
        p1.price=1999;

        System.out.println(p1.brand);
        System.out.println(p1.price);
        //使用成员方法
        p1.call();
        p1.sendMessage();
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");

        //创建对象
        Phone p2=new Phone();

        p2.brand="huawei";
        p2.price=2999;

        //使用成员变量
        System.out.println(p2.brand);
        System.out.println(p2.price);
        //使用成员方法
        p2.call();
        p2.sendMessage();

    }
}
