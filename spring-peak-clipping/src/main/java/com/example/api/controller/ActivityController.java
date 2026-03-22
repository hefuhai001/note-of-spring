package com.example.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.entity.Activity;
import com.example.api.entity.ActivityStatus;
import com.example.api.repository.ActivityRepository;
import com.example.api.service.ActivityCacheService;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityRepository activityRepository;
    private final ActivityCacheService activityCacheService;

    public ActivityController(ActivityRepository activityRepository,
                              ActivityCacheService activityCacheService) {
        this.activityRepository = activityRepository;
        this.activityCacheService = activityCacheService;
    }

    @PostMapping
    public ResponseEntity<Activity> createActivity(@RequestBody Activity activity) {
        Activity saved = activityRepository.save(activity);
        activityCacheService.cacheActivity(saved);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Activity> getActivity(@PathVariable Long id) {
        Optional<Activity> activity = activityCacheService.getCachedActivity(id);
        return activity.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Activity>> getAllActivities() {
        List<Activity> activities = activityRepository.findAll();
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Activity>> getActivitiesByStatus(@PathVariable String status) {
        ActivityStatus activityStatus = ActivityStatus.fromCode(status);
        List<Activity> activities = activityRepository.findByStatus(activityStatus);
        return ResponseEntity.ok(activities);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Activity> updateStatus(@PathVariable Long id, 
                                                  @RequestParam String status) {
        ActivityStatus newStatus = ActivityStatus.fromCode(status);
        activityCacheService.updateActivityStatus(id, newStatus);
        
        Activity updated = activityRepository.findById(id).orElseThrow();
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/refresh-cache")
    public ResponseEntity<Void> refreshCache(@PathVariable Long id) {
        activityCacheService.refreshActivityCache(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/cache")
    public ResponseEntity<Void> evictCache(@PathVariable Long id) {
        activityCacheService.evictActivity(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cache")
    public ResponseEntity<Void> evictAllCache() {
        activityCacheService.evictAllActivities();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/preload")
    public ResponseEntity<String> triggerPreload() {
        activityCacheService.preloadActivities();
        return ResponseEntity.ok("Preload triggered successfully");
    }

    @PostMapping("/preload/upcoming")
    public ResponseEntity<String> preloadUpcoming(@RequestParam(defaultValue = "30") int minutes) {
        activityCacheService.preloadUpcomingActivities(minutes);
        return ResponseEntity.ok("Upcoming activities preloaded for next " + minutes + " minutes");
    }
}
