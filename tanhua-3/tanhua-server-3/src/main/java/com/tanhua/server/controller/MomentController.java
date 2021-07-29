package com.tanhua.server.controller;

import com.tanhua.domain.vo.MomentVo;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.domain.vo.PublishVo;
import com.tanhua.server.service.MomentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("")
public class MomentController {

    @Autowired
    private MomentService momentService;

    @PostMapping("/")
    public ResponseEntity postMoment(MultipartFile[] imageContent, PublishVo vo){
        momentService.postMoment(vo,imageContent);
        return ResponseEntity.ok(null);
    }

    @GetMapping
    public ResponseEntity queryFriendPublish(@RequestParam(value = "page",defaultValue = "1") Long page,
                                             @RequestParam(value = "pagesize",defaultValue = "10") Long pageSize){
        PageResult<MomentVo> pageResult = momentService.queryFriendPublishList(page,pageSize);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/recommend")
    public ResponseEntity recommendMoment(@RequestParam(value = "page",defaultValue = "1") Long page,
                                          @RequestParam(value = "pagesize",defaultValue = "10") Long pageSize){
        PageResult<MomentVo> pageResult = momentService.recommentMoment(page,pageSize);
        return ResponseEntity.ok(pageResult);
    }

    @GetMapping("/add")
    public ResponseEntity queryMyPublishList(@RequestParam(value = "page",defaultValue = "1") Long page,
                                             @RequestParam(value = "pagesize",defaultValue = "10") Long pageSize
                                             ,Long userId){
        PageResult<MomentVo> pageResult = momentService.queryMyPublishList(page,pageSize,userId);
        return ResponseEntity.ok(pageResult);
    }

}
