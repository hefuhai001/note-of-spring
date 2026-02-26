package _02_Java_JingJie;

/*
   测试类
*/
public class _11_Lei_FangFa {
    public static void main(String[] args) {
        //创建对象
        //重载方式
        //默认无参构造 如果给出构造方法 系统将不再给出默认构造方法
        _10_Lei s1=new _10_Lei();
        s1.show();

        _10_Lei s2=new _10_Lei("xiaohe");
        s2.show();

        _10_Lei s3=new _10_Lei(18);
        s3.show();

        _10_Lei s4=new _10_Lei("xiaohe",18);
        s4.show();
    }


}
