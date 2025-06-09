package com.ali.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Order implements Serializable {
    private Long id;
    private String orderNo;
    private int status;
    private long createTime;
    private long updateTime;
}
