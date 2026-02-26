package _04_Private_Student;

public class Student {
    //    String name;
    //    int age;

    //    private
    private String name;
    private int age;

    //    set/get方法
    public void setName(String name) {
        //this指代成员变量（全局变量）
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAge(int age) {
//        age = a;
        if (age < 0 || age > 120) {
            System.out.println("数据有误");
        } else {
            this.age = age;
        }
    }

    public int getAge() {
        return age;
    }


    public void show() {
        System.out.println(name + "," + age);
    }
}
