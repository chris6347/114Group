package com.tanhua;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;

@SpringBootApplication
public class TanhuaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TanhuaServerApplication.class,args);
    }

    // 解决redis客户端乱码问题
    @Resource
    public void setKeySerializer(RedisTemplate redisTemplate){
        redisTemplate.setKeySerializer(new StringRedisSerializer());
    }

}
