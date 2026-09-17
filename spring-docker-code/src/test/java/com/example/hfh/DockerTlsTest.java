package com.example.hfh;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.PingCmd;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DockerTlsTest {

    @Autowired
    private DockerClient client;

    @Test
    void testPing() {
        PingCmd ping = client.pingCmd();
        ping.exec();           // 无异常即 TLS 握手成功
        System.out.println("TLS 连接 OK");
    }
}
