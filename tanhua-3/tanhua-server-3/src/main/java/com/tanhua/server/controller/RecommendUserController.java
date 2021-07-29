package com.tanhua.server.controller;

import com.tanhua.domain.vo.PageResult;
import com.tanhua.domain.vo.RecommendUserQueryParam;
import com.tanhua.domain.vo.RecommendUserVo;
import com.tanhua.server.service.RecommendUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tanhua")
public class RecommendUserController {

    @Autowired
    private RecommendUserService recommendService;

    @GetMapping("/todayBest")
    public ResponseEntity todayBest(){
        RecommendUserVo vo = recommendService.todayBest();
        return ResponseEntity.ok(vo);
    }

    @GetMapping("/recommend")
    public ResponseEntity recommendList(@RequestBody RecommendUserQueryParam queryParam){
        PageResult<RecommendUserVo> result = recommendService.recommendList(queryParam);
        return ResponseEntity.ok(result);
    }

}
