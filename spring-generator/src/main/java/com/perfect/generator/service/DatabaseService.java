package com.perfect.generator.service;


import com.perfect.generator.entity.ColumnInfo;
import com.perfect.generator.entity.TableInfo;

import java.util.List;

public interface DatabaseService {
    List<TableInfo> getAllTables();

    TableInfo getTableInfo(String tableName);

    List<ColumnInfo> getTableColumns(String tableName);

    // 新增方法，支持指定schema
    TableInfo getTableInfo(String tableName, String schemaName);

    List<ColumnInfo> getTableColumns(String tableName, String schemaName);
}