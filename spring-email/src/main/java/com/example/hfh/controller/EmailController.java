package com.example.hfh.controller;

import com.example.hfh.dto.EmailRequest;
import com.example.hfh.dto.TemplateEmailRequest;
import com.example.hfh.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
@Validated  // 添加此注解启用方法参数验证
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/simple")
    public ResponseEntity<?> sendSimpleEmail(@Valid @RequestBody EmailRequest request) {
        CompletableFuture<Void> future = emailService.sendSimpleEmailAsync(request);
        return ResponseEntity.ok(Map.of(
                "code", 200,
                "message", "简单邮件已加入异步发送队列",
                "recipients", request.getToList() != null ? request.getToList() : List.of(request.getTo())
        ));
    }

    @PostMapping("/html")
    public ResponseEntity<?> sendHtmlEmail(@Valid @RequestBody EmailRequest request) {
        request.setHtml(true);
        CompletableFuture<Void> future = emailService.sendHtmlEmailAsync(request);
        return ResponseEntity.ok(Map.of(
                "code", 200,
                "message", "HTML邮件已加入异步发送队列",
                "recipients", request.getToList() != null ? request.getToList() : List.of(request.getTo())
        ));
    }

    @PostMapping("/template")
    public ResponseEntity<?> sendTemplateEmail(@Valid @RequestBody TemplateEmailRequest request) {
        CompletableFuture<Void> future = emailService.sendTemplateEmailAsync(request);
        return ResponseEntity.ok(Map.of(
                "code", 200,
                "message", "模板邮件已加入异步发送队列",
                "recipients", request.getToList() != null ? request.getToList() : List.of(request.getTo())
        ));
    }

    @PostMapping("/batch")
    public ResponseEntity<?> sendBatchEmails(@Valid @RequestBody List<EmailRequest> requests) {
        CompletableFuture<Void> future = emailService.sendBatchEmailsAsync(requests);
        return ResponseEntity.ok(Map.of(
                "code", 200,
                "message", "批量邮件已加入异步发送队列",
                "count", requests.size()
        ));
    }
}