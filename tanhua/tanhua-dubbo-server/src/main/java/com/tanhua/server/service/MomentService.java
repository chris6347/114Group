package com.tanhua.server.service;

import com.tanhua.commons.templates.OssTemplate;
import com.tanhua.commons.exception.TanHuaException;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.mongo.Visitor;
import com.tanhua.domain.mongo.Publish;
import com.tanhua.domain.vo.MomentVo;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.domain.vo.PublishVo;
import com.tanhua.domain.vo.VisitorVo;
import com.tanhua.dubbo.api.PublishApi;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.dubbo.api.VisitorApi;
import com.tanhua.server.interceptor.UserHolder;
import com.tanhua.commons.utils.RelativeDateFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MomentService {

    @Autowired
    private OssTemplate ossTemplate;

    @Reference
    private PublishApi publishApi;

    @Reference
    private UserInfoApi userInfoApi;

    @Autowired
    private RedisTemplate redisTemplate;

    @Reference
    private VisitorApi visitorApi;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void postMoment(PublishVo vo, MultipartFile[] imageContents) {
        // 获取登录用户id
        Long userId = UserHolder.getUserId();
        // 上传过来的图片,存入oss
        List<String> urls = new ArrayList<>();
        if (imageContents != null) {
            Arrays.stream(imageContents).forEach(image->{
                try {
                    String url = ossTemplate.upload(image.getOriginalFilename(),image.getInputStream());
                    urls.add(url);
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("上传动态图片失败",e);
                    throw new TanHuaException("发布失败");
                }
            });
        }
        // 构建Publish对象
        Publish publish = new Publish();
        BeanUtils.copyProperties(vo,publish);
        // 添加没有的数据
        publish.setUserId(userId);
        publish.setMedias(urls);
        publish.setState(0);// 未审核
        publish.setSeeType(1);// 公开
        String publishId = publishApi.add(publish);

        // 发送消息给MQ审核,需要用到动态id
        rocketMQTemplate.convertAndSend("tanhua-publish",publishId);
    }

    public PageResult<MomentVo> queryFriendPublishList(long page, long pageSize) {
        Long userId = UserHolder.getUserId();
        // 通过用户id分页查询好友动态
        PageResult pageResult = publishApi.findFriendPublishByTimeLine(userId,page,pageSize);
        // 获取查询的结果集(动态的结果)
        List<Publish> publishList = pageResult.getItems();

        if (!CollectionUtils.isEmpty(publishList)) {
            List<Long> userIds = publishList.stream().map(Publish::getUserId).collect(Collectors.toList());
            List<UserInfo> userInfos = userInfoApi.findByBatchIds(userIds);
            Map<Long, UserInfo> userInfoMap = userInfos.stream().collect(Collectors.toMap(UserInfo::getId, userInfo -> userInfo));
            // 需要返回momentVo
            List<MomentVo> momentVos = publishList.stream().map(publish -> {
                MomentVo vo = new MomentVo();
                BeanUtils.copyProperties(publish, vo);
                // 把list转成数组
                vo.setImageContent(publish.getMedias().toArray(new String[0]));
                vo.setDistance("500米");
                // 把ObjectId转成字符串
                vo.setId(publish.getId().toHexString());
                // 把日期转成字符串 (多久前发布)
                vo.setCreateDate(RelativeDateFormat.format(new Date(publish.getCreated())));
                // 复制作者信息
                UserInfo userInfo = userInfoMap.get(publish.getUserId());
                BeanUtils.copyProperties(userInfo, vo);
                vo.setTags(StringUtils.split(userInfo.getTags(), ","));

                // 是否点赞过
                vo.setHasLiked(0);
                vo.setHasLoved(0);
                if(isLikeOrLove(1,vo.getId(),userId)){
                    vo.setHasLiked(1);
                }
                if (isLikeOrLove(3,vo.getId(),userId)) {
                    vo.setHasLoved(1);
                }
                return vo;
            }).collect(Collectors.toList());
            pageResult.setItems(momentVos);
        }
        return pageResult;
    }

    public PageResult<MomentVo> queryRecommendPublishList(Long page, Long pageSize) {
        Long userId = UserHolder.getUserId();
        PageResult pageResult = publishApi.findRecommendPublish(userId,page,pageSize);
        List<Publish> items = pageResult.getItems();
        List<MomentVo> momentVos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(items)) {
            items.forEach(p->{
                MomentVo vo = new MomentVo();
                BeanUtils.copyProperties(p,vo);
                vo.setId(p.getId().toHexString());
                vo.setImageContent(p.getMedias().toArray(new String[0]));
                vo.setDistance("500米");
                vo.setCreateDate(RelativeDateFormat.format(new Date(p.getCreated())));
                vo.setHasLoved(0);
                vo.setHasLiked(0);
                if(isLikeOrLove(1,vo.getId(),userId)){
                    vo.setHasLiked(1);
                }
                if (isLikeOrLove(3,vo.getId(),userId)) {
                    vo.setHasLoved(1);
                }
                UserInfo userInfo = userInfoApi.findById(p.getUserId());
                BeanUtils.copyProperties(userInfo,vo);
                vo.setTags(StringUtils.split(userInfo.getTags(),","));
                momentVos.add(vo);
            });
            pageResult.setItems(momentVos);
        }
        return pageResult;
    }

    public PageResult<MomentVo> queryMyPublishList(Long userId, Long page, Long pagesize) {
        Long loginUserId = UserHolder.getUserId();
        if (userId != null) {
            loginUserId = userId;
        }
        final long loginUser_id = loginUserId;

        // 调用api查询登录用户自己的动态
        PageResult pageResult = publishApi.findMyPublishList(loginUser_id,page,pagesize);
        List<Publish> publishList = pageResult.getItems();
        // 补全用户信息
        List<MomentVo> momentVoList = null;
        if (!CollectionUtils.isEmpty(publishList)) {
            List<Long> publisherIds = publishList.stream().map(Publish::getUserId).collect(Collectors.toList());
            List<UserInfo> userInfos = userInfoApi.findByBatchIds(publisherIds);
            // id:userInfo 键值对
            Map<Long, UserInfo> map = userInfos.stream().collect(Collectors.toMap(UserInfo::getId, userInfo -> userInfo));
            MomentVo vo = new MomentVo();
            momentVoList = publishList.stream().map(publish -> {
                BeanUtils.copyProperties(publish,vo);
                UserInfo userInfo = map.get(publish.getUserId());
                BeanUtils.copyProperties(userInfo,vo);
                vo.setTags(StringUtils.split(userInfo.getTags(),","));
                vo.setHasLiked(0);  //TODO
                vo.setHasLoved(0);
                if(isLikeOrLove(1,publish.getId().toHexString(),userId)){
                    vo.setHasLiked(1);
                }
                if (isLikeOrLove(3,publish.getId().toHexString(),userId)) {
                    vo.setHasLoved(1);
                }
                vo.setImageContent(publish.getMedias().toArray(new String[0]));
                vo.setDistance("500米");
                vo.setCreateDate(RelativeDateFormat.format(new Date(publish.getCreated())));
                vo.setId(publish.getId().toHexString());
                /*String key = "publish_like_"+vo.getId()+"_"+loginUser_id;
                if (redisTemplate.hasKey(key)) {
                    vo.setHasLiked(1); // 点赞过了
                }
                key = "publish_love_"+vo.getId()+"_"+loginUser_id;
                if (redisTemplate.hasKey(key)) {
                    vo.setHasLoved(1);
                }*/
                return vo;
            }).collect(Collectors.toList());
            pageResult.setItems(momentVoList);
        }
        return pageResult;
    }

    private boolean isLikeOrLove (Integer type,String publishId,Long userId) {
        String key = null;
        if (type == 1) {
            key = "publish_like_"+publishId+"_"+userId;
        } else {
            key = "publish_love_"+publishId+"_"+userId;
        }
        return redisTemplate.hasKey(key);
    }

    public MomentVo findById(String publishId) {
        Long userId = UserHolder.getUserId();
        Publish publish = publishApi.findById(publishId);
        MomentVo vo = new MomentVo();
        BeanUtils.copyProperties(publish,vo);
        vo.setId(publishId);
        vo.setImageContent(publish.getMedias().toArray(new String[0]));
        vo.setDistance("500米");
        vo.setCreateDate(RelativeDateFormat.format(new Date(publish.getCreated())));
        Long publisherId = publish.getUserId();
        UserInfo userInfo = userInfoApi.findById(publisherId);
        BeanUtils.copyProperties(userInfo,vo);
        vo.setTags(StringUtils.split(userInfo.getTags(),","));
        vo.setHasLiked(0);
        vo.setHasLoved(0);
        if(isLikeOrLove(1,vo.getId(),userId)){
            vo.setHasLiked(1);
        }
        if (isLikeOrLove(3, vo.getId(), userId)) {
            vo.setHasLoved(1);
        }
        return vo;
    }

    public List<VisitorVo> queryVisitors() {
        Long userId = UserHolder.getUserId();
        // 取出redis中设置上次查询时间
        String key = "visitors_time_" + UserHolder.getUserId();
        Long lastQueryTime = (Long) redisTemplate.opsForValue().get(key);
        List<Visitor> visitors = visitorApi.queryLast4Visitors(userId,lastQueryTime);
        List<VisitorVo> vos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(visitors)) {
            List<Long> ids = visitors.stream().map(Visitor::getVisitorUserId).collect(Collectors.toList());
            List<UserInfo> infoList = userInfoApi.findByBatchIds(ids);
            Map<Long, UserInfo> infoMap = infoList.stream().collect(Collectors.toMap(UserInfo::getId, userInfo -> userInfo));
            vos = visitors.stream().map(visitor -> {
                VisitorVo vo = new VisitorVo();
                UserInfo info = infoMap.get(visitor.getVisitorUserId());
                BeanUtils.copyProperties(info,vo);
                vo.setTags(StringUtils.split(info.getTags(),","));
                if (visitor.getScore()==null) {
                    vo.setFateValue(RandomUtils.nextInt(70,90));
                } else {
                    vo.setFateValue(visitor.getScore().intValue());
                }
                return vo;
            }).collect(Collectors.toList());
        }
        redisTemplate.opsForValue().set(key,System.currentTimeMillis());
        return vos;
    }
}
