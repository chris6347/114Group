package com.tanhua.dubbo.api;

import com.tanhua.domain.mongo.UserLike;
import com.tanhua.domain.vo.PageResult;

import java.util.List;

public interface UserLikeApi {

    Long countByUserId(Long userId);

    Long countFansById(Long userId);

    void saveLike(Long userId,Long targetUserId);

    PageResult findLikesPage(Long page, Long pagesize, Long userId);

    PageResult findFansPage(Long page, Long pagesize, Long userId);

    List<UserLike> findLikes(Long userId);

    boolean fansLike(Long userId, Long fansId);

}
