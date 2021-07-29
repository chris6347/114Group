package com.tanhua.server.controller;

import com.tanhua.domain.vo.ContactVo;
import com.tanhua.domain.vo.MessageVo;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.server.service.IMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/messages")
public class IMController {

    @Autowired
    private IMService imService;

    @PostMapping("/contacts")
    public ResponseEntity addContacts(@RequestBody Map<String,Integer> param){
        imService.addContacts(param.get("userId"));
        return ResponseEntity.ok(null);
    }

    @GetMapping("/contacts")
    public ResponseEntity findFriendPage(@RequestParam(value = "page",required = false,defaultValue = "1") Long page,
                                         @RequestParam(value = "pagesize",required = false,defaultValue = "10") Long pagesize,
                                         String keyword){
        page = page < 1 ? 1 : page ;
        PageResult<ContactVo> pageResult = imService.findFriendPage(page,pagesize,keyword);
        return ResponseEntity.ok(pageResult);
    }

    @GetMapping("/likes")
    public ResponseEntity likes(@RequestParam(value = "page",required = false,defaultValue = "1") Long page,
                                @RequestParam(value = "pagesize",required = false,defaultValue = "10") Long pagesize){
        PageResult<MessageVo> pageResult = imService.messageCommentList(1,page,pagesize);
        return ResponseEntity.ok(pageResult);
    }

    @GetMapping("/comments")
    public ResponseEntity comments(@RequestParam(value = "page",required = false,defaultValue = "1") Long page,
                                @RequestParam(value = "pagesize",required = false,defaultValue = "10") Long pagesize){
        PageResult<MessageVo> pageResult = imService.messageCommentList(2,page,pagesize);
        return ResponseEntity.ok(pageResult);
    }

    @GetMapping("/loves")
    public ResponseEntity loves(@RequestParam(value = "page",required = false,defaultValue = "1") Long page,
                                @RequestParam(value = "pagesize",required = false,defaultValue = "10") Long pagesize){
        PageResult<MessageVo> pageResult = imService.messageCommentList(3,page,pagesize);
        return ResponseEntity.ok(pageResult);
    }

}
