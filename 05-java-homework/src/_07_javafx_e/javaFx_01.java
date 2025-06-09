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

public class javaFx_01 extends Application {

    @Override
    public void start(Stage primaryStage) {
        HBox top = new HBox();
        Label topLabel = new Label("个人信息");
        topLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #217fea;");
        top.getChildren().add(topLabel);
        top.setAlignment(Pos.CENTER);
        top.setPadding(new Insets(20));

        VBox center = new VBox();
        Label nameLabel = new Label("真实姓名:");
        TextField nameField = new TextField();
        HBox hb1 = new HBox(10, nameLabel, nameField);

        Label sexLabel = new Label("性别:");
        ToggleGroup sexGroup = new ToggleGroup();
        RadioButton maleRadio = new RadioButton("男");
        maleRadio.setToggleGroup(sexGroup);
        RadioButton femaleRadio = new RadioButton("女");
        femaleRadio.setToggleGroup(sexGroup);
        HBox hb2 = new HBox(10);
        hb2.getChildren().add(sexLabel);
        hb2.getChildren().add(maleRadio);
        hb2.getChildren().add(femaleRadio);

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
        HBox hb3 = new HBox(10);
        hb3.getChildren().add(birthLabel);
        hb3.getChildren().add(yearField);
        hb3.getChildren().add(yearLabel);
        hb3.getChildren().add(monthField);
        hb3.getChildren().add(monthLabel);
        hb3.getChildren().add(dayField);
        hb3.getChildren().add(dayLabel);

        Label typeLabel = new Label("证件类型:");
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().add("身份证");
        typeBox.getItems().add("军人证");
        typeBox.getItems().add("学生证");
        typeBox.getItems().add("护照");
        typeBox.setValue("身份证");
        HBox hb4 = new HBox(10, typeLabel, typeBox);

        Label numLabel = new Label("证件号码:");
        TextField numField = new TextField();
        HBox hb5 = new HBox(10, numLabel, numField);

        center.getChildren().add(hb1);
        center.getChildren().add(hb2);
        center.getChildren().add(hb3);
        center.getChildren().add(hb4);
        center.getChildren().add(hb5);
        center.setAlignment(Pos.CENTER);
        center.setSpacing(20);
        center.setPadding(new Insets(20));

        HBox bottom = new HBox();
        Button submitBtn = new Button("提交");
        Button clearBtn = new Button("重填");
        bottom.getChildren().add(submitBtn);
        bottom.getChildren().add(clearBtn);
        bottom.setAlignment(Pos.CENTER);
        bottom.setSpacing(20);
        bottom.setPadding(new Insets(20));

        TextArea right = new TextArea();
        right.setEditable(false);
        right.setPrefRowCount(10);
        right.setPrefColumnCount(30);

        BorderPane main = new BorderPane();
        main.setTop(top);
        main.setCenter(center);
        main.setRight(right);
        main.setBottom(bottom);

        submitBtn.setOnAction(event -> {
            String name = nameField.getText();
            String sex = maleRadio.isSelected() ? "男" : "女";
            String birth = yearField.getText() + "年" + monthField.getText() + "月" + dayField.getText() + "日";
            String type = typeBox.getValue();
            String num = numField.getText();
            StringBuilder s = new StringBuilder("姓名：").append(name)
                    .append("\n性别：").append(sex)
                    .append("\n出生日期：").append(birth)
                    .append("\n证件类型：").append(type)
                    .append("\n证件号码：").append(num);
            right.setText(s.toString());
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

        Scene scene = new Scene(main, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
