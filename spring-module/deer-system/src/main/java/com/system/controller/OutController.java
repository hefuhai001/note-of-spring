package com.system.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "测试")
@RestController
public class OutController {

    @GetMapping("/")
    public String test() {
        return "请求成功";
    }
}