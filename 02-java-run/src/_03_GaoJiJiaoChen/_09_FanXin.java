package _03_GaoJiJiaoChen;

/**
 * @Description
 * @Author XiaoHe
 * @CreateTime 2023/5/12 16:24
 */

import java.util.*;

public class _09_FanXin {

    public static void main(String[] args) {
        List<String> name = new ArrayList<String>();
        List<Integer> age = new ArrayList<Integer>();
        List<Number> number = new ArrayList<Number>();
        name.add("icon");
        age.add(18);
        number.add(314);
        //getUperNumber(name);//1
        getUperNumber(age);//2
        getUperNumber(number);//3
    }

    public static void getData(List<?> data) {
        System.out.println("data :" + data.get(0));
    }

    public static void getUperNumber(List<? extends Number> data) {
        System.out.println("data :" + data.get(0));
    }
}
