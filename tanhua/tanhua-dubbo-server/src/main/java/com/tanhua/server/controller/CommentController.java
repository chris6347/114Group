package com.tanhua.server.controller;

import com.tanhua.domain.mongo.Comment;
import com.tanhua.domain.vo.CommentVo;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.server.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping
    public ResponseEntity findPage(@RequestParam(value = "movementId") String movementId,
                                   @RequestParam(defaultValue = "1",required = false) Long page,
                                   @RequestParam(defaultValue = "10",required = false) Long pagesize){
        page = page < 1 ? 1 : page ;
        PageResult<CommentVo> pageResult = commentService.findPage(movementId,page,pagesize);
        return ResponseEntity.ok(pageResult);
    }

    @PostMapping
    public ResponseEntity add(@RequestBody Map<String,String> param){
        String movementId = param.get("movementId");
        String comment = param.get("comment");
        commentService.add(movementId,comment);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{commentId}/like")
    public ResponseEntity like(@PathVariable("commentId") String commentId){
        Long likeCount = commentService.likeComment(commentId);
        return ResponseEntity.ok(likeCount);
    }

    @GetMapping("/{commentId}/dislike")
    public ResponseEntity disLike(@PathVariable("commentId") String commentId) {
        Long likeCount = commentService.disLikeComment(commentId);
        return ResponseEntity.ok(likeCount);
    }

}
