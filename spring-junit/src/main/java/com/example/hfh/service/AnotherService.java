package com.example.hfh.service;

import org.springframework.stereotype.Service;

@Service
public class AnotherService {
    public String processInput(String input) {
        System.out.println(input);
        return input;
    }

}
