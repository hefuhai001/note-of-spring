package com.example.api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.api.entity.Activity;
import com.example.api.entity.ActivityStatus;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findByStatus(ActivityStatus status);

    List<Activity> findByIsActiveTrue();

    @Query("SELECT a FROM Activity a WHERE a.status IN :statuses " +
           "AND a.startTime BETWEEN :startTime AND :endTime " +
           "AND a.isActive = true")
    List<Activity> findActivitiesToPreload(
            @Param("statuses") List<ActivityStatus> statuses,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    @Query("SELECT a FROM Activity a WHERE a.status = :status " +
           "AND a.startTime <= :thresholdTime " +
           "AND a.isActive = true")
    List<Activity> findUpcomingActivities(
            @Param("status") ActivityStatus status,
            @Param("thresholdTime") LocalDateTime thresholdTime);

    @Query("SELECT a FROM Activity a WHERE a.status IN :statuses AND a.isActive = true")
    List<Activity> findByStatusInAndIsActiveTrue(@Param("statuses") List<ActivityStatus> statuses);
}
