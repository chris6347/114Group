package com.itheima.service;

import com.itheima.health.pojo.User;

public interface UserService {

    /**
     * 根据用户名来查询用户数据
     * @param username 用户名
     * @return 用户数据
     */
   User findUserByUsername(String username);
}
