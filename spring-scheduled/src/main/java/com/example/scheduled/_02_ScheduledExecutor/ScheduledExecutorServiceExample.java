package com.example.scheduled._02_ScheduledExecutor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutorServiceExample {
    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(() -> {
            System.out.println("任务执行：" + System.currentTimeMillis());
        }, 0, 5, TimeUnit.SECONDS);
    }
}