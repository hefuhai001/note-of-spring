package _03_GaoJiJiaoChen;

/**
 * @Description
 * @Author XiaoHe
 * @CreateTime 2023/5/12 16:21
 */

/*
 * Java Iterator（迭代器）
 * */

// 引入 ArrayList 和 Iterator 类

import java.util.ArrayList;
import java.util.Iterator;

public class _07_Iterator {
    public static void main(String[] args) {
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        numbers.add(12);
        numbers.add(8);
        numbers.add(2);
        numbers.add(23);
        Iterator<Integer> it = numbers.iterator();
        while (it.hasNext()) {
            Integer i = it.next();
            if (i < 10) {
                it.remove(); // 删除小于 10 的元素
            }
        }
        System.out.println(numbers);
    }
}
