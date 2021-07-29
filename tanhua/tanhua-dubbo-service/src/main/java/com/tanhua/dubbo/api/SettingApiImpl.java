package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.tanhua.domain.db.Settings;
import com.tanhua.domain.vo.SettingsVo;
import com.tanhua.dubbo.mapper.SettingMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class SettingApiImpl implements SettingApi{

    @Autowired
    private SettingMapper settingMapper;

    @Override
    public Settings findByUserId(Long userId) {
        QueryWrapper<Settings> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        return settingMapper.selectOne(wrapper);
    }

    @Override
    public int save(Settings settings) {
        Settings oldSettings = findByUserId(settings.getUserId());
        if (oldSettings!=null) {
            UpdateWrapper<Settings> wrapper = new UpdateWrapper<>();
            wrapper.eq("user_id", settings.getUserId());
            return settingMapper.update(settings, wrapper);
        }
        return settingMapper.insert(settings);
    }

}
