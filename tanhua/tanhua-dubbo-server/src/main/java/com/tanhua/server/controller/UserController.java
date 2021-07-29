package com.tanhua.server.controller;

import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.vo.UserInfoVo;
import com.tanhua.server.service.UserInfoService;
import com.tanhua.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserInfoService userInfoService;

    // 完善用户信息
    @PostMapping("/loginRegInfo")
    public ResponseEntity loginRegInfo(@RequestBody UserInfoVo userInfoVo, @RequestHeader("Authorization") String token){
        userInfoService.loginRegInfo(userInfoVo,token);
        return ResponseEntity.ok(null);
    }

    // 完善用户信息 选取头像   avatar:用户头像
    @PostMapping("/loginRegInfo/head")
    public ResponseEntity uploadAvatar(MultipartFile headPhoto,@RequestHeader("Authorization") String token) {
        userInfoService.uploadAvatar(headPhoto,token);
        return ResponseEntity.ok(null);
    }


}
