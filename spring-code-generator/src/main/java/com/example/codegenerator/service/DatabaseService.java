package com.example.codegenerator.service;

import com.example.codegenerator.entity.ColumnInfo;
import com.example.codegenerator.entity.TableInfo;

import java.util.List;

public interface DatabaseService {
    List<TableInfo> getAllTables();

    TableInfo getTableInfo(String tableName);

    List<ColumnInfo> getTableColumns(String tableName);

    // 新增方法，支持指定schema
    TableInfo getTableInfo(String tableName, String schemaName);

    List<ColumnInfo> getTableColumns(String tableName, String schemaName);
}