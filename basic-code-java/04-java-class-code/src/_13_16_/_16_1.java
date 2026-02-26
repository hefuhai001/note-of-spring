package _13_16_;

import java.net.*;
import java.io.*; //客户端

public class _16_1 {
    public static void main(String[] args) {
        Socket csocket = null;
        BufferedReader sockIn;
        try {
            csocket = new Socket("127.0.0.1", 8800);// 表示主机是本机，端口8800
            sockIn = new BufferedReader(new InputStreamReader(csocket.getInputStream()));
            String s = sockIn.readLine();
            System.out.println("Clinet receiving:" + s);
            sockIn.close();
            csocket.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}

//import java.net.*;//服务器端
//import java.io.*;
//
//public class Server0 {
//    public static void main(String[] args) {
//        ServerSocket serversocket = null;
//        Socket ssocket = null;
//        PrintWriter sockOut;
//        try {
//            serversocket = new ServerSocket(8800);
//            ssocket = serversocket.accept();
//            sockOut = new PrintWriter(ssocket.getOutputStream());
//            sockOut.println("This is from Server!");
//            sockOut.close();
//            ssocket.close();
//            serversocket.close();
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//    }
//}



//import java.net.InetAddress;
//import java.net.UnknownHostException;
//
//public class MyAddress {
//    public static void main(String[] args) throws UnknownHostException {
//        InetAddress address = null;
//        address = InetAddress.getLocalHost();
//        System.out.println("本机的IP地址：" + address.getHostAddress());
//        System.out.println("本机的主机名：" + address.getHostName());
//    }
//}
