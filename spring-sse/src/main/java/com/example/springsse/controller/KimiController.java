package com.example.springsse.controller;

import com.example.springsse.entity.Message;
import com.example.springsse.utils.MoonshotAiUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/kimi")
public class KimiController {

    @PostMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamEvents(@RequestBody List<Message> messages) {

//        return Flux.interval(
//                Duration.ofSeconds(1)).map(sequence -> ServerSentEvent.<String>builder()
//                .id(String.valueOf(sequence))
//                .event("time-update")
//                .data("SSE from WebFlux - " + LocalDateTime.now())
//                .build());

        return MoonshotAiUtils.chatStream("moonshot-v1-8k", messages)
                .map(data -> ServerSentEvent.<String>builder()
                        .id(LocalDateTime.now().toString())
                        .event("message")
                        .data(data)
                        .build());

    }


}