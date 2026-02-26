package _08_jdbc;

import javafx.stage.Stage;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.*;

public class Demo extends Application {

    private TextField SId = new TextField();
    private TextField SName = new TextField();
    private TextField SMath = new TextField();
    private TextField SEnglish = new TextField();
    private TextField SComputer = new TextField();
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/xsgl?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static final String USER = "root";
    static final String PASS = "password";

    @Override
    public void start(Stage primaryStage) {
        VBox main = new VBox();

        HBox id = new HBox();
        Label labelId = new Label("请输入学号:");
        id.setAlignment(Pos.CENTER);
        id.getChildren().addAll(labelId, SId);

        HBox name = new HBox();
        Label labelName = new Label("请输入姓名:");
        name.setAlignment(Pos.CENTER);
        name.getChildren().addAll(labelName, SName);

        HBox grade = new HBox();
        Label labelMath = new Label("数学成绩:");
        Label labelEnglish = new Label("英语成绩:");
        Label labelChanese = new Label("计算机成绩:");
        SMath.setPrefWidth(40);
        SEnglish.setPrefWidth(40);
        SComputer.setPrefWidth(40);
        grade.setAlignment(Pos.CENTER);
        grade.getChildren().addAll(labelMath, SMath);
        grade.getChildren().addAll(labelEnglish, SEnglish);
        grade.getChildren().addAll(labelChanese, SComputer);

        HBox btn = new HBox();
        Button btnInsert = new Button("插入");
        Button btnReset = new Button("重置");
        Button btnExit = new Button("退出");
        btn.setAlignment(Pos.CENTER);
        btn.getChildren().addAll(btnInsert, btnReset, btnExit);

        VBox.setMargin(id, new Insets(16));
        VBox.setMargin(name, new Insets(16));
        VBox.setMargin(grade, new Insets(16));
        VBox.setMargin(btn, new Insets(16));
        main.setPadding(new Insets(16));
        main.getChildren().addAll(id, name, grade, btn);

        btnInsert.setOnAction(e -> insertRecord());
        btnReset.setOnAction(e -> resetFields());
        btnExit.setOnAction(e -> primaryStage.close());

        Scene scene = new Scene(main, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("插入记录");
        primaryStage.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void insertRecord() {
        String id = SId.getText().trim();
        String name = SName.getText().trim();
        String computerStr = SComputer.getText().trim();
        String mathStr = SMath.getText().trim();
        String englishStr = SEnglish.getText().trim();
        if (id.isEmpty() || name.isEmpty() || computerStr.isEmpty() || mathStr.isEmpty() || englishStr.isEmpty()) {
            showAlert("学号、姓名和成绩不能为空！");
            return;
        }

        int math = 0, english = 0, computer = 0;
        try {
            math = Integer.parseInt(mathStr);
            english = Integer.parseInt(englishStr);
            computer = Integer.parseInt(computerStr);
        } catch (NumberFormatException e) {
            showAlert("成绩必须为数字！");
            return;
        }

        if (computer < 0 || computer > 100 || math < 0 || math > 100 || english < 0 || english > 100) {
            showAlert("成绩必须在0-100之间！");
            return;
        }

        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "insert into student values (?,?,?,?,?)";
            PreparedStatement psmt = conn.prepareStatement(sql);
            psmt.setString(1, id);
            psmt.setString(2, name);
            psmt.setInt(3, math);
            psmt.setInt(4, english);
            psmt.setInt(5, computer);
            psmt.executeUpdate();
            showAlert("插入记录成功！");
            conn.close();
        } catch (Exception se) {
            se.printStackTrace();
        } finally {
            try {
                DriverManager.getConnection(DB_URL, USER, PASS).close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    private void resetFields() {
        SId.setText("");
        SName.setText("");
        SComputer.setText("");
        SMath.setText("");
        SEnglish.setText("");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
