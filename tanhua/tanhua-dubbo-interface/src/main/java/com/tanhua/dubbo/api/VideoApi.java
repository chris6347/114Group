package com.tanhua.dubbo.api;

import com.tanhua.domain.mongo.FollowUser;
import com.tanhua.domain.mongo.Video;
import com.tanhua.domain.vo.PageResult;

public interface VideoApi {

    void save(Video video);

    PageResult findPage(Long page, Long pagesize);

    void followUser(FollowUser followUser);

    void unfollowUser(FollowUser followUser);

    PageResult findAllById(Long page, Long pagesize, Long userId);

}
