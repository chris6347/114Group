package com.tanhua.server.service;

import com.alibaba.fastjson.JSON;
import com.tanhua.commons.templates.FaceTemplate;
import com.tanhua.commons.templates.HuanXinTemplate;
import com.tanhua.commons.templates.OssTemplate;
import com.tanhua.commons.templates.SmsTemplate;
import com.tanhua.commons.exception.TanHuaException;
import com.tanhua.domain.db.User;
import com.tanhua.domain.vo.ErrorResult;
import com.tanhua.dubbo.api.UserApi;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.dubbo.api.UserLikeApi;
import com.tanhua.server.interceptor.UserHolder;
import com.tanhua.server.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserService {

    @Reference
    private UserApi userApi;

    @Reference
    private UserInfoApi userInfoApi;

    @Autowired
    private SmsTemplate smsTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Value("${tanhua.redisValidateCodeKeyPrefix}")
    private String redisValidateCodeKeyPrefix;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private OssTemplate ossTemplate;

    @Autowired
    private FaceTemplate faceTemplate;

    @Autowired
    private HuanXinTemplate huanXinTemplate;

    @Reference
    private UserLikeApi userLikeApi;

    public ResponseEntity findByMobile(String mobile) {
        User user = userApi.findByMobile(mobile);
        return ResponseEntity.ok(user);
    }

    public ResponseEntity saveUser(String mobile,String password) {
        User user = new User();
        user.setMobile(mobile);
        user.setPassword(password);
        userApi.save(user);
        return ResponseEntity.ok(null);
    }

    public void sendValidateCode(String phone) {
        String key = redisValidateCodeKeyPrefix + phone ;
        String codeInRedis = (String) redisTemplate.opsForValue().get(key);
        //String codeInRedis = redisTemplate.boundValueOps(key).get();
        if (StringUtils.isNotEmpty(codeInRedis)) {
            throw new TanHuaException(ErrorResult.duplicate());
        }
        // 生成验证码
        String validateCode = "123456";//RandomStringUtils.randomNumeric(6);
        // 发送验证码
        log.debug("发送验证码:{},{}",phone,validateCode);
        //Map<String, String> smsRs = smsTemplate.sendValidateCode(phone, validateCode);
//        if (smsRs!=null) {  //  不=null就失败
//            throw new TanHuaException(ErrorResult.fail());
//        }
        // 将验证码存入redis 有效时间5分钟
        log.info("将验证码存入redis");
        redisTemplate.opsForValue().set(key,validateCode,5, TimeUnit.MINUTES);
        //redisTemplate.boundValueOps(key).set(validateCode,5,TimeUnit.MINUTES);
    }

    // 用户登录
    public Map<String, Object> loginVerification(String phone, String verificationCode) {
        // 消息类型为用户登录
        String type = "0102";
        // redis 中存入验证码的key
        String key = redisValidateCodeKeyPrefix + phone ;
        // redis 中的验证码
        String codeInRedis = (String) redisTemplate.boundValueOps(key).get();
        log.debug("============校验 验证码:{},{},{}",phone,codeInRedis,verificationCode);
        if (StringUtils.isEmpty(codeInRedis)) {
            throw new TanHuaException(ErrorResult.loginError());  // 验证码失效
        }
        if (!codeInRedis.equals(verificationCode)) {
            throw new TanHuaException(ErrorResult.validateCodeError());  // 验证码不正确
        }
        // 到了这里,验证码匹配成功
        redisTemplate.delete(key);
        User user = userApi.findByMobile(phone);
        boolean isNew = false;
        if (user == null) {
            // 注册用户,让他补全资料
            user = new User();
            user.setMobile(phone);
            // 手机后六位为默认密码
            // 探花使用md5加密密码
            user.setPassword(DigestUtils.md5Hex(phone.substring(phone.length()-6)));
            log.info("===================添加新用户 {}",phone);
            Long userId = userApi.save(user);
            user.setId(userId);
            isNew=true;

            // 消息类型为用户注册
            type = "0101";
            // 注册环信通讯
            huanXinTemplate.register(user.getId());
        }
        // 签发token令牌
        String token = jwtUtils.createJWT(phone, user.getId());
        // 用户信息存入redis,方便后期获取,有效期为1天
        String userString = JSON.toJSONString(user);
        redisTemplate.opsForValue().set("TOKEN_"+token,userString,1,TimeUnit.DAYS);
        log.debug("================= 签发token:{}",token);
        Map<String,Object> map = new HashMap<>();
        map.put("isNew",isNew);
        map.put("token",token);
        // RocketMQ 发送消息 记录用户登录注册信息,用于后台统计
        // 构建日志数据
        Map<String,Object> msgMap = new HashMap<>();
        msgMap.put("userId",user.getId());
        msgMap.put("type",type);
        msgMap.put("log_time",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        msgMap.put("place","深圳");
        msgMap.put("equipment","iphone12");
        // 调用mq发送消息
        rocketMQTemplate.convertAndSend("tanhua_log",JSON.toJSONString(msgMap));
        return map;
    }

    // 喜欢请求
    public void fansLike(Long fansId) {
        Long userId = UserHolder.getUserId();
        // 登录用户来喜欢粉丝
        boolean flag = userLikeApi.fansLike(userId,fansId);
        if (flag) {
            // 互相喜欢,在环信上注册为好友,可以聊天
            huanXinTemplate.makeFriends(userId,fansId);
        }
    }

    /*private User getUserByToken(String token) {
        String jsonUser = redisTemplate.opsForValue().get("TOKEN_"+token);
        if (StringUtils.isEmpty(jsonUser)) {
            log.info("登录超时");
            throw new TanHuaException("登录超时,请重新登录");
        }
        redisTemplate.expire("TOKEN_"+token,1,TimeUnit.DAYS);
        User user = JSON.parseObject(jsonUser,User.class);
        return user;
    }
*/

}
