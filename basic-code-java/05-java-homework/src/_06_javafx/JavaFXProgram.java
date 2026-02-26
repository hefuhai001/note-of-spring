package _06_javafx;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
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

public class JavaFXProgram extends Application {

    @Override
    public void start(Stage primaryStage) {

        // HBox 在 Top 区域
        HBox hbox = new HBox();
        Circle circle = new Circle(60);
        circle.setFill(Color.RED);
        Ellipse blackEllipse = new Ellipse(120, 40);
        blackEllipse.setFill(Color.BLACK);
        hbox.getChildren().addAll(circle, blackEllipse);
        hbox.setAlignment(Pos.CENTER);

        // VBox 在 Left 区域
        VBox vboxLeft = new VBox();
        vboxLeft.setAlignment(Pos.CENTER);
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setValue("选项一");
        comboBox.getItems().addAll("选项一", "选项二", "选项三", "选项四");
        vboxLeft.getChildren().add(comboBox);

        // VBox 在 Right 区域
        VBox vboxRight = new VBox();
        Polygon triangle = new Polygon(new double[]{0, 50, 25, 0, 50, 50});
        triangle.setFill(Color.BLACK);
        triangle.setStroke(Color.BLACK);
        vboxRight.getChildren().add(triangle);
        vboxRight.setAlignment(Pos.CENTER);

        // TextFlow 在 Bottom 区域
        TextFlow textFlow = new TextFlow();
        Text text1 = new Text("Hello");
        text1.setFill(Color.RED);
        Text text2 = new Text("Blod");
        text2.setFill(Color.YELLOW);
        text2.setStyle("-fx-font-weight:bold;");
        Text text3 = new Text("word");
        text3.setFill(Color.GREEN);
        Rotate rotate = new Rotate(90, text3.getX() + text3.getLayoutBounds().getWidth() * 1.5,
                text3.getY() + text3.getLayoutBounds().getHeight() * 2);
        text3.getTransforms().add(rotate);

        textFlow.setStyle("-fx-font-size: 40");
        textFlow.getChildren().addAll(text1, text2, text3);
        textFlow.setPrefWidth(Double.MAX_VALUE);
        textFlow.setTextAlignment(TextAlignment.CENTER);

        // 图片在 Center 区域
        Image image = new Image("file:D:/__easyProjects__/IntelliJ IDEA/Java_Journey/01.png", 400, 600, true, true);
        ImageView imageView = new ImageView(image);

        // 创建 BorderPane 并设置各个区域
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(hbox);
        borderPane.setBottom(textFlow);
        borderPane.setLeft(vboxLeft);
        borderPane.setRight(vboxRight);
        borderPane.setCenter(imageView);

        // 创建场景并显示
        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setTitle("JavaFX 基础");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
