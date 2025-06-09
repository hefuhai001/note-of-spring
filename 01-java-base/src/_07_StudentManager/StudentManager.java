package _07_StudentManager;

import java.util.ArrayList;
import java.util.Scanner;

public class StudentManager {
    public static void main(String[] args) {
        ArrayList<Student> array = new ArrayList<>();

        while (true) {
            System.out.println("------欢迎来到学生管理系统------");
            System.out.println("\t\t 1添加学生");
            System.out.println("\t\t 2删除学生");
            System.out.println("\t\t 3修改学生");
            System.out.println("\t\t 4查看所有学生");
            System.out.println("\t\t 5退出系统");
            System.out.println("------请输入你的选择：\t\t----");

            Scanner sc = new Scanner(System.in);
            String line = sc.nextLine();
            switch (line) {
                case "1":
//                    System.out.println("添加学生");
                    addStudent(array);
                    break;
                case "2":
//                    System.out.println("删除学生");
                    deleteStudent(array);
                    break;
                case "3":
                    updateStudent(array);
//                    System.out.println("修改学生");
                    break;
                case "4":
//                    System.out.println("查看所有学生");
                    findStudent(array);
                    break;
                case "5":
                    System.out.println("退出系统成功！");
//                    break;
                    System.exit(0);//JVM退出
            }
        }
    }

    public static void addStudent(ArrayList<Student> array) {
        Scanner sc = new Scanner(System.in);
        String sid;
        while (true) {
            System.out.println("请输入学生学号：");
            sid = sc.nextLine();
            boolean flag = isUsed(array, sid);
            if (flag) {
                System.out.println("你输入的学号已经被使用，请重新输入");
            } else {
                break;
            }
        }

//        Scanner sc = new Scanner(System.in);
//        String sid;
//        while (true) {
//            System.out.println("请输入学生学号：");
//            sid = sc.nextLine();
//
//            //~~~添加校验输入0为退出添加功能~~~
//            if (sid == "0") {
//                System.out.println("输入0退出");
//                break;
//            }else {
//                boolean flag = isUsed(array, sid);
//                if (flag) {
//                    System.out.println("你输入的学号已经被使用，请重新输入");
//                } else {
//                    break;
//                }
//            }
//        }

        System.out.println("请输入学生名字：");
        String name = sc.nextLine();
        System.out.println("请输入学生年龄：");
        String age = sc.nextLine();
        System.out.println("请输入学生地址：");
        String address = sc.nextLine();

        Student s = new Student();
        s.setSid(sid);
        s.setName(name);
        s.setAge(age);
        s.setAddress(address);

        array.add(s);
        System.out.println("添加成功!");

    }

    public static void deleteStudent(ArrayList<Student> array) {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入你要删除学生的学号：");
        String sid = sc.nextLine();

        int index = -1;
        for (int i = 0; i < array.size(); i++) {
            Student s = array.get(i);
            if (s.getSid().equals(sid)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            System.out.println("你输入的信息不存在，请重新选择");
        } else {
            array.remove(index);
            System.out.println("删除学生成功！");
        }
        //你输入的信息不存在->会退出到系统主菜单，无法直接重新输入？？？
    }

    public static void updateStudent(ArrayList<Student> array) {
        //03-修改
//        Scanner sc = new Scanner(System.in);
//        String sid;
//        while (true) {
//            System.out.println("请输入学生学号：");
//            sid = sc.nextLine();
//            boolean flag = isUsed(array, sid);
//            if (!flag) {
//                System.out.println("你输入的学号有误，请重新输入：");
//            } else {
//                break;
//            }
//        }
//
//        System.out.println("请输入学生名字：");
//        String name = sc.nextLine();
//        System.out.println("请输入学生年龄：");
//        String age = sc.nextLine();
//        System.out.println("请输入学生地址：");
//        String address = sc.nextLine();
//
//        case01_Student s = new case01_Student();
//        s.setSid(sid);
//        s.setName(name);
//        s.setAge(age);
//        s.setAddress(address);
//
//        array.set(Integer.parseInt(sid), s);
//        System.out.println("修改成功!");
//        //array.set(Integer.parseInt(sid), s);->报错退出系统？？？

        //02-修改
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入修改学生的学号：");
        String sid = sc.nextLine();
        int num = -1;
        for (int i = 0; i < array.size(); i++) {
            Student s = array.get(i);
            if (s.getSid().equals(sid)) {
                num = i;
                break;
            }
        }
        if (num == -1) {
            System.out.println("输入的信息不存在，请重新选择");
        } else {
            System.out.println("请输入学生新姓名：");
            String name = sc.nextLine();
            System.out.println("请输入学生新年龄：");
            String age = sc.nextLine();
            System.out.println("请输入学生新地址：");
            String address = sc.nextLine();
            //创建新对象
            Student s = new Student();
            s.setSid(sid);
            s.setName(name);
            s.setAge(age);
            s.setAddress(address);
            array.set(num, s);
            System.out.println("修改学生成功！");
        }
        //输入的信息不存在->会退出到系统主菜单，无法直接重新输入？？？


        //01-修改
//        for (int i = 0; i <= array.size(); i++) {
//            case01_Student case01_student = array.get(i);
//            if (case01_student.getSid().equals(sid)) {
//                System.out.println("请输入学生新姓名");
//                String name = sc.nextLine();
//                System.out.println("请输入学生新年龄");
//                String age = sc.nextLine();
//                System.out.println("请输入学生新地址");
//                String address = sc.nextLine();
//                //创建新对象
//                case01_Student s = new case01_Student();
//                s.setSid(sid);
//                s.setName(name);
//                s.setAge(age);
//                s.setAddress(address);
//
//                array.set(i, s);
//                System.out.println("修改学生成功！");
//
//            } else {
//                System.out.println("输入的信息不存在，请重新输入");
//                //->会报错退出系统？？？
//            }
//        }
    }

    public static void findStudent(ArrayList<Student> array) {
        if (array.size() == 0) {
            System.out.println("无记录 请先输入学生信息");
            return;//为了绕过后方程序
        }
        System.out.println("学号\t\t姓名\t\t年龄\t\t地址");
        for (int i = 0; i < array.size(); i++) {
            Student s = array.get(i);
            System.out.println(s.getSid() + "\t\t" + s.getName() + "\t\t" + s.getAge() + "岁\t\t" + s.getAddress());
        }
    }

    //校验{添加&&03-修改}学生学号是否重复
    public static boolean isUsed(ArrayList<Student> array, String sid) {
        boolean flag = false;

        for (int i = 0; i < array.size(); i++) {
            Student s = array.get(i);
            if (s.getSid().equals(sid)) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}
