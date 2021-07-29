package com.tanhua.dubbo.api;

import com.tanhua.domain.mongo.UserLike;
import com.tanhua.domain.vo.PageResult;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserLikeApiImple implements UserLikeApi{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private FriendApi friendApi;

    @Override
    public Long countByUserId(Long userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoTemplate.count(query, UserLike.class);
    }

    @Override
    public Long countFansById(Long userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("likeUserId").is(userId));
        return mongoTemplate.count(query,UserLike.class);
    }

    @Override
    public void saveLike(Long userId,Long targetUserId){
        UserLike userLike = new UserLike();
        userLike.setUserId(userId);
        userLike.setLikeUserId(targetUserId);
        userLike.setCreated(System.currentTimeMillis());
        mongoTemplate.insert(userLike);
    }

    // 我的喜欢
    @Override
    public PageResult findLikesPage(Long page, Long pagesize, Long userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        long count = mongoTemplate.count(query,UserLike.class);
        List<UserLike> userLikes = new ArrayList<>();
        if (count>0) {
            query.skip((page-1)*pagesize).limit(pagesize.intValue())
                    .with(Sort.by(Sort.Order.desc("created")));
            userLikes = mongoTemplate.find(query,UserLike.class);
        }
        return PageResult.pageResult(page,pagesize,userLikes,count);
    }

    // 喜欢我的
    @Override
    public PageResult findFansPage(Long page, Long pagesize, Long userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("likeUserId").is(userId));
        long count = mongoTemplate.count(query, UserLike.class);
        List<UserLike> userLikes = new ArrayList<>();
        if (count>0) {
            query.skip((page-1)*pagesize).limit(pagesize.intValue())
                    .with(Sort.by(Sort.Order.desc("created")));
            userLikes = mongoTemplate.find(query,UserLike.class);
        }
        return PageResult.pageResult(page,pagesize,userLikes,count);
    }

    //所有我的喜欢
    @Override
    public List<UserLike> findLikes(Long userId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        return mongoTemplate.find(query,UserLike.class);
    }

    @Override
    public boolean fansLike(Long userId, Long fansId) {
        // 先判断对方是否也喜欢我
        Query query = new Query();
        query.addCriteria(Criteria.where("likeUserId").is(userId).and("userId").is(fansId));
        if (mongoTemplate.exists(query,UserLike.class)) {
            // 是则可以交友
            // 删除粉丝的喜欢的记录
            mongoTemplate.remove(query,UserLike.class);
            friendApi.addFriend(userId,fansId);
            return true;
        }
        UserLike userLike = new UserLike();
        userLike.setUserId(userId);
        userLike.setLikeUserId(fansId);
        userLike.setCreated(System.currentTimeMillis());
        mongoTemplate.insert(userLike);
        return false;
    }

}
