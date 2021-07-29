package com.tanhua.server.controller;

import com.tanhua.domain.vo.SettingsVo;
import com.tanhua.server.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class SettingsController {

    @Autowired
    private SettingsService settingsService;

    @GetMapping("/settings")
    public ResponseEntity findSettings(){
        SettingsVo vo = settingsService.findSettings();
        return ResponseEntity.ok(vo);
    }

    @PostMapping("/notification/setting")
    public ResponseEntity updateNotification(@RequestBody SettingsVo vo){
        settingsService.updateNotification(vo);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/questions")
    public ResponseEntity updateQuestion(@RequestBody Map<String,String> param){
        settingsService.updateQuestion(param.get("content"));
        return ResponseEntity.ok(null);
    }

}
