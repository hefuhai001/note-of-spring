package _01_UDP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServe {
    public static void main(String[] args) {
        try {
            DatagramSocket serverSocket = new DatagramSocket(9999);
            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                InetAddress clientIP = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                String clientRequest = new String(receivePacket.getData(), 0, receivePacket.getLength());

                System.out.println("[收到客户端" + clientIP.getHostAddress() + ":" + clientPort + "的消息：" + clientRequest + "]");

                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("请输入回复给客户端" + clientIP.getHostAddress() + ":" + clientPort + "的消息：");
                String serverResponse = reader.readLine();

                sendData = serverResponse.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientIP, clientPort);
                serverSocket.send(sendPacket);

                System.out.println("[已向客户端" + clientIP.getHostAddress() + ":" + clientPort + "发送消息：" + serverResponse + "]");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("服务器已停止工作。");
        }
    }
}
