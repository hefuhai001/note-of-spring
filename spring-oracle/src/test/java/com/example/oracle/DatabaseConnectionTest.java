package com.example.oracle;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.util.Map;

@SpringBootTest
@ActiveProfiles("test") // 指定使用test配置文件，如果你的测试配置文件名不是application-test.properties或application-test.yml，请相应修改
public class DatabaseConnectionTest {

    @Resource
    private DataSource dataSource;

    @Test
    public void testDatabaseConnection() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT 1 FROM DUAL";
        Map<String, Object> result = jdbcTemplate.queryForMap(sql);
        System.out.println(result);
    }
}
