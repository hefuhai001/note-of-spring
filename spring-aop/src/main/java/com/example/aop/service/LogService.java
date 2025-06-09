package com.example.aop.service;

import com.example.aop.entity.SysOperLog;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LogService {

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public void saveLog(SysOperLog log) {
        entityManager.persist(log);
    }


}
