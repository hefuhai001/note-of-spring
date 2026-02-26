//import javafx.application.Application;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.*;
//import javafx.scene.paint.Color;
//import javafx.stage.Stage;
//
//public class _14_0_TextField extends Application {
//
//    @Override
//    public void start(Stage P) {
//        VBox vBox = new VBox();
//        vBox.setPadding(new Insets(10, 90, 10, 90));
//
//        HBox hBox1 = new HBox();
//        Label label = new Label();
//        label.setMaxHeight(40);
//        label.setMinWidth(201);
//        label.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
//        label.setBorder(new Border(new BorderStroke(Color.GREY, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(1))));
//        hBox1.getChildren().addAll(label);
//
//        TextField textField = new TextField();
//        Button btn1 = new Button("打印");
//        btn1.setAlignment(Pos.BASELINE_RIGHT);
//        btn1.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
//        btn1.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                label.setText(textField.getText());
//            }
//        });
//
//        HBox hBox2 = new HBox();
//        hBox2.setPadding(new Insets(10, 0, 0, 0));
//        hBox2.getChildren().addAll(textField, btn1);
//
//        vBox.getChildren().addAll(hBox1, hBox2);
//        Scene s = new Scene(vBox, 400, 300);
//        P.setTitle("_14_0_TextField");
//        P.setScene(s);
//        P.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
