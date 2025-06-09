//import javafx.application.Application;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.layout.HBox;
//import javafx.stage.Stage;
//
//public class _13_7_ButtonClickExample extends Application {
//
//    @Override
//    public void start(Stage P) {
//        Button btn1 = new Button("BTN1");
//        btn1.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                System.out.println("BTN1 Clicked");
//            }
//        });
//        Button btn2 = new Button("BTN2");
//        btn2.setOnAction(event -> System.out.println("BTN2 Clicked"));
//
//        HBox hBox = new HBox();
//        hBox.setAlignment(Pos.BASELINE_RIGHT);
//        hBox.getChildren().addAll(btn1, btn2);
//        Scene s = new Scene(hBox, 400, 300);
//        P.setTitle("_13_7_ButtonClickExample");
//        P.setScene(s);
//        P.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
