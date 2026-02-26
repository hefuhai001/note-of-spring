package _02_Java_JingJie;

import java.util.ArrayList;

public class _22_ArrayList {
    public static void main(String[] args) {

        ArrayList<String> array=new  ArrayList<>();
        array.add("hello");
        array.add("world");

        System.out.println(array.add("java"));
        array.add(0,"javaee");
        System.out.println("array:"+array);
    }
}
