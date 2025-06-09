package _06_javafx;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class JavaFXApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        HBox topBox = new HBox();
        topBox.setAlignment(Pos.CENTER);
        topBox.setSpacing(20);
        Circle redCircle = new Circle(40);
        redCircle.setFill(Color.RED);
        Ellipse blackEllipse = new Ellipse(60, 30);
        blackEllipse.setFill(Color.BLACK);
        topBox.getChildren().addAll(redCircle, blackEllipse);

        TextFlow textFlow = new TextFlow();
        Text text1 = new Text("Hello");
        text1.setFill(Color.RED);
        Text text2 = new Text("Blod     ");
        text2.setFill(Color.YELLOW);
        text2.setStyle("-fx-font-weight:bold;");
        Text text3 = new Text("word");
        text3.setFill(Color.GREEN);
        Rotate rotate = new Rotate(90);
        text3.getTransforms().add(rotate);
        textFlow.setStyle("-fx-font-size: 30");
        textFlow.getChildren().addAll(text1, text2, text3);
        textFlow.setPrefWidth(Double.MAX_VALUE);
        textFlow.setTextAlignment(TextAlignment.CENTER);

        VBox leftBox = new VBox();
        leftBox.setAlignment(Pos.CENTER);
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setValue("选项一");
        comboBox.getItems().addAll("选项一", "选项二", "选项三", "选项四");
        leftBox.getChildren().addAll(new Label(" "), comboBox);

        VBox rightBox = new VBox();
        rightBox.setAlignment(Pos.CENTER);
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(15.0, 30.0,
                30.0, 0.0,
                0.0, 0.0);
        triangle.setFill(Color.BLACK);
        triangle.setStroke(Color.BLACK);
        triangle.setRotate(180);
        rightBox.getChildren().add(triangle);

        ImageView imageView = new ImageView(new Image("file:C:\\Users\\21096\\Downloads\\1.png"));
        imageView.setFitWidth(400);
        imageView.setPreserveRatio(true);

        BorderPane root = new BorderPane();
        root.setTop(topBox);
        root.setBottom(textFlow);
        root.setLeft(leftBox);
        root.setRight(rightBox);
        root.setCenter(imageView);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("JavaFX 基础");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}