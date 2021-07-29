package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanhua.domain.db.User;
import com.tanhua.dubbo.mapper.UserMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Service
public class UserApiImpl implements UserApi{

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByMobile(String phone) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",phone);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public void save(User user) {
        //user.setCreated(new Date());
        //user.setUpdated(new Date());
        userMapper.insert(user);
    }

}
