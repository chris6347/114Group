package com.tanhua.server.service;

import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.mongo.RecommendUser;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.domain.vo.RecommendUserQueryParam;
import com.tanhua.domain.vo.RecommendUserVo;
import com.tanhua.dubbo.api.RecommendUserApi;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.server.interceptor.UserHolder;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RecommendUserService {

    @Reference
    private RecommendUserApi recommendUserApi;

    @Reference
    private UserInfoApi userInfoApi;

    public RecommendUserVo todayBest() {
        Long userId = UserHolder.getUserId();
        RecommendUser recommendUser = recommendUserApi.todayBest(userId);
        if (null == recommendUser) {
            recommendUser = new RecommendUser();
            recommendUser.setUserId(1L);
            recommendUser.setScore(RandomUtils.nextDouble(60,80));
        }
        UserInfo userInfo = userInfoApi.findById(recommendUser.getUserId());
        RecommendUserVo vo = new RecommendUserVo();
        BeanUtils.copyProperties(userInfo,vo);
        BeanUtils.copyProperties(recommendUser,vo);
        vo.setFateValue(recommendUser.getScore().longValue());
        vo.setTags(StringUtils.split(userInfo.getTags(),","));
        return vo;
    }

    public PageResult<RecommendUserVo> recommendList(RecommendUserQueryParam queryParam) {
        Long userId = UserHolder.getUserId();
        PageResult pageResult  = recommendUserApi.findPage(userId,queryParam.getPage(),queryParam.getPagesize());
        List<RecommendUser> recommendUserList = pageResult.getItems();
        if (CollectionUtils.isEmpty(recommendUserList)) {
            recommendUserList = getDefaultRecommendUsers();
        }
        List<Long> ids = recommendUserList.stream().map(RecommendUser::getUserId).collect(Collectors.toList());
        List<UserInfo> userInfoList = userInfoApi.findByBatchId(ids);
        Map<Long, UserInfo> infoMap = userInfoList.stream().collect(Collectors.toMap(UserInfo::getId, u -> u));
        List<RecommendUserVo> voList = recommendUserList.stream().map(recommendUser -> {
            RecommendUserVo vo = new RecommendUserVo();
            UserInfo info = infoMap.get(recommendUser.getUserId());
            BeanUtils.copyProperties(recommendUser, vo);
            BeanUtils.copyProperties(info, vo);
            vo.setFateValue(recommendUser.getScore().longValue());
            vo.setTags(StringUtils.split(info.getTags(), ","));
            return vo;
        }).collect(Collectors.toList());
        pageResult.setItems(voList);
        return pageResult;
    }

    private List<RecommendUser> getDefaultRecommendUsers() {
        List<RecommendUser> list = new ArrayList<>();
        for (long i = 1; i <= 10; i++) {
            RecommendUser recommendUser = new RecommendUser();
            recommendUser.setUserId(i);
            recommendUser.setScore(RandomUtils.nextDouble(60,80));
            list.add(recommendUser);
        }
        return list;
    }


}
