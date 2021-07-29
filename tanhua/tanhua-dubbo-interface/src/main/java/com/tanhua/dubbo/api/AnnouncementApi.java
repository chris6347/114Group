package com.tanhua.dubbo.api;

import com.tanhua.domain.db.Announcement;
import com.tanhua.domain.vo.PageResult;


public interface AnnouncementApi {

    PageResult<Announcement> announcementsPage(Integer page, Integer pageSize);

}
