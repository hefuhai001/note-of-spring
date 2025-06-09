package _03_GaoJiJiaoChen;

/**
 * @Description
 * @Author XiaoHe
 * @CreateTime 2023/5/12 16:17
 */
// 引入 HashSet 类

import java.util.HashSet;

public class _05_HashSet {
    public static void main(String[] args) {
        HashSet<String> sites = new HashSet<String>();
        sites.add("Runoob");
        sites.add("Taobao");
        sites.add("Google");
        sites.add("Zhihu");
        sites.add("Runoob"); // 重复的元素不会被添加
        sites.clear();       //删除集合中所有元素可以使用 clear 方法：
        System.out.println(sites);
        System.out.println(sites.contains("Taobao"));
    }
}