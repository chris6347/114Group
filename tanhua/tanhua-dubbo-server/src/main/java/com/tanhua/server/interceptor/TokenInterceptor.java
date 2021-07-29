package com.tanhua.server.interceptor;

import com.alibaba.fastjson.JSON;
import com.tanhua.domain.db.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("访问了路径:"+request.getRequestURI());
        log.info("请求controller中的方法:"+handler);
        String token = request.getHeader("Authorization");
        if (StringUtils.isNotEmpty(token)) {
            User user = getUserByToken(token);
            if (user!=null) {
                UserHolder.setUser(user);
                return true;
            }
        }
        response.setStatus(401); // 401:没有认证,没有登录
        return false;
    }

    public User getUserByToken(String token){
        String key = "TOKEN_" + token;
        String jsonUser = (String) redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(jsonUser)) {
            return null;
        }
        User user = JSON.parseObject(jsonUser,User.class);
        redisTemplate.expire(key,1,TimeUnit.DAYS);
        return user;
    }

}
