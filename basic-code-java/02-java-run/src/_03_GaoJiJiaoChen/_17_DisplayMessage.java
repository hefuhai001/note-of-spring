package _03_GaoJiJiaoChen;

/**
 * @Description
 * @Author XiaoHe
 * @CreateTime 2023/5/12 17:31
 */

// 文件名 : DisplayMessage.java
// 通过实现 Runnable 接口创建线程
public class _17_DisplayMessage implements Runnable {
    private String message;

    public _17_DisplayMessage(String message) {
        this.message = message;
    }

    public void run() {
        while(true) {
            System.out.println(message);
        }
    }
}
