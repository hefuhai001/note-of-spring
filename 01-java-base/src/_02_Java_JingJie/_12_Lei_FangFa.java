package _02_Java_JingJie;
/*
    标准类的制作
*/
public class _12_Lei_FangFa {
    private String name;
    private int age;

    public _12_Lei_FangFa() {

    }

    //使用带参构造方法直接创建带有属性值的对象
    public _12_Lei_FangFa(String name, int age) {
        this.name = name;
        this.age = age;
    }

    //无参构造方法后使用set赋值
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void show() {
        System.out.println(name + "," + age);
    }
}
