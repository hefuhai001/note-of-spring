package com.ali.controller;

import com.ali.dao.OrderMapper;
import com.ali.entity.Order;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderMapper orderMapper;

    @GetMapping("/list")
    public List<Order> getAll() {
        return orderMapper.getAll();
    }

}
