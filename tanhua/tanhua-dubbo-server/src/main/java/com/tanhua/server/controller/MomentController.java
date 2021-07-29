package com.tanhua.server.controller;

import com.tanhua.domain.vo.MomentVo;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.domain.vo.PublishVo;
import com.tanhua.domain.vo.VisitorVo;
import com.tanhua.server.service.CommentService;
import com.tanhua.server.service.MomentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/movements")
public class MomentController {

    @Autowired
    private MomentService momentService;

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity postMoment(PublishVo vo, MultipartFile[] imageContent){
        momentService.postMoment(vo,imageContent);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/query")
    public ResponseEntity queryFriendPublishList(@RequestParam(defaultValue = "1") long page,@RequestParam(defaultValue = "10") long pageSize){
        page = pageSize < 1 ? 1 : page;
        PageResult<MomentVo> pageResult = momentService.queryFriendPublishList(page,pageSize);
        return ResponseEntity.ok(pageResult);
    }

    @GetMapping("/recommend")
    public ResponseEntity queryRecommendPublishList(@RequestParam(defaultValue = "1") long page,@RequestParam(defaultValue = "10") long pageSize){
        page = page < 1 ? 1 : page ;
        PageResult<MomentVo> pageResult = momentService.queryRecommendPublishList(page,pageSize);
        return ResponseEntity.ok(pageResult);
    }

    @GetMapping("/all")
    public ResponseEntity queryMyPublishList(@RequestParam(defaultValue = "1") Long page,
                                             @RequestParam(defaultValue = "10") Long pagesize,
                                             @RequestParam(required = false) Long userId){
        page = page < 1 ? 1 : page ;
        PageResult<MomentVo> pageResult = momentService.queryMyPublishList(userId,page,pagesize);
        return ResponseEntity.ok(pageResult);
    }

    @GetMapping("/{publishId}/like")
    public ResponseEntity like(@PathVariable("publishId") String publishId){
        long likeCount = commentService.like(publishId);
        return ResponseEntity.ok(likeCount);
    }

    @GetMapping("/{publishId}/dislike")
    public ResponseEntity disLike(@PathVariable("publishId") String publishId){
        long likeCount = commentService.disLike(publishId);
        return ResponseEntity.ok(likeCount);
    }

    @GetMapping("/{publishId}/love")
    public ResponseEntity love(@PathVariable("publishId") String publishId) {
        long loveCount = commentService.love(publishId);
        return ResponseEntity.ok(loveCount);
    }

    @GetMapping("/{publishId}/unlove")
    public ResponseEntity unLove(@PathVariable("publishId") String publishId){
        long loveCount = commentService.unLove(publishId);
        return ResponseEntity.ok(loveCount);
    }

    @GetMapping("/{publishId}")
    public ResponseEntity findById(@PathVariable("publishId") String publishId){
        if ("visitors".equals(publishId)) {
            return ResponseEntity.ok(null);
        }
        MomentVo vo = momentService.findById(publishId);
        return ResponseEntity.ok(vo);
    }

    @GetMapping("/visitors")
    public ResponseEntity movementVisitor(){
        List<VisitorVo> vos = momentService.queryVisitors();
        return ResponseEntity.ok(vos);
    }

}
