package com.hfh.factory.controller;

import com.hfh.factory.factory.PaymentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * <p>
 * Class description goes here.
 * </p>
 *
 * @author 何福海
 * @version 1.0
 * @since 2025/7/22
 */
@RestController
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private PaymentFactory factory;

    @GetMapping
    public String pay(@RequestParam String type,
                      @RequestParam BigDecimal amount) {
        return factory.getService(type).pay(amount);
    }
}

