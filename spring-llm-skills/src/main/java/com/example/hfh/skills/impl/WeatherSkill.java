package com.example.hfh.skills.impl;

import com.example.hfh.dto.SkillResult;
import com.example.hfh.skills.Skill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class WeatherSkill implements Skill {

    @Override
    public String getName() {
        return "get_weather";
    }

    @Override
    public String getDescription() {
        return "获取指定城市的当前天气信息。当用户询问天气、温度、下雨等情况时调用。";
    }

    @Override
    public Map<String, Object> getParametersSchema() {
        Map<String, Object> schema = new HashMap<>();
        schema.put("type", "object");
        schema.put("properties", Map.of(
                "city", Map.of(
                        "type", "string",
                        "description", "城市名称，如北京、上海、纽约"
                ),
                "date", Map.of(
                        "type", "string",
                        "description", "日期，格式YYYY-MM-DD，默认为今天"
                )
        ));
        schema.put("required", List.of("city"));
        return schema;
    }

    @Override
    public SkillResult execute(Map<String, Object> arguments) {
        String city = (String) arguments.get("city");
        String date = (String) arguments.getOrDefault("date", "today");

        log.info("Executing weather skill for city: {}, date: {}", city, date);

        // 模拟天气数据（实际项目中调用真实天气API）
        Map<String, Object> weatherData = new HashMap<>();
        weatherData.put("city", city);
        weatherData.put("date", date);
        weatherData.put("temperature", 25);
        weatherData.put("condition", "晴朗");
        weatherData.put("humidity", "60%");
        weatherData.put("wind", "东南风 3级");

        return SkillResult.success(getName(), weatherData);
    }
}