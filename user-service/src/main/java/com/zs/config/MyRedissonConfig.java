package com.zs.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: zs
 * @Date: Created in 2022/3/12
 * @Description:
 */
@Configuration
public class MyRedissonConfig {

    @Bean
    public RedissonClient RedissonClient() {
        Config config = new Config();
        //单机模式
        config.useSingleServer()
                .setAddress("redis://127.0.0.1:6379")
                //.setPassword("111")
                .setDatabase(1);

        //集群模式
        /*com.zs.config.useClusterServers()
                .addNodeAddress("redis://192.168.56.101:36379")
                .addNodeAddress("redis://192.168.56.102:36379")
                .addNodeAddress("redis://192.168.56.103:36379")
                .setPassword("1111111")
                .setScanInterval(5000);*/

        //哨兵模式
        /*com.zs.config.useSentinelServers().addSentinelAddress("redis://ip1:port1",
                "redis://ip2:port2",
                "redis://ip3:port3")
                .setMasterName("mymaster")
                .setPassword("password")
                .setDatabase(0);*/

        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }

}
