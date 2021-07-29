package com.tanhua.server.controller;

import com.tanhua.domain.vo.UserInfoVo;
import com.tanhua.server.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @GetMapping
    public ResponseEntity findUserInfo(Long userId,Long huanxinID){
        UserInfoVo vo = userInfoService.findUserInfo(userId);
        return ResponseEntity.ok(vo);
    }

    @PutMapping
    public ResponseEntity updateUserInfo(@RequestBody UserInfoVo vo){
        userInfoService.updateUserInfo(vo);
        return ResponseEntity.ok(null);
    }

}
