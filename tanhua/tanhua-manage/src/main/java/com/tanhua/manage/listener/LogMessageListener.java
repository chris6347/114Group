package com.tanhua.manage.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tanhua.manage.domain.Log;
import com.tanhua.manage.service.LogService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "tanhua_log",consumerGroup = "tanhua_log_consumer")
public class LogMessageListener implements RocketMQListener<String> {

    @Autowired
    private LogService logService;

    @Override
    public void onMessage(String message) {
        System.out.println(message);
        JSONObject msgMap = JSON.parseObject(message);
        Log log = new Log();
        log.setLogTime(msgMap.getString("log_time"));
        log.setType(msgMap.getString("type"));
        log.setUserId(msgMap.getLong("userId"));
        log.setEquipment(msgMap.getString("equipment"));
        log.setPlace(msgMap.getString("place"));
        logService.add(log);
    }

}
