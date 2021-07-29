package com.tanhua.dubbo.api;

import com.tanhua.domain.db.BlackList;
import com.tanhua.domain.db.SwagQuery;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.vo.PageResult;

import java.util.List;

public interface UserInfoApi {

    //用户注册后完善个人信息
    void add(UserInfo userInfo);

    //更新用户信息
    void update(UserInfo userInfo);

    UserInfo findById(Long userId);

    List<UserInfo> findByBatchIds(List<Long> list);

    PageResult<UserInfo> findPage(Long page, Long pagesize);

    List<UserInfo> findSwag(SwagQuery query);

}
