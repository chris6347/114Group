package com.tanhua.server.service;

import com.tanhua.commons.exception.TanHuaException;
import com.tanhua.commons.templates.HuanXinTemplate;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.mongo.Comment;
import com.tanhua.domain.mongo.Friend;
import com.tanhua.domain.vo.ContactVo;
import com.tanhua.domain.vo.MessageVo;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.dubbo.api.CommentApi;
import com.tanhua.dubbo.api.FriendApi;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.server.interceptor.UserHolder;
import com.tanhua.commons.utils.RelativeDateFormat;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class IMService {

    @Reference
    private FriendApi friendApi;

    @Reference
    private UserInfoApi userInfoApi;

    @Reference
    private CommentApi commentApi;

    @Autowired
    private HuanXinTemplate huanXinTemplate;

    public void addContacts(Integer userId) {
        Long loginUserId = UserHolder.getUserId();
        if (userId.longValue()==loginUserId) {
            throw new TanHuaException("不能添加自己为好友");
        }
        // 好友关系写入mongoDB
        friendApi.addFriend(loginUserId,userId.longValue());
        // 好友关系写入环信
        huanXinTemplate.makeFriends(loginUserId,userId.longValue());
    }

    public PageResult<ContactVo> findFriendPage(Long page, Long pagesize, String keyword) {
        //根据登录用户id,从mongoDB中分页查询好友ids
        Long userId = UserHolder.getUserId();
        PageResult pageResult = friendApi.findPage(page,pagesize,userId);
        //根据ids从mysql中查询对应的userInfo
        List<Friend> friendList = pageResult.getItems();
        List<ContactVo> vos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(friendList)) {
            List<Long> ids = friendList.stream().map(Friend::getFriendId).collect(Collectors.toList());
            List<UserInfo> userInfos = userInfoApi.findByBatchIds(ids);
            for (UserInfo userInfo : userInfos) {
                ContactVo vo = new ContactVo();
                BeanUtils.copyProperties(userInfo,vo);
                vo.setUserId(userInfo.getId().toString());
                vos.add(vo);
            }
        }
        //返回pageResult items=ContactVo
        pageResult.setItems(vos);
        return pageResult;
    }

    // 被谁评论,喜欢,点赞 的列表
    public PageResult<MessageVo> messageCommentList(Integer commentType, Long page, Long pagesize) {
        Long userId = UserHolder.getUserId();
        PageResult pageResult = commentApi.findPageByCommentType(UserHolder.getUserId(),commentType,page,pagesize);
        List<Comment> comments = pageResult.getItems();
        System.out.println(comments);
        List<MessageVo> vos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(comments)) {
            // comment 的 targetUserId 就是登录用户自己
            // 获取评论者ids
            List<Long> ids = comments.stream().map(Comment::getUserId).collect(Collectors.toList());
            // 根据ids找到对应的评论者userInfo
            List<UserInfo> infos = userInfoApi.findByBatchIds(ids);
            Map<Long,UserInfo> infoMap = infos.stream().collect(Collectors.toMap(UserInfo::getId,userInfo -> userInfo));
            vos = comments.stream().map(comment -> {
                MessageVo vo = new MessageVo();
                BeanUtils.copyProperties(infoMap.get(comment.getUserId()),vo);
                vo.setCreateDate(RelativeDateFormat.format(new Date(comment.getCreated())));
                vo.setId(comment.getUserId().toString());
                return vo;
            }).collect(Collectors.toList());
        }
        pageResult.setItems(vos);
        return pageResult;
    }
}
