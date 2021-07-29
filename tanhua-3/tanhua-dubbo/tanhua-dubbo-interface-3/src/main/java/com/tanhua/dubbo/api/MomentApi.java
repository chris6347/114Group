package com.tanhua.dubbo.api;

import com.tanhua.domain.mongo.Publish;
import com.tanhua.domain.vo.PageResult;

public interface MomentApi {

    void postMoment(Publish publish);

    PageResult queryFriendPublishList(Long userId, Long page, Long pageSize);

    PageResult recommendMoment(Long userId,Long page, Long pageSize);

}
