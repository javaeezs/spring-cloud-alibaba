package com.zs.config;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.zs.annotation.ExtApiIdempotent;
import com.zs.util.RedisToken;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class ExtApiAopIdempotent {

    @Autowired
    private RedisToken redisToken;

    // 1.使用AOP环绕通知拦截所有controller的方法
    // 2.这里是你需要拦截的controller位置，设置一个我们上层公共位置可以了，
    @Pointcut("execution(* com.zs.controller.*.*(..)))")
    public void rlAop() {

    }

    /**
     * 封装数据-在请求连接中获取需要的参数，例如：请求头中token
     */
    public HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes.getRequest();
    }

    /**
     * 环绕通知
     */
    @Around("rlAop()")
    public Object doBefore(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 获取被增强的方法相关信息 - 查看方法上是否有次注解
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        ExtApiIdempotent declaredAnnotation = methodSignature.getMethod().getDeclaredAnnotation(ExtApiIdempotent.class);
        if (declaredAnnotation != null) {
            HttpServletRequest request = getRequest();
            String token = request.getHeader("token");

            // 获取不到token
            if (StringUtils.isEmpty(token)) {
                return "获取不到token";
            }

            // 接口获取对应的令牌,如果能够获取该(从redis获取令牌)令牌(将当前令牌删除掉) 就直接执行该访问的业务逻辑
            boolean isToken = redisToken.findToken(token);
            // 接口获取对应的令牌,如果获取不到该令牌 直接返回请勿重复提交
            if (!isToken) {
                return "请勿重复提交!";
            }
        }
        return proceedingJoinPoint.proceed();
    }
}