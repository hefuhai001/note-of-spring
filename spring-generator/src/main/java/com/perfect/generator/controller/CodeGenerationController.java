package com.perfect.generator.controller;

import com.perfect.generator.bo.CodeGenerationRequest;
import com.perfect.generator.service.VelocityTemplateService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@AllArgsConstructor
@RestController
@RequestMapping("/api/code")
public class CodeGenerationController {

    final private VelocityTemplateService templateService;

    /**
     * 代码生成
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

    /**
     * 一次性生成所有模板代码
     */
    @PostMapping("/generate/all")
    public ResponseEntity<Map<String, String>> generateAllCodes(@RequestBody Map<String, Object> variables) {
        try {
            // 定义所有模板文件名
            String[] templateNames = {
                    "entity.vm",
                    "controller.vm",
                    "service.vm",
                    "service-impl.vm",
                    "mapper.vm",
                    "mapper-xml.vm",
                    "bo.vm",
                    "vo.vm",
                    "api.vm",
                    "types.vm",
                    "index.vm"
            };

            Map<String, String> results = new LinkedHashMap<>();

            // 为每个模板生成代码
            for (String templateName : templateNames) {
                try {
                    String code = templateService.mergeTemplate(templateName, variables);
                    results.put(templateName, code);
                } catch (Exception e) {
                    results.put(templateName, "生成失败: " + e.getMessage());
                }
            }

            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "代码生成失败: " + e.getMessage()));
        }
    }

    /**
     * 生成所有代码并打包成ZIP文件下载
     */
    @PostMapping("/generate/all/zip")
    public ResponseEntity<byte[]> generateAllCodesAsZip(@RequestBody Map<String, Object> variables) {
        try {
            // 定义所有模板文件名和对应的输出文件名
            Map<String, String> templateToFileMap = new LinkedHashMap<>();
            templateToFileMap.put("entity.vm", variables.get("className") + "Entity.java");
            templateToFileMap.put("controller.vm", variables.get("className") + "Controller.java");
            templateToFileMap.put("service.vm", variables.get("className") + "Service.java");
            templateToFileMap.put("service-impl.vm", variables.get("className") + "ServiceImpl.java");
            templateToFileMap.put("mapper.vm", variables.get("className") + "Mapper.java");
            templateToFileMap.put("mapper-xml.vm", variables.get("className") + "Mapper.xml");
            templateToFileMap.put("bo.vm", variables.get("className") + "BO.java");
            templateToFileMap.put("vo.vm", variables.get("className") + "VO.java");
            templateToFileMap.put("api.vm", variables.get("className") + "Api.ts");
            templateToFileMap.put("types.vm", variables.get("className") + "Types.ts");
            templateToFileMap.put("index.vm", "index.vue");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(baos);

            // 为每个模板生成代码并添加到ZIP文件中
            for (Map.Entry<String, String> entry : templateToFileMap.entrySet()) {
                String templateName = entry.getKey();
                String fileName = entry.getValue();
                
                try {
                    String code = templateService.mergeTemplate(templateName, variables);
                    // 根据模板类型确定文件夹路径
                    String folderPath = getFolderPathForTemplate(templateName);
                    String entryPath = folderPath + fileName;
                    zos.putNextEntry(new ZipEntry(entryPath));
                    zos.write(code.getBytes());
                    zos.closeEntry();
                } catch (Exception e) {
                    // 如果某个模板生成失败，仍然继续处理其他模板
                    String folderPath = getFolderPathForTemplate(templateName);
                    String entryPath = folderPath + fileName;
                    zos.putNextEntry(new ZipEntry(entryPath));
                    zos.write(("生成失败: " + e.getMessage()).getBytes());
                    zos.closeEntry();
                }
            }

            zos.close();
            baos.close();

            byte[] zipBytes = baos.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"generated-code.zip\"");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/zip");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(zipBytes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(("代码生成失败: " + e.getMessage()).getBytes());
        }
    }


    @GetMapping("/example/{templateName}")
    public ResponseEntity<Map<String, Object>> getExampleData(@PathVariable String templateName) {
        Map<String, Object> exampleData = createExampleData(templateName);
        if (exampleData != null) {
            return ResponseEntity.ok(exampleData);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private Map<String, Object> createExampleData(String templateName) {
        // 根据模板名称返回示例数据
        // 这里简化处理，实际应用中可以从配置文件或数据库读取
        return Map.of();
    }

    private String getFolderPathForTemplate(String templateName) {
        return switch (templateName) {
            case "controller.vm" -> "controller/";
            case "service.vm" -> "service/";
            case "service-impl.vm" -> "service/impl/";
            case "mapper.vm", "mapper-xml.vm" -> "mapper/";
            case "entity.vm" -> "entity/";
            case "bo.vm" -> "bo/";
            case "vo.vm" -> "vo/";
            case "api.vm", "types.vm" -> "ts/";
            case "index.vm" -> "vue/";
            default -> "";
        };
    }

}