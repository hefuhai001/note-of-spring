package _02_Java_JingJie;

public class _19_String_apply {
    public static void main(String[] args) {
        StringBuilder sb=new StringBuilder();
        StringBuilder sb2=sb.append("hello");
        System.out.println("sb:"+sb);
        System.out.println("sb2:"+sb2);

        System.out.println(sb==sb2);
        //添加
        sb.append("_hello_");
        sb.append("_java_");
        sb.append("_100_");

        //sb.append("hello").append("java").append("100");
        System.out.println("sb:"+sb);

        sb.reverse();
        //反转
        System.out.println("sb:"+sb);



    }
}
