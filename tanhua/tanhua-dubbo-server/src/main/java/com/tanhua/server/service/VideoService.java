package com.tanhua.server.service;

import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.tanhua.commons.templates.OssTemplate;
import com.tanhua.commons.exception.TanHuaException;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.mongo.FollowUser;
import com.tanhua.domain.mongo.Video;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.domain.vo.VideoVo;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.dubbo.api.VideoApi;
import com.tanhua.server.interceptor.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VideoService {

    @Reference
    private VideoApi videoApi;

    @Autowired
    private FastFileStorageClient client;

    @Autowired
    private FdfsWebServer fdfsWebServer;

    @Autowired
    private OssTemplate ossTemplate;

    @Reference
    private UserInfoApi userInfoApi;

    @Autowired
    private RedisTemplate redisTemplate;

    @CacheEvict(value = "videoList" , allEntries = true)
    // 上传小视频
    public void post(MultipartFile videoThumbnail, MultipartFile videoFile) {
        // 封面要上传到oss
        try {
            String picUrl = ossTemplate.upload(videoThumbnail.getOriginalFilename(), videoThumbnail.getInputStream());
            // 视频文件要上传fastdfs
            String videoFileName = videoFile.getOriginalFilename();
            // 小视频的后缀
            String suffix = videoFileName.substring(videoFileName.lastIndexOf(".")+1);
            // 上传视频到fastDFS
            StorePath storePath = client.uploadFile(videoFile.getInputStream(),videoFile.getSize(),suffix,null);
            // 小视频的完整路径,真实开发下要区分开服务地址和文件的相对路径
            // 真实开发下应该存storePath.getFullPath()
            String videoPathUrl = fdfsWebServer.getWebServerUrl()+"/"+storePath.getFullPath();
            // 构建video对象
            Video video = new Video();
            video.setVideoUrl(videoPathUrl);
            video.setPicUrl(picUrl);
            video.setUserId(UserHolder.getUserId());
            video.setCreated(System.currentTimeMillis());
            video.setText("曾哥出品");
            videoApi.save(video);
        } catch (IOException e) {
            log.error("上传小视频封面图片失败",e);
            throw new TanHuaException("上传小视频失败");
        }
    }
    @Cacheable(value = "videoList" , key = "#page + '_' + #pageSize")
    public PageResult getVideoList(Long page,Long pageSize){
        return videoApi.findPage(page,pageSize);
    }


    public PageResult<VideoVo> findPage(Long page, Long pagesize) {
        log.info("从数据库查询小视频列表...");
        PageResult pageResult = getVideoList(page,pagesize);
        List<Video> videoList = pageResult.getItems();
        if (!CollectionUtils.isEmpty(videoList)) {
            List<Long> ids = videoList.stream().map(Video::getUserId).collect(Collectors.toList());
            List<UserInfo> userInfos = userInfoApi.findByBatchIds(ids);
            Map<Long, UserInfo> userInfoMap = userInfos.stream().collect(Collectors.toMap(UserInfo::getId, userInfo -> userInfo));
            List<VideoVo> voList = videoList.stream().map(video -> {
                VideoVo vo = new VideoVo();
                BeanUtils.copyProperties(userInfoMap.get(video.getUserId()),vo);
                BeanUtils.copyProperties(video,vo);
                vo.setCover(video.getPicUrl());
                vo.setId(video.getId().toHexString());
                vo.setSignature(video.getText());
                Long userId = UserHolder.getUserId();
                vo.setHasLiked(0);
                vo.setHasFocus(0);
                String key = "follow_user_"+vo.getUserId()+"_"+userId;
                if (redisTemplate.hasKey(key)) {
                    vo.setHasFocus(1);
                }
                key = "video_like_"+vo.getId()+"_"+userId;
                if (redisTemplate.hasKey(key)) {
                    vo.setHasLiked(1);
                }
                return vo;
            }).collect(Collectors.toList());
            pageResult.setItems(voList);
        }
        return pageResult;
    }

    public void followUser(Long targetUserId) {
        Long userId = UserHolder.getUserId();
        FollowUser followUser = new FollowUser();
        followUser.setFollowUserId(targetUserId);
        followUser.setUserId(userId);
        videoApi.followUser(followUser);
        String key = "follow_user_"+targetUserId+"_"+userId;
        redisTemplate.opsForValue().set(key,1);
    }

    public void unfollowUser(Long targetUserId) {
        // 取消关注需要 userId  targetUserId
        Long userId = UserHolder.getUserId();
        FollowUser followUser = new FollowUser();
        followUser.setUserId(userId);
        followUser.setFollowUserId(targetUserId);
        videoApi.unfollowUser(followUser);
        String key = "follow_user_"+targetUserId+"_"+userId;
        redisTemplate.delete(key);
    }

}
