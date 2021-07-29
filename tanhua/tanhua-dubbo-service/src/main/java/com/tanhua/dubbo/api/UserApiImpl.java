package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanhua.domain.db.User;
import com.tanhua.dubbo.mapper.UserMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserApiImpl implements UserApi{

    @Autowired
    private UserMapper userMapper;

    // 添加用户
    @Override
    public Long save(User user) {
        /*user.setCreated(new Date());
        user.setUpdated(new Date());*/
        userMapper.insert(user);
        return user.getId();
    }

    // 通过手机号查询用户
    @Override
    public User findByMobile(String mobile) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        User user = userMapper.selectOne(wrapper);
        return user;
    }

    @Override
    public void updatePhone(Long id, String phone) {
        User user = new User();
        user.setId(id);
        user.setMobile(phone);
        userMapper.updateById(user);
    }

}
