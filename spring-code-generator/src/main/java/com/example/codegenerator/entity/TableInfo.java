package com.example.codegenerator.entity;

/**
 * 数据库表信息实体
 */
public class TableInfo {
    private String tableName;
    private String tableComment;
    private String tableSchema;

    public TableInfo() {
    }

    public TableInfo(String tableName, String tableComment, String tableSchema) {
        this.tableName = tableName;
        this.tableComment = tableComment;
        this.tableSchema = tableSchema;
    }

    // Getters and Setters
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public String getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
    }
}