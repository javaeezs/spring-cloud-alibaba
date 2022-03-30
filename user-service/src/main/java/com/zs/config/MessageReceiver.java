package com.zs.config;

import org.springframework.stereotype.Component;

/**
 * @Author: zs
 * @Date: Created in 2022/3/30
 * @Description:
 */
@Component
public class MessageReceiver {

    /**
     * 接收消息方法
     */
    public void receiverMessage(String message) {
        System.out.println("MessageReceiver收到一条新消息：" + message);
    }

}
