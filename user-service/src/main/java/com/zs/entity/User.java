package com.zs.entity;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @Author: zs
 * @Date: Created in 2022/3/12
 * @Description:
 */
@TableName("t_user")
public class User {

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }
}
