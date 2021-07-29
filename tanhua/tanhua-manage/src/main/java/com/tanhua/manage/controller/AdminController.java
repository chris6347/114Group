package com.tanhua.manage.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.tanhua.manage.service.AdminService;
import com.tanhua.manage.vo.AdminVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/system/users")
@Slf4j
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * 后台登陆时 图片验证码 生成
     */
    @GetMapping("/verification")
    public void showValidateCodePic(String uuid,HttpServletRequest req, HttpServletResponse res){
        res.setDateHeader("Expires",0);
        res.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        res.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        res.setHeader("Pragma", "no-cache");
        // ===========以上的设置作用: 让浏览器不要缓存的这响应的结果============

        res.setContentType("image/jpeg");// 告诉浏览器响应的内容体是图片
        // 创建一张图片,在图片中生成验证码
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(299, 97);
        // 获取生成的验证码
        String code = lineCaptcha.getCode();
        log.debug("uuid={},code={}",uuid,code);
        // 保存验证码到redis里面
        adminService.saveCode(uuid,code);
        try {
            // 把图片响应给请求者
            lineCaptcha.write(res.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/login")
    public ResponseEntity loginVerification(@RequestBody Map<String,String> param){
        String token = adminService.loginVerification(param.get("username"),param.get("password"),
                param.get("verificationCode"),param.get("uuid"));
        return ResponseEntity.ok(token);
    }

    @PostMapping("/profile")
    public ResponseEntity profile(){
        AdminVo vo = adminService.profile();
        return ResponseEntity.ok(vo);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(@RequestHeader("Authorization") String token){
        token = token.replace("Bearer","");
        adminService.logout(token);
        return ResponseEntity.ok(null);
    }

}