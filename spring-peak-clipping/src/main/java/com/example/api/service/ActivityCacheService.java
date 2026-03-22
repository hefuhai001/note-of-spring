package com.example.api.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.api.config.ActivityCacheProperties;
import com.example.api.entity.Activity;
import com.example.api.entity.ActivityStatus;
import com.example.api.repository.ActivityRepository;

@Service
public class ActivityCacheService {

    private static final Logger log = LoggerFactory.getLogger(ActivityCacheService.class);

    private final RedisTemplate<String, Object> redisTemplate;
    private final ActivityRepository activityRepository;
    private final ActivityCacheProperties cacheProperties;

    public ActivityCacheService(RedisTemplate<String, Object> redisTemplate,
                                ActivityRepository activityRepository,
                                ActivityCacheProperties cacheProperties) {
        this.redisTemplate = redisTemplate;
        this.activityRepository = activityRepository;
        this.cacheProperties = cacheProperties;
    }

    public void preloadActivities() {
        log.info("Starting activity preloading...");
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime preloadThreshold = now.plusMinutes(30);
        
        List<ActivityStatus> statusesToPreload = List.of(
                ActivityStatus.NOT_STARTED,
                ActivityStatus.IN_PROGRESS
        );
        
        List<Activity> activitiesToPreload = activityRepository
                .findActivitiesToPreload(statusesToPreload, now, preloadThreshold);
        
        int preloadedCount = 0;
        for (Activity activity : activitiesToPreload) {
            try {
                cacheActivity(activity);
                preloadedCount++;
                log.debug("Preloaded activity: id={}, name={}, status={}", 
                         activity.getId(), activity.getName(), activity.getStatus());
            } catch (Exception e) {
                log.error("Failed to preload activity: id={}, error={}", 
                         activity.getId(), e.getMessage(), e);
            }
        }
        
        log.info("Activity preloading completed. Total preloaded: {}", preloadedCount);
    }

    public void cacheActivity(Activity activity) {
        String cacheKey = getCacheKey(activity.getId());
        Duration ttl = Duration.ofMinutes(cacheProperties.getTtlMinutes());
        
        redisTemplate.opsForValue().set(cacheKey, activity, ttl.toMillis(), TimeUnit.MILLISECONDS);
        
        String statusKey = getStatusKey(activity.getStatus());
        redisTemplate.opsForSet().add(statusKey, activity.getId().toString());
        
        log.debug("Cached activity: id={}, key={}", activity.getId(), cacheKey);
    }

    public Optional<Activity> getCachedActivity(Long activityId) {
        String cacheKey = getCacheKey(activityId);
        Object cached = redisTemplate.opsForValue().get(cacheKey);
        
        if (cached instanceof Activity activity) {
            log.debug("Cache hit for activity: id={}", activityId);
            return Optional.of(activity);
        }
        
        log.debug("Cache miss for activity: id={}", activityId);
        Activity activity = activityRepository.findById(activityId).orElse(null);
        
        if (activity != null) {
            cacheActivity(activity);
            return Optional.of(activity);
        }
        
        return Optional.empty();
    }

    @Transactional
    public void updateActivityStatus(Long activityId, ActivityStatus newStatus) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found: " + activityId));
        
        ActivityStatus oldStatus = activity.getStatus();
        activity.setStatus(newStatus);
        activityRepository.save(activity);
        
        String oldStatusKey = getStatusKey(oldStatus);
        redisTemplate.opsForSet().remove(oldStatusKey, activityId.toString());
        
        String newStatusKey = getStatusKey(newStatus);
        redisTemplate.opsForSet().add(newStatusKey, activityId.toString());
        
        cacheActivity(activity);
        
        log.info("Updated activity status: id={}, oldStatus={}, newStatus={}", 
                activityId, oldStatus, newStatus);
    }

    public void evictActivity(Long activityId) {
        String cacheKey = getCacheKey(activityId);
        redisTemplate.delete(cacheKey);
        log.debug("Evicted activity from cache: id={}", activityId);
    }

    public void evictAllActivities() {
        String pattern = cacheProperties.getKeyPrefix() + "*";
        var keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            log.info("Evicted all activities from cache. Count: {}", keys.size());
        }
    }

    public void refreshActivityCache(Long activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found: " + activityId));
        cacheActivity(activity);
        log.info("Refreshed activity cache: id={}", activityId);
    }

    public void preloadUpcomingActivities(int minutesBeforeStart) {
        log.info("Preloading activities starting within {} minutes...", minutesBeforeStart);
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threshold = now.plusMinutes(minutesBeforeStart);
        
        List<Activity> upcomingActivities = activityRepository
                .findUpcomingActivities(ActivityStatus.NOT_STARTED, threshold);
        
        for (Activity activity : upcomingActivities) {
            cacheActivity(activity);
            log.info("Preloaded upcoming activity: id={}, name={}, startTime={}", 
                    activity.getId(), activity.getName(), activity.getStartTime());
        }
    }

    public void preloadAllActiveActivities() {
        log.info("Preloading all active activities...");
        
        List<ActivityStatus> activeStatuses = List.of(
                ActivityStatus.NOT_STARTED,
                ActivityStatus.IN_PROGRESS
        );
        
        List<Activity> activeActivities = activityRepository
                .findByStatusInAndIsActiveTrue(activeStatuses);
        
        for (Activity activity : activeActivities) {
            cacheActivity(activity);
        }
        
        log.info("Preloaded {} active activities", activeActivities.size());
    }

    private String getCacheKey(Long activityId) {
        return cacheProperties.getKeyPrefix() + activityId;
    }

    private String getStatusKey(ActivityStatus status) {
        return cacheProperties.getKeyPrefix() + "status:" + status.getCode();
    }
}
