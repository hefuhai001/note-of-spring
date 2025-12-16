package com.example.servicea.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/service-a")
public class ServiceAController {

    @Value("${message:Default message}")
    private String message;

    @Value("${server.port:Unknown}")
    private String port;

    @GetMapping("/message")
    public String getMessage() {
        return "Service A: " + message + " (Port: " + port + ")";
    }

    @GetMapping("/health")
    public String health() {
        return "Service A is healthy on port: " + port;
    }

    @GetMapping("/config")
    public String config() {
        return "Current config: " + message;
    }
}