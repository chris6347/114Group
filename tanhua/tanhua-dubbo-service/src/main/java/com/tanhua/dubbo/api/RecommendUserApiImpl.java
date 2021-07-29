package com.tanhua.dubbo.api;

import com.tanhua.domain.db.RecommendUser;
import com.tanhua.domain.vo.PageResult;
import org.apache.commons.lang3.RandomUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RecommendUserApiImpl implements RecommendUserApi{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public RecommendUser todayBest(Long userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("toUserId").is(userId));
        // 查询结果可能有多个,需要按score降序排序查询,最大的在最上面,findOne直接获取最大分数的记录
        query.with(Sort.by(Sort.Order.desc("score")));
        return mongoTemplate.findOne(query,RecommendUser.class);
    }

    @Override
    public PageResult<RecommendUser> findPage(Integer page, Integer pagesize, Long userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("toUserId").is(userId));
        // 总条数
        long count = mongoTemplate.count(query, RecommendUser.class);
        List<RecommendUser> list = null;
        if (count > 0) {
            query.with(PageRequest.of(page-1, pagesize));
            list = mongoTemplate.find(query, RecommendUser.class);
        }
        return PageResult.pageResult(page.longValue(),pagesize.longValue(),list,count);
    }

    @Override
    public Double findScore(Long userId, Long targetUserId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(targetUserId).and("toUserId").is(userId));
        RecommendUser recommendUser = mongoTemplate.findOne(query,RecommendUser.class);
        if (null != recommendUser) {
            return recommendUser.getScore();
        }
        return RandomUtils.nextDouble(70,90);
    }

    @Override
    public Map<Long,Double> findScoreBatch(List<Long> userIds, Long toUserId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").in(userIds).and("toUserId").is(toUserId));
        List<RecommendUser> recommendUsers = mongoTemplate.find(query,RecommendUser.class);
        return recommendUsers.stream().collect(Collectors.toMap(RecommendUser::getUserId,recommendUser -> {
            Double score = recommendUser.getScore();
            if (score==null) {
                score = RandomUtils.nextDouble(70,90);
            }
            return score;
        }));
    }

    @Override
    public void save(RecommendUser recommendUser) {
        mongoTemplate.insert(recommendUser);
    }

}
