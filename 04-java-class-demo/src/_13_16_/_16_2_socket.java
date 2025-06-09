package _13_16_;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class _16_2_socket extends Application {// JavaFx下的一种客户端
    Button connection, send;
    TextField inputText;
    TextArea showResult;
    Socket socket = null;
    DataInputStream in = null;
    DataOutputStream out = null;
    Thread thread = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        connection = new Button("请求连接服务器");
        send = new Button("发送");
        send.setDisable(true);
        inputText = new TextField();
        inputText.setPrefColumnCount(6);
        showResult = new TextArea();
        HBox hbox = new HBox(new Label("输入服务请求"), inputText, send);
        hbox.setAlignment(Pos.CENTER);
        BorderPane root = new BorderPane();
        BorderPane.setAlignment(connection, Pos.CENTER);
        root.setTop(connection);
        root.setCenter(showResult);
        root.setBottom(hbox);
        connection.addEventHandler(ActionEvent.ACTION, new MyHandler());
        send.setOnAction((e) -> {
            try {
                out.writeUTF(inputText.getText());
            } catch (IOException e1) {
            }
        });
        socket = new Socket();

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("客户端：");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    class MyHandler implements EventHandler<ActionEvent>, Runnable {
        @Override
        public void handle(ActionEvent e) {
            try { // 请求和服务器建立套接字连接：
                if (socket.isConnected()) {
                } else {
                    socket = new Socket("192.168.0.108", 9090);
                    in = new DataInputStream(socket.getInputStream());
                    out = new DataOutputStream(socket.getOutputStream());
                    send.setDisable(false);
                    if (thread == null || !thread.isAlive()) {
                        thread = new Thread(this);
                    }
                    thread.start();
                }
            } catch (IOException ee) {
                System.out.println(ee);
                socket = new Socket();
            }
        }

        @Override
        public void run() {
            String s = null;
            while (true) {// 多次读取来自服务器的体积信息
                try {
                    s = in.readUTF();// 产生阻塞，直到读取成功或引起异常
                    showResult.appendText(s + "\n");
                } catch (IOException e) {
                    showResult.setText("与服务器已断开" + e);
                    socket = new Socket();
                    break;
                }
            }
        }
    }

    public void stop() throws Exception {// 断开连接
        if (socket != null) {
            socket.close();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}