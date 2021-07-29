package com.tanhua.server.controller;

import com.tanhua.domain.vo.CountsVo;
import com.tanhua.domain.vo.FriendVo;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.domain.vo.UserInfoVo;
import com.tanhua.server.service.UserInfoService;
import com.tanhua.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/users")
@RestController
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserService userService;

    // 用id查找userInfo
    @GetMapping("/findUserById")
    public ResponseEntity findUserById(@RequestHeader("Authorization") String token, Long userID,Long huanxinID){
        // userID和huanxinID暂不处理
        UserInfoVo userInfoVo = userInfoService.getLoginUserInfo(token);
        return ResponseEntity.ok(userInfoVo);
    }

    // 更新userInfo
    @PutMapping("/update")
    public ResponseEntity update(@RequestHeader("Authorization") String token,@RequestBody UserInfoVo vo){
        userInfoService.updateUserInfo(token,vo);
        return ResponseEntity.ok("更新成功");
    }

    @PostMapping("/header")
    public ResponseEntity updateAvatar(@RequestHeader("Authorization") String token, MultipartFile headPhoto){
        userInfoService.updateHeadPhoto(token,headPhoto);
        return ResponseEntity.ok("更新成功");
    }

    @GetMapping("/counts")
    public ResponseEntity counts(){
        CountsVo vo = userInfoService.counts();
        return ResponseEntity.ok(vo);
    }

    @GetMapping("/friends/{type}")
    public ResponseEntity queryUserLikeList(@PathVariable("type") Integer type,
                                            @RequestParam(value = "page",defaultValue = "1") Long page,
                                            @RequestParam(value = "pagesize",defaultValue = "10",required = false) Long pagesize,
                                            @RequestParam(value = "nickname",required = false) String nickname){
        PageResult<FriendVo> vos = userInfoService.queryUserLikeList(type,page,pagesize,nickname);
        return ResponseEntity.ok(vos);
    }

    // 喜欢请求
    @PostMapping("/fans/{fansId}")
    public  ResponseEntity fansLike(@PathVariable Long fansId) {
        userService.fansLike(fansId);
        return ResponseEntity.ok(null);
    }

}
