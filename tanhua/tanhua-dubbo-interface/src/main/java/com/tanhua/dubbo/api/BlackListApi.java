package com.tanhua.dubbo.api;

import com.tanhua.domain.db.BlackList;
import com.tanhua.domain.vo.PageResult;

import java.util.List;

public interface BlackListApi {

    PageResult findPageByUserId(Long page, Long pageSize, Long userId);

    void removeBlackUser(Long userId, Long blackUserId);

    List<BlackList> findAllByUserId(Long queryUserId);

    void saveBlackUser(Long userId, Long unLoveUserId);

}
