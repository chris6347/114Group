package com.tanhua.server.service;

import com.tanhua.commons.templates.FaceTemplate;
import com.tanhua.commons.templates.OssTemplate;
import com.tanhua.commons.exception.TanHuaException;
import com.tanhua.domain.db.User;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.mongo.Friend;
import com.tanhua.domain.mongo.UserLike;
import com.tanhua.domain.mongo.Visitor;
import com.tanhua.domain.vo.*;
import com.tanhua.dubbo.api.*;
import com.tanhua.server.interceptor.UserHolder;
import com.tanhua.server.utils.GetAgeUtil;
import org.apache.commons.lang3.RandomUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserInfoService {

    @Reference
    private UserInfoApi userInfoApi;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private OssTemplate ossTemplate;

    @Autowired
    private FaceTemplate faceTemplate;

    @Reference
    private FriendApi friendApi;

    @Reference
    private UserLikeApi userLikeApi;

    @Reference
    private VisitorApi visitorApi;

    @Reference
    private RecommendUserApi recommendUserApi;

    // 首次登录添加基本资料
    public void loginRegInfo(UserInfoVo userInfoVo, String token) {
        Long loginUserId = UserHolder.getUserId();
        // vo转成db的pojo
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userInfoVo,userInfo);
        userInfo.setId(loginUserId);
        userInfo.setAge(Integer.parseInt(userInfoVo.getAge()));
        userInfoApi.add(userInfo);
    }

    // 添加头像
    public void uploadAvatar(MultipartFile headPhoto, String token) {
        User user = UserHolder.getUser();
        try {
            byte[] bytes = headPhoto.getBytes();
            boolean flag = faceTemplate.detect(bytes);
            if (!flag) {
                throw new TanHuaException(ErrorResult.faceError());
            }
            String originalFilename = headPhoto.getOriginalFilename();
            InputStream inputStream = headPhoto.getInputStream();
            String url = ossTemplate.upload(originalFilename, inputStream);
            UserInfo userInfo = new UserInfo();
            userInfo.setId(user.getId());
            userInfo.setAvatar(url);
            userInfoApi.update(userInfo);
        } catch (IOException e) {
            e.printStackTrace();
            throw new TanHuaException("上传头像失败");
        }
    }

    // 根据id查询userInfo
    public UserInfoVo getLoginUserInfo(String token) {
        User user = UserHolder.getUser();
        UserInfo info = userInfoApi.findById(user.getId());
        UserInfoVo vo = new UserInfoVo();
        BeanUtils.copyProperties(info,vo);
        return vo;
    }

    public void updateUserInfo(String token, UserInfoVo vo) {
        User user = UserHolder.getUser();
        UserInfo info = new UserInfo();
        BeanUtils.copyProperties(vo,info);
        // vo接收参数时 没有id和age值,需要自己添加
        info.setAge(GetAgeUtil.getAge(vo.getBirthday()));
        info.setId(user.getId());
        userInfoApi.update(info);
    }

    public void updateHeadPhoto(String token, MultipartFile headPhoto) {
        Long userId = UserHolder.getUserId();
        try {
            byte[] bytes = headPhoto.getBytes();
            boolean flag = faceTemplate.detect(bytes);
            if (!flag) {
                throw new TanHuaException(ErrorResult.faceError());
            }
            // 上传oss头像
            String url = ossTemplate.upload(headPhoto.getOriginalFilename(),headPhoto.getInputStream());
            // 获取老头像url
            UserInfo oldInfo = userInfoApi.findById(userId);
            String oldUrl = oldInfo.getAvatar();
            // 修改老头像
            UserInfo info = new UserInfo();
            info.setId(userId);
            info.setAvatar(url);
            userInfoApi.update(info);
            // 删除oss老头像
            ossTemplate.deleteFile(oldUrl);
        } catch (IOException e) {
            e.printStackTrace();
            throw new TanHuaException("更新头像失败");
        }
    }

    // 互相喜欢,喜欢,粉丝统计
    public CountsVo counts() {
        CountsVo vo = new CountsVo();
        Long userId = UserHolder.getUserId();
        // 互相喜欢数量 就是朋友数量
        Long eachLoveCount = friendApi.countByUserId(userId);
        // 喜欢数量
        Long loveCount = userLikeApi.countByUserId(userId);
        // 粉丝数量
        Long fanCount = userLikeApi.countFansById(userId);
        vo.setFanCount(fanCount);
        vo.setLoveCount(loveCount);
        vo.setEachLoveCount(eachLoveCount);
        return vo;
    }

    public PageResult<FriendVo> queryUserLikeList(Integer type,Long page,Long pagesize,String nickname) {
        Long userId = UserHolder.getUserId();
        PageResult pageResult = null;
        // 互相关注: mongo的user_friend 1
        if (type==1) {
            pageResult = friendApi.findPage(page,pagesize,userId);
            List<Friend> friends =  pageResult.getItems();
            if (!CollectionUtils.isEmpty(friends)) {
                List<Long> ids = friends.stream().map(Friend::getFriendId).collect(Collectors.toList());
                List<UserInfo> infoList = userInfoApi.findByBatchIds(ids);
                Map<Long, UserInfo> infoMap = infoList.stream().collect(Collectors.toMap(UserInfo::getId, userInfo -> userInfo));
                Map<Long, Double> scoreMap = recommendUserApi.findScoreBatch(ids, userId);
                List<FriendVo> items = friends.stream().map(friend -> {
                    FriendVo vo = new FriendVo();
                    BeanUtils.copyProperties(infoMap.get(friend.getFriendId()), vo);
                    Double score = scoreMap.get(infoMap.get(friend.getFriendId()).getId());
                    if (null == score) {
                        score = RandomUtils.nextDouble(50,100);
                    }
                    vo.setMatchRate(score.intValue());
                    return vo;
                }).collect(Collectors.toList());
                pageResult.setItems(items);
            }
        }
        // 谁看过我: mongo的visitors 4
        if (type==4) {
            pageResult = visitorApi.findPage(page,pagesize,userId);
            List<Visitor> visitors = pageResult.getItems();
            if (!CollectionUtils.isEmpty(visitors)) {
                List<Long> ids = visitors.stream().map(Visitor::getVisitorUserId).collect(Collectors.toList());
                List<UserInfo> infoList = userInfoApi.findByBatchIds(ids);
                Map<Long, UserInfo> infoMap = infoList.stream().collect(Collectors.toMap(UserInfo::getId, userInfo -> userInfo));
                Map<Long, Double> scoreBatch = recommendUserApi.findScoreBatch(ids, userId);
                List<FriendVo> vos = visitors.stream().map(visitor -> {
                    UserInfo userInfo = infoMap.get(visitor.getVisitorUserId());
                    FriendVo vo = new FriendVo();
                    BeanUtils.copyProperties(userInfo,vo);
                    Double score = scoreBatch.get(userInfo.getId());
                    if (null==score) {
                        score = RandomUtils.nextDouble(40,100);
                    }
                    vo.setMatchRate(score.intValue());
                    return vo;
                }).collect(Collectors.toList());
                pageResult.setItems(vos);
            }
        }
        // 我的喜欢: mongo的user_like 2 此时userId是自己 likeUserId是对方
        if (type==2) {
            pageResult = userLikeApi.findLikesPage(page,pagesize,userId);
            List<UserLike> likes = pageResult.getItems();
            if (!CollectionUtils.isEmpty(likes)) {
                List<Long> ids = likes.stream().map(UserLike::getLikeUserId).collect(Collectors.toList());
                List<UserInfo> infoList = userInfoApi.findByBatchIds(ids);
                Map<Long, UserInfo> infoMap = infoList.stream().collect(Collectors.toMap(UserInfo::getId, userInfo -> userInfo));
                Map<Long, Double> scoreBatch = recommendUserApi.findScoreBatch(ids, userId);
                List<FriendVo> vos = likes.stream().map(userLike -> {
                    UserInfo userInfo = infoMap.get(userLike.getLikeUserId());
                    FriendVo vo = new FriendVo();
                    BeanUtils.copyProperties(userInfo,vo);
                    Double score = scoreBatch.get(userInfo.getId());
                    if (null==score) {
                        score = RandomUtils.nextDouble(40,100);
                    }
                    vo.setMatchRate(score.intValue());
                    return vo;
                }).collect(Collectors.toList());
                pageResult.setItems(vos);
            }
        }
        // 粉丝: mongo的user_like 3 需要用到alreadyLove属性 此时likeUserId是自己  对方是userId
        if (type==3) {
            pageResult = userLikeApi.findFansPage(page,pagesize,userId);
            List<UserLike> fans =  pageResult.getItems();
            if (!CollectionUtils.isEmpty(fans)) {
                List<Long> ids = fans.stream().map(UserLike::getUserId).collect(Collectors.toList());
                List<UserInfo> infoList = userInfoApi.findByBatchIds(ids);
                Map<Long, UserInfo> infoMap = infoList.stream().collect(Collectors.toMap(UserInfo::getId, userInfo -> userInfo));
                Map<Long, Double> scoreBatch = recommendUserApi.findScoreBatch(ids, userId);
                // 查询我的喜欢
                List<UserLike> likes = userLikeApi.findLikes(userId);
                List<Long> likeUserIds = null;
                if (!CollectionUtils.isEmpty(likes)) {
                    likeUserIds = likes.stream().map(UserLike::getLikeUserId).collect(Collectors.toList());
                }
                // TODO stream流中的final变量问题
                List<Long> finalLikeUserIds = likeUserIds;
                List<FriendVo> vos = fans.stream().map(userLike -> {
                    UserInfo userInfo = infoMap.get(userLike.getUserId());
                    FriendVo vo = new FriendVo();
                    BeanUtils.copyProperties(userInfo, vo);
                    Double score = scoreBatch.get(userInfo.getId());
                    if (null==score) {
                        score = RandomUtils.nextDouble(40,100);
                    }
                    vo.setMatchRate(score.intValue());
                    vo.setAlreadyLove(false);
                    if (!CollectionUtils.isEmpty(finalLikeUserIds)&&finalLikeUserIds.contains(userInfo.getId())) {
                        vo.setAlreadyLove(true);
                    }
                    return vo;
                }).collect(Collectors.toList());
                pageResult.setItems(vos);
            }
        }
        return pageResult;
    }

}
