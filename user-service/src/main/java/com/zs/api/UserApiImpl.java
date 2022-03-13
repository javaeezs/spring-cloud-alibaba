package com.zs.api;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.zs.convert.UserConvert;
import com.zs.service.UserService;
import com.zs.util.RedisUtil;
import com.zs.vo.UserVO;
import common.ResponseResult;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * @Author: zs
 * @Date: Created in 2022/3/12
 * @Description:
 */
@DubboService
public class UserApiImpl implements UserApi {

    @Autowired
    private UserService userService;

    @Autowired
    private UserConvert userConvert;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public ResponseResult<UserVO> getById(Long id) {
        return ResponseResult.ok(userConvert.convertToVO(userService.getById(id)));
    }

    @Override
    public ResponseResult<Long> insert(Long id) {
        return ResponseResult.ok(userService.insert(id));
    }

    @Override
    public ResponseResult<String> getToken() {
        String token = UUID.randomUUID().toString();
        token = token.replaceAll("-", "");
        // 将token放到Redis中，用UUID保证唯一性
        redisUtil.set(token, token, 200);
        return ResponseResult.ok(token);
    }

    @Override
    public ResponseResult<Boolean> findToken(String tokenKey) {
        String tokenValue = (String) redisUtil.get(tokenKey);
        // 如果能够获取该(从redis获取令牌)令牌(将当前令牌删除掉) 就直接执行该访问的业务逻辑
        if (StringUtils.isEmpty(tokenValue)) {
            return ResponseResult.ok(false);
        }
        // 保证每个接口对应的token 只能访问一次，保证接口幂等性问题，用完直接删掉
        redisUtil.del(tokenKey);
        return ResponseResult.ok(true);
    }

}
