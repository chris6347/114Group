package com.tanhua.server.interceptor;

import com.tanhua.domain.db.User;
import org.springframework.stereotype.Component;


public class UserHolder {

    private static final ThreadLocal<User> threadLocal = new ThreadLocal<>();

    public static User getUser(){
        return threadLocal.get();
    }

    public static Long getUserId(){
        return threadLocal.get().getId();
    }

    public static void setUser(User user){
        threadLocal.set(user);
    }

    public static void resetThreadLocal(){
        threadLocal.remove();
    }

}
