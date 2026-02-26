package _06_Phone_Extends;

public class Phone_New extends Phone {

    @Override
    public void call(String name){
        System.out.println("开启视频功能");

        System.out.println("打电话给" + name);
//        super.call(name);
    }

}
