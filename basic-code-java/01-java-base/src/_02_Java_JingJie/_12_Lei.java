package _02_Java_JingJie;
/*
    标准类的制作测试
*/
public class _12_Lei {
    public static void main(String[] args) {
        //无参构造方法后使用set赋值
        _12_Lei_FangFa s1 = new _12_Lei_FangFa();
        s1.setName("xiaohe");
        s1.setAge(18);
        s1.show();
        //使用带参构造方法直接创建带有属性值的对象
        _12_Lei_FangFa s2 = new _12_Lei_FangFa("xiaohe", 18);
        s2.show();

    }
}
