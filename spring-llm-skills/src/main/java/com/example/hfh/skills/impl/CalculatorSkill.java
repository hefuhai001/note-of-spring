package com.example.hfh.skills.impl;

import com.example.hfh.dto.SkillResult;
import com.example.hfh.skills.Skill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CalculatorSkill implements Skill {

    @Override
    public String getName() {
        return "calculate";
    }

    @Override
    public String getDescription() {
        return "执行数学计算。当用户需要进行加减乘除、复杂数学运算时调用。";
    }

    @Override
    public Map<String, Object> getParametersSchema() {
        return Map.of(
                "type", "object",
                "properties", Map.of(
                        "expression", Map.of(
                                "type", "string",
                                "description", "数学表达式，如 '2 + 2', 'sqrt(16)', '(100 - 20) * 0.8'"
                        )
                ),
                "required", List.of("expression")
        );
    }

    @Override
    public SkillResult execute(Map<String, Object> arguments) {
        String expression = (String) arguments.get("expression");

        log.info("Calculating: {}", expression);

        try {
            // 简单计算实现（生产环境建议使用更安全的表达式引擎）
            double result = evaluateExpression(expression);

            return SkillResult.success(getName(), Map.of(
                    "expression", expression,
                    "result", result
            ));
        } catch (Exception e) {
            return SkillResult.fail(getName(), "计算错误: " + e.getMessage());
        }
    }

    private double evaluateExpression(String expr) {
        // 简化实现，实际使用请引入 exp4j 或 ScriptEngine
        // 这里仅作演示
        expr = expr.replaceAll("\\s+", "");

        // 支持基本四则运算
        if (expr.contains("+")) {
            String[] parts = expr.split("\\+");
            return Double.parseDouble(parts[0]) + Double.parseDouble(parts[1]);
        } else if (expr.contains("-")) {
            String[] parts = expr.split("-");
            return Double.parseDouble(parts[0]) - Double.parseDouble(parts[1]);
        } else if (expr.contains("*")) {
            String[] parts = expr.split("\\*");
            return Double.parseDouble(parts[0]) * Double.parseDouble(parts[1]);
        } else if (expr.contains("/")) {
            String[] parts = expr.split("/");
            return Double.parseDouble(parts[0]) / Double.parseDouble(parts[1]);
        }

        return Double.parseDouble(expr);
    }
}