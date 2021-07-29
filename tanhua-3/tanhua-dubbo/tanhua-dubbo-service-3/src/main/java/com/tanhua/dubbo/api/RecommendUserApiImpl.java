package com.tanhua.dubbo.api;

import com.tanhua.domain.mongo.RecommendUser;
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
public class RecommendUserApiImpl implements RecommendUserApi {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public RecommendUser todayBest(Long userId) {
        Query query = new Query(Criteria.where("toUserId").is(userId));
        query.with(Sort.by(Sort.Order.desc("score")));
        return mongoTemplate.findOne(query, RecommendUser.class);
    }

    @Override
    public PageResult findPage(Long userId, Integer page, Integer pagesize) {
        Query query = new Query(Criteria.where("toUserId").is(userId));
        long count = mongoTemplate.count(query, RecommendUser.class);
        List<RecommendUser> recommendUserList = new ArrayList<>();
        if (count>0) {
            query.with(PageRequest.of(page-1,pagesize))
            .with(Sort.by(Sort.Order.desc("score")));
            recommendUserList = mongoTemplate.find(query,RecommendUser.class);
        }
        return PageResult.pageResult(page.longValue(), page.longValue(), recommendUserList,count);
    }

}