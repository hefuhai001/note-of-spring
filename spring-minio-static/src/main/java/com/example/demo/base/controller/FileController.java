package com.example.demo.base.controller;

import com.example.demo.FileUploadResp;
import com.example.demo.base.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * Class description goes here.
 * </p>
 *
 * @author 何福海
 * @version 1.0
 * @since 2025/8/1
 */
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    // **上传**
    @PostMapping("/upload")
    public FileUploadResp upload(@RequestPart("file") MultipartFile file) {
        return fileService.upload(file);
    }

    // **下载**
    @GetMapping("/download/{fileName}")
    public ResponseEntity<InputStreamResource> download(@PathVariable String fileName) {
        InputStreamResource resource = fileService.download(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}