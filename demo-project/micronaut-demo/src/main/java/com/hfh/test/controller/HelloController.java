package com.hfh.test.controller;

import io.micronaut.http.annotation.*;

import java.util.Collections;
import java.util.Map;

@Controller("/api/v1")
public class HelloController {

    /**
     * GET /api/v1/hello -> 返回纯文本
     */
    @Get(value = "/hello", produces = "text/plain")
    public String hello() {
        return "Hello, Micronaut!";
    }

    /**
     * GET /api/v1/hello/{name} -> 动态路径参数
     */
    @Get("/hello/{name}")
    public Map<String, String> helloWithName(@PathVariable String name) {
        return Collections.singletonMap("message", "Hello, " + name);
    }

    /**
     * POST /api/v1/echo -> 接收 JSON 并原样返回
     */
    @Post(value = "/echo", consumes = "application/json", produces = "application/json")
    public Map<String, Object> echo(@Body Map<String, Object> body) {
        return body;
    }

}