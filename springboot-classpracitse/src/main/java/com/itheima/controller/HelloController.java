package com.itheima.controller;

import com.itheima.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Value("${name}")
    private String name;

    @Value("${city[3]}")
    private String city;

    @Value("${students[1].age}")
    private String age;

    @Value("${maps.name}")   // 一般在开发中使用  程序员
    private String zs;

    @Autowired
    private User user;

    @Autowired  // 一般在框架的封装中使用  架构师
    private Environment environment;

    @RequestMapping("/hello")
    public String sayHello(){
        System.out.println(name);
        System.out.println(city);
        System.out.println(age);
        System.out.println(zs);
        System.out.println(user);
        return "hello";
    }

    @RequestMapping("/print")
    private String print(){
        String name = environment.getProperty("students[2].name");
        System.out.println(name);
        return "printing..";
    }

}
