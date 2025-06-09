package com.example.springoss.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public class uploadUtil {
    public static final String bucketName = "xiaohelikesleep";

    public static String uploadImage(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String ext = "." + FilenameUtils.getExtension(originalFilename);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String fileName = uuid + ext;

        //地域节点
        String endpoint = "oss-cn-beijing.aliyuncs.com";
        String accessKeyId = "your-access-key-id";
        String accessKeySecret = "your-access-key-secret";

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(
                bucketName,
                fileName,
                file.getInputStream()
        );
        ossClient.shutdown();
        // https://xppll.oss-cn-beijing.aliyuncs.com/01.jpg
        return "https://" + bucketName + "." + endpoint + "/" + fileName;
    }
}
