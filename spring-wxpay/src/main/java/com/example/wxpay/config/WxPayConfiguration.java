package com.example.wxpay.config;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WxPayConfiguration {

    @Value("${wxpay.appId}")
    private String appId;
    @Value("${wxpay.mchId}")
    private String mchId;
    @Value("${wxpay.mchKey}")
    private String mchKey;
    @Value("${wxpay.keyPath}")
    private String keyPath;
    @Value("${wxpay.notifyUrl}")
    private String notifyUrl;

    @Bean
    public WxPayConfig wxPayConfig() {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(appId);
        payConfig.setMchId(mchId);
        payConfig.setMchKey(mchKey);
        payConfig.setKeyPath(keyPath);
        payConfig.setNotifyUrl(notifyUrl);
        return payConfig;
    }

    @Bean
    public WxPayService wxPayService(WxPayConfig wxPayConfig) {
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(wxPayConfig);
        return wxPayService;
    }
}
