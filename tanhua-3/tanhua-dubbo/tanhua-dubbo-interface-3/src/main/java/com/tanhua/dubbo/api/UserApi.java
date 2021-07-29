package com.tanhua.dubbo.api;

import com.tanhua.domain.db.User;

public interface UserApi {

    User findByMobile(String phone);

    void save(User user);

}
