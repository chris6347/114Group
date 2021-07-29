package com.tanhua.server.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.tanhua.commons.templates.HuanXinTemplate;
import com.tanhua.domain.db.Question;
import com.tanhua.domain.db.RecommendUser;
import com.tanhua.domain.db.SwagQuery;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.vo.*;
import com.tanhua.dubbo.api.*;
import com.tanhua.server.interceptor.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RecommendUserService {

    @Reference
    private RecommendUserApi recommendUserApi;

    @Reference
    private UserInfoApi userInfoApi;

    @Reference
    private QuestionApi questionApi;

    @Reference
    private UserLikeApi userLikeApi;

    @Reference
    private BlackListApi blackListApi;

    @Autowired
    private HuanXinTemplate huanXinTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Reference
    private UserLocationApi userLocationApi;

    public RecommendUserVo todayBest(){
        Long userId = UserHolder.getUserId();
        // 通过登录用户的id调用mongoDB查询,推荐给这个用户的分数最高的用户id
        RecommendUser recommendUser = recommendUserApi.todayBest(userId);
        Long todayBestUserId = 99l; // 默认佳人
        Long fateValue = 80l; // 默认分数
        // 判断是否有推荐用户(佳人)
        if (recommendUser != null) {
            todayBestUserId = recommendUser.getUserId();
            fateValue = recommendUser.getScore().longValue();
        }
        UserInfo info = userInfoApi.findById(todayBestUserId);
        RecommendUserVo vo = new RecommendUserVo();
        BeanUtils.copyProperties(info,vo);
        vo.setTags(StringUtils.split(info.getTags(),","));
        vo.setFateValue(fateValue);
        return vo;
    }

    public PageResult<RecommendUserVo> recommendation(RecommendUserQueryParam queryParam) {
        Long userId = UserHolder.getUserId();
        PageResult<RecommendUser> pageResult = recommendUserApi.findPage(queryParam.getPage(),queryParam.getPagesize(),userId);
        List<RecommendUser> items = pageResult.getItems();
        if (CollectionUtils.isEmpty(items)) {
            pageResult.setCounts(10L);
            pageResult.setPages(1L);
            items = defaultRecommend();
        }
        List<RecommendUserVo> list = new ArrayList<>();
        items.forEach(recommendUser -> {
            UserInfo info = userInfoApi.findById(recommendUser.getUserId());
            RecommendUserVo vo = new RecommendUserVo();
            BeanUtils.copyProperties(info,vo);
            vo.setFateValue(recommendUser.getScore().longValue());
            vo.setTags(StringUtils.split(info.getTags(),","));
            list.add(vo);
        });
        PageResult<RecommendUserVo> result = new PageResult<>();
        result.setItems(list);
        result.setPagesize(queryParam.getPagesize().longValue());
        result.setPage(queryParam.getPage().longValue());
        result.setPages(pageResult.getPages());
        result.setCounts(pageResult.getCounts());
        return result;
    }

    private List<RecommendUser> defaultRecommend() {
        List<RecommendUser> list = new ArrayList<>();
        RecommendUser user = null;
        for (long i = 1; i <= 10 ; i++) {
            user = new RecommendUser();
            user.setUserId(i);
            user.setScore(RandomUtils.nextDouble(70,80));
            list.add(user);
        }
        return list;
    }

    public RecommendUserVo getUserInfo(Long targetUserId) {
        UserInfo info = userInfoApi.findById(targetUserId);
        RecommendUserVo vo = new RecommendUserVo();
        BeanUtils.copyProperties(info,vo);
        vo.setTags(StringUtils.split(info.getTags(),","));
        Double score = recommendUserApi.findScore(UserHolder.getUserId(),targetUserId);
        vo.setFateValue(score.longValue());
        return vo;
    }

    public String findQuestionsById(Long userId) {
        Question question = questionApi.findByUserId(userId);
        if ( null == question ) {
            return "你真的喜欢我吗?";
        }
        return question.getTxt();
    }

    public void replyQuestions(Integer userId, String reply) {
        // 查询登录用户的昵称
        UserInfo userInfo = userInfoApi.findById(UserHolder.getUserId());
        // 查询佳人的问题
        Question question = questionApi.findByUserId(userId.longValue());
        String txt = "你真的喜欢我吗?";
        if (question!=null) {
            txt = question.getTxt();
        }
        // 构建消息的内容体  需要发送者id,发送者nickname,接收者问题,发送消息内容
        Map<String,Object> map = new HashMap<>();
        map.put("userId",UserHolder.getUserId());
        map.put("nickname",userInfo.getNickname());
        map.put("strangerQuestion",txt);
        map.put("reply",reply);
        huanXinTemplate.sendMsg(userId.toString(), JSON.toJSONString(map));
    }

    public List<NearUserVo> searchNearUser(String gender, Integer distance) {
        Long userId = UserHolder.getUserId();
        List<UserLocationVo> locationVos = userLocationApi.searchNearUser(userId,distance);
        log.info("附近的人过滤前列表:{}",locationVos);
        List<NearUserVo> vos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(locationVos)) {
            List<Long> ids = locationVos.stream().map(UserLocationVo::getUserId).collect(Collectors.toList());
            List<UserInfo> userInfoList = userInfoApi.findByBatchIds(ids);
            vos = userInfoList.stream().filter(userInfo ->
                gender.equals(userInfo.getGender())
            ).map(userInfo -> {
                NearUserVo vo = new NearUserVo();
                vo.setUserId(userInfo.getId());
                vo.setAvatar(userInfo.getAvatar());
                vo.setNickname(userInfo.getNickname());
                return vo;
            }).collect(Collectors.toList());
        }
        return vos;
    }

    public RecommendUserVo swag() {
        Long userId = UserHolder.getUserId();
        String key = "SWAG_" + userId;
        String indexKey = "SWAG_INDEX_" + userId;
        String jsonSwag = (String) redisTemplate.opsForValue().get(key);
        Integer index = (Integer) redisTemplate.opsForValue().get(indexKey);
        if (jsonSwag == null || JSONArray.parseArray(jsonSwag,RecommendUserVo.class).isEmpty() || index == null ||index >= JSONArray.parseArray(jsonSwag,RecommendUserVo.class).size()) {
            UserInfo loginUserInfo = userInfoApi.findById(userId);
            Integer loginUserAge = loginUserInfo.getAge();
            String loginUserGender = loginUserInfo.getGender();
            Integer minAge = loginUserAge - 5;
            Integer maxAge = loginUserAge + 5;
            String gender ;
            if (loginUserGender.equals("women")) {
                gender = "man";
            } else {
                gender = "women";
            }
            // 获取page
            String pageKey = "SWAG_PAGE_" + userId;
            Long page = (Long) redisTemplate.opsForValue().get(pageKey);
            if (page == null) {
                page = 1L;
            }
            SwagQuery query = new SwagQuery(userId, gender, minAge, maxAge,page);
            List<UserInfo> swag = userInfoApi.findSwag(query);
            List<RecommendUserVo> voList = swag.stream().map(userInfo -> {
                RecommendUserVo vo = new RecommendUserVo();
                BeanUtils.copyProperties(userInfo, vo);
                vo.setTags(StringUtils.split(userInfo.getTags(), ","));
                return vo;
            }).collect(Collectors.toList());
            redisTemplate.opsForValue().set(key,JSON.toJSONString(voList),1, TimeUnit.DAYS);
            redisTemplate.opsForValue().set(indexKey,1,1,TimeUnit.DAYS);
            redisTemplate.opsForValue().set(pageKey,page + 1,1,TimeUnit.DAYS);
            return voList.get(0);
        }
        List<RecommendUserVo> voList = JSONArray.parseArray(jsonSwag, RecommendUserVo.class);
        redisTemplate.opsForValue().set(indexKey,index+1,1,TimeUnit.DAYS);
        return voList.get(index);
    }

    public void loveSwag(Long likeUserId) {
        userLikeApi.saveLike(UserHolder.getUserId(),likeUserId);
    }

    public void unLoveSwag(Long unLoveUserId) {
        blackListApi.saveBlackUser(UserHolder.getUserId(),unLoveUserId);
    }

}
