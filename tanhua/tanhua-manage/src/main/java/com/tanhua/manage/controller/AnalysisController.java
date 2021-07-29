package com.tanhua.manage.controller;

import com.tanhua.manage.service.AnalysisService;
import com.tanhua.manage.vo.AnalysisSummaryVo;
import com.tanhua.manage.vo.AnalysisUsersVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class AnalysisController {

    @Autowired
    private AnalysisService analysisService;

    @GetMapping("/summary")
    public ResponseEntity summary(){        // summary: 概括
        AnalysisSummaryVo vo = analysisService.getSummary();
        return ResponseEntity.ok(vo);
    }

    @GetMapping("/users")
    public ResponseEntity users(@RequestParam Long sd,@RequestParam Long ed,@RequestParam Integer type){
        AnalysisUsersVo vo = analysisService.getUsersCount(sd,ed,type);
        return ResponseEntity.ok(vo);
    }

}
