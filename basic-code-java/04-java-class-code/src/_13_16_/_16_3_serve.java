package _13_16_;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class _16_3_serve {// 服务器端
    ServerSocket server = null;
    public static void main(String args[]) {
        ServerSocket server = null;
        Socket you = null;
        while (true) {
            try {
                server = new ServerSocket(9090);
            } catch (IOException e1) {
                System.out.println("正在监听"); // ServerSocket对象不能重复创建
            }
            try {
                System.out.println(" 等待客户呼叫");
                you = server.accept();// 返回Sockt,该方法阻塞，直到建立连接。
            } catch (IOException e) {
                System.out.println("正在等待客户");
            }
            if (you != null) {
                new ServerCodeThread(you).start(); // 为每个客户启动一个专门的线程
            }
        }
    }
}

class ServerCodeThread extends Thread {// 服务线程
    Socket socket;
    DataOutputStream out = null;
    DataInputStream in = null;

    ServerCodeThread(Socket t) {// 构造方法，初始化客户端socket，并建立与该客户通信的DataOutputStream和DataInputStream
        socket = t;
        try {
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
        }
    }

    public void run() {// 书写服务代码
        while (true) {
            try {
                String s = in.readUTF();// 堵塞状态，除非读取到信息
                out.writeUTF("服务器收到客户机" + socket.getInetAddress() + "发来的信息：" + s);
            } catch (IOException e) {
                System.out.println(socket.getInetAddress() + "客户离开");
                return;
            }
        }
    }
}

