package com.itheima.listener;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@RocketMQMessageListener(topic = "delayMessageTopic" , consumerGroup = "delayMsgConsumer")
public class DelayMessageListener implements RocketMQListener<String> {
    @Override
    public void onMessage(String s) {
        System.out.println("延迟消息:"+s);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
}
