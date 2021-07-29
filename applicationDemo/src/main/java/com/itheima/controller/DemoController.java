package com.itheima.controller;

import com.itheima.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Autowired
    private User user;

    @RequestMapping("/user")
    public String demo(){
        System.out.println(user.getName());
        return "demo";
    }

}
