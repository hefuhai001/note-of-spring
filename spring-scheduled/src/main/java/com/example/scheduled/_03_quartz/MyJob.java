package com.example.scheduled._03_quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class MyJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //System.out.println("Quartz 任务执行：" + System.currentTimeMillis());
    }


}
