package com.itheima.listener;

import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * RocketMQ监听器
 *  1、实现RocketMQListener接口
 *  2、实现onMessage方法，完成业务处理
 *  3、需要交给容器管理：@Component
 *  4、在监听器上通过注解配置，需要监听的topic和消费者的组名
 *
 *  consumeMode: 顺序消息.
 *  消息消费者端:通过多线程获取消息的
 *  获取的消息顺序:保持一致
 *  ConsumeMode.ORDERLY 同步消费
 *  ConsumeMode.CONCURRENTLY 并发消费
 */
@Component
@RocketMQMessageListener(
        topic = "basicTopic36",consumerGroup = "orderlyGroup",consumeMode = ConsumeMode.ORDERLY
)
public class BaseMessageListener implements RocketMQListener<String> {


    /**
     * 当获取到中间中，最新消息时候
     * 自动的调用onMessage方法
     * 参数：发送的消息内容
     */
    @Override
    public void onMessage(String message) {
        String name = Thread.currentThread().getName();
        System.out.println(name+"顺序消息: " + message);
    }
}

