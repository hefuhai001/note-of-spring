package _02_Java_JingJie;

public class _20_String_Builder {
    public static void main(String[] args) {
        StringBuilder sb=new StringBuilder();
        sb.append("hello");

        String s=sb.toString();
        System.out.println("s:"+s);



        String h= "hello";
        StringBuilder sh=new StringBuilder(h);

        System.out.println("sh:"+sh);

    }
}
