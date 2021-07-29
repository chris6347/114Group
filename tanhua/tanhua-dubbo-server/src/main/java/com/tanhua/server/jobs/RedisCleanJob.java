package com.tanhua.server.jobs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
public class RedisCleanJob {

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${tanhua.voiceCountPrefix}")
    private String voiceCountPrefix;

    @Scheduled(cron = "0/10 * * * * ?")
    public void cleanVoiceCountJob(){
        log.info("开始清理统计次数");
        Set<String> keys = redisTemplate.keys(voiceCountPrefix+"*");
        redisTemplate.delete(keys);
        log.info("清理完毕");
    }

}
