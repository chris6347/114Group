package com.tanhua.manage.service;

import com.tanhua.commons.utils.RelativeDateFormat;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.mongo.Comment;
import com.tanhua.domain.mongo.Publish;
import com.tanhua.domain.mongo.Video;
import com.tanhua.domain.vo.CommentVo;
import com.tanhua.domain.vo.MomentVo;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.domain.vo.VideoVo;
import com.tanhua.dubbo.api.CommentApi;
import com.tanhua.dubbo.api.PublishApi;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.dubbo.api.VideoApi;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Reference
    private UserInfoApi userInfoApi;

    @Reference
    private VideoApi videoApi;

    @Reference
    private PublishApi publishApi;

    @Reference
    private CommentApi commentApi;

    public PageResult<UserInfo> findPage(Long page,Long pagesize){
        return userInfoApi.findPage(page,pagesize);
    }

    public UserInfo findUserDetail(Long userId) {
        return userInfoApi.findById(userId);
    }

    // 获取当前用户所有视频分页列表
    public ResponseEntity findAllVideos(int page, int pagesize, Long userId) {
        PageResult pageResult = videoApi.findAllById((long) page,(long)pagesize,userId);
        List<Video> videos = pageResult.getItems();
        List<VideoVo> vos = new ArrayList<>();
        if (null != videos) {
            UserInfo info = userInfoApi.findById(userId);
            vos = videos.stream().map(video -> {
                VideoVo vo = new VideoVo();
                BeanUtils.copyProperties(info,vo);
                BeanUtils.copyProperties(video,vo);
                vo.setId(video.getId().toHexString());
                vo.setSignature(video.getText());
                vo.setCover(video.getPicUrl());
                return vo;
            }).collect(Collectors.toList());
        }
        pageResult.setItems(vos);
        return ResponseEntity.ok(pageResult);
    }

    public ResponseEntity findAllMovements(int page, int pagesize, Long userId, Integer publishState) {
        PageResult pageResult = publishApi.findAll(page,pagesize,userId,publishState);
        List<Publish> publishes = pageResult.getItems();
        List<MomentVo> vos = new ArrayList<>();
        if (null != publishes) {
            List<Long> ids = publishes.stream().map(Publish::getUserId).collect(Collectors.toList());
            List<UserInfo> infos = userInfoApi.findByBatchIds(ids);
            Map<Long, UserInfo> infoMap = infos.stream().collect(Collectors.toMap(UserInfo::getId, u -> u));
            vos = publishes.stream().map(publish -> {
                MomentVo vo = new MomentVo();
                BeanUtils.copyProperties(publish,vo);
                vo.setId(publish.getId().toHexString());
                vo.setImageContent(publish.getMedias().toArray(new String[0]));
                vo.setDistance("50米");
                vo.setCreateDate(RelativeDateFormat.format(new Date(publish.getCreated())));
                UserInfo info = infoMap.get(publish.getUserId());
                BeanUtils.copyProperties(info,vo);
                vo.setTags(StringUtils.split(info.getTags(),","));
                return vo;
            }).collect(Collectors.toList());
        }
        pageResult.setItems(vos);
        return ResponseEntity.ok(pageResult);
    }

    // 找某个动态
    public ResponseEntity findMovementById(String publishId) {
        Publish publish = publishApi.findById(publishId);
        MomentVo vo = new MomentVo();
        UserInfo info = userInfoApi.findById(publish.getUserId());
        if (info!=null) {
            BeanUtils.copyProperties(info,vo);
            vo.setTags(StringUtils.split(info.getTags(),","));
        }
        BeanUtils.copyProperties(publish,vo);
        vo.setId(publishId);
        vo.setDistance("-1米");
        vo.setImageContent(publish.getMedias().toArray(new String[0]));
        vo.setCreateDate(RelativeDateFormat.format(new Date(publish.getCreated())));
        return ResponseEntity.ok(vo);
    }

    public ResponseEntity findAllComments(int page, int pagesize, String messageID) {
        PageResult pageResult = commentApi.findPage(messageID,(long)page,(long)pagesize);
        List<Comment> comments = pageResult.getItems();
        List<CommentVo> vos = new ArrayList<>();
        if (null!=comments) {
            List<Long> ids = comments.stream().map(Comment::getUserId).collect(Collectors.toList());
            List<UserInfo> userInfos = userInfoApi.findByBatchIds(ids);
            Map<Long, UserInfo> infoMap = userInfos.stream().collect(Collectors.toMap(UserInfo::getId, u -> u));
            vos = comments.stream().map(comment -> {
                CommentVo vo = new CommentVo();
                BeanUtils.copyProperties(comment,vo);
                vo.setId(comment.getId().toHexString());
                vo.setCreateDate(RelativeDateFormat.format(new Date(comment.getCreated())));
                UserInfo userInfo = infoMap.get(comment.getUserId());
                BeanUtils.copyProperties(userInfo,vo);
                return vo;
            }).collect(Collectors.toList());
        }
        pageResult.setItems(vos);
        return ResponseEntity.ok(pageResult);
    }
}
