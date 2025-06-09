package _08_jdbc;

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
import javafx.stage.Stage;

import java.sql.*;

public class Java extends Application {

    private TextField tfId = new TextField();
    private TextField tfName = new TextField();
    private TextField tfMath = new TextField();
    private TextField tfEnglish = new TextField();
    private TextField tfComputer = new TextField();

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/xsgl?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    static final String USER = "root";
    static final String PASS = "password";

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        HBox id = new HBox();
        HBox name = new HBox();
        HBox grade = new HBox();
        HBox btn = new HBox();

        Label labelId = new Label("请输入学号:");
        labelId.setPrefWidth(90);
        id.getChildren().addAll(labelId, tfId);

        Label labelName = new Label("请输入姓名:");
        labelName.setPrefWidth(90);
        name.getChildren().addAll(labelName, tfName);

        Label labelMath = new Label("数学成绩:");
        Label labelEnglish = new Label("英语成绩:");
        Label labelChanese = new Label("计算机成绩:");
        tfMath.setPrefWidth(50);
        tfEnglish.setPrefWidth(50);
        tfComputer.setPrefWidth(50);
        grade.getChildren().addAll(labelMath, tfMath);
        grade.getChildren().addAll(labelEnglish, tfEnglish);
        grade.getChildren().addAll(labelChanese, tfComputer);

        Button btnInsert = new Button("插入");
        Button btnReset = new Button("重置");
        Button btnExit = new Button("退出");
        btn.setSpacing(20);
        btn.setAlignment(Pos.CENTER);
        btn.getChildren().addAll(btnInsert, btnReset, btnExit);

        root.setPadding(new Insets(20));
        VBox.setMargin(id, new Insets(10));
        VBox.setMargin(name, new Insets(10));
        VBox.setMargin(grade, new Insets(10));
        VBox.setMargin(btn, new Insets(10));
        root.getChildren().addAll(id, name, grade, btn);

        btnInsert.setOnAction(e -> insertRecord());
        btnReset.setOnAction(e -> resetFields());
        btnExit.setOnAction(e -> primaryStage.close());

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("插入记录");
        primaryStage.show();
    }

    private void insertRecord() {
        String id = tfId.getText().trim();
        String name = tfName.getText().trim();
        String computerStr = tfComputer.getText().trim();
        String mathStr = tfMath.getText().trim();
        String englishStr = tfEnglish.getText().trim();

        if (id.isEmpty() || name.isEmpty() || computerStr.isEmpty() || mathStr.isEmpty() || englishStr.isEmpty()) {
            showAlert("学号、姓名和成绩不能为空！");
            return;
        }

        int computer = 0, math = 0, english = 0;
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

        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql = "INSERT INTO student (id, name, math, english ,computer) VALUES (" + "'" + id + "'" + "," + "'" + name + "'" + "," + math + "," + english + "," + computer + ")";
            int rs = stmt.executeUpdate(sql);
            if (rs == 1) {
                showAlert("插入记录成功！");
            }
            stmt.close();
            conn.close();
        } catch (Exception se) {
            se.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException ignored) {
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    private void resetFields() {
        tfId.setText("");
        tfName.setText("");
        tfComputer.setText("");
        tfMath.setText("");
        tfEnglish.setText("");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
