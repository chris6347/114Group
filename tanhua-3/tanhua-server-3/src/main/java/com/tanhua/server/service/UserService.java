package com.tanhua.server.service;

import com.alibaba.fastjson.JSON;
import com.tanhua.commons.exception.TanhuaException;
import com.tanhua.commons.templates.FaceTemplate;
import com.tanhua.commons.templates.OssTemplate;
import com.tanhua.commons.templates.SmsTemplate;
import com.tanhua.domain.db.User;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.vo.ErrorResult;
import com.tanhua.domain.vo.UserInfoVo;
import com.tanhua.dubbo.api.UserApi;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.server.interceptor.UserHolder;
import com.tanhua.server.utils.GetAgeUtil;
import com.tanhua.server.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserService {

    @Reference
    private UserApi userApi;

    @Reference
    private UserInfoApi userInfoApi;

    @Autowired
    private SmsTemplate smsTemplate;

    @Autowired
    private FaceTemplate faceTemplate;

    @Autowired
    private OssTemplate ossTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${tanhua.redisValidateCodeKeyPrefix}")
    private String redisValidateCodeKeyPrefix;

    @Autowired
    private JwtUtils jwtUtils;

    private final String tokenPrefix = "TOKEN_";

    public User findByMobile(String phone) {
        User user = userApi.findByMobile(phone);
        return user;
    }

    public void saveUser(User user) {
        userApi.save(user);
    }

    public void sendMsg(String phone) {
        String key = redisValidateCodeKeyPrefix+phone;
        if (redisTemplate.hasKey(key)) {
            throw new TanhuaException(ErrorResult.duplicate());
        }
        String validateCode = RandomStringUtils.randomNumeric(6);
        log.info("请求验证码:phone={},validateCode={}",phone,validateCode);
        Map<String, String> map = smsTemplate.sendValidateCode(phone, validateCode);
        if (null != map) {
            throw new TanhuaException(ErrorResult.fail());
        }
        redisTemplate.opsForValue().set(key,validateCode,10, TimeUnit.MINUTES);
    }

    public Map<String, Object> verificationCode(String verificationCode,String phone) {
        String key = redisValidateCodeKeyPrefix + phone;
        String code = (String) redisTemplate.opsForValue().get(key);
        if (null == code) {
            throw new TanhuaException(ErrorResult.loginError());
        }
        if (!code.equals(verificationCode)) {
            throw new TanhuaException(ErrorResult.validateCodeError());
        }
        User user = userApi.findByMobile(phone);
        boolean isNew = false;
        if (null == user) {
            user = new User();
            user.setMobile(phone);
            user.setPassword(DigestUtils.md5Hex(phone.substring(phone.length()-6)));
            isNew = true;
        }
        redisTemplate.delete(key);
        // 将来系统放大了 其他系统可以从token中可以获取到用户信息 所以要在token中存入手机号和id
        String token = jwtUtils.createJWT(phone, user.getId());
        key = tokenPrefix + token;
        redisTemplate.opsForValue().set(key, JSON.toJSONString(user),7,TimeUnit.DAYS);
        Map<String,Object> result = new HashMap<>();
        result.put("isNew",isNew);
        result.put("token",token);
        return result;
    }

    public void loginRegInfo(String token, UserInfoVo vo) {
        User user = UserHolder.getUser();
        Long userId = user.getId();
        UserInfo info = new UserInfo();
        // 属性名必须一致,类型必须一致
        BeanUtils.copyProperties(vo,info);
        info.setId(userId);
        info.setAge(GetAgeUtil.getAge(vo.getBirthday()));
        userInfoApi.save(info);
    }

    public void loginRegInfoHead(String token, MultipartFile headPhoto) {
        User user = UserHolder.getUser();
        try {
            boolean detect = faceTemplate.detect(headPhoto.getBytes());
            if (!detect) {
                throw new TanhuaException(ErrorResult.faceError());
            }
            String url = ossTemplate.upload(headPhoto.getOriginalFilename(), headPhoto.getInputStream());
            UserInfo info = new UserInfo();
            info.setId(user.getId());
            info.setAvatar(url);
            userInfoApi.update(info);
        } catch (IOException e) {
            e.printStackTrace();
            throw new TanhuaException("上传头像失败");
        }
    }
}
