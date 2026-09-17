package com.example.hfh.controller;

import com.example.hfh.service.AlipayService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlipayController {

    @Autowired
    private AlipayService alipayService;

    @GetMapping("/pay")
    public void pay(@RequestParam String outTradeNo, @RequestParam String subject, @RequestParam String totalAmount, HttpServletResponse response) throws Exception {
        String form = alipayService.pay(outTradeNo, subject, totalAmount);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(form);
    }
}
