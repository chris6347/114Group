package com.tanhua.server.service;

import com.tanhua.commons.exception.TanHuaException;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.mongo.Comment;
import com.tanhua.domain.vo.CommentVo;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.dubbo.api.CommentApi;
import com.tanhua.dubbo.api.PublishApi;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.server.interceptor.UserHolder;
import com.tanhua.commons.utils.RelativeDateFormat;
import org.apache.dubbo.config.annotation.Reference;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Reference
    private CommentApi commentApi;

    @Autowired
    private RedisTemplate redisTemplate;

    @Reference
    private UserInfoApi userInfoApi;

    @Reference
    private PublishApi publishApi;

    public long like(String publishId){
        Long userId = UserHolder.getUserId();
        String key = "publish_like_"+publishId+"_"+userId;
        if (redisTemplate.hasKey(key)) {
            throw new TanHuaException("不能重复点赞");
        }
        Comment comment = new Comment();
        comment.setCommentType(1);
        comment.setUserId(userId);
        comment.setTargetType(1);  // 1:对动态操作
        comment.setTargetId(new ObjectId(publishId));
        // 调用api 保存评论,获取返回的点赞数
        Long count = commentApi.save(comment);
        redisTemplate.opsForValue().set(key,1);
        return count;
    }

    public long disLike(String publishId) {
        Long userId = UserHolder.getUserId();
        String key = "publish_like_"+publishId+"_"+userId;
        if (redisTemplate.hasKey(key)) {
            throw new TanHuaException("已取消");
        }
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setTargetType(1);
        comment.setCreated(System.currentTimeMillis());
        comment.setTargetId(new ObjectId(publishId));
        comment.setCommentType(1);
        Long count = commentApi.remove(comment);
        redisTemplate.delete(key);
        return count;
    }

    public Long love(String publishId) {
        Long userId = UserHolder.getUserId();
        Comment comment = new Comment();
        comment.setCommentType(3);
        comment.setTargetType(1);
        comment.setTargetId(new ObjectId(publishId));
        comment.setUserId(userId);
        Long loveCount = commentApi.save(comment);
        String key = "publish_love_"+publishId+"_"+userId;
        redisTemplate.opsForValue().set(key,1);
        return loveCount;
    }

    public Long unLove(String publishId) {
        Long userId = UserHolder.getUserId();
        Comment comment = new Comment();
        comment.setCommentType(3);
        comment.setTargetType(1);
        comment.setTargetId(new ObjectId(publishId));
        comment.setUserId(userId);
        Long loveCount = commentApi.remove(comment);
        String key = "publish_love_"+publishId+"_"+userId;
        redisTemplate.delete(key);
        return loveCount;
    }

    public PageResult<CommentVo> findPage(String movementId, Long page, Long pagesize) {
        PageResult pageResult = commentApi.findPage(movementId,page,pagesize);
        List<Comment> comments = pageResult.getItems();
        List<CommentVo> voList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(comments)) {
            //  获取评论者的ids
            List<Long> ids = comments.stream().map(Comment::getUserId).collect(Collectors.toList());
            // 根据ids获取评论者信息
            List<UserInfo> userInfos = userInfoApi.findByBatchIds(ids);
            // 评论者信息和对应的id撞到map里
            Map<Long,UserInfo> infoMap = userInfos.stream().collect(Collectors.toMap(UserInfo::getId, userInfo -> userInfo));
            // 构建vo
            voList = comments.stream().map(comment -> {
                CommentVo vo = new CommentVo();
                BeanUtils.copyProperties(comment,vo);
                vo.setCreateDate(RelativeDateFormat.format(new Date(comment.getCreated())));
                vo.setId(comment.getId().toHexString());
                BeanUtils.copyProperties(infoMap.get(comment.getUserId()), vo);
                String key = "comment_like_"+vo.getId()+"_"+UserHolder.getUserId();
                vo.setHasLiked(0);
                if (redisTemplate.hasKey(key)) {
                    vo.setHasLiked(1);
                }
                return vo;
            }).collect(Collectors.toList());
        }
        pageResult.setItems(voList);
        return pageResult;
    }

    public void add(String movementId, String userComment) {
        Long userId = UserHolder.getUserId();
        Comment comment = new Comment();
        comment.setTargetId(new ObjectId(movementId));
        comment.setUserId(userId);
        comment.setContent(userComment);
        comment.setCommentType(2);
        comment.setTargetType(1);
        commentApi.save(comment);
    }

    public Long likeComment(String commentId) {
        Long userId = UserHolder.getUserId();
        Comment comment = new Comment();
        comment.setCommentType(1);
        comment.setTargetType(3);
        comment.setUserId(userId);
        comment.setTargetId(new ObjectId(commentId));
        Long likeCount = commentApi.likeComment(comment);
        String key = "comment_like_"+commentId+"_"+userId;
        redisTemplate.opsForValue().set(key,1);
        return likeCount;
    }

    public Long disLikeComment(String commentId) {
        Long userId = UserHolder.getUserId();
        Comment comment = new Comment();
        comment.setTargetId(new ObjectId(commentId));
        comment.setUserId(userId);
        comment.setCommentType(1);
        Long likeCount = commentApi.disLikeComment(comment);
        String key = "comment_like_"+commentId+"_"+userId;
        redisTemplate.delete(key);
        return likeCount;
    }

}
