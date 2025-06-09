package _09_.me;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server extends Application {

    private DatagramSocket serverSocket;
    private TextArea messageArea;
    private TextField inputField;
    private Button sendButton;
    private InetAddress clientAddress;
    private int clientPort;

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = new VBox();

        HBox top = new HBox();
        messageArea = new TextArea();
        messageArea.setEditable(false);
        top.getChildren().addAll(messageArea);

        HBox center = new HBox();
        inputField = new TextField();
        inputField.setOnAction(event -> sendMessage());
        sendButton = new Button("发送");
        sendButton.setOnAction(event -> sendMessage());
        center.setAlignment(Pos.CENTER);
        center.setPadding(new Insets(30));
        center.getChildren().addAll(inputField, sendButton);

        root.getChildren().addAll(top, center);

        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.setTitle("杨（服务端）:");
        primaryStage.show();

        new Thread(this::receiveMessages).start();
    }

    private void receiveMessages() {
        try {
            serverSocket = new DatagramSocket(9876);
            byte[] receiveData = new byte[1024];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                clientAddress = receivePacket.getAddress();
                clientPort = receivePacket.getPort();
                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                Platform.runLater(() -> messageArea.appendText("钱（客户端）: " + message + "\n"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = inputField.getText();
        if (!message.isEmpty()) {
            try {
                byte[] sendData = message.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);
                Platform.runLater(() -> messageArea.appendText("\t\t\t\t\t\t\t\t\t" + "杨（服务端）: " + message + "\n"));
                inputField.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        serverSocket.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}