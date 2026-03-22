package com.example.api.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.api.config.ActivityCacheProperties;
import com.example.api.service.ActivityCacheService;

/**
 * 活动预热调度器
 * 
 * 核心功能：将活动信息从PostgreSQL数据库同步到Redis缓存
 * 
 * 设计目标：
 * 1. 防止活动上架瞬间数据库被高并发请求击垮
 * 2. 提前将活动数据预热到Redis，实现峰值削峰
 * 3. 分层预热策略，避免一次性大量加载造成系统压力
 * 
 * 预热策略：
 * - 定时预热：每5分钟扫描即将开始的活动
 * - 即将开始：每分钟检查5分钟内开始的活动
 * - 全量刷新：每5分钟刷新所有活跃活动
 * 
 * @author spring-peak-clipping
 */
@Component
@EnableScheduling
@ConditionalOnProperty(prefix = "activity.cache.preload", name = "enabled", havingValue = "true")
public class ActivityPreloadScheduler {

    private static final Logger log = LoggerFactory.getLogger(ActivityPreloadScheduler.class);

    private final ActivityCacheService activityCacheService;
    private final ActivityCacheProperties cacheProperties;

    public ActivityPreloadScheduler(ActivityCacheService activityCacheService,
                                    ActivityCacheProperties cacheProperties) {
        this.activityCacheService = activityCacheService;
        this.cacheProperties = cacheProperties;
    }

    /**
     * 定时预热活动
     * 
     * 执行频率：每5分钟执行一次（可通过配置 activity.cache.preload.cron 修改）
     * 
     * 预热范围：
     * - 状态为 NOT_STARTED（未开始）的活动
     * - 状态为 IN_PROGRESS（进行中）的活动
     * - 开始时间在未来30分钟内的活动
     * 
     * 作用：
     * - 提前将即将开始的活动加载到Redis
     * - 确保活动开始时数据已在缓存中，避免数据库压力
     */
    @Scheduled(cron = "${activity.cache.preload.cron:0 */5 * * * *}")
    public void scheduledPreloadActivities() {
        log.info("Starting scheduled activity preload...");
        try {
            activityCacheService.preloadActivities();
        } catch (Exception e) {
            log.error("Error during scheduled activity preload: {}", e.getMessage(), e);
        }
    }

    /**
     * 预热即将开始的活动
     * 
     * 执行频率：每1分钟执行一次
     * 
     * 预热范围：
     * - 状态为 NOT_STARTED（未开始）
     * - 开始时间在接下来5分钟内的活动
     * 
     * 设计意图：
     * - 活动开始前5分钟是用户访问高峰期
     * - 提前预热确保活动开始瞬间缓存已就绪
     * - 高频检查确保不会遗漏任何即将开始的活动
     */
    @Scheduled(fixedRate = 60000)
    public void preloadUpcomingActivities() {
        log.debug("Checking for upcoming activities to preload...");
        try {
            activityCacheService.preloadUpcomingActivities(5);
        } catch (Exception e) {
            log.error("Error during upcoming activity preload: {}", e.getMessage(), e);
        }
    }

    /**
     * 刷新所有活跃活动缓存
     * 
     * 执行频率：每5分钟执行一次
     * 
     * 刷新范围：
     * - 所有状态为 NOT_STARTED（未开始）的活动
     * - 所有状态为 IN_PROGRESS（进行中）的活动
     * - 仅刷新 isActive = true 的活动
     * 
     * 作用：
     * - 保持缓存数据与数据库同步
     * - 更新库存等实时变化的数据
     * - 重置缓存过期时间，防止缓存失效
     */
    @Scheduled(fixedRate = 300000)
    public void refreshActiveActivities() {
        log.info("Refreshing all active activities in cache...");
        try {
            activityCacheService.preloadAllActiveActivities();
        } catch (Exception e) {
            log.error("Error during active activities refresh: {}", e.getMessage(), e);
        }
    }
}
