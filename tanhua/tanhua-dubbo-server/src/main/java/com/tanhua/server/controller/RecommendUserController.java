package com.tanhua.server.controller;

import com.tanhua.domain.vo.*;
import com.tanhua.server.service.RecommendUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/tanhua")  // recommend: 推荐
public class RecommendUserController {

    @Autowired
    private RecommendUserService recommendUserService;

    /*@Autowired
    private IMService imService;*/

    @GetMapping("/todayBest")
    public ResponseEntity todayBest(){
        RecommendUserVo vo = recommendUserService.todayBest();
        return ResponseEntity.ok(vo);
    }

    @GetMapping("/recommendation")
    public ResponseEntity recommendation(@RequestBody RecommendUserQueryParam queryParam){
        PageResult<RecommendUserVo> pageResult = recommendUserService.recommendation(queryParam);
        return ResponseEntity.ok(pageResult);
    }

    @GetMapping("/{userId}/personalInfo")
    public ResponseEntity getUserInfo(@PathVariable("userId") Long targetUserId){
        RecommendUserVo vo = recommendUserService.getUserInfo(targetUserId);
        return ResponseEntity.ok(vo);
    }

    @GetMapping("/strangerQuestions")
    public ResponseEntity strangerQuestions(Long userId){
        log.info("userId={}",userId);
        String questionTxt = recommendUserService.findQuestionsById(userId);
        return ResponseEntity.ok(questionTxt);
    }

    @PostMapping("/strangerQuestions")
    public ResponseEntity replyStrangerQuestions(@RequestBody Map<String,Object> param){
        recommendUserService.replyQuestions((Integer) param.get("userId"),(String) param.get("reply"));
        return ResponseEntity.ok(null);
    }

    @GetMapping("/search")
    public ResponseEntity searchNearUser(String gender,Integer distance){
        List<NearUserVo> vos = recommendUserService.searchNearUser(gender,distance);
        return ResponseEntity.ok(vos);
    }

    @GetMapping("/cards")
    public ResponseEntity swag(){
        RecommendUserVo vo = recommendUserService.swag();
        return ResponseEntity.ok(vo);
    }

    @GetMapping("/{loveUserId}/love")
    public ResponseEntity loveSwag(@PathVariable Long loveUserId){
        recommendUserService.loveSwag(loveUserId);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{unLoveUserId}/unlove")
    public ResponseEntity unLoveSwag(@PathVariable Long unLoveUserId){
        recommendUserService.unLoveSwag(unLoveUserId);
        return ResponseEntity.ok(null);
    }

}
