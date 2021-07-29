package com.tanhua.test;

import com.tanhua.domain.mongo.Comment;
import com.tanhua.domain.mongo.Publish;
import com.tanhua.domain.mongo.RecommendQuanzi;
import com.tanhua.dubbo.DubboServiceApplication;
import org.apache.commons.lang3.RandomUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest(classes = DubboServiceApplication.class)
@RunWith(SpringRunner.class)
public class MongoDB {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void addRecommendQuanzi(){
        //先查出有哪些动态
        List<Publish> publishList = mongoTemplate.find(new Query(),Publish.class);
        publishList.forEach(publish -> {
            ObjectId publishId = publish.getId();
            RecommendQuanzi quanzi = new RecommendQuanzi();
            quanzi.setCreated(System.currentTimeMillis());
            quanzi.setScore(RandomUtils.nextDouble(80,90));
            quanzi.setUserId(1L);
            quanzi.setPublishId(publishId);
            mongoTemplate.insert(quanzi);
        });
    }

    @Test
    public void addComment(){
        Comment comment = new Comment();
        comment.setId(new ObjectId());
        mongoTemplate.insert(comment,"quanzi_comment");
    }

}
