package asia.hfh.code;

import asia.hfh.code.bo.admin.CodeGenerateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CodeGenerateTest {

    @Autowired
    private CodeGenerateService codeGenerateService;

    @Test
    public void testGenerateCode() {
        // 创建代码生成请求
        CodeGenerateRequest request = new CodeGenerateRequest();
        request.setEntity("User");
        request.setTableName("sys_user");
        request.setTableComment("用户表");

        // 设置字段信息
        CodeGenerateRequest.FieldInfo idField = new CodeGenerateRequest.FieldInfo();
        idField.setName("id");
        idField.setPropertyName("id");
        idField.setPropertyType("Long");
        idField.setComment("主键ID");
        idField.setPrimaryKey(true);
        idField.setNullable(false);

        CodeGenerateRequest.FieldInfo nameField = new CodeGenerateRequest.FieldInfo();
        nameField.setName("name");
        nameField.setPropertyName("name");
        nameField.setPropertyType("String");
        nameField.setComment("用户名");
        nameField.setPrimaryKey(false);
        nameField.setNullable(true);

        CodeGenerateRequest.FieldInfo emailField = new CodeGenerateRequest.FieldInfo();
        emailField.setName("email");
        emailField.setPropertyName("email");
        emailField.setPropertyType("String");
        emailField.setComment("邮箱");
        emailField.setPrimaryKey(false);
        emailField.setNullable(true);

        request.setFields(Arrays.asList(idField, nameField, emailField));

        // 添加其他参数
        Map<String, Object> params = new HashMap<>();
        params.put("author", "xiaoHe");
        params.put("since", "2025-12-31");
        request.setParams(params);

        // 执行代码生成
        Map<String, String> generatedCode = codeGenerateService.generateCode(convertToMap(request));

        // 验证生成的代码
        assertNotNull(generatedCode);
        assertFalse(generatedCode.isEmpty());
        assertTrue(generatedCode.containsKey("User.java"));
        assertTrue(generatedCode.containsKey("UserController.java"));
        assertTrue(generatedCode.containsKey("UserService.java"));
        assertTrue(generatedCode.containsKey("UserServiceImpl.java"));
        assertTrue(generatedCode.containsKey("UserMapper.java"));
        assertTrue(generatedCode.containsKey("UserMapper.xml"));

        // 验证生成的实体类内容
        String entityCode = generatedCode.get("User.java");
        assertNotNull(entityCode);
        assertTrue(entityCode.contains("class User"));
        assertTrue(entityCode.contains("private Long id;"));
        assertTrue(entityCode.contains("private String name;"));
        assertTrue(entityCode.contains("private String email;"));

        // 验证生成的Controller内容
        String controllerCode = generatedCode.get("UserController.java");
        assertNotNull(controllerCode);
        assertTrue(controllerCode.contains("UserController"));
        assertTrue(controllerCode.contains("UserServiceImpl"));

        System.out.println("Generated Entity Code:");
        System.out.println(entityCode);
    }

    @Test
    public void testGenerateCodeWithEmptyFields() {
        // 测试没有字段的情况
        CodeGenerateRequest request = new CodeGenerateRequest();
        request.setEntity("EmptyEntity");
        request.setTableName("empty_table");
        request.setTableComment("空表");
        request.setFields(null); // 没有字段

        // 执行代码生成
        Map<String, String> generatedCode = codeGenerateService.generateCode(convertToMap(request));

        // 验证生成的代码
        assertNotNull(generatedCode);
        assertFalse(generatedCode.isEmpty());
        assertTrue(generatedCode.containsKey("EmptyEntity.java"));

        // 验证生成的实体类内容
        String entityCode = generatedCode.get("EmptyEntity.java");
        assertNotNull(entityCode);
        assertTrue(entityCode.contains("class EmptyEntity"));

        System.out.println("Generated Entity Code (with empty fields):");
        System.out.println(entityCode);
    }

    /**
     * 将CodeGenerateRequest对象转换为Map参数
     *
     * @param request 请求对象
     * @return 转换后的参数Map
     */
    private Map<String, Object> convertToMap(CodeGenerateRequest request) {
        Map<String, Object> params = new HashMap<>();
        params.put("entity", request.getEntity());
        params.put("tableName", request.getTableName());
        params.put("tableComment", request.getTableComment());
        params.put("fields", request.getFields());

        if (request.getParams() != null) {
            params.putAll(request.getParams());
        }

        return params;
    }
}