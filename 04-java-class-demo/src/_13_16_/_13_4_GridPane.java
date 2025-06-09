//import javafx.application.Application;
//import javafx.geometry.Insets;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.layout.GridPane;
//import javafx.stage.Stage;
//
//public class _13_4_GridPane extends Application {
//    @Override
//    public void start(Stage P) {
//        GridPane gridPane = new GridPane();
//        Button btn1 = new Button("BTN1");
//        Button btn2 = new Button("BTN2");
//        Button btn3 = new Button("BTN3");
//        Button btn4 = new Button("BTN4");
//
//        gridPane.add(btn1, 0, 0);
//        gridPane.add(btn2, 0, 1);
//        gridPane.add(btn3, 1, 0);
//        gridPane.add(btn4, 1, 1);
//
//        gridPane.setHgap(100);
//        gridPane.setVgap(10);
//        gridPane.setPadding(new Insets(10));
//
//        Scene s = new Scene(gridPane, 400, 300);
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
