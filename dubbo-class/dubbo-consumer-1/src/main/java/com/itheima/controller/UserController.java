package com.itheima.controller;

import com.itheima.pojo.User;
import com.itheima.server.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServer userServer;

    @RequestMapping("/findById")
    public ResponseEntity findById(Integer id){
        User user = userServer.findById(id);
        return ResponseEntity.status(200).body(user);
    }

}
