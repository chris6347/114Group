package com.tanhua.server.config;

import com.tanhua.server.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//代表springMVC的配置类
@Configuration
public class WebConfiguration implements WebMvcConfigurer{

    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Override                       //拦截器注册器
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/**")
        .excludePathPatterns("/user/login","/user/loginVerification");
    }

}
