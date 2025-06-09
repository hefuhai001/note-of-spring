package com.example.aop.utils;

import com.example.aop.service.LogService;
import com.example.aop.entity.SysOperLog;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

@Aspect
@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    private LogService logService; // 日志服务，用于保存日志

    @Pointcut("@annotation(com.example.aop.utils.Log)")
    public void logPointCut() {}

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed(); // 执行方法
        long timeTaken = System.currentTimeMillis() - startTime;

        // 获取方法上的注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log logAnnotation = method.getAnnotation(Log.class);

        // 获取请求信息
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getRemoteAddr();
        String methodName = joinPoint.getSignature().getName();
        String params = Arrays.toString(joinPoint.getArgs()); // 假设第一个参数是请求参数

        // 创建日志实体
        SysOperLog log = new SysOperLog();
        log.setOperation(logAnnotation.operation());
        log.setMethod(methodName);
        log.setParams(params);
        log.setIp(ip);
        log.setCreateTime(new Date());

        // 保存日志
        logService.saveLog(log);

        return result;
    }
}
