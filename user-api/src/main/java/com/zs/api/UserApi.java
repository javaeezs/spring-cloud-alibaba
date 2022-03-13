package com.zs.api;

import com.zs.vo.UserVO;
import common.ResponseResult;

/**
 * @Author: zs
 * @Date: Created in 2022/3/12
 * @Description:
 */
public interface UserApi {

    ResponseResult<UserVO> getById(Long id);

    ResponseResult<Long> insert(Long id);

    ResponseResult<String> getToken();

    ResponseResult<Boolean> findToken(String tokenKey);
}
