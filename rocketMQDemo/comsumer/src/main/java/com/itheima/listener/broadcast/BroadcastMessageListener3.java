package com.itheima.listener.broadcast;

import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "BroadcastTopic",consumerGroup = "broadcast",messageModel = MessageModel.BROADCASTING)
public class BroadcastMessageListener3 implements RocketMQListener<String> {
    @Override
    public void onMessage(String s) {
        System.out.println("Broadcast模式3:"+s);
    }
}
