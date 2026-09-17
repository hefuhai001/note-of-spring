package com.example.hfh.service;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AlipayService {

    @Value("${alipay.appId}")
    private String appId;
    @Value("${alipay.appPrivateKey}")
    private String appPrivateKey;
    @Value("${alipay.alipayPublicKey}")
    private String alipayPublicKey;
    @Value("${alipay.notifyUrl}")
    private String notifyUrl;
    @Value("${alipay.returnUrl}")
    private String returnUrl;
    @Value("${alipay.gatewayUrl}")
    private String gatewayUrl;
    @Value("${alipay.charset}")
    private String charset;
    @Value("${alipay.signType}")
    private String signType;

    public String pay(String outTradeNo, String subject, String totalAmount) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, appId, appPrivateKey, "json", charset, alipayPublicKey, signType);
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setReturnUrl(returnUrl);
        request.setNotifyUrl(notifyUrl);
        request.setBizContent("{" +
                "\"out_trade_no\":\"" + outTradeNo + "\"," +
                "\"total_amount\":\"" + totalAmount + "\"," +
                "\"subject\":\"" + subject + "\"," +
                "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        return alipayClient.pageExecute(request).getBody();
    }
}