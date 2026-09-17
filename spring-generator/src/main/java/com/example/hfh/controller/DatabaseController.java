package com.example.hfh.controller;

import com.example.hfh.entity.ColumnInfo;
import com.example.hfh.entity.TableInfo;
import com.example.hfh.service.DatabaseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/code")
public class DatabaseController {

    final private DatabaseService databaseService;

    /**
     * 获取所有表信息
     */
    @GetMapping("/database/tables")
    public ResponseEntity<List<TableInfo>> getAllTables() {
        try {
            List<TableInfo> tables = databaseService.getAllTables();
            return ResponseEntity.ok()
                    .body(tables);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    /**
     * 获取指定表的详细信息（支持指定schema）
     */
    @GetMapping("/database/table/{schemaName}/{tableName}")
    public ResponseEntity<Map<String, Object>> getTableDetail(
            @PathVariable String schemaName,
            @PathVariable String tableName) {
        try {
            TableInfo tableInfo = databaseService.getTableInfo(tableName, schemaName);
            List<ColumnInfo> columns = databaseService.getTableColumns(tableName, schemaName);

            return ResponseEntity.ok(Map.of(
                    "tableInfo", tableInfo,
                    "columns", columns
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

}
