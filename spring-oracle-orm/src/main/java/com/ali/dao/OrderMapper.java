package com.ali.dao;

import com.ali.entity.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    /**
     * 查询订单
     */
    List<Order> queryOrder(Map<String, Object> param);


    List<Order> getAll();


}