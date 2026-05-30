package com.example.demo.resp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileUploadResp {
    private String permanentUrl;   // 自己拼的，不会变
    private String presignedUrl;   // MinIO 签名的，有有效期
}