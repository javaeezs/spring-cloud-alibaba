package com.zs.util;

import com.zs.api.UserApi;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

@Component
public class RedisToken {

    @DubboReference
    private UserApi userApi;

    /**
     * 生成Token
     */
    public String getToken() {
        return userApi.getToken().getBody();
    }

    public synchronized boolean findToken(String tokenKey) {
        return userApi.findToken(tokenKey).getBody();
    }

}