package _07_javafx_e;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class javaFxApp extends Application {

    @Override
    public void start(Stage primaryStage) {

        // 布局
        BorderPane root = new BorderPane();
        HBox top = new HBox();
        VBox center = new VBox();
        TextArea right = new TextArea();
        HBox bottom = new HBox();

        // Top
        Label topLabel = new Label("个人信息");
        topLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #004799;");
        top.getChildren().add(topLabel);
        top.setAlignment(Pos.CENTER);
        top.setPadding(new Insets(20));

        // Center
        Label nameLabel = new Label("真实姓名:");
        TextField nameField = new TextField();
        HBox nameBox = new HBox(10, nameLabel, nameField);

        Label sexLabel = new Label("性别:");
        ToggleGroup sexGroup = new ToggleGroup();
        RadioButton maleRadio = new RadioButton("男");
        maleRadio.setToggleGroup(sexGroup);
        RadioButton femaleRadio = new RadioButton("女");
        femaleRadio.setToggleGroup(sexGroup);
        HBox sexBox = new HBox(10, sexLabel, maleRadio, femaleRadio);

        Label birthLabel = new Label("出生日期:");
        TextField yearField = new TextField();
        yearField.setPrefWidth(60);
        Label yearLabel = new Label("年");
        TextField monthField = new TextField();
        monthField.setPrefWidth(40);
        Label monthLabel = new Label("月");
        TextField dayField = new TextField();
        dayField.setPrefWidth(40);
        Label dayLabel = new Label("日");
        HBox birthBox = new HBox(10, birthLabel, yearField, yearLabel, monthField, monthLabel, dayField, dayLabel);

        Label typeLabel = new Label("证件类型:");
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("身份证", "军人证", "学生证", "护照");
        typeBox.setValue("身份证");
        HBox typeBoxHBox = new HBox(10, typeLabel, typeBox);

        Label numLabel = new Label("证件号码:");
        TextField numField = new TextField();
        HBox numBox = new HBox(10, numLabel, numField);

        center.getChildren().addAll(nameBox, sexBox, birthBox, typeBoxHBox, numBox);
        center.setAlignment(Pos.CENTER);
        center.setSpacing(20);
        center.setPadding(new Insets(20));

        // Bottom
        Button submitBtn = new Button("提交");
        Button clearBtn = new Button("重填");
        bottom.getChildren().addAll(submitBtn, clearBtn);
        bottom.setAlignment(Pos.CENTER);
        bottom.setSpacing(20);
        bottom.setPadding(new Insets(20));

        // Right
        right.setEditable(false);
        right.setPrefRowCount(10);
        right.setPrefColumnCount(20);

        submitBtn.setOnAction(event -> {
            String name = nameField.getText();
            String sex = maleRadio.isSelected() ? "男" : "女";
            String birth = yearField.getText() + "年" + monthField.getText() + "月" + dayField.getText() + "日";
            String type = typeBox.getValue();
            String num = numField.getText();
            StringBuilder sb = new StringBuilder("姓名：").append(name)
                    .append("\n性别：").append(sex)
                    .append("\n出生日期：").append(birth)
                    .append("\n证件类型：").append(type)
                    .append("\n证件号码：").append(num);
            right.setText(sb.toString());
        });

        clearBtn.setOnAction(event -> {
            nameField.clear();
            sexGroup.selectToggle(null);
            yearField.clear();
            monthField.clear();
            dayField.clear();
            typeBox.getSelectionModel().clearSelection();
            numField.clear();
            right.clear();
        });

        root.setTop(top);
        root.setCenter(center);
        root.setRight(right);
        root.setBottom(bottom);

        Scene scene = new Scene(root, 600, 400);

        primaryStage.setTitle(" ");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
