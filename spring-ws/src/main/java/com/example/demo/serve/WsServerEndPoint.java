package com.example.demo.serve;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 监听websocket地址  /myWs
 * @Date 2022/5/18 20:10
 * @Author bestwishes0203
 **/

@ServerEndpoint("/myWs") // websocket终端地址
@Component // 将该类交给spring管理
@Slf4j // 用于输出日志信息
public class WsServerEndPoint {
    // ConcurrentHashMap 和hashMap的区别是 ConcurrentHashMap是线程安全的
    static Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    /**
     * websocket建立链接时触发方法
     *
     * @param session 每一个websocket的链接 对于服务端都是一个session
     * @author bestwishes0203
     * @date 20:15 2022/5/18
     **/
    @OnOpen
    public void onOpen(Session session) {
        // 建立连接时，将session存入map中
        sessionMap.put(session.getId(), session);
        log.info("websocket is open");
    }

    /**
     * 收到了客户端消息时触发方法
     *
     * @param text 客户端传来的消息内容
     * @return java.lang.String
     * @author bestwishes0203
     * @date 20:20 2022/5/18
     **/
    @OnMessage
    public String onMessage(String text) {
        log.info("接受到一条消息：" + text);
        return "已经接收到消息";
    }

    /**
     * 连接关闭时触发方法
     *
     * @param session 每一个websocket的链接 对于服务端都是一个session
     * @author bestwishes0203
     * @date 20:21 2022/5/18
     **/
    @OnClose
    public void onClose(Session session) {
        sessionMap.remove(session.getId());
        log.info(("websocket is close"));
    }

    /**
     * 定时任务模拟服务器给客户端主动发消息
     *
     * @author bestwishes0203
     * @date 20:24 2022/5/18
     **/
    @Scheduled(fixedRate = 2000)  // fixedRate = 2000 每隔2s触发一次定时任务
    public void sendMessage() throws IOException {
        // 遍历sessionMap中的所有会话，给每个客户端都发送消息
        Set<String> keySet = sessionMap.keySet();
        for (String sessionId : keySet) {
            Session session = sessionMap.get(sessionId);
            RemoteEndpoint.Basic basicRemote = session.getBasicRemote();
            basicRemote.sendText("心跳");
        }
    }

}

