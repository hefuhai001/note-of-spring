package _04_12_;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class _12_1 {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("输入文件夹名称 !");
        String dirName = sc.nextLine();

        CreateDir(dirName);
        WriteTxt(dirName);
    }

    public static void CreateDir(String dirName) {
        String dir = "C:/Users/Administrator/Desktop/" + dirName;
        File d = new File(dir);
        d.mkdirs();
    }


    public static void WriteTxt(String dirName) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("输入文件名称");
        String fileName = sc.nextLine();
        System.out.println("输入文件名后缀");
        String fileNameLast = sc.nextLine();
        File f = new File("C:/Users/Administrator/Desktop/" + dirName + "/" + fileName + "" + fileNameLast);
        FileOutputStream fop = new FileOutputStream(f);
        // 构建FileOutputStream对象,文件不存在会自动新建
        OutputStreamWriter writer = new OutputStreamWriter(fop, StandardCharsets.UTF_8);
        // 构建OutputStreamWriter对象,参数可以指定编码,默认为操作系统默认编码,windows上是gbk
        System.out.println("输入文件内容");
        String inputText = sc.nextLine();
        writer.append(inputText);
        // 写入到缓冲区
        writer.append("\r\n");
        // 换行
        // 刷新缓存冲,写入到文件,如果下面已经没有写入的内容了,直接close也会写入
        writer.close();
        // 关闭写入流,同时会把缓冲区内容写入文件,所以上面的注释掉
        fop.close();
        // 关闭输出流,释放系统资源
        FileInputStream fip = new FileInputStream(f);
        // 构建FileInputStream对象
        InputStreamReader reader = new InputStreamReader(fip, StandardCharsets.UTF_8);
        // 构建InputStreamReader对象,编码与写入相同
        StringBuilder sb = new StringBuilder();
        while (reader.ready()) {
            sb.append((char) reader.read());
            // 转成char加到StringBuffer对象中
        }
//        System.out.println(sb.toString());
        reader.close();
        // 关闭读取流
        fip.close();
        // 关闭输入流,释放系统资源
        System.out.println("文件写入完成");

    }
}
