package com.tanhua.dubbo.api;

import com.tanhua.domain.db.UserInfo;

import java.util.List;

public interface UserInfoApi {

    void save(UserInfo info);

    void update(UserInfo info);

    UserInfo findById(Long loginUserId);

    List<UserInfo> findByBatchId(List<Long> ids);
}
