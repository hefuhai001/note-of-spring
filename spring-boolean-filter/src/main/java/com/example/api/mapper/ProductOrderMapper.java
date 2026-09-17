package com.example.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.api.entity.ProductOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductOrderMapper extends BaseMapper<ProductOrder> {

    /**
     * 查询所有订单号，用于初始化布隆过滤器
     */
    @Select("SELECT order_no FROM product_order WHERE deleted = 0")
    List<String> selectAllOrderNos();

    /**
     * 查询所有有效订单，用于预加载 Redis 缓存
     */
    @Select("SELECT * FROM product_order WHERE deleted = 0")
    List<ProductOrder> selectAllOrders();
}
