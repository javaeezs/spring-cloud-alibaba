package com.zs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author: zs
 * @Date: Created in 2022/3/12
 * @Description:
 */
@EnableDiscoveryClient
@SpringBootApplication
public class UserWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserWebApplication.class);
    }

}
