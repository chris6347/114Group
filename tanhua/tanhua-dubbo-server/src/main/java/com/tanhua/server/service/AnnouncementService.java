package com.tanhua.server.service;

import com.tanhua.domain.db.Announcement;
import com.tanhua.domain.vo.AnnouncementVo;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.dubbo.api.AnnouncementApi;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AnnouncementService {

    @Reference
    private AnnouncementApi announcementApi;

    public PageResult<AnnouncementVo> announcementsPage(Integer page,Integer pagesize){
        PageResult<Announcement> pageResult = announcementApi.announcementsPage(page,pagesize);
        List<Announcement> items = pageResult.getItems();
        List<AnnouncementVo> list = new ArrayList<>();
        items.forEach(announcement -> {
            AnnouncementVo vo = new AnnouncementVo();
            if (null != announcement.getCreated()) {
                vo.setCreateDate(new SimpleDateFormat("yyyy-MM-dd hh:mm").format(announcement.getCreated()));
            } else {
                vo.setCreateDate(new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date()));
            }
            BeanUtils.copyProperties(announcement,vo);
            list.add(vo);
        });
        PageResult<AnnouncementVo> result = new PageResult<>();
        result.setPage(pageResult.getPage());
        result.setPages(pageResult.getPages());
        result.setPagesize(pageResult.getPagesize());
        result.setCounts(pageResult.getCounts());
        result.setItems(list);
        return result;
    }

}
