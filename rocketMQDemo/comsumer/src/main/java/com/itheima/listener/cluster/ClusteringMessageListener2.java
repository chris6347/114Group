package com.itheima.listener.cluster;

import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "ClusteringTopic",consumerGroup = "clustering",messageModel = MessageModel.CLUSTERING)
public class ClusteringMessageListener2 implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        System.out.println("Cluster模式2:"+message);
    }
}