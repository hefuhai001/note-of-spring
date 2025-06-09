package _01_UDP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
    public static void main(String[] args) {
        try {
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress serverIP = InetAddress.getByName("localhost");
            int serverPort = 9999;

            while (true) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("请输入要发送给服务器的消息：");
                String clientRequest = reader.readLine();

                byte[] sendData = clientRequest.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverIP, serverPort);
                clientSocket.send(sendPacket);

                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);

                InetAddress serverIPResponse = receivePacket.getAddress();
                int serverPortResponse = receivePacket.getPort();
                String serverResponse = new String(receivePacket.getData(), 0, receivePacket.getLength());

                System.out.println("[收到服务器" + serverIPResponse.getHostAddress() + ":" + serverPortResponse + "的消息：" + serverResponse + "]");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("客户端已停止工作。");
        }
    }
}