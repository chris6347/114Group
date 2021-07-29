package com.tanhua.dubbo.api;

import com.tanhua.domain.mongo.FollowUser;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@Service
public class FollowUserApiImpl implements FollowUserApi {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(FollowUser followUser) {
        followUser.setCreated(System.currentTimeMillis());
        mongoTemplate.insert(followUser);
    }

    @Override
    public void delete(FollowUser followUser) {
        Query query = new Query(Criteria.where("userId").is(followUser.getUserId()).and("followUserId").is(followUser.getFollowUserId()));
        mongoTemplate.remove(query,FollowUser.class);
    }

}
