package com.example.codegenerator.controller;

import com.example.codegenerator.dto.CodeGenerationRequest;
import com.example.codegenerator.service.VelocityTemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/code")
public class CodeGenerationController {

    private final VelocityTemplateService templateService;

    public CodeGenerationController(VelocityTemplateService templateService) {
        this.templateService = templateService;
    }

    /**
     * {
     *     "templateName": "java-class.vm",
     *     "variables": {
     *       "packageName": "com.example.model",
     *       "className": "User",
     *       "fields": [
     *         {"name": "id", "type": "Long"},
     *         {"name": "username", "type": "String"},
     *         {"name": "email", "type": "String"}
     *       ]
     *     }
     * }
     */

    /**
     * {
     *   "templateName": "controller.vm",
     *   "variables": {
     *       "packageName": "com.example.controller",
     *       "className": "UserController",
     *       "entityName": "User",
     *       "entityNameLower": "users",
     *       "idType": "Long"
     *     }
     * }
     */

    /**
     * 代码生成示例，参数如上
     */
    @PostMapping("/generate")
    public ResponseEntity<String> generateCode(@RequestBody CodeGenerationRequest request) {
        try {
            String result = templateService.mergeTemplate(
                    request.getTemplateName(),
                    request.getVariables()
            );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("代码生成失败: " + e.getMessage());
        }
    }
}