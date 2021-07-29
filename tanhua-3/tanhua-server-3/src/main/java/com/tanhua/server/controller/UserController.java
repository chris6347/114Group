package com.tanhua.server.controller;

import com.tanhua.domain.vo.UserInfoVo;
import com.tanhua.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    // 新用户注册填写信息
    @PostMapping("/loginReginfo")
    public ResponseEntity loginRegInfo(@RequestHeader("Authorization") String token, @RequestBody UserInfoVo vo){
        userService.loginRegInfo(token,vo);
        return ResponseEntity.ok(null);
    }

    // 用户更新头像
    @PostMapping("/loginReginfo/head")
    public ResponseEntity loginRegInfoHead(@RequestHeader("Authorization")String token, MultipartFile headPhoto){
        userService.loginRegInfoHead(token,headPhoto);
        return ResponseEntity.ok(null);
    }

}
