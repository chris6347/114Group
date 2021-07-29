package com.tanhua.dubbo.api;

import com.tanhua.domain.mongo.Friend;
import com.tanhua.domain.vo.PageResult;

import java.util.List;

public interface FriendApi {

    void addFriend(Long userId,Long targetId);

    PageResult findPage(Long page, Long pagesize, Long userId);

    Long countByUserId(Long userId);

    List<Friend> findAllByUserId(Long queryUserId);

}
