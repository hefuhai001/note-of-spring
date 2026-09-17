package com.example.hfh.service;

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
public interface PaymentService {
    String pay(BigDecimal amount);
}
