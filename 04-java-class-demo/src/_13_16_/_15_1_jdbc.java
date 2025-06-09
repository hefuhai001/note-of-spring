package _13_16_;

import java.sql.*;

public class _15_1_jdbc {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    //    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";//8.0以下的
    static final String URL = "jdbc:mysql://localhost:3306/db_demo?serverTimezone=UTC";

    // 这里是定义数据库的用户名与密码
    static final String USER = "root";
    static final String PASS = "password";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;

        PreparedStatement ps = null;
        try {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("连接数据库。。。");
            //System.out.println("我到这了");
            conn = DriverManager.getConnection(URL, USER, PASS);
            ps = conn.prepareStatement("select * from user");


            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT id, name FROM user";
            ResultSet rs = stmt.executeQuery(sql);

            // 展开结果集数据库
            while (rs.next()) {
                // 通过字段检索
                int id = rs.getInt("id");
                String name = rs.getString("name");
                //String score = rs.getString("score");

                // 输出数据
                System.out.print("ID: " + id);
                System.out.print(", 姓名: " + name);
                //System.out.print(", 成绩: " + score);
                System.out.print("\n");
            }
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
            ps.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("退出程序！！！");
    }
}
