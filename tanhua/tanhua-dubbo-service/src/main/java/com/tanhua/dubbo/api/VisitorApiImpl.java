package com.tanhua.dubbo.api;

import com.tanhua.domain.db.RecommendUser;
import com.tanhua.domain.mongo.Visitor;
import com.tanhua.domain.vo.PageResult;
import org.apache.commons.lang3.RandomUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VisitorApiImpl implements VisitorApi{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Visitor> queryLast4Visitors(Long userId, Long lastQueryTime) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        // 是否需要时间条件
        // 有lastQueryTime时,查询的结果为上次查询之后的来访用户
        if (lastQueryTime != null ) {
            query.addCriteria(Criteria.where("date").gte(lastQueryTime));
        }
        query.with(Sort.by(Sort.Order.desc("date"))).limit(4);
        List<Visitor> visitors = mongoTemplate.find(query,Visitor.class);
        if (!CollectionUtils.isEmpty(visitors)) {
            List<Long> ids = visitors.stream().map(Visitor::getVisitorUserId).collect(Collectors.toList());
            query = new Query();
            query.addCriteria(Criteria.where("toUserId").is(userId).and("userId").in(ids));
            // 查询用户对应该访客的缘分值
            List<RecommendUser> recommendUsers = mongoTemplate.find(query, RecommendUser.class);
            Map<Long, Double> scoreMap = recommendUsers.stream().collect(Collectors.toMap(RecommendUser::getUserId, RecommendUser::getScore));
            visitors.forEach(visitor -> {
                Double score = scoreMap.get(visitor.getVisitorUserId());
                if (score == null) {
                    score = RandomUtils.nextDouble(70,90);
                }
                visitor.setScore(score);
            });
        }
        return visitors;
    }

    @Override
    public void save(Visitor visitor){
        mongoTemplate.save(visitor);
    }

    @Override
    public PageResult findPage(Long page, Long pagesize, Long userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        long count = mongoTemplate.count(query, Visitor.class);
        List<Visitor> visitors = new ArrayList<>();
        if (count>0) {
            query.with(PageRequest.of(page.intValue()-1,pagesize.intValue()))
                    .with(Sort.by(Sort.Order.desc("date")));
            visitors = mongoTemplate.find(query,Visitor.class);
        }
        return PageResult.pageResult(page,pagesize,visitors,count);
    }

}
