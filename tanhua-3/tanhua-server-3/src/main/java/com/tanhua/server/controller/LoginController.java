package com.tanhua.server.controller;

import com.tanhua.domain.db.User;
import com.tanhua.domain.vo.UserInfoVo;
import com.tanhua.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class LoginController {

    @Autowired
    private UserService userService;

    // ResponseEntity: 响应内容,spring封装的响应http请求的对象
    // 与前端约定好了,不能改,只能用这个 规范
    @GetMapping("/findByMobile")
    public ResponseEntity findByMobile(String phone){
        // 调用业务层
        User user = userService.findByMobile(phone);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/add")
    public ResponseEntity saveUser(@RequestBody User user){
        userService.saveUser(user);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/login")
    public ResponseEntity sendValidateCode(@RequestBody Map<String,String> param){
        userService.sendMsg(param.get("phone"));
        return ResponseEntity.ok(null);
    }

    @PostMapping("/loginVerification")
    public ResponseEntity verificationCode(@RequestBody Map<String,String> param){
        Map<String,Object> map = userService.verificationCode(param.get("verificationCode"),param.get("phone"));
        return ResponseEntity.ok(map);
    }

}
