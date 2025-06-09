package com.example.service;

import org.springframework.stereotype.Service;

@Service
public class MyService {
    private final AnotherService anotherService;

    public MyService(AnotherService anotherService) {
        this.anotherService = anotherService;
    }

    public String doSomething(String input) {
        // 假设这个方法处理输入，并依赖anotherService
        String processedInput = anotherService.processInput(input);
        return "Result: " + processedInput;
    }
}
