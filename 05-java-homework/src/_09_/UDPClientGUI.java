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

public class UDPClientGUI extends Application {

    private DatagramSocket clientSocket;
    private TextArea messageArea;
    private TextField messageField;

    public UDPClientGUI() {
        try {
            clientSocket = new DatagramSocket();
        } catch (Exception e) {
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
            String sentence = messageField.getText();
            byte[] sendData = sentence.getBytes();
            DatagramPacket sendPacket = null;
            try {
                sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("localhost"), 9876);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            try {
                clientSocket.send(sendPacket);
            } catch (Exception e) {
                e.printStackTrace();
            }
            messageField.setText("");
        });
        VBox bottomVBox = new VBox();
        bottomVBox.getChildren().addAll(messageField, sendButton);
        root.setBottom(bottomVBox);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("UDP Client");
        primaryStage.setScene(scene);
        primaryStage.show();

        new Thread(() -> {
            byte[] receiveData = new byte[1024];
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                try {
                    clientSocket.receive(receivePacket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String modifiedSentence = new String(receivePacket.getData(), 0, receivePacket.getLength());

                Platform.runLater(() -> {
                    messageArea.appendText(modifiedSentence + "\n");
                });
            }
        }).start();
    }
}