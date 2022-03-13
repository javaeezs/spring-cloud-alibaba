package com.zs.vo;

import java.io.Serializable;

/**
 * @Author: zs
 * @Date: Created in 2022/3/12
 * @Description:
 */
public class UserVO implements Serializable {

    private static final long serialVersionUID = 6868194802493901884L;

    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public UserVO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserVO setName(String name) {
        this.name = name;
        return this;
    }
}


