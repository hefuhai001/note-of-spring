package com.example.upload.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class WebController {

    @RequestMapping("/")
    public String welcome() {
        return "index";
    }

}
