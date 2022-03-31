package com.zs.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CaffeineCacheConfig {

    @Bean
    public CacheManager cacheManager() {
        /*
         * initialCapacity=[integer]: 初始的缓存空间大小
         * maximumSize=[long]: 缓存的最大条数
         * maximumWeight=[long]: 缓存的最大权重
         * expireAfterAccess=[duration]: 最后一次写入或访问后经过固定时间过期
         * expireAfterWrite=[duration]: 最后一次写入后经过固定时间过期
         * refreshAfterWrite=[duration]: 创建缓存或者最近一次更新缓存后经过固定的时间间隔，刷新缓存
         * weakKeys: 打开key的弱引用
         * weakValues：打开value的弱引用
         * softValues：打开value的软引用
         * recordStats：开发统计功能 注意：
         * expireAfterWrite和expireAfterAccess同事存在时，以expireAfterWrite为准。
         * maximumSize和maximumWeight不可以同时使用
         * weakValues和softValues不可以同时使用
         * */
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
                //初始容量
                .initialCapacity(128)
                //最后一次写入后经过60s过期
                .expireAfterWrite(60, TimeUnit.SECONDS)
                //缓存的最大条数
                .maximumSize(1024);
        caffeineCacheManager.setCaffeine(caffeine);
        return caffeineCacheManager;
    }

}
