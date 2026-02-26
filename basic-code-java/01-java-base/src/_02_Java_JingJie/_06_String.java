package _02_Java_JingJie;

public class _06_String {
    public static void main(String[] args) {
        //创建空白字符串对象
        String s1=new String();
        System.out.println("s1:"+s1);

        //根据字符数组内容来创建对象
        char[] chs={'a','b','c'};
        String s2=new String(chs);
        System.out.println("s2:"+s2);

        //根据字节数组内容来创建对象
        byte[] bys={97,98,99,100,101};
        String s3=new String(bys);
        System.out.println("s3:"+s3);

        //直接赋值创建对象
        String s4="bcdedit";
        System.out.println("s4:"+s4);

    }
}
