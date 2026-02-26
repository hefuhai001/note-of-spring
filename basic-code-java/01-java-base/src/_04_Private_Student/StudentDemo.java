package _04_Private_Student;

/*
   测试类
*/
public class StudentDemo {
    public static void main(String[] args) {
        Student h = new Student();
//        h.name = "xiaohe";
        h.setName("xiaohe");
        h.setAge(18);


        h.show();
        System.out.println(h.getName() + "~~~" + h.getAge());
        System.out.println(h.getName());
        System.out.println(h.getAge());

    }

}
