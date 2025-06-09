package com.example.springboot.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @Description
 * @Author XiaoHe
 * @CreateTime 2023/5/4 21:33
 */
public class uploadUtil {
    public static final String ALI_DOMAIN = "https://czh123-text.oss-cn-guangzhou...";

    public static String uploadImage(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String ext = "." + FilenameUtils.getExtension(originalFilename);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String fileName = uuid + ext;

//      地域节点
        String endpoint = "http://oss-cn-guangzhou.aliyuncs.com";
        String accessKeyId = "your-access-key-id";
        String accessKeySecret = "your-access-key-secret";

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(
                "czh123-text",
                fileName,
                file.getInputStream()
        );
        ossClient.shutdown();
        return ALI_DOMAIN + fileName;
    }
}
