package com.hfh.api.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Slf4j
@Component
public class MysqlHealthCheck {

    private final DataSource dataSource;

    public MysqlHealthCheck(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void check() {
        try (Connection conn = dataSource.getConnection()) {
            boolean valid = conn.isValid(3);
            if (valid) {
                System.out.println("[MySQL] success - " + conn.getMetaData().getURL());
            } else {
                System.err.println("[MySQL] failed");
            }
        } catch (Exception e) {
            System.err.println("[MySQL] error: " + e.getMessage());
        }
    }
}
