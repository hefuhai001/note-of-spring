package com.example.hfh.controller;

import com.example.hfh.utils.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Log(operation = "查询用户信息")
    @GetMapping("/list")
    public String list() {
        return "User Data";
    }

}
