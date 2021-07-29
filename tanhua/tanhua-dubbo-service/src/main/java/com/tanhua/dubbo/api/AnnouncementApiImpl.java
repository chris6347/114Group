package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanhua.domain.db.Announcement;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.dubbo.mapper.AnnouncementMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class AnnouncementApiImpl implements AnnouncementApi{

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Override
    public PageResult<Announcement> announcementsPage(Integer page, Integer pageSize) {
        IPage<Announcement> iPage = new Page<>(page,pageSize);
        announcementMapper.selectPage(iPage,null);
        PageResult<Announcement> pageResult = new PageResult<>();
        pageResult.setPage(iPage.getCurrent());
        pageResult.setPages(iPage.getPages());
        pageResult.setPagesize(iPage.getSize());
        pageResult.setCounts(iPage.getTotal());
        pageResult.setItems(iPage.getRecords());
        return pageResult;
    }
}
