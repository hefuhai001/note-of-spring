package com.example.hfh.controller;

import com.example.hfh.dto.CompileRequest;
import com.example.hfh.dto.CompileRunRequest;
import com.example.hfh.dto.RunRequest;
import com.example.hfh.service.GccService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class GccController {

    @Autowired
    private GccService gccService;

    @PostMapping("/compile_run")
    public String compileRunC(@RequestBody CompileRunRequest req) throws Exception {
        return gccService.compileAndRun(req.getCode(), req.getArgs());
    }

    @PostMapping("/compile")
    public String compileC(@RequestBody CompileRequest req) throws Exception {
        return gccService.compile(req.getCode());
    }

    @PostMapping("/run")
    public String runC(@RequestBody RunRequest req) throws Exception {
        return gccService.run(req.getTaskId(), req.getArgs());
    }

}
