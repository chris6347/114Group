package com.tanhua.server.controller;

import com.tanhua.server.service.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/findUser",method = RequestMethod.GET)
    public ResponseEntity findUser(String mobile) {
        System.out.println(mobile);
        return userService.findByMobile(mobile);
    }

    @RequestMapping(value = "/saveUser",method = RequestMethod.POST)
    public ResponseEntity saveUser(@RequestBody Map<String,Object> param){
        String mobile = (String) param.get("mobile");
        String password = (String) param.get("password");
        return userService.saveUser(mobile,password);
    }

    // 发送验证码
    @PostMapping("/login")
    public ResponseEntity sendValidateCode(@RequestBody Map<String,String> param) {
        System.out.println(param);
        String phone = param.get("phone");
        userService.sendValidateCode(phone);
        return ResponseEntity.ok("短信发送成功");
    }

    // 校验验证码登录
    @PostMapping("/loginVerification")   // verification 确认
    public ResponseEntity loginVerification(@RequestBody Map<String,String> param) {
        String phone = param.get("phone");
        String verificationCode = param.get("verificationCode");
        Map<String,Object> map = userService.loginVerification(phone,verificationCode);
        return ResponseEntity.ok(map);
    }

}
