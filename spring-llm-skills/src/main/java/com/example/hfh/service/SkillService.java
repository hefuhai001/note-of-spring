package com.example.hfh.service;

import com.example.hfh.dto.ChatRequest;
import com.example.hfh.dto.ChatResponse;
import com.example.hfh.skills.SkillRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SkillService {

    private final LlmService llmService;
    private final SkillRegistry skillRegistry;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 处理用户输入，自动识别并调用 Skills
     */
    public Mono<String> processWithSkills(String userInput) {
        List<ChatRequest.Message> messages = new ArrayList<>();

        // 系统提示
        messages.add(ChatRequest.Message.builder()
                .role("system")
                .content("你是一个智能助手，可以使用工具帮助用户。当需要时，请调用合适的工具。")
                .build());

        // 用户输入
        messages.add(ChatRequest.Message.builder()
                .role("user")
                .content(userInput)
                .build());

        return callWithToolLoop(messages, 0);
    }

    /**
     * 递归调用，处理 Tool Calls
     */
    private Mono<String> callWithToolLoop(List<ChatRequest.Message> messages, int depth) {
        if (depth > 5) {  // 防止无限循环
            return Mono.just("调用层级过深，请简化请求");
        }

        return llmService.chat(messages, skillRegistry.getAllTools())
                .flatMap(response -> {
                    ChatResponse.Choice choice = response.getChoices().get(0);
                    ChatRequest.Message message = choice.getMessage();

                    // 检查是否有 Tool Call
                    if (message.getToolCall() != null) {
                        ChatRequest.ToolCall toolCall = message.getToolCall();
                        String toolName = toolCall.getFunction().getName();
                        String arguments = toolCall.getFunction().getArguments();

                        log.info("LLM requested tool: {}, args: {}", toolName, arguments);

                        // 执行 Skill
                        return Mono.fromCallable(() -> {
                                    Map<String, Object> args = objectMapper.readValue(arguments, Map.class);
                                    return skillRegistry.execute(toolName, args);
                                })
                                .subscribeOn(Schedulers.boundedElastic())
                                .flatMap(result -> {
                                    // 将 assistant 的 tool call 加入历史
                                    messages.add(message);

                                    // 将 tool 结果加入历史
                                    messages.add(ChatRequest.Message.builder()
                                            .role("tool")
                                            .toolCallId(toolCall.getId())
                                            .name(toolName)
                                            .content(toJson(result))
                                            .build());

                                    // 继续循环，让 LLM 生成最终回复
                                    return callWithToolLoop(messages, depth + 1);
                                });
                    }

                    // 没有 Tool Call，直接返回结果
                    return Mono.just(message.getContent());
                });
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return obj.toString();
        }
    }
}