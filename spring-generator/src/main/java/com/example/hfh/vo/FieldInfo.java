package com.example.hfh.vo;

public class FieldInfo {
    private String name;
    private String type;
    private String comment;
    private Boolean isPrimaryKey = false;
    private Boolean isAutoIncrement = false;

    public FieldInfo() {
    }

    public FieldInfo(String name, String type, String comment) {
        this.name = name;
        this.type = type;
        this.comment = comment;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getIsPrimaryKey() {
        return isPrimaryKey;
    }

    public void setIsPrimaryKey(Boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    public Boolean getIsAutoIncrement() {
        return isAutoIncrement;
    }

    public void setIsAutoIncrement(Boolean autoIncrement) {
        isAutoIncrement = autoIncrement;
    }
}