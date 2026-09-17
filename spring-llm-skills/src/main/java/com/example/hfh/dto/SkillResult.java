package com.example.hfh.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillResult {
    private boolean success;
    private String skillName;
    private Object data;
    private String message;

    public static SkillResult success(String skillName, Object data) {
        return SkillResult.builder()
                .success(true)
                .skillName(skillName)
                .data(data)
                .message("执行成功")
                .build();
    }

    public static SkillResult fail(String skillName, String error) {
        return SkillResult.builder()
                .success(false)
                .skillName(skillName)
                .message(error)
                .build();
    }
}