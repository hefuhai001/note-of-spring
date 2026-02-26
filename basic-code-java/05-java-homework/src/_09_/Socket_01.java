package _09_;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Socket_01 extends Application {

    private DatagramSocket clientSocket;
    private TextArea xiaoxiWenBenYu;
    private TextField shuruFiled;
    private Button fasongBtn;
    private InetAddress serverAddress;

    @Override
    public void start(Stage primaryStage) throws Exception {
        xiaoxiWenBenYu = new TextArea();
        xiaoxiWenBenYu.setEditable(false);
        shuruFiled = new TextField();
        shuruFiled.setOnAction(event -> sendMessage());
        fasongBtn = new Button("发送");
        fasongBtn.setOnAction(event -> sendMessage());

        VBox root = new VBox();
        root.getChildren().addAll(xiaoxiWenBenYu, shuruFiled, fasongBtn);

        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setTitle("杨：");
        primaryStage.show();

        new Thread(this::receiveMessages).start();
    }

    private void receiveMessages() {
        try {
            clientSocket = new DatagramSocket();
            serverAddress = InetAddress.getLocalHost();
            byte[] receiveData = new byte[1024];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                Platform.runLater(() -> xiaoxiWenBenYu.appendText("钱: " + message + "\n"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = shuruFiled.getText();
        if (!message.isEmpty()) {
            try {
                byte[] sendData = message.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, 9876);
                clientSocket.send(sendPacket);
                Platform.runLater(() -> xiaoxiWenBenYu.appendText("杨: " + message + "\n"));
                shuruFiled.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        clientSocket.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}