package com.tanhua.server.controller;

import com.tanhua.domain.vo.PostSoulVo;
import com.tanhua.domain.vo.ReportDimensionsVo;
import com.tanhua.domain.vo.SoulVo;
import com.tanhua.server.service.SoulService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/testSoul")
public class SoulController {

    @Autowired
    private SoulService soulService;

    @GetMapping
    public ResponseEntity findAll(){
        List<SoulVo> vos = soulService.findAll();
        return ResponseEntity.ok(vos);
    }

    @PostMapping
    public ResponseEntity postAnswers(@RequestBody Map<String,List<PostSoulVo>> param){
        soulService.postAnswer(param.get("answers"));
        return ResponseEntity.ok(null);
    }

    @GetMapping("/report/{reportId}")
    public ResponseEntity getReport(@PathVariable Long reportId){
        ReportDimensionsVo vo = soulService.getReport(reportId);
        return ResponseEntity.ok(vo);
    }

}
