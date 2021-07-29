package com.tanhua.server;

import com.tanhua.commons.properties.SmsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;

@SpringBootApplication
//@EnableConfigurationProperties({SmsProperties.class})
@EnableCaching  // 开启缓存注解
@EnableScheduling
public class TanhuaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TanhuaServerApplication.class, args);
    }

    @Resource
    public void changeSerialized(RedisTemplate redisTemplate){
        redisTemplate.setKeySerializer(new StringRedisSerializer());
    }

}
