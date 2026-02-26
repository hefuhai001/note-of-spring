package _04_;

public class Main {
    public static void main(String[] args) {
        String srcFile = "test.docx";
        String destFile = "mytest.docx";
        String textFile = "myfile.txt";
        FileCopyThread copyThread = new FileCopyThread(srcFile, destFile);
        TextCountThread countThread = new TextCountThread(textFile);
        copyThread.start();
        countThread.start();
        try {
            copyThread.join();
            countThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("程序正常结束!");
    }
}