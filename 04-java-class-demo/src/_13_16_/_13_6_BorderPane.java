//import javafx.application.Application;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.HBox;
//import javafx.stage.Stage;
//
//public class _13_6_BorderPane extends Application {
//    @Override
//    public void start(Stage P) {
//        BorderPane borderPane = new BorderPane();
//
//        Button btn1 = new Button("BTN1");
//        HBox hBox = new HBox();
//        hBox.setAlignment(Pos.TOP_CENTER);
//        hBox.getChildren().addAll(btn1);
//        Button btn2 = new Button("BTN2");
//        Button btn3 = new Button("BTN3");
//        Button btn4 = new Button("BTN4");
//        Button btn5 = new Button("BTN5");
//
//        borderPane.setTop(hBox);
//        borderPane.setRight(btn2);
//        borderPane.setBottom(btn3);
//        borderPane.setLeft(btn4);
//        borderPane.setCenter(btn5);
//
//        Scene s = new Scene(borderPane, 400, 300);
//        P.setScene(s);
//        P.setTitle("");
//        P.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//
//    }
//}
