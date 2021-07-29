package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanhua.domain.db.BlackList;
import com.tanhua.domain.db.SwagQuery;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.mongo.Friend;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.domain.vo.UserLocationVo;
import com.tanhua.dubbo.mapper.UserInfoMapper;
import org.apache.commons.lang3.RandomUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserInfoApiImpl implements UserInfoApi{

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private BlackListApi blackListApi;

    @Autowired
    private FriendApi friendApi;

    @Autowired
    private UserLocationApi userLocationApi;

    @Override
    public void add(UserInfo userInfo) {
        userInfoMapper.insert(userInfo);
    }

    @Override
    public void update(UserInfo userInfo) {
        userInfoMapper.updateById(userInfo);
    }

    @Override
    public UserInfo findById(Long userId) {
        return userInfoMapper.selectById(userId);
    }

    @Override
    public List<UserInfo> findByBatchIds(List<Long> list) {
        return userInfoMapper.selectBatchIds(list);
    }

    @Override
    public PageResult<UserInfo> findPage(Long page, Long pagesize) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        Long count = Long.valueOf(userInfoMapper.selectCount(wrapper));
        IPage<UserInfo> iPage = new Page<>(page,pagesize);
        if (count>0) {
            userInfoMapper.selectPage(iPage,wrapper);
        }
        return PageResult.pageResult(page,pagesize,iPage.getRecords(),count);
    }

    @Override
    public List<UserInfo> findSwag(SwagQuery query) {
        // 找到黑名单所有人
        List<BlackList> blackPerson = blackListApi.findAllByUserId(query.getQueryUserId());
        List<Long> blackIds = blackPerson.stream().map(BlackList::getBlackUserId).collect(Collectors.toList());
        // 找到所有好友
        List<Friend> friends = friendApi.findAllByUserId(query.getQueryUserId());
        List<Long> friendIds = friends.stream().map(Friend::getFriendId).collect(Collectors.toList());
        // 查询
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.between("age",query.getMinAge(),query.getMaxAge()).eq("gender",query.getGender());
        if (!CollectionUtils.isEmpty(blackIds)) {
            wrapper.notIn("id",blackIds);
        }
        if (!CollectionUtils.isEmpty(friendIds)) {
            wrapper.notIn("id",friendIds);
        }
        IPage<UserInfo> page = new Page<>(query.getPage(), 20);
        userInfoMapper.selectPage(page,wrapper);
        List<UserInfo> userInfoList = page.getRecords();
        if (CollectionUtils.isEmpty(userInfoList)) {
            List<Long> managerIds = new ArrayList<>();
            for (long i = 1; i <= 20; i++) {
                managerIds.add(i);
            }
            return userInfoMapper.selectBatchIds(managerIds);
        }
        return userInfoList;
    }

}
