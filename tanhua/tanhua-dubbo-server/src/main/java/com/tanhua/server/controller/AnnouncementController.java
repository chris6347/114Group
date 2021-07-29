package com.tanhua.server.controller;

import com.tanhua.domain.vo.AnnouncementVo;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.server.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @GetMapping("/announcements")
    public ResponseEntity announcementsPage(@RequestBody @RequestParam(value = "page",defaultValue = "1") Integer page,
                                            @RequestBody @RequestParam(value = "pagesize",defaultValue = "10") Integer pagesize){
        PageResult<AnnouncementVo> pageResult = announcementService.announcementsPage(page,pagesize);
        return ResponseEntity.ok(pageResult);
    }

}
