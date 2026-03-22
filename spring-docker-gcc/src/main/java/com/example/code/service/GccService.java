package com.example.code.service;

import java.util.List;

public interface GccService {
    String compile(String code) throws Exception;

    String run(String taskId, List<String> args) throws Exception;

    String compileAndRun(String code, List<String> args) throws Exception;
}
