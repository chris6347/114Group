package com.itheima.controller;

import com.itheima.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/user")
    public Result getUsername(){

        //从security里面获取用户对象 这里的到的是User对象，不是我们自己的那个User类。
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return new Result(true , "查询成功" , user.getUsername());
    }
}
