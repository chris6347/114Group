package com.tanhua.server.interceptor;

import com.tanhua.domain.db.User;

public class UserHolder {

    // 每次请求都会创建一条独立的线程访问,每次访问都会经过拦截器,每次经过拦截器都会为该线程set redis中找到的user
    // key是当前线程,value是user  ThreadLocal本身是一个map,项目运行中钟会有这一个ThreadLocal和TokenInterceptor
    private static final ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    // 向当前线程中存入user
    public static void setUser(User user) {
        userThreadLocal.set(user);
    }

    // 从当前线程中获取用户数据
    public static User getUser(){
        return userThreadLocal.get();
    }

    // 获取登录用户的id
    public static Long getUserId(){
        return userThreadLocal.get().getId();
    }

}
