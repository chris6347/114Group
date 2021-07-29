package com.tanhua.dubbo.api;

import com.tanhua.domain.mongo.FollowUser;

public interface FollowUserApi {

    void save(FollowUser followUser);

    void delete(FollowUser followUser);

}
