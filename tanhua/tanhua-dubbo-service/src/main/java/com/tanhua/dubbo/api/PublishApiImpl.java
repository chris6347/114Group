package com.tanhua.dubbo.api;

import com.tanhua.domain.mongo.Friend;
import com.tanhua.domain.mongo.Publish;
import com.tanhua.domain.mongo.RecommendQuanzi;
import com.tanhua.domain.mongo.TimeLine;
import com.tanhua.domain.vo.PageResult;
import org.apache.dubbo.config.annotation.Service;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublishApiImpl implements PublishApi{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public String add(Publish publish) {
        // 生成动态id
        ObjectId publishId = new ObjectId();
        // 获取当前时间戳
        long timeMillis = System.currentTimeMillis();
        // 动态创建的时间
        publish.setCreated(timeMillis);
        // 动态的id
        publish.setId(publishId);
        mongoTemplate.insert(publish);

        // 添加自己的时间线表 quanzi_time_line_userId
        String collectionName = "quanzi_time_line_"+publish.getUserId();
        TimeLine timeLine = new TimeLine();
        timeLine.setPublishId(publishId);
        timeLine.setCreated(timeMillis);
        timeLine.setUserId(publish.getUserId());
        mongoTemplate.insert(timeLine,collectionName); // 动态创建表

        // 3. 查询好友列表
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(publish.getUserId()));
        List<Friend> friendList = mongoTemplate.find(query,Friend.class);

        // 4. 循环好友,添加好友的时间线表
        // ? 如果我有1000个好友,到第11天用mq来解决
        if (!CollectionUtils.isEmpty(friendList)) {
            for (Friend friend : friendList) {
                String friendCollectionName = "quanzi_time_line"+friend.getFriendId();
                TimeLine friendTimeLine = new TimeLine();
                friendTimeLine.setPublishId(publishId);
                friendTimeLine.setCreated(timeMillis);
                friendTimeLine.setUserId(publish.getUserId());
                mongoTemplate.insert(friendTimeLine,friendCollectionName); // 动态创建表
            }
        }
        return publishId.toHexString();
    }

    // 别看名字   这tm是查自己的动态
    // 根据userId 查询对应的TimeLine表,得出Publish的ObjectId再查Publish表
    @Override
    public PageResult findFriendPublishByTimeLine(Long userId, Long page, Long pageSize) {
        // 先查询自己的时间线表,凭借集合名
        String collectionName = "quanzi_time_line_" + userId;
        // 构建查询的条件
        Query timeLineQuery = new Query();
        // 查总数
        long total = mongoTemplate.count(timeLineQuery,collectionName);
        List<Publish> publishList = new ArrayList<>();
        // 总数>0,查询结果集,时间线表的结果
        if (total > 0) {
            // 发布时间降序查询
            timeLineQuery.with(Sort.by(Sort.Order.desc("created")));
            // 分页
            timeLineQuery.skip((page-1)*pageSize).limit(pageSize.intValue());
            // p2: 返回值类型
            List<TimeLine> timeLines = mongoTemplate.find(timeLineQuery,TimeLine.class,collectionName);
            if (!CollectionUtils.isEmpty(timeLines)) {
                // 获取动态的ids集合
                List<ObjectId> publishIds = timeLines.stream().map(TimeLine::getPublishId).collect(Collectors.toList());
                // 通过ids查询动态信息,按发布时间降序查询
                Query publishQuery = new Query();
                publishQuery.with(Sort.by(Sort.Order.desc("created")))
                    .addCriteria(Criteria.where("_id").in(publishIds));
                publishList = mongoTemplate.find(publishQuery,Publish.class);
            }
        }
        return PageResult.pageResult(page,pageSize,publishList,total);
        /*List<Friend> friends = findFriendsById(userId);
        Query query = new Query();
        query.with(Sort.by(Sort.Order.desc("created")))
                .with(PageRequest.of((int)page-1,(int)pageSize))
                .addCriteria(Criteria.where("userId").in(friends));
        List<Publish> publishes = mongoTemplate.find(query, Publish.class);
        mongoTemplate.count(query);
        return PageResult.pageResult(page,pageSize,publishes,);*/
    }

    @Override
    public PageResult findRecommendPublish(Long userId, Long page, Long pageSize) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        long count = mongoTemplate.count(query, RecommendQuanzi.class);
        List<Publish> publishList = new ArrayList<>();
        if (count > 0) {
            query.with(Sort.by(Sort.Order.desc("score"))).skip((page - 1) * pageSize).limit(pageSize.intValue());
            List<RecommendQuanzi> recommendQuanzis = mongoTemplate.find(query, RecommendQuanzi.class);
            if (!CollectionUtils.isEmpty(recommendQuanzis)) {
                List<ObjectId> publishIds = recommendQuanzis.stream().map(RecommendQuanzi::getPublishId).collect(Collectors.toList());
                Query publishQuery = new Query();
                publishQuery.addCriteria(Criteria.where("_id").in(publishIds))
                        .with(Sort.by(Sort.Order.desc("created")));
                publishList = mongoTemplate.find(publishQuery,Publish.class);
            }
        }
        return PageResult.pageResult(page,pageSize,publishList,count);
    }

    @Override
    public PageResult findMyPublishList(long loginUser_id, Long page, Long pagesize) {
        // 先查询登录用户的时间线表,按创建的时间降序
        String collectionName = "quanzi_time_line_"+loginUser_id;
        Query query = new Query();
        // 我的相册,是登录用户发布的动态
        query.addCriteria(Criteria.where("userId").is(loginUser_id));
        long total = mongoTemplate.count(query,collectionName);
        List<Publish> publishList = null ;
        if (total > 0) {
            query.with(Sort.by(Sort.Order.desc("created")))
                    .skip((page-1)*pagesize).limit(pagesize.intValue());
            List<TimeLine> timeLineList = mongoTemplate.find(query, TimeLine.class, collectionName);
            List<ObjectId> publishIds = timeLineList.stream().map(TimeLine::getPublishId).collect(Collectors.toList());
            Query publishQuery = new Query();
            publishQuery.addCriteria(Criteria.where("_id").in(publishIds)).with(Sort.by(Sort.Order.desc("created")));
            publishList = mongoTemplate.find(publishQuery, Publish.class);
        }
        return PageResult.pageResult(page,pagesize,publishList,total);
    }

    // 查询好友列表
    public List<Friend> findFriendsById(Long userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoTemplate.find(query,Friend.class);
    }

    @Override
    public Publish findById(String publishId) {
        return mongoTemplate.findById(new ObjectId(publishId),Publish.class);
    }

    @Override
    public PageResult findAll(int page, int pagesize, Long userId, Integer publishState) {
        Query query = new Query();
        if (null!=userId) {
            query.addCriteria(Criteria.where("userId").is(userId));
        }
        Long count = mongoTemplate.count(query,Publish.class);
        List<Publish> publishes = new ArrayList<>();
        if (count>0) {
            query.with(PageRequest.of(page-1,pagesize)).with(Sort.by(Sort.Order.desc("created")));
            publishes = mongoTemplate.find(query,Publish.class);
        }
        return PageResult.pageResult((long)page,(long)pagesize,publishes,count);
    }

    @Override
    public void updateState(String publishId, Integer state) {
        Query query = new Query(Criteria.where("_id").is(new ObjectId(publishId)));
        Update update = new Update();
        update.set("state",state);
        mongoTemplate.updateFirst(query,update,Publish.class);
    }

}
