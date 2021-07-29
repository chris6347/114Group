package com.tanhua.dubbo.api;

import com.tanhua.domain.mongo.Video;
import com.tanhua.domain.vo.PageResult;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@Service
public class VideoApiImpl implements VideoApi{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void postVideo(Video video) {
        video.setCreated(System.currentTimeMillis());
        mongoTemplate.insert(video);
    }

    @Override
    public PageResult findPage(Long page, Long pageSize) {
        Query query = new Query();
        long count = mongoTemplate.count(query, Video.class);
        if (count == 0) {
            return PageResult.pageResult(page,pageSize,null,count);
        }
        query.skip((page-1)*pageSize).limit(page.intValue()).with(Sort.by(Sort.Order.desc("created")));
        List<Video> videos = mongoTemplate.find(query, Video.class);
        return PageResult.pageResult(page,pageSize,videos,count);
    }

}
