package com.example.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.example.api.service.ActivityCacheService;

@Component
@ConditionalOnProperty(prefix = "activity.cache.preload", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ActivityCacheInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ActivityCacheInitializer.class);

    private final ActivityCacheService activityCacheService;

    public ActivityCacheInitializer(ActivityCacheService activityCacheService) {
        this.activityCacheService = activityCacheService;
    }

    @Override
    public void run(String... args) {
        log.info("Initializing activity cache on application startup...");
        try {
            activityCacheService.preloadAllActiveActivities();
            log.info("Activity cache initialization completed successfully");
        } catch (Exception e) {
            log.error("Failed to initialize activity cache: {}", e.getMessage(), e);
        }
    }
}
