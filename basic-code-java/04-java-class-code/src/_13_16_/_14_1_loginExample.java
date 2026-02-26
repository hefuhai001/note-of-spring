//import javafx.application.Application;
//import javafx.geometry.Insets;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.*;
//import javafx.scene.paint.Color;
//import javafx.stage.Stage;
//
//public class _14_1_loginExample extends Application {
//
//    @Override
//    public void start(Stage P) {
//        TextField textField_acc = new TextField();
//        TextField textField_pwd = new TextField();
//        Button btn = new Button("登录");
//
//        BorderPane borderPane = new BorderPane();
//        borderPane.setTop(textField_acc);
//        borderPane.setCenter(textField_pwd);
//        borderPane.setBottom(btn);
//        borderPane.setPadding(new Insets(80));
//
//        textField_acc.setPadding(new Insets(10, 10, 10, 10));
//        textField_acc.setPromptText("账号");
//        textField_pwd.setPromptText("密码");
//        textField_pwd.setPadding(new Insets(10, 10, 10, 10));
//        btn.setPadding(new Insets(10, 108, 10, 108));
//        btn.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
//        btn.setStyle("-fx-text-fill:white;");
//
//
//        Scene s = new Scene(borderPane, 400, 300);
//        P.setTitle("_14_1_loginExample");
//        P.setScene(s);
//        P.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
