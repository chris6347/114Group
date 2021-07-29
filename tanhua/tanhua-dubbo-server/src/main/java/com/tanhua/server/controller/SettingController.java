package com.tanhua.server.controller;

import com.tanhua.domain.vo.PageResult;
import com.tanhua.domain.vo.SettingsVo;
import com.tanhua.domain.vo.UserInfoVo;
import com.tanhua.server.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/users")
@RestController
public class SettingController {

    @Autowired
    private SettingService settingService;

    @GetMapping("/settings")
    public ResponseEntity settings(){
        SettingsVo vo = settingService.findByUserId();
        return ResponseEntity.ok(vo);
    }

    @PostMapping("/notification/setting")
    public ResponseEntity updateNotification(@RequestBody SettingsVo vo) {
        settingService.updateNotification(vo);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/blacklist")
    public ResponseEntity blackListPage(@RequestBody @RequestParam(value = "page",defaultValue = "1") Long page,
                                        @RequestBody @RequestParam(value = "pagesize",defaultValue = "10") Long pageSize){
        PageResult<UserInfoVo> pageResult = settingService.blackListPage(page,pageSize);
        return ResponseEntity.ok(pageResult);
    }

    @DeleteMapping("/blacklist/{blackUserId}")
    public ResponseEntity removeBlackList(@PathVariable("blackUserId") Long blackUserId){
        settingService.removeBlackUser(blackUserId);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/updateQuestion")
    public ResponseEntity updateQuestion(@RequestBody Map<String,String> param){
        settingService.updateQuestion(param.get("content"));
        return ResponseEntity.ok(null);
    }

    @PostMapping("/phone/sendVerificationCode")
    public ResponseEntity sendVerificationCode(){
        settingService.sendVerificationCode();
        return ResponseEntity.ok(null);
    }

    @PostMapping("/phone/checkVerificationCode")
    public ResponseEntity checkValidateCode(@RequestBody Map<String,String> param){
        boolean verification = settingService.checkValidateCode(param.get("verificationCode"));
        Map<String,Boolean> map = new HashMap<>();
        map.put("verification",verification);
        return ResponseEntity.ok(map);
    }

    @PostMapping("/phone")
    public ResponseEntity updatePhone(@RequestBody Map<String,String> param,@RequestHeader("Authorization") String token){
        settingService.updatePhone(token,param.get("phone"));
        return ResponseEntity.ok(null);
    }

}
