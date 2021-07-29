package com.itheima.listener.broadcast;

import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component// 注册到spring容器,消费者监听broker,要指定消息的数据类型
@RocketMQMessageListener(topic = "BroadcastTopic",consumerGroup = "broadcast",messageModel = MessageModel.BROADCASTING)
// 泛型对应消息类型
// Topic主题,消息的分类,在这里指定这个监听器(消费者)只会消费这个topic下的消息
public class BroadcastMessageListener1 implements RocketMQListener<String> {
    @Override
    public void onMessage(String s) {
        System.out.println("Broadcast模式1:"+s);
    }
}
