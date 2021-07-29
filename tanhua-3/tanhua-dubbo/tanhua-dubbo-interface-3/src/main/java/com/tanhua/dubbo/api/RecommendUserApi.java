package com.tanhua.dubbo.api;

import com.tanhua.domain.mongo.RecommendUser;
import com.tanhua.domain.vo.PageResult;

public interface RecommendUserApi {
    RecommendUser todayBest(Long userId);

    PageResult findPage(Long userId, Integer page, Integer pagesize);
}
