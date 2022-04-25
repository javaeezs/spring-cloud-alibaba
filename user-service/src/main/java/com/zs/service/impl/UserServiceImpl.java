package com.zs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.entity.User;
import com.zs.mapper.UserMapper;
import com.zs.service.UserService;
import common.RedisKeyPrefixConst;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.zs.util.RedisUtil;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @Author: zs
 * @Date: Created in 2022/3/12
 * @Description:
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private RedissonClient redisson;

    @Resource
    private RedisUtil redisUtil;

    private final Logger logger = LogManager.getLogger(UserServiceImpl.class);


    public static final long USER_CACHE_TIMEOUT = 60 * 60 * 24;
    public static final String LOCK_USER_CACHE_CREATE_PREFIX = "lock:user:cache_create:";
    public static final String LOCK_USER_CACHE_UPDATE_PREFIX = "lock:user:cache_update:";

    @Override
    @Cacheable(value = "user:caffeine", key = "#id")
    public User getById(Long id) {
        User user = null;
        String userCacheKey = RedisKeyPrefixConst.USER_CACHE + id;
        //先查缓存
        user = getUserFromCache(userCacheKey);
        if (user != null) {
            return user;
        }

        //解决并发重建问题
        RLock lock = redisson.getLock(LOCK_USER_CACHE_CREATE_PREFIX + id);
        //拿锁失败时会不停的重试，获取锁时具有watch dog自动延期机制，默认30秒，每隔10秒续期
        //lock.lock();

        //没有watch dog，10s后自动释放
        //lock.lock(10, TimeUnit.SECONDS);

        try {
            //最多等待两秒
            lock.tryLock(2, TimeUnit.SECONDS);

            //尝试拿锁2秒后停止重试，返回false，没有watch dog，3秒后自动释放
            //lock.tryLock(2, 3, TimeUnit.SECONDS);

            //双重检测
            user = getUserFromCache(userCacheKey);
            if (user != null) {
                return user;
            }

            //读写锁：解决双写不一致问题
            RReadWriteLock readWriteLock = redisson.getReadWriteLock(LOCK_USER_CACHE_UPDATE_PREFIX + id);
            readWriteLock.readLock().lock();
            try {
                logger.info("get data from db");
                user = baseMapper.selectById(id);
                if (user != null) {
                    //缓存重建
                    redisUtil.set(RedisKeyPrefixConst.USER_CACHE + user.getId(), user);
                } else {
                    //缓存一个时间较短的空对象，避免缓存穿透
                    redisUtil.set(RedisKeyPrefixConst.USER_CACHE + id, new User(), getEmptyCacheTimeout());
                }
            } finally {
                readWriteLock.readLock().unlock();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return user;
    }

    private User getUserFromCache(String key) {
        logger.info("get data from redis");
        User user = (User) redisUtil.get(key);
        if (user != null) {
            //缓存延期
            redisUtil.expire(key, getUserCacheTimeout());
        }
        return user;
    }

    @Override
    @CachePut(value = "user:caffeine", key = "#user.id")
    public User updateUser(User user) {
        RReadWriteLock readWriteLock = redisson.getReadWriteLock(LOCK_USER_CACHE_UPDATE_PREFIX + user.getId());
        readWriteLock.writeLock().lock();
        try {
            boolean flag = updateById(user);
            if (flag) {
                redisUtil.set(RedisKeyPrefixConst.USER_CACHE + user.getId(), user, getUserCacheTimeout());
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
        return user;
    }

    @Override
    public Long insert(Long id) {
        User user = new User().setId(id).setName(UUID.randomUUID().toString());
        boolean flag = saveOrUpdate(user);
        if (flag) {
            redisUtil.set(RedisKeyPrefixConst.USER_CACHE + id, user, getUserCacheTimeout());
            redisUtil.convertAndSend(RedisKeyPrefixConst.TOPIC_USER, user);
            return user.getId();
        }
        return null;
    }

    /*
     * 缓存设置随机时间，避免大量key同时过期导致缓存雪崩
     * */
    private long getUserCacheTimeout() {
        return USER_CACHE_TIMEOUT + ThreadLocalRandom.current().nextInt(5) * 60 * 60;
    }

    /*
     * 空对象缓存时间
     * */
    private long getEmptyCacheTimeout() {
        return 60 + ThreadLocalRandom.current().nextInt(30);
    }

}
