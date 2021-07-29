package com.tanhua.dubbo.api;

import com.tanhua.domain.db.UserInfo;
import com.tanhua.dubbo.mapper.UserInfoMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class UserInfoApiImpl implements UserInfoApi{

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public void save(UserInfo info) {
        userInfoMapper.insert(info);
    }

    @Override
    public void update(UserInfo info) {
        userInfoMapper.updateById(info);
    }

    @Override
    public UserInfo findById(Long loginUserId) {
        return userInfoMapper.selectById(loginUserId);
    }

    @Override
    public List<UserInfo> findByBatchId(List<Long> ids) {
        return userInfoMapper.selectBatchIds(ids);
    }

}
