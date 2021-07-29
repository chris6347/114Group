package com.tanhua.dubbo.api;

import com.tanhua.domain.mongo.Friend;
import com.tanhua.domain.mongo.Publish;
import com.tanhua.domain.mongo.RecommendQuanzi;
import com.tanhua.domain.mongo.TimeLine;
import com.tanhua.domain.vo.PageResult;
import org.apache.dubbo.config.annotation.Service;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MomentApiImpl implements MomentApi{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void postMoment(Publish publish) {
        mongoTemplate.insert(publish);

        Query query= new Query(Criteria.where("userId").is(publish.getUserId()));
        List<Friend> friendList = mongoTemplate.find(query, Friend.class);
        List<Long> friendIds = friendList.stream().map(Friend::getFriendId).collect(Collectors.toList());
        friendIds.add(publish.getUserId());
        for (Long friendId : friendIds) {
            TimeLine timeLine = new TimeLine();
            BeanUtils.copyProperties(publish,timeLine);
            timeLine.setPublishId(publish.getId());
            mongoTemplate.insert(timeLine,"quanzi_time_line_"+friendId);
        }
    }

    @Override
    public PageResult queryFriendPublishList(Long userId, Long page, Long pageSize) {
        Query query = new Query();
        String timeLineCollectionName = "quanzi_time_line_" + userId;
        long count = mongoTemplate.count(query,timeLineCollectionName);
        List<Publish> publishes = new ArrayList<>();
        if (count==0) {
            return PageResult.pageResult(page,pageSize,publishes,count);
        }
        query.with(PageRequest.of(page.intValue()-1,pageSize.intValue()))
        .with(Sort.by(Sort.Order.desc("created")));
        // p2:指定返回值类型
        List<TimeLine> timeLines = mongoTemplate.find(query, TimeLine.class, timeLineCollectionName);
        List<ObjectId> objIds = timeLines.stream().map(TimeLine::getPublishId).collect(Collectors.toList());
        Query publishQuery = new Query();
        publishQuery.addCriteria(Criteria.where("_id").in(objIds));
        publishes = mongoTemplate.find(publishQuery, Publish.class);
        return PageResult.pageResult(page,pageSize,publishes,count);
    }

    @Override
    public PageResult recommendMoment(Long userId,Long page, Long pageSize) {
        Query query = new Query(Criteria.where("userId").is(userId));
        long count = mongoTemplate.count(query, RecommendQuanzi.class);
        List<Publish> publishes = new ArrayList<>();
        if (count==0) {
            return PageResult.pageResult(page,pageSize,publishes,count);
        }
        query.with(PageRequest.of(page.intValue()-1,pageSize.intValue()))
                .with(Sort.by(Sort.Order.desc("created")));
        List<RecommendQuanzi> list = mongoTemplate.find(query,RecommendQuanzi.class);
        List<ObjectId> publishIds = list.stream().map(RecommendQuanzi::getPublishId).collect(Collectors.toList());
        Query publishQuery = new Query();
        publishQuery.addCriteria(Criteria.where("_id").in(publishIds));
        publishes = mongoTemplate.find(publishQuery,Publish.class);
        publishes.sort((publish1, publish2) -> {
            return publish2.getCreated().intValue() - publish1.getCreated().intValue();
        });
        return PageResult.pageResult(page,pageSize,publishes,count);
    }

}
