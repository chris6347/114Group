package com.tanhua.dubbo.api;

import com.tanhua.domain.mongo.FollowUser;
import com.tanhua.domain.mongo.Video;
import com.tanhua.domain.vo.PageResult;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

@Service
public class VideoApiImpl implements VideoApi{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(Video video) {
        mongoTemplate.insert(video);
    }

    @Override
    public PageResult findPage(Long page, Long pagesize) {
        Query query = new Query();
        long count = mongoTemplate.count(query,Video.class);
        List<Video> videos = new ArrayList<>();
        if (count>0) {
            query.with(Sort.by(Sort.Order.desc("created"))).skip((page - 1) * pagesize).limit(pagesize.intValue());
            videos = mongoTemplate.find(query, Video.class);
        }
        return PageResult.pageResult(page,pagesize,videos,count);
    }

    @Override
    public void followUser(FollowUser followUser) {
        followUser.setCreated(System.currentTimeMillis());
        mongoTemplate.save(followUser);
    }

    @Override
    public void unfollowUser(FollowUser followUser) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(followUser.getUserId())
                .and("followUserId").is(followUser.getFollowUserId()));
        mongoTemplate.remove(query,FollowUser.class);
    }

    @Override
    public PageResult findAllById(Long page, Long pagesize, Long userId) {
        Query query = new Query();
        if (userId!=null) {
            query.addCriteria(Criteria.where("userId").is(userId));
        }
        long count = mongoTemplate.count(query, Video.class);
        List<Video> videos = new ArrayList<>();
        if (count>0) {
            query.with(PageRequest.of(page.intValue()-1,pagesize.intValue()))
                .with(Sort.by(Sort.Order.desc("created")));
            videos = mongoTemplate.find(query, Video.class);
        }
        return PageResult.pageResult(page,pagesize,videos,count);
    }

}
