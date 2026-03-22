package com.perfect.generator.entity;

/**
 * 数据库列信息实体
 */
public class ColumnInfo {
    private String columnName;
    private String dataType;
    private String columnComment;
    private Boolean isNullable;
    private Boolean isPrimaryKey;
    private Integer characterMaximumLength;
    private String columnDefault;

    public ColumnInfo() {
    }

    public ColumnInfo(String columnName, String dataType, String columnComment,
                      Boolean isNullable, Boolean isPrimaryKey,
                      Integer characterMaximumLength, String columnDefault) {
        this.columnName = columnName;
        this.dataType = dataType;
        this.columnComment = columnComment;
        this.isNullable = isNullable;
        this.isPrimaryKey = isPrimaryKey;
        this.characterMaximumLength = characterMaximumLength;
        this.columnDefault = columnDefault;
    }

    // Getters and Setters
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public Boolean getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(Boolean nullable) {
        isNullable = nullable;
    }

    public Boolean getIsPrimaryKey() {
        return isPrimaryKey;
    }

    public void setIsPrimaryKey(Boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    public Integer getCharacterMaximumLength() {
        return characterMaximumLength;
    }

    public void setCharacterMaximumLength(Integer characterMaximumLength) {
        this.characterMaximumLength = characterMaximumLength;
    }

    public String getColumnDefault() {
        return columnDefault;
    }

    public void setColumnDefault(String columnDefault) {
        this.columnDefault = columnDefault;
    }
}
