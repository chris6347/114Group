package com.tanhua.dubbo.api;

import com.tanhua.domain.db.RecommendUser;
import com.tanhua.domain.vo.PageResult;

import java.util.List;
import java.util.Map;

public interface RecommendUserApi {

    RecommendUser todayBest(Long userId);

    PageResult<RecommendUser> findPage(Integer page, Integer pagesize, Long userId);

    Double findScore(Long userId, Long targetUserId);

    Map<Long,Double> findScoreBatch(List<Long> userIds, Long toUserId);

    void save(RecommendUser recommendUser);

}
