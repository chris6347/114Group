package com.tanhua.server.controller;

import com.tanhua.domain.mongo.Video;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.domain.vo.VideoVo;
import com.tanhua.server.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/smallVideos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @PostMapping
    public ResponseEntity post(MultipartFile videoThumbnail,MultipartFile videoFile){
        System.out.println(videoFile.getSize());
        System.out.println(videoThumbnail.getSize());
        videoService.post(videoThumbnail,videoFile);
        return ResponseEntity.ok(null);
    }

    @GetMapping
    public ResponseEntity findPage(@RequestParam("page") Long page,
                                   @RequestParam("pagesize") Long pagesize){
        PageResult<VideoVo> pageResult = videoService.findPage(page,pagesize);
        return ResponseEntity.ok(pageResult);
    }

    @PostMapping("/{userId}/userFocus")
    public ResponseEntity followUser(@PathVariable("userId") Long targetUserId){
        videoService.followUser(targetUserId);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/{userId}/userUnFocus")
    public ResponseEntity unfollowUser(@PathVariable("userId") Long targetUserId){
        videoService.unfollowUser(targetUserId);
        return ResponseEntity.ok(null);
    }

}
