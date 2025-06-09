package _02_Java_JingJie;

public class _10_Lei {
    private String name;
    private int age;

    //构造方法
    public _10_Lei(){
        System.out.println("无参构造方法");
    }
    public _10_Lei(String name){
        this.name=name;
    }
    public _10_Lei(int age){
        this.age=age;
    }
    public _10_Lei(String name, int age){
        this.name=name;
        this.age=age;
    }
    public void show(){
        System.out.println(name+","+age);
    }
}
