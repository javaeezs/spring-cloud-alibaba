package com.zs.controller;

import com.zs.annotation.ExtApiIdempotent;
import com.zs.api.UserApi;
import com.zs.util.RedisToken;
import com.zs.vo.UserVO;
import common.ResponseResult;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: zs
 * @Date: Created in 2022/3/12
 * @Description:
 */
@RestController
@RequestMapping("user")
public class UserController {

    @DubboReference
    private UserApi userApi;

    @Resource
    private RedisToken token;

    @RequestMapping("/redisToken")
    public String getRedisToken() {
        return token.getToken();
    }

    @GetMapping("getById/{id}")
    public ResponseResult<UserVO> getById(@PathVariable Long id) {
        return userApi.getById(id);
    }

    @GetMapping("insert/{id}")
    //@ExtApiIdempotent
    public ResponseResult<Long> insert(@PathVariable Long id) {
        return userApi.insert(id);
    }

}
