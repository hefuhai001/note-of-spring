package com.example.hfh.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BusinessMetricsService {

    @Autowired
    private MeterRegistry meterRegistry;

    private Counter requestCounter;
    private Counter errorCounter;
    private Timer requestTimer;
    private AtomicInteger activeSessions;

    @PostConstruct
    public void init() {
        // 初始化自定义指标
        requestCounter = Counter.builder("api.requests.total")
                .description("Total API requests")
                .register(meterRegistry);

        errorCounter = Counter.builder("api.errors.total")
                .description("Total API errors")
                .register(meterRegistry);

        requestTimer = Timer.builder("api.request.duration")
                .description("API request duration")
                .register(meterRegistry);

        activeSessions = meterRegistry.gauge("app.active.sessions",
                new AtomicInteger(0));
    }

    public void recordRequest(boolean success, long duration) {
        requestCounter.increment();
        requestTimer.record(duration, TimeUnit.MILLISECONDS);

        if (!success) {
            errorCounter.increment();
        }
    }

    public void updateActiveSessions(int delta) {
        activeSessions.addAndGet(delta);
    }

    // 模拟业务方法
    public String processBusinessLogic() {
        long startTime = System.currentTimeMillis();
        Random random = new Random();

        try {
            // 模拟业务处理
            Thread.sleep(random.nextInt(500));

            boolean success = random.nextDouble() > 0.2; // 80%成功率
            if (!success) {
                throw new RuntimeException("业务处理失败");
            }

            recordRequest(true, System.currentTimeMillis() - startTime);
            return "业务处理成功";

        } catch (Exception e) {
            recordRequest(false, System.currentTimeMillis() - startTime);
            return "业务处理失败: " + e.getMessage();
        }
    }
}