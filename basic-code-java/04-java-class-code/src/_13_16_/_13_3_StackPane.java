//import javafx.application.Application;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.layout.StackPane;
//import javafx.stage.Stage;
//
//public class _13_3_StackPane extends Application {
//
//    @Override
//    public void start(Stage P) {
//        StackPane stackpane = new StackPane();
//
//        Button btn1 = new Button("BTN1");
//        Button btn2 = new Button("BTN2");
//        Button btn3 = new Button("BTN3");
//        StackPane.setMargin(btn1, new Insets(10, 10, 10, 10));
//        StackPane.setMargin(btn2, new Insets(10, 10, 10, 10));
//        StackPane.setMargin(btn3, new Insets(10, 10, 10, 10));
//        StackPane.setAlignment(btn1, Pos.CENTER_LEFT);
//        StackPane.setAlignment(btn2, Pos.CENTER);
//        StackPane.setAlignment(btn3, Pos.CENTER_RIGHT);
//        stackpane.getChildren().addAll(btn1, btn2, btn3);
//
//        Scene scane = new Scene(stackpane, 400, 300);
//        P.setTitle("StackPane");
//        P.setScene(scane);
//        P.show();
//
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
