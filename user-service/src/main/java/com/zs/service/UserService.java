package com.zs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.entity.User;

/**
 * @Author: zs
 * @Date: Created in 2022/3/12
 * @Description:
 */
public interface UserService extends IService<User> {

    User getById(Long id);

    User updateUser(User user);

    Long insert(Long id);
}
