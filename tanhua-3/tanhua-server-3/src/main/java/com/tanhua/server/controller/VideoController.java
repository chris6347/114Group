package com.tanhua.server.controller;

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
    public ResponseEntity postVideo(MultipartFile videoThumbnail,MultipartFile videoFile){
        videoService.post(videoThumbnail,videoFile);
        return ResponseEntity.ok(null);
    }

    @GetMapping
    public ResponseEntity getVideo(@RequestParam(value = "page",defaultValue = "1") Long page,
                                   @RequestParam(value = "pagesize",defaultValue = "10") Long pageSize){
        PageResult<VideoVo> vos = videoService.getVideo(page,pageSize);
        return ResponseEntity.ok(vos);
    }

    @PostMapping("/{userId}/userFocus")
    public ResponseEntity focus(@PathVariable Long userId){
        videoService.focusUser(userId);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/{userId}/userUnFocus")
    public ResponseEntity unFocus(@PathVariable Long userId){
        videoService.unFocusUser(userId);
        return ResponseEntity.ok(null);
    }

}
