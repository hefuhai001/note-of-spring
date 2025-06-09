package _09_;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class UDPServerGUI extends Application {

    private DatagramSocket serverSocket;
    private TextArea messageArea;
    private TextField messageField;
    private static List<InetAddress> clientIPs = new ArrayList<>();

    public UDPServerGUI() {
        try {
            serverSocket = new DatagramSocket(9876);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();

        messageArea = new TextArea();
        messageArea.setEditable(false);
        VBox centerVBox = new VBox();
        centerVBox.getChildren().add(messageArea);
        root.setCenter(centerVBox);

        messageField = new TextField();
        Button sendButton = new Button("Send");
        sendButton.setOnAction(event -> {
            String message = messageField.getText();
            byte[] messageBytes = message.getBytes();
            for (InetAddress clientIP : clientIPs) {
                DatagramPacket sendPacket = new DatagramPacket(messageBytes, messageBytes.length, clientIP, 9876);
                try {
                    serverSocket.send(sendPacket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Platform.runLater(() -> {
                messageArea.appendText("Server: " + message + "\n");
            });
            messageField.setText("");
        });
        VBox bottomVBox = new VBox();
        bottomVBox.getChildren().addAll(messageField, sendButton);
        root.setBottom(bottomVBox);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("UDP Server");
        primaryStage.setScene(scene);
        primaryStage.show();

        new Thread(() -> {
            byte[] receiveData = new byte[1024];
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                try {
                    serverSocket.receive(receivePacket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String sentence = new String(receivePacket.getData());
                InetAddress clientIP = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                if (!clientIPs.contains(clientIP)) {
                    clientIPs.add(clientIP);
                }

                Platform.runLater(() -> {
                    messageArea.appendText("Received from " + clientIP + ":" + clientPort + ": " + sentence + "\n");
                });
            }
        }).start();
    }
}