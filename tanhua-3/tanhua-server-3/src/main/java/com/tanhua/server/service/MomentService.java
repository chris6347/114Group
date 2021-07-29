package com.tanhua.server.service;

import com.tanhua.commons.exception.TanhuaException;
import com.tanhua.commons.templates.OssTemplate;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.mongo.Publish;
import com.tanhua.domain.vo.MomentVo;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.domain.vo.PublishVo;
import com.tanhua.dubbo.api.MomentApi;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.server.interceptor.UserHolder;
import com.tanhua.server.utils.RelativeDateFormat;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MomentService {

    @Reference
    private MomentApi momentApi;

    @Autowired
    private OssTemplate ossTemplate;

    @Reference
    private UserInfoApi userInfoApi;

    public void postMoment(PublishVo vo, MultipartFile[] imageContent) {
        Long userId = UserHolder.getUserId();
        try {
            List<String> urls = new ArrayList<>();
            if (!ArrayUtils.isEmpty(imageContent)) {
                for (MultipartFile multipartFile : imageContent) {
                    String url = ossTemplate.upload(multipartFile.getOriginalFilename(), multipartFile.getInputStream());
                    urls.add(url);
                }
            }
            Publish publish = new Publish();
            BeanUtils.copyProperties(vo,publish);
            publish.setMedias(urls);
            publish.setCreated(System.currentTimeMillis());
            publish.setState(0);
            publish.setSeeType(1);
            publish.setUserId(userId);
            publish.setLocationName(vo.getLocation());
            momentApi.postMoment(publish);
        } catch (IOException e) {
            e.printStackTrace();
            throw new TanhuaException("发布动态失败");
        }
    }

    public PageResult<MomentVo> queryFriendPublishList(Long page, Long pageSize) {
        Long userId = UserHolder.getUserId();
        PageResult pageResult = momentApi.queryFriendPublishList(userId,page,pageSize);
        List<Publish> publishes = pageResult.getItems();
        List<MomentVo> voList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(publishes)) {
            List<Long> ids = publishes.stream().map(Publish::getUserId).collect(Collectors.toList());
            List<UserInfo> userInfoList = userInfoApi.findByBatchId(ids);
            Map<Long, UserInfo> infoMap = userInfoList.stream().collect(Collectors.toMap(UserInfo::getId, u -> u));
            voList = publishes.stream().map(publish -> {
                MomentVo vo = new MomentVo();
                UserInfo userInfo = infoMap.get(publish.getUserId());
                BeanUtils.copyProperties(publish,vo);
                BeanUtils.copyProperties(userInfo,vo);
                vo.setImageContent(publish.getMedias().toArray(new String[]{}));
                vo.setCreateDate(RelativeDateFormat.format(new Date(publish.getCreated())));
                vo.setTags(StringUtils.split(userInfo.getTags(),","));
                vo.setId(publish.getId().toHexString());
                // TODO
                vo.setHasLoved(0);
                vo.setHasLiked(0);
                return vo;
            }).collect(Collectors.toList());
        }
        pageResult.setItems(voList);
        return pageResult;
    }

    public PageResult<MomentVo> recommentMoment(Long page, Long pageSize) {
        Long userId = UserHolder.getUserId();
        PageResult pageResult = momentApi.recommendMoment(userId,page,pageSize);
        List<Publish> publishes = pageResult.getItems();
        List<MomentVo> voList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(publishes)) {
            List<Long> ids = publishes.stream().map(Publish::getUserId).collect(Collectors.toList());
            List<UserInfo> userInfoList = userInfoApi.findByBatchId(ids);
            Map<Long, UserInfo> infoMap = userInfoList.stream().collect(Collectors.toMap(UserInfo::getId, u -> u));
            voList = publishes.stream().map(publish -> {
                MomentVo vo = new MomentVo();
                UserInfo userInfo = infoMap.get(publish.getUserId());
                BeanUtils.copyProperties(publish,vo);
                BeanUtils.copyProperties(userInfo,vo);
                vo.setImageContent(publish.getMedias().toArray(new String[]{}));
                vo.setCreateDate(RelativeDateFormat.format(new Date(publish.getCreated())));
                vo.setTags(StringUtils.split(userInfo.getTags(),","));
                vo.setId(publish.getId().toHexString());
                // TODO
                vo.setHasLoved(0);
                vo.setHasLiked(0);
                return vo;
            }).collect(Collectors.toList());
            
        }
        pageResult.setItems(voList);
        return pageResult;
    }

    public PageResult<MomentVo> queryMyPublishList(Long page, Long pageSize, Long userId1) {
        /*Long userId = UserHolder.getUserId();
        if (userId1 != null) {
            userId = userId1;
        }
        PageResult pageResult = momentApi.queryMyPublishList(userId,page,pageSize);
        List<Publish> publishes = pageResult.getItems();
        List<MomentVo> voList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(publishes)) {
            List<Long> ids = publishes.stream().map(Publish::getUserId).collect(Collectors.toList());
            List<UserInfo> userInfoList = userInfoApi.findByBatchId(ids);
            Map<Long, UserInfo> infoMap = userInfoList.stream().collect(Collectors.toMap(UserInfo::getId, u -> u));
            voList = publishes.stream().map(publish -> {
                MomentVo vo = new MomentVo();
                UserInfo userInfo = infoMap.get(publish.getUserId());
                BeanUtils.copyProperties(publish,vo);
                BeanUtils.copyProperties(userInfo,vo);
                vo.setImageContent(publish.getMedias().toArray(new String[]{}));
                vo.setCreateDate(RelativeDateFormat.format(new Date(publish.getCreated())));
                vo.setTags(StringUtils.split(userInfo.getTags(),","));
                vo.setId(publish.getId().toHexString());
                // TODO
                vo.setHasLoved(0);
                vo.setHasLiked(0);
                return vo;
            }).collect(Collectors.toList());
        }
        pageResult.setItems(voList);
        return pageResult;*/
        return null;
    }
}
