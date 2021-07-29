package com.itheima;

import com.itheima.dto.Order;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProducerTest {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Test
    public void testSendNormalMessage(){
        rocketMQTemplate.convertAndSend("basicTopic36","Hello RocketMQ");
        //rocketMQTemplate.syncSend("basicTopic36","Hello RocketMQ",30000);
    }

    @Test
    public void testOrderMessage(){
        // 构建订单
        List<Order> orderList = Order.buildOrders();
        // 添加队列选择器
        rocketMQTemplate.setMessageQueueSelector(new MessageQueueSelector() {
            // mqs 队列列表   msg 消费内容    arg 额外参数,发送消息时额外的参数
            // MessageQueue 就是选中的消息队列
            @Override
            public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                System.out.println("调用selectMessageQueue方法...");
                int queueSize = mqs.size();
                int id = Integer.parseInt(arg.toString()); // 订单id
                // 下标
                int idx = id % queueSize; // 1 0 - (size-1)
                return mqs.get(idx);
            }
        });
        for (Order order : orderList) {
            // p1:topic   p2:消息内容   p3:hashKey 区分唯一
            rocketMQTemplate.syncSendOrderly("basicTopic36",order,order.getId().toString());
        }
    }

    @Test
    public void testDelayMessage(){
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        org.springframework.messaging.Message<String> msg = new GenericMessage<String>("延迟消息:"+date);

        rocketMQTemplate.syncSend("delayMessageTopic",msg,3000,3);
    }

    @Test
    public void testClustering(){
        for (int i = 1; i <= 20; i++) {
            rocketMQTemplate.convertAndSend("ClusteringTopic","消息内容:"+i);
        }
    }

    @Test
    public void testBroadcasting(){
        rocketMQTemplate.convertAndSend("BroadcastTopic","广播消息");
    }

}
