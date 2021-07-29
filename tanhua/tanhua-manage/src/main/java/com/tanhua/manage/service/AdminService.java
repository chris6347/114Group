package com.tanhua.manage.service;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanhua.manage.domain.Admin;
import com.tanhua.manage.exception.BusinessException;
import com.tanhua.manage.interceptor.AdminHolder;
import com.tanhua.manage.mapper.AdminMapper;
import com.tanhua.manage.utils.JwtUtils;
import com.tanhua.manage.vo.AdminVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AdminService extends ServiceImpl<AdminMapper, Admin> {

    private static final String CACHE_KEY_CAP_PREFIX = "MANAGE_CAP_";
    public static final String CACHE_KEY_TOKEN_PREFIX="MANAGE_TOKEN_";

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 保存生成的验证码
     * @param uuid
     * @param code
     */
    public void saveCode(String uuid, String code) {
        String key = CACHE_KEY_CAP_PREFIX + uuid;
        // 缓存验证码，10分钟后失效
        redisTemplate.opsForValue().set(key,code, Duration.ofMinutes(10));
    }

    /**
     * 获取登陆用户信息
     * @return
     */
    public Admin getByToken(String authorization) {
        String token = authorization.replaceFirst("Bearer ","");
        String tokenKey = CACHE_KEY_TOKEN_PREFIX + token;
        String adminString = (String) redisTemplate.opsForValue().get(tokenKey);
        Admin admin = null;
        if(StringUtils.isNotEmpty(adminString)) {
            admin = JSON.parseObject(adminString, Admin.class);
            // 延长有效期 30分钟
            redisTemplate.expire(tokenKey,30, TimeUnit.DAYS);
        }
        return admin;
    }

    public String loginVerification(String username, String password, String verificationCode, String uuid) {
        String key = CACHE_KEY_CAP_PREFIX + uuid;
        String code = (String) redisTemplate.opsForValue().get(key);
        if (null == code) {
            throw new BusinessException("验证码已失效");
        }
        if (!verificationCode.equals(code)) {
            throw new BusinessException("验证码错误");
        }
        // 记得删除验证码
        redisTemplate.delete(key);
        // 还要判断是否为空的??前端不会做?
        if (StringUtils.isBlank(username)||StringUtils.isBlank(password)) {
            throw new BusinessException("用户名或密码不能为空");
        }

        /*QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);*/
        // TODO
        Admin admin = query().eq("username",username).one();
        if (null == admin) {
            throw new BusinessException("用户名错误");
        }
        // Secure 和 DigestUtils 的区别是什么
        String encodePassword = SecureUtil.md5(password);
        if (!encodePassword.equals(admin.getPassword())) {
            throw new BusinessException("密码错误");
        }
        String token =  jwtUtils.createJWT(admin.getUsername(), admin.getId());
        String tokenKey = CACHE_KEY_TOKEN_PREFIX + token;
        redisTemplate.opsForValue().set(tokenKey,JSON.toJSONString(admin),30,TimeUnit.DAYS);
        return token;
    }

    public AdminVo profile() {
        Admin admin = AdminHolder.getAdmin();
        AdminVo vo = new AdminVo();
        BeanUtils.copyProperties(admin,vo);
        return vo;
    }

    public void logout(String token) {
        redisTemplate.delete(CACHE_KEY_TOKEN_PREFIX+token);
    }

}
