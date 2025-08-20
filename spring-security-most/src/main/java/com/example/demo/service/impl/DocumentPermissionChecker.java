package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

@Service
public class DocumentPermissionChecker {

    public boolean canViewDocument(String usernameWithType, String docId) {
        // 实际项目中这里会有更复杂的逻辑
        // 这里简化为: 管理员可以查看所有文档，用户只能查看自己的文档

        String[] parts = usernameWithType.split("\\|");
        String username = parts[0];
        String userType = parts[1];

        return "admin".equalsIgnoreCase(userType) ||
                docId.startsWith(username + "_");
    }

    public boolean canEditDocument(String usernameWithType, String docId) {
        // 实际项目中这里会有更复杂的逻辑
        // 这里简化为: 文档所有者可以编辑

        String[] parts = usernameWithType.split("\\|");
        String username = parts[0];

        return docId.startsWith(username + "_");
    }
}