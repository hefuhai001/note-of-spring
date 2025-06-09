package com.example.wxpay.controller;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pay")
public class WxPayController {

    @Autowired
    private WxPayService wxPayService;

    @PostMapping("/createOrder")
    public String createOrder() throws WxPayException {
        WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
        request.setBody("测试商品");
        request.setOutTradeNo("1234567890");
        request.setTotalFee(1); // 单位：分
        request.setSpbillCreateIp("127.0.0.1");
        request.setNotifyUrl("https://yourdomain.com/notify");
        request.setTradeType("JSAPI");
        request.setOpenid("用户的openid");
        WxPayUnifiedOrderResult result = wxPayService.unifiedOrder(request);
        return result.getPrepayId();
    }
}
