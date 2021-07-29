package com.tanhua.server.service;

import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.tanhua.commons.exception.TanhuaException;
import com.tanhua.commons.templates.OssTemplate;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.mongo.FollowUser;
import com.tanhua.domain.mongo.Video;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.domain.vo.VideoVo;
import com.tanhua.dubbo.api.FollowUserApi;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.dubbo.api.VideoApi;
import com.tanhua.server.interceptor.UserHolder;
import org.apache.dubbo.config.annotation.Reference;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VideoService {

    @Reference
    private VideoApi videoApi;

    @Reference
    private UserInfoApi userInfoApi;

    @Reference
    private FollowUserApi followUserApi;

    @Autowired
    private OssTemplate ossTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private FastFileStorageClient client;

    @Autowired
    private FdfsWebServer fdfsWebServer;

    public void post(MultipartFile videoThumbnail, MultipartFile videoFile){
        try {
            String fmUrl = ossTemplate.upload(videoThumbnail.getOriginalFilename(), videoThumbnail.getInputStream());
            String filename = videoFile.getOriginalFilename();
            StorePath storePath = client.uploadFile(videoFile.getInputStream(), videoFile.getSize(), filename.substring(filename.lastIndexOf(".")+1), null);
            String videoUrl = fdfsWebServer.getWebServerUrl() + "/" + storePath.getFullPath();
            Video video = new Video();
            video.setVideoUrl(videoUrl);
            video.setPicUrl(fmUrl);
            video.setText("zwz出品");
            video.setUserId(UserHolder.getUserId());
            videoApi.postVideo(video);
        } catch (IOException e) {
            e.printStackTrace();
            throw new TanhuaException("上传失败");
        }
    }

    public PageResult<VideoVo> getVideo(Long page, Long pageSize) {
        PageResult pageResult = videoApi.findPage(page,pageSize);
        List<Video> videos = pageResult.getItems();
        if (CollectionUtils.isEmpty(videos)) {
            return null;
        }
        List<Long> ids = videos.stream().map(Video::getUserId).collect(Collectors.toList());
        List<UserInfo> infos = userInfoApi.findByBatchId(ids);
        Map<Long, UserInfo> infoMap = infos.stream().collect(Collectors.toMap(UserInfo::getId, u -> u));
        List<VideoVo> voList = videos.stream().map(video -> {
            VideoVo vo = new VideoVo();
            UserInfo userInfo = infoMap.get(video.getUserId());
            BeanUtils.copyProperties(userInfo, vo);
            BeanUtils.copyProperties(video, vo);
            vo.setSignature(video.getText());
            vo.setCover(video.getPicUrl());
            String key = "FOLLOW_USER_"+UserHolder.getUserId()+"_"+video.getUserId();
            if (redisTemplate.hasKey(key)) {
                vo.setHasFocus(1);
            }
            return vo;
        }).collect(Collectors.toList());
        pageResult.setItems(voList);
        return pageResult;
    }

    public void focusUser(Long userId) {
        Long loginUserId = UserHolder.getUserId();
        FollowUser followUser = new FollowUser();
        followUser.setUserId(loginUserId);
        followUser.setFollowUserId(userId);
        followUserApi.save(followUser);
        String key = "FOLLOW_USER_"+loginUserId+"_"+userId;
        redisTemplate.opsForValue().set(key,1);
    }

    public void unFocusUser(Long userId) {
        FollowUser followUser = new FollowUser();
        followUser.setUserId(UserHolder.getUserId());
        followUser.setFollowUserId(userId);
        followUserApi.delete(followUser);
        String key = "FOLLOW_USER_"+ UserHolder.getUserId() + "_" +userId;
        redisTemplate.delete(key);
    }

}


