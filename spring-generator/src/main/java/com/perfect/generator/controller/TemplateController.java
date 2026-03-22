package com.perfect.generator.controller;

import com.perfect.generator.service.VelocityTemplateService;
import com.perfect.generator.vo.TemplateConfig;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@AllArgsConstructor
@RestController
@RequestMapping("/api/code")
public class TemplateController {

    final private VelocityTemplateService templateService;

    @GetMapping("/templates")
    public ResponseEntity<List<TemplateConfig>> getAvailableTemplates() {
        return ResponseEntity.ok(templateService.getAvailableTemplates());
    }

    @GetMapping("/template/{templateName}")
    public ResponseEntity<TemplateConfig> getTemplateConfig(@PathVariable String templateName) {
        TemplateConfig config = templateService.getTemplateConfig(templateName);
        if (config != null) {
            return ResponseEntity.ok(config);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
