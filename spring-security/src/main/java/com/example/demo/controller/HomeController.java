package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, public page!";
    }

    @GetMapping("/admin")
    public String admin() {
        return "Hello, admin page!";
    }

}