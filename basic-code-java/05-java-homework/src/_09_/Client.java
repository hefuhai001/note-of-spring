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

public class Client extends Application {

    private DatagramSocket clientSocket;
    private TextArea messageArea;
    private TextField inputField;
    private Button sendButton;

    @Override
    public void start(Stage primaryStage) throws Exception {
        messageArea = new TextArea();
        messageArea.setEditable(false);
        inputField = new TextField();
        inputField.setOnAction(event -> sendMessage());
        sendButton = new Button("Send");
        sendButton.setOnAction(event -> sendMessage());

        VBox root = new VBox();
        root.getChildren().addAll(messageArea, inputField, sendButton);

        primaryStage.setScene(new Scene(root, 400, 400));
        primaryStage.setTitle("Client");
        primaryStage.show();

        new Thread(this::receiveMessages).start();
    }

    private void receiveMessages() {
        try {
            clientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getLocalHost();
            byte[] receiveData = new byte[1024];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                Platform.runLater(() -> messageArea.appendText("Server: " + message + "\n"));
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
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getLocalHost(), 9876);
                clientSocket.send(sendPacket);
                Platform.runLater(() -> messageArea.appendText("Client: " + message + "\n"));
                inputField.setText("");
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