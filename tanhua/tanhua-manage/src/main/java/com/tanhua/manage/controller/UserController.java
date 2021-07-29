package com.tanhua.manage.controller;

import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.manage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manage")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity findPage(@RequestParam(value = "page",defaultValue = "1",required = false)Long page,
                                   @RequestParam(value = "pagesize",defaultValue = "10",required = false)Long pagesize){
        page = page < 1 ? 1 : page;
        pagesize = pagesize > 50 ? 50 : pagesize;
        pagesize = pagesize < 2 ? 2 : pagesize;
        PageResult<UserInfo> pageResult = userService.findPage(page,pagesize);
        return ResponseEntity.ok(pageResult);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity findUserDetail(@PathVariable("userId")Long userId){
        UserInfo userInfo = userService.findUserDetail(userId);
        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/videos")
    public ResponseEntity findAllVideos(@RequestParam(defaultValue = "1",required = false)int page,@RequestParam(defaultValue = "10",required = false)int pagesize,@RequestParam(required = false)Long userId){
        return userService.findAllVideos(page,pagesize,userId);
    }

    @GetMapping("/messages")
    public ResponseEntity findAllMovements(@RequestParam(defaultValue = "1",required = false) int page,
                                           @RequestParam(defaultValue = "10",required = false) int pagesize,
                                           @RequestParam(required = false) Long userId,
                                           @RequestParam(required = false) String state){
        return userService.findAllMovements(page,pagesize,userId,0);
    }

    @GetMapping("/messages/{publishId}")
    public ResponseEntity findMovementById(@PathVariable("publishId")String publishId) {
        return userService.findMovementById(publishId);
    }

    @GetMapping("/messages/comments")
    public ResponseEntity findAllComments(@RequestParam(defaultValue = "1",required = false) int page,
                                          @RequestParam(defaultValue = "10",required = false) int pagesize,
                                          String messageID) {
        return userService.findAllComments(page,pagesize,messageID);
    }

}
