package com.tanhua.server.service;

import com.tanhua.commons.templates.SmsTemplate;
import com.tanhua.commons.utils.RedisKeyConst;
import com.tanhua.commons.exception.TanHuaException;
import com.tanhua.domain.db.*;
import com.tanhua.domain.vo.ErrorResult;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.domain.vo.SettingsVo;
import com.tanhua.domain.vo.UserInfoVo;
import com.tanhua.dubbo.api.*;
import com.tanhua.server.interceptor.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SettingService {

    @Reference
    private UserApi userApi;

    @Reference
    private SettingApi settingApi;

    @Reference
    private QuestionApi questionApi;

    @Reference
    private BlackListApi blackListApi;

    @Reference
    private UserInfoApi userInfoApi;

    @Autowired
    private SmsTemplate smsTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    public SettingsVo findByUserId(){
        // 获取用户
        User user = UserHolder.getUser();
        Settings settings = settingApi.findByUserId(user.getId());
        log.info("查询settings成功");
        // 通过用户id查询陌生人问题
        Question question = questionApi.findByUserId(user.getId());
        log.info("查询question成功");
        //构建vo
        SettingsVo vo = new SettingsVo();
        // 设置默认的陌生人问题
        vo.setStrangerQuestion("你喜欢我吗?");
        if (null != settings) {
            BeanUtils.copyProperties(settings,vo);
        }
        // 把phone和question的txt设置进vo
        if (null != question) {
            vo.setStrangerQuestion(question.getTxt());
        }
        vo.setPhone(user.getMobile());
        return vo;
    }

    public void updateNotification(SettingsVo vo) {
        Long userId = UserHolder.getUserId();
        Settings settings = new Settings();
        BeanUtils.copyProperties(vo,settings);
        settings.setUserId(userId);
        settingApi.save(settings);
    }

    public PageResult<UserInfoVo> blackListPage(Long page, Long pageSize) {
        Long userId = UserHolder.getUserId();
        PageResult pageResult = blackListApi.findPageByUserId(page,pageSize,userId);
        List<BlackList> list = pageResult.getItems();
        if (!CollectionUtils.isEmpty(list)) {
            List<Long> blackUserIds = list.stream().map(BlackList::getBlackUserId).collect(Collectors.toList());
            // 根据ids查找userInfo列表
            List<UserInfo> userInfoList = userInfoApi.findByBatchIds(blackUserIds);
            pageResult.setItems(userInfoList);
        }
        return pageResult;
    }

    public void removeBlackUser(Long blackUserId) {
        Long userId = UserHolder.getUserId();
        blackListApi.removeBlackUser(userId,blackUserId);
    }

    public void updateQuestion(String content) {
        Long userId = UserHolder.getUserId();
        // 在这里判断是更新还是添加的话:查询,判断,添加/更新  还不如直接在apiImpl里做判断
        questionApi.updateQuestion(userId,content);
    }

    public void sendVerificationCode() {
        String mobile = UserHolder.getUser().getMobile();
        String verification = (String) redisTemplate.opsForValue().get(RedisKeyConst.CHANGE_MOBILE_VALIDATE_CODE + mobile);
        if (StringUtils.isNotEmpty(verification)) {
            log.error("验证码未失效");
            throw new TanHuaException(ErrorResult.duplicate());  // 未失效
        }
        String validate = RandomStringUtils.randomNumeric(6);
        Map<String, String> map = smsTemplate.sendValidateCode(mobile, validate);
        log.info("发送验证码成功,手机号:{},验证码:{}",mobile,validate);
        if (map != null) {
            log.error("发送验证码失败");
            throw new TanHuaException(ErrorResult.fail());
        }
        redisTemplate.opsForValue().set(RedisKeyConst.CHANGE_MOBILE_VALIDATE_CODE+mobile,validate,5, TimeUnit.MINUTES);
    }

    public boolean checkValidateCode(String verificationCode) {
        String key = RedisKeyConst.CHANGE_MOBILE_VALIDATE_CODE + UserHolder.getUser().getMobile();
        String validate = (String) redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(validate)) {
            throw new TanHuaException(ErrorResult.loginError());  // 已失效
        }
        if (!validate.equals(verificationCode)) {
            throw new TanHuaException(ErrorResult.validateCodeError());  // 验证码错误
        }
        redisTemplate.delete(key);
        return true;
    }

    public void updatePhone(String token , String phone) {
        User user = UserHolder.getUser();
        userApi.updatePhone(user.getId(),phone);
        log.info("修改手机号成功(old:{})=>(new:{})",user.getMobile(),phone);
        redisTemplate.delete(RedisKeyConst.TOKEN+token);
    }
}
