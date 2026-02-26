package _01_JavaJiChu;

import java.io.*;

public class _22_Stream_File_IO {
    public _22_Stream_File_IO() throws FileNotFoundException {


    }
    /*
     * 读取控制台输入
     * */
//    public static void main(String[] args) {
//        BufferedReader br = new BufferedReader(new
//                InputStreamReader(System.in));
//    }

    /*
     * 从控制台读取多字符输入
     * */
//    public static void main(String[] args) throws IOException {
//        char c;
//        // 使用 System.in 创建 BufferedReader
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        System.out.println("输入字符, 按下 'q' 键退出。");
//        // 读取字符
//        do {
//            c = (char) br.read();
//            System.out.println(c);
//        } while (c != 'q');
//    }


    /*
     * 从控制台读取字符串
     * */
//    public static void main(String[] args) throws IOException {
//        // 使用 System.in 创建 BufferedReader
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        String str;
//        System.out.println("Enter lines of text.");
//        System.out.println("Enter 'end' to quit.");
//        do {
//            str = br.readLine();
//            System.out.println(str);
//        } while (!str.equals("end"));
//    }

    /*
     * 控制台输出
     * */
//    public static void main(String[] args) {
//        int b;
//        b = 'A';
//        System.out.write(b);
//        System.out.write('\n');
//    }

    /*
     * 读写文件
     * */

    /*
     * FileInputStream
     * 1	public void close() throws IOException{}
     * 关闭此文件输入流并释放与此流有关的所有系统资源。抛出IOException异常。
     * 2	protected void finalize()throws IOException {}
     * 这个方法清除与该文件的连接。确保在不再引用文件输入流时调用其 close 方法。抛出IOException异常。
     * 3	public int read(int r)throws IOException{}
     * 这个方法从 InputStream 对象读取指定字节的数据。返回为整数值。返回下一字节数据，如果已经到结尾则返回-1。
     * 4	public int read(byte[] r) throws IOException{}
     * 这个方法从输入流读取r.length长度的字节。返回读取的字节数。如果是文件结尾则返回-1。
     * 5	public int available() throws IOException{}
     * 返回下一次对此输入流调用的方法可以不受阻塞地从此输入流读取的字节数。返回一个整数值。
     * */
//    File f = new File("C:/java/hello");
//    InputStream in = new FileInputStream(f);

    /*
     * FileOutputStream
     * 1	public void close() throws IOException{}
     * 关闭此文件输入流并释放与此流有关的所有系统资源。抛出IOException异常。
     * 2	protected void finalize()throws IOException {}
     * 这个方法清除与该文件的连接。确保在不再引用文件输入流时调用其 close 方法。抛出IOException异常。
     * 3	public void write(int w)throws IOException{}
     * 这个方法把指定的字节写到输出流中。
     * 4	public void write(byte[] w)
     * 把指定数组中w.length长度的字节写到OutputStream中。
     * */
//    public static void main(String[] args) {
//        try {
//            byte[] bWrite = {11, 21, 3, 40, 5};
//            OutputStream os = new FileOutputStream("_22_File_1.txt");
//            for (byte b : bWrite) {
//                os.write(b); // writes the bytes
//            }
//            os.close();
//
//            InputStream is = new FileInputStream("test.txt");
//            int size = is.available();
//
//            for (int i = 0; i < size; i++) {
//                System.out.print((char) is.read() + "  ");
//            }
//            is.close();
//        } catch (IOException e) {
//            System.out.print("Exception");
//        }
//    }

    /*
     * 解决乱码问题：
     * */
//    public static void main(String[] args) throws IOException {
//        File f = new File("_22_File_2.txt");
//        FileOutputStream fop = new FileOutputStream(f);
//        // 构建FileOutputStream对象,文件不存在会自动新建
//        OutputStreamWriter writer = new OutputStreamWriter(fop, "UTF-8");
//        // 构建OutputStreamWriter对象,参数可以指定编码,默认为操作系统默认编码,windows上是gbk
//        writer.append("中文输入");
//        // 写入到缓冲区
//        writer.append("\r\n");
//        // 换行
//        writer.append("English");
//        // 刷新缓存冲,写入到文件,如果下面已经没有写入的内容了,直接close也会写入
//        writer.close();
//        // 关闭写入流,同时会把缓冲区内容写入文件,所以上面的注释掉
//        fop.close();
//        // 关闭输出流,释放系统资源
//        FileInputStream fip = new FileInputStream(f);
//        // 构建FileInputStream对象
//        InputStreamReader reader = new InputStreamReader(fip, "UTF-8");
//        // 构建InputStreamReader对象,编码与写入相同
//        StringBuilder sb = new StringBuilder();
//        while (reader.ready()) {
//            sb.append((char) reader.read());
//            // 转成char加到StringBuffer对象中
//        }
//        System.out.println(sb.toString());
//        reader.close();
//        // 关闭读取流
//        fip.close();
//        // 关闭输入流,释放系统资源
//    }


    /*
     * 文件和I/O
     * Java中的目录
     * 创建目录：
     * File类中有两个方法可以用来创建文件夹：
     * mkdir( )方法创建一个文件夹，成功则返回true，失败则返回false。失败表明File对象指定的路径已经存在，或者由于整个路径还不存在，该文件夹不能被创建。
     * mkdirs()方法创建一个文件夹和它的所有父文件夹。
     * */
//    public static void main(String[] args) {
//        String dirname = "/__easyProjects__/IntelliJ IDEA/Runoob.com/_22_IO_mkdir";
//        File d = new File(dirname);
//        // 现在创建目录
//        d.mkdirs();
//    }

    /*
     * 读取目录
     * */
    public static void main(String[] args) {
        String dirname = "/__easyProjects__/IntelliJ IDEA/Runoob.com";
        File f1 = new File(dirname);
        if (f1.isDirectory()) {
            System.out.println("目录 " + dirname);
            String s[] = f1.list();
            for (int i = 0; i < s.length; i++) {
                File f = new File(dirname + "/" + s[i]);
                if (f.isDirectory()) {
                    System.out.println(s[i] + " 是一个目录");
                } else {
                    System.out.println(s[i] + " 是一个文件");
                }
            }
        } else {
            System.out.println(dirname + " 不是一个目录");
        }
    }

    /*
     * 删除目录或文件
     * */
//    public static void main(String[] args) {
//        // 这里修改为自己的测试目录
//        File folder = new File("/__easyProjects__/IntelliJ IDEA/Runoob.com/_22_IO_delete");
//        deleteFolder(folder);
//    }
//
//    // 删除文件及目录
//    public static void deleteFolder(File folder) {
//        File[] files = folder.listFiles();
//        if (files != null) {
//            for (File f : files) {
//                if (f.isDirectory()) {
//                    deleteFolder(f);
//                } else {
//                    f.delete();
//                }
//            }
//        }
//        folder.delete();
//    }


}
