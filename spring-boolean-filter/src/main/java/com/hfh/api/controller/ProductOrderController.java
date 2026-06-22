package com.hfh.api.controller;

import com.hfh.api.entity.ProductOrder;
import com.hfh.api.service.ProductOrderService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class ProductOrderController {

    private final ProductOrderService productOrderService;

    public ProductOrderController(ProductOrderService productOrderService) {
        this.productOrderService = productOrderService;
    }

    /**
     * 根据订单号查询订单（布隆过滤器拦截无效查询）
     */
    @GetMapping("/{orderNo}")
    public Map<String, Object> getByOrderNo(@PathVariable String orderNo) {
        ProductOrder order = productOrderService.getByOrderNo(orderNo);
        Map<String, Object> result = new HashMap<>();
        if (order == null) {
            result.put("success", false);
            result.put("message", "订单不存在");
        } else {
            result.put("success", true);
            result.put("data", order);
        }
        return result;
    }

    /**
     * 创建订单
     */
    @PostMapping
    public Map<String, Object> create(@RequestBody ProductOrder order) {
        boolean created = productOrderService.createOrder(order);
        Map<String, Object> result = new HashMap<>();
        result.put("success", created);
        result.put("message", created ? "创建成功" : "创建失败");
        return result;
    }
}
