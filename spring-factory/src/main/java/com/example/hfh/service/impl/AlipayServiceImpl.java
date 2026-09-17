package com.example.hfh.service.impl;

import com.example.hfh.service.PaymentService;
import org.springframework.stereotype.Service;

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
@Service("alipay")
public class AlipayServiceImpl implements PaymentService {

    @Override
    public String pay(BigDecimal amount) {
        return "使用支付宝支付：" + amount + " 元";
    }

}
