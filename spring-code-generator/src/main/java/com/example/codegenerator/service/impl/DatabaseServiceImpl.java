package com.example.codegenerator.service.impl;

import com.example.codegenerator.entity.ColumnInfo;
import com.example.codegenerator.entity.TableInfo;
import com.example.codegenerator.mapper.DatabaseMapper;
import com.example.codegenerator.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DatabaseServiceImpl implements DatabaseService {

    @Autowired
    private DatabaseMapper databaseMapper;

    @Override
    public List<TableInfo> getAllTables() {
        List<TableInfo> tables = databaseMapper.selectAllTables();

        // 为每个表设置注释
        for (TableInfo table : tables) {
            try {
                String comment = databaseMapper.selectTableComment(table.getTableName());
                table.setTableComment(comment != null ? comment : "");
            } catch (Exception e) {
                table.setTableComment("");
            }
        }

        return tables;
    }

    @Override
    public TableInfo getTableInfo(String tableName) {
        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName(tableName);
        tableInfo.setTableSchema("public");

        try {
            String comment = databaseMapper.selectTableComment(tableName);
            tableInfo.setTableComment(comment != null ? comment : "");
        } catch (Exception e) {
            tableInfo.setTableComment("");
        }

        return tableInfo;
    }

    @Override
    public List<ColumnInfo> getTableColumns(String tableName) {
        // 获取所有列信息
        List<ColumnInfo> columns = databaseMapper.selectColumnsByTableName(tableName);

        // 获取主键信息
        try {
            Set<String> primaryKeys = databaseMapper.selectPrimaryKeys(tableName)
                    .stream()
                    .collect(Collectors.toSet());

            // 标记主键列
            for (ColumnInfo column : columns) {
                column.setIsPrimaryKey(primaryKeys.contains(column.getColumnName()));
            }
        } catch (Exception e) {
            // 如果查询主键失败，将所有列的主键标记设为false
            for (ColumnInfo column : columns) {
                column.setIsPrimaryKey(false);
            }
        }

        return columns;
    }

    // 以下方法在简化版本中不需要实现
    @Override
    public TableInfo getTableInfo(String tableName, String schemaName) {
        return getTableInfo(tableName);
    }

    @Override
    public List<ColumnInfo> getTableColumns(String tableName, String schemaName) {
        return getTableColumns(tableName);
    }
}
