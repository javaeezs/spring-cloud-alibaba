package com.zs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @Author: zs
 * @Date: Created in 2022/3/12
 * @Description:
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableAspectJAutoProxy
public class UserWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserWebApplication.class);
    }

}
