package com.example.codegenerator.controller;

import com.example.codegenerator.dto.CodeGenerationRequest;
import com.example.codegenerator.entity.ColumnInfo;
import com.example.codegenerator.entity.TableInfo;
import com.example.codegenerator.model.TemplateConfig;
import com.example.codegenerator.service.DatabaseService;
import com.example.codegenerator.service.VelocityTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/code")
public class CodeGenerationController {

    private final VelocityTemplateService templateService;

    public CodeGenerationController(VelocityTemplateService templateService) {
        this.templateService = templateService;
    }


    /**
     * {
     *     "templateName": "service.vm",
     *     "variables": {
     *       "packageName": "com.example.service",
     *       "className": "UserService",
     *       "entityName": "User",
     *       "primaryKeyType": "Long"
     *     }
     *   }
     */

    /**
     * {
     *     "templateName": "service-impl.vm",
     *     "variables": {
     *       "packageName": "com.example.service",
     *       "className": "UserServiceImpl",
     *       "entityName": "User",
     *       "primaryKeyType": "Long"
     *     }
     *   }
     */

    /**
     * {
     *     "templateName": "mapper.vm",
     *     "variables": {
     *       "packageName": "com.example.mapper",
     *       "className": "UserMapper",
     *       "entityName": "User",
     *       "primaryKeyType": "Long"
     *     }
     *   }
     */

    /**
     * {
     *     "templateName": "controller.vm",
     *     "variables": {
     *       "packageName": "com.example.controller",
     *       "className": "UserController",
     *       "entityName": "User",
     *       "entityNameLower": "users",
     *       "idType": "Long"
     *     }
     *   }
     */

    /**
     * {
     *     "templateName": "mapper-xml.vm",
     *     "variables": {
     *       "namespace": "com.example.mapper.UserMapper",
     *       "entityName": "User",
     *       "tableName": "t_user",
     *       "primaryKey": {
     *         "name": "id",
     *         "type": "Long",
     *         "isPrimaryKey": true
     *       },
     *       "fields": [
     *         {"name": "id", "type": "Long", "isPrimaryKey": true},
     *         {"name": "username", "type": "String", "comment": "用户名"},
     *         {"name": "email", "type": "String", "comment": "邮箱"}
     *       ]
     *     }
     *   }
     */

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
     *  {
     *  "templateName": "request-dto.vm",
     *  "variables": {
     *    "packageName": "com.example.model.dto",
     *    "className": "UserDto",
     *    "classComment":"UserDto",
     *    "fields": [
     *      {"name": "id", "type": "Long"},
     *      {"name": "username", "type": "String"},
     *      {"name": "email", "type": "String"}
     *      ]
     *    }
     *  }
     */

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

    // 新增的数据库查询接口

    @Autowired
    private DatabaseService databaseService;

    /**
     * 获取所有表信息
     */
    @GetMapping("/database/tables")
    public ResponseEntity<List<TableInfo>> getAllTables() {
        try {
            List<TableInfo> tables = databaseService.getAllTables();
            return ResponseEntity.ok()
                    .body(tables);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    /**
     * 获取指定表的详细信息（支持指定schema）
     */
    @GetMapping("/database/table/{schemaName}/{tableName}")
    public ResponseEntity<Map<String, Object>> getTableDetail(
            @PathVariable String schemaName,
            @PathVariable String tableName) {
        try {
            TableInfo tableInfo = databaseService.getTableInfo(tableName, schemaName);
            List<ColumnInfo> columns = databaseService.getTableColumns(tableName, schemaName);

            return ResponseEntity.ok(Map.of(
                    "tableInfo", tableInfo,
                    "columns", columns
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

}