package com.zs.config;

import common.ResponseCode;
import common.ResponseResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: zs
 * @Date: Created in 2022/3/13
 * @Description:
 */
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult<String> customerException(Exception e) {
        return ResponseResult.status(ResponseCode.BAD_REQUEST);
    }

}
