package com.ali;

import com.ali.dao.OrderMapper;
import com.ali.entity.Order;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class OrderTest {
    @Resource
    private OrderMapper orderMapper;

    @Test
    public void queryTest1() {
        Map<String, Object> param = new HashMap<>();
        param.put("start", "0");
        param.put("size", "10");
        List<Order> orders = orderMapper.queryOrder(param);
        System.out.println("queryTest1：" + orders);
    }
}
