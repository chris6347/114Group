package com.itheima.controller;

import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    @RequestMapping("/findUser")
    public User findById(Integer id) {
        return userService.findById(id);
    }

}
