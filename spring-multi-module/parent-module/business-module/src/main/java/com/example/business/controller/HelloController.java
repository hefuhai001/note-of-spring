package com.example.business.controller;

import com.example.core.exception.CustomException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        if (true) {
            throw new CustomException("Something went wrong");
        }
        return "Hello, World!";
    }

    @GetMapping("/test")
    public String test() {
        return "Hello, World!";
    }


}
