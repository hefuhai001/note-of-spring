package com.example.monitor.endpoint;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Endpoint(id = "custom-metrics")
public class CustomMetricsEndpoint {

    @Autowired
    private MeterRegistry meterRegistry;

    @ReadOperation
    public Map<String, Object> getCustomMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        // 获取自定义指标
        metrics.put("custom_counter",
                meterRegistry.find("custom.request.count").counter());
        metrics.put("custom_gauge",
                meterRegistry.find("custom.active.sessions").gauge());

        return metrics;
    }
}