package com.hfh.api.skill.controller;

import com.hfh.api.skill.model.Skill;
import com.hfh.api.skill.model.SkillExecutionRequest;
import com.hfh.api.skill.model.SkillExecutionResponse;
import com.hfh.api.skill.service.SkillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/skills")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping
    public ResponseEntity<List<Skill>> listSkills() {
        List<Skill> skills = skillService.listSkills();
        return ResponseEntity.ok(skills);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Skill> getSkill(@PathVariable String name) {
        return skillService.getSkill(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{name}/prompt")
    public ResponseEntity<Map<String, String>> getSkillPrompt(@PathVariable String name) {
        String prompt = skillService.getSkillPrompt(name);
        if (prompt == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of("skillName", name, "prompt", prompt));
    }

    @PostMapping("/{name}/execute")
    public ResponseEntity<SkillExecutionResponse> executeSkill(
            @PathVariable String name,
            @RequestBody SkillExecutionRequest request) {
        request.setSkillName(name);
        SkillExecutionResponse response = skillService.executeSkill(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/execute")
    public ResponseEntity<SkillExecutionResponse> executeSkillDirect(
            @RequestBody SkillExecutionRequest request) {
        SkillExecutionResponse response = skillService.executeSkill(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }
}
