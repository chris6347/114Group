package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanhua.domain.db.BlackList;
import com.tanhua.domain.vo.PageResult;
import com.tanhua.dubbo.mapper.BlackListMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BlackListApiImpl implements BlackListApi{

    @Autowired
    private BlackListMapper blackListMapper;

    @Override
    public PageResult findPageByUserId(Long page, Long pageSize, Long userId) {
        IPage<BlackList> iPage = new Page<>(page,pageSize);
        QueryWrapper<BlackList> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId).orderByDesc("created");
        blackListMapper.selectPage(iPage,wrapper);
        PageResult pageResult = new PageResult();
        pageResult.setItems(iPage.getRecords());
        pageResult.setPage(page);
        pageResult.setPagesize(pageSize);
        pageResult.setCounts(iPage.getTotal());
        pageResult.setPages(iPage.getPages());
        return pageResult;
    }

    @Override
    public void removeBlackUser(Long userId, Long blackUserId) {
        Map<String,Object> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("black_user_id",blackUserId);
        int i = blackListMapper.deleteByMap(map);
        log.info("删除了{}个被拉黑的人",i);
    }

    @Override
    public List<BlackList> findAllByUserId(Long queryUserId) {
        QueryWrapper<BlackList> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",queryUserId);
        return blackListMapper.selectList(wrapper);
    }

    @Override
    public void saveBlackUser(Long userId, Long unLoveUserId) {
        BlackList blackList = new BlackList();
        blackList.setUserId(userId);
        blackList.setBlackUserId(unLoveUserId);
        blackListMapper.insert(blackList);
    }

}
