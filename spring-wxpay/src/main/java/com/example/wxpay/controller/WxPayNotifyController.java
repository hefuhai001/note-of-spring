package com.example.wxpay.controller;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayOrderQueryRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notify")
public class WxPayNotifyController {

    @Autowired
    private WxPayService wxPayService;

    @PostMapping("/payNotify")
    public String payNotify(@RequestBody String xmlData) throws WxPayException {
        WxPayOrderNotifyResult notifyResult = wxPayService.parseOrderNotifyResult(xmlData);
        // 处理业务逻辑，例如更新订单状态
        String outTradeNo = notifyResult.getOutTradeNo();
        WxPayOrderQueryRequest queryRequest = new WxPayOrderQueryRequest();
        queryRequest.setOutTradeNo(outTradeNo);
        WxPayOrderQueryResult queryResult = wxPayService.queryOrder(queryRequest);
        // 返回成功响应
        return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    }
}
