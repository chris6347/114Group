package com.itheima.service;

import com.itheima.dao.UserMapper;
import com.itheima.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private User user;

    @Autowired
    private UserMapper mapper;

    // 封装了redis的操作
    @Autowired
    private RedisTemplate redisTemplate;

    public User getUser(){
        return user;
    }

    public List<User> findAll(){
        return mapper.findAllUser();
    }

    public List<User> find(){
        String key = "user_list";
        redisTemplate.opsForValue().set(key,new ArrayList<User>());
        redisTemplate.opsForValue().get(key);
        // redis 存的类 必须要实现序列化
        // opsForValue 封装了key=value的操作
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 设置key 的序列化方式为字符串的序列化, string.getBytes()   序列化
                                        // new String(bytes[]) 反序列化

        return null;
    }

}
