package com.itheima.service.impl;

import com.itheima.dao.UserDao;
import com.itheima.health.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    //要注入进来dao...
    @Autowired
    private UserDao userDao;


    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return
     */
    @Override
    public User findUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }
}
