package com.tanhua.server.service;

import com.alibaba.fastjson.JSON;
import com.tanhua.commons.exception.TanhuaException;
import com.tanhua.domain.db.User;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.vo.UserInfoVo;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.server.interceptor.UserHolder;
import com.tanhua.server.utils.GetAgeUtil;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserInfoService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Reference
    private UserInfoApi userInfoApi;

    public UserInfoVo findUserInfo(Long userId) {
        User user = UserHolder.getUser();
        Long loginUserId = user.getId();
        if (null != userId) {
            loginUserId = userId;
        }
        UserInfo info = userInfoApi.findById(loginUserId);
        UserInfoVo vo = new UserInfoVo();
        BeanUtils.copyProperties(info,vo);
        vo.setAge(info.getAge().toString());
        return vo;
    }


    public void updateUserInfo(UserInfoVo vo) {
        User user = UserHolder.getUser();
        Long userId = user.getId();
        UserInfo info = new UserInfo();
        BeanUtils.copyProperties(vo,info);
        info.setAge(GetAgeUtil.getAge(vo.getBirthday()));
        info.setId(userId);
        userInfoApi.update(info);
    }
}
