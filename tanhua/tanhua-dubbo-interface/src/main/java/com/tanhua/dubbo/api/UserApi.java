package com.tanhua.dubbo.api;

import com.tanhua.domain.db.User;

public interface UserApi {

    // 添加用户
    Long save(User user);

    // 通过手机号码查询
    User findByMobile(String mobile);

    void updatePhone(Long id, String phone);

}
