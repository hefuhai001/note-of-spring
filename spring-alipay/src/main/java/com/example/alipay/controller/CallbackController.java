package com.example.alipay.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CallbackController {

    @GetMapping("/callback")
    public String callback() {
        return "支付成功";
    }


}
