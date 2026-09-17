package com.example.hfh.config;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.core.RemoteApiVersion;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.File;

@Configuration
public class DockerConfig {

    @Value("${docker.host}")        // 对应 yml 里的 docker.host
    private String dockerHost;

    @Value("${docker.tls-verify}") // 没配就默认 false
    private boolean tlsVerify;

    @Value("${docker.cert-path}")
    private String certPath;

    @Bean
    public DockerClient dockerClient() throws Exception {
        DefaultDockerClientConfig.Builder configBuilder =
                DefaultDockerClientConfig.createDefaultConfigBuilder()
                        .withDockerHost(dockerHost)
                        .withDockerTlsVerify(tlsVerify);

        if (tlsVerify) {
            // 支持 classpath: 前缀
            String realPath = certPath.replace("classpath:", "");
            File certDir = ResourceUtils.getFile("classpath:" + realPath);
            configBuilder
                    .withDockerCertPath(certDir.getAbsolutePath()) // 自动找 ca.pem/cert.pem/key.pem
                    .withApiVersion(RemoteApiVersion.VERSION_1_44); // 可选：指定 API 版本
        }

        DefaultDockerClientConfig config = configBuilder.build();

        ApacheDockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())   // 这里会加载证书
                .build();

        return DockerClientImpl.getInstance(config, httpClient);
    }

    //@Bean
    //public DockerClient dockerClient() {
    //    DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
    //            .withDockerHost(dockerHost)
    //            .withDockerTlsVerify(tlsVerify)
    //            .build();
    //    ApacheDockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
    //            .dockerHost(config.getDockerHost())
    //            .sslConfig(config.getSSLConfig())
    //            .build();
    //    return DockerClientImpl.getInstance(config, httpClient);
    //}

}