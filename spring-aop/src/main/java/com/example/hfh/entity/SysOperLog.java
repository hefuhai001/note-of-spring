package com.example.hfh.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "sys_oper_log")
public class SysOperLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String operation;

    @Column
    private String method;

    @Column(columnDefinition = "TEXT")
    private String params;

    @Column
    private String ip;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public SysOperLog() {

    }

    public SysOperLog(Long id, String operation, String method, String params, String ip, Date createTime) {
        this.id = id;
        this.operation = operation;
        this.method = method;
        this.params = params;
        this.ip = ip;
        this.createTime = createTime;
    }
}
