package _05_并查集;

/*
 *并查集
 *并查集基础
 *并查集快速查找
 *并查集快速合并
 *并查集 size 的优化
 *并查集 rank 的优化
 *并查集路径压缩
 * */

/**
 * 并查集的构造
 */
public class UnionFind {
    private int[] id;
    // 数据个数
    private int count;

    public UnionFind(int n) {
        count = n;
        id = new int[n];
        // 初始化, 每一个id[i]指向自己, 没有合并的元素
        for (int i = 0; i < n; i++)
            id[i] = i;
    }

}