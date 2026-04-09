package com.example.monitor.controller;

import com.example.monitor.service.BusinessMetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/monitor")
@CrossOrigin(origins = "http://localhost:3000")
public class MonitorController {

    @Autowired
    private BusinessMetricsService metricsService;

    @GetMapping("/test")
    public Map<String, Object> testEndpoint() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("message", "Monitor API is working");
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    @PostMapping("/business")
    public Map<String, Object> processBusiness() {
        Map<String, Object> result = new HashMap<>();
        String response = metricsService.processBusinessLogic();
        result.put("result", response);
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    @PostMapping("/sessions")
    public Map<String, Object> updateSession(@RequestParam int delta) {
        metricsService.updateActiveSessions(delta);
        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("delta", delta);
        return result;
    }
}