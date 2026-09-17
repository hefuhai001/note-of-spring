package com.example.hfh.controller;

import com.example.hfh.dto.SkillResult;
import com.example.hfh.service.SkillService;
import com.example.hfh.skills.SkillRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;
    private final SkillRegistry skillRegistry;

    /**
     * 智能对话接口 - 自动识别并使用 Skills
     */
    @PostMapping("/chat")
    public Mono<String> chat(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        log.info("Received message: {}", message);

        return skillService.processWithSkills(message)
                .doOnNext(response -> log.info("Response: {}", response));
    }

    /**
     * 直接调用指定 Skill（调试用）
     */
    @PostMapping("/skill/{skillName}")
    public SkillResult executeSkill(
            @PathVariable String skillName,
            @RequestBody Map<String, Object> arguments) {

        log.info("Direct skill call: {} with args {}", skillName, arguments);
        return skillRegistry.execute(skillName, arguments);
    }

    /**
     * 列出所有可用 Skills
     */
    @GetMapping("/skills")
    public Object listSkills() {
        return skillRegistry.getAllTools();
    }
}