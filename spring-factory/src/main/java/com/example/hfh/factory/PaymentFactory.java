package com.example.hfh.factory;

import com.example.hfh.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * Class description goes here.
 * </p>
 *
 * @author 何福海
 * @version 1.0
 * @since 2025/7/22
 */
@Component
public class PaymentFactory {

    @Autowired
    private Map<String, PaymentService> paymentMap = new ConcurrentHashMap<>();

    public PaymentService getService(String type) {
        PaymentService service = paymentMap.get(type.toLowerCase());
        if (service == null) {
            throw new IllegalArgumentException("暂不支持的支付类型：" + type);
        }
        return service;
    }
}
