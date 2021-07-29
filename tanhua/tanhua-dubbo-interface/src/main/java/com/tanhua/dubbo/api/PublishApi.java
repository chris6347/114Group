package com.tanhua.dubbo.api;

import com.tanhua.domain.mongo.Publish;
import com.tanhua.domain.vo.PageResult;
import org.bson.types.ObjectId;

public interface PublishApi {

    String add(Publish publish);

    PageResult findFriendPublishByTimeLine(Long userId, Long page, Long pageSize);

    PageResult findRecommendPublish(Long userId, Long page, Long pageSize);

    PageResult findMyPublishList(long loginUser_id, Long page, Long pagesize);

    Publish findById(String publishId);

    PageResult findAll(int page, int pagesize, Long userId, Integer publishState);

    void updateState(String id,Integer state);

}
