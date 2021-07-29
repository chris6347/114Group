package com.tanhua.dubbo.api;

import com.tanhua.domain.mongo.Friend;
import com.tanhua.domain.vo.PageResult;
import org.apache.commons.lang3.StringUtils;
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
public class FriendApiImpl implements FriendApi {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void addFriend(Long userId, Long targetId) {
        // 添加好友需要我加你你加我两条记录
        Friend friend = new Friend();
        friend.setUserId(userId);
        friend.setFriendId(targetId);
        friend.setCreated(System.currentTimeMillis());
        mongoTemplate.insert(friend);

        friend.setId(null);
        friend.setUserId(targetId);
        friend.setFriendId(userId);
        friend.setCreated(System.currentTimeMillis());
        mongoTemplate.insert(friend);
    }

    @Override
    public PageResult findPage(Long page, Long pagesize, Long userId) {
        Query query= new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        long total = mongoTemplate.count(query,Friend.class);
        List<Friend> list = new ArrayList<>();
        if (total>0) {
            query.with(PageRequest.of(page.intValue()-1,pagesize.intValue()))
                    .with(Sort.by(Sort.Order.desc("created")));
            list = mongoTemplate.find(query,Friend.class);
        }
        return PageResult.pageResult(page,pagesize,list,total);
    }

    @Override
    public Long countByUserId(Long userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoTemplate.count(query,Friend.class);
    }

    @Override
    public List<Friend> findAllByUserId(Long queryUserId) {
        Query query = new Query(Criteria.where("userId").is(queryUserId));
        return mongoTemplate.find(query,Friend.class);
    }

}
