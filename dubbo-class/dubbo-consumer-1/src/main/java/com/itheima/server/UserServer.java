package com.itheima.server;

import com.itheima.api.UserApi;
import com.itheima.pojo.User;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

@Service
public class UserServer {

    //订阅服务,订阅 接口的服务
    //是一个动态代理
    @Reference
    private UserApi userApi;

    public User findById(Integer id) {
        return userApi.findById(id);
    }

}
