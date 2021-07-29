package com.tanhua.manage.listener;

import com.tanhua.commons.templates.HuaWeiUGCTemplate;
import com.tanhua.domain.mongo.Publish;
import com.tanhua.dubbo.api.PublishApi;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
@RocketMQMessageListener(topic = "tanhua-publish",consumerGroup = "tanhua_publish_group")
public class PublishMessageListener implements RocketMQListener<String> {

    @Autowired
    private HuaWeiUGCTemplate huaWeiUGCTemplate;

    @Reference
    private PublishApi publishApi;

    @Override
    public void onMessage(String publishId) {
        Publish publish = publishApi.findById(publishId);
        boolean textCheck = false;
        boolean imageCheck = false;
        if (publish.getState()==0) {
            String textContent = publish.getTextContent();
            textCheck = huaWeiUGCTemplate.textContentCheck(textContent);
            if (textCheck && !CollectionUtils.isEmpty(publish.getMedias())) {
                String[] urls = publish.getMedias().toArray(new String[]{});
                imageCheck = huaWeiUGCTemplate.imageContentCheck(urls);
            }
        }
        int state = 2; // 人工复审
        if (textCheck&&imageCheck) {
            state = 1;
        }
        System.out.println("state===================="+state);
        publishApi.updateState(publishId,state);
    }

}
