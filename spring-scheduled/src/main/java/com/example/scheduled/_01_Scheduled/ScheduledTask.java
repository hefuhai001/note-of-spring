package com.example.scheduled._01_Scheduled;

import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {
    // 固定频率：每5秒执行一次任务
    //@Scheduled(fixedRate = 5000)
    //public void executeTask() {
    //    System.out.println("【@Scheduled 任务】执行时间：" + LocalDateTime.now());
    //}
    //
    //// Cron 表达式：每天中午12点执行一次任务
    //@Scheduled(cron = "0 0 12 * * ?")
    //public void executeCronTask() {
    //    System.out.println("【Cron 任务】执行时间：" + LocalDateTime.now());
    //}
}