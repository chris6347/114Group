package com.itheima.api;

import com.itheima.mapper.UserMapper;
import com.itheima.pojo.User;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
// Service必须使用dubbo发布服务
// 服务实现类所在包必须在接口包下或子包下
// 扫接口包
@Service(timeout = 3000)
public class UserApiImpl implements UserApi{

    @Autowired
    private UserMapper mapper;

    @Override
    public User findById(Integer id) {
        return mapper.findById(id);
    }
}
