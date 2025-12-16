package com.example.serviceb.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/service-b")
public class ServiceBController {

    @Value("${message:Default message}")
    private String message;

    @Value("${server.port:Unknown}")
    private String port;

    @GetMapping("/message")
    public String getMessage() {
        return "Service B: " + message + " (Port: " + port + ")";
    }

    @GetMapping("/health")
    public String health() {
        return "Service B is healthy on port: " + port;
    }

    @GetMapping("/info")
    public String info() {
        return "Service B information: Running on port " + port;
    }
}