package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.tanhua.domain.db.Settings;
import com.tanhua.dubbo.mapper.SettingsMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class SettingsApiImpl implements SettingsApi{

    @Autowired
    private SettingsMapper settingsMapper;

    @Override
    public Settings findSettingsById(Long userId) {
        QueryWrapper<Settings> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        return settingsMapper.selectOne(wrapper);
    }

    @Override
    public void updateNotification(Settings settings) {
        Settings settingsById = findSettingsById(settings.getId());
        if (null != settingsById) {
            settings.setId(settingsById.getId());
            settingsMapper.updateById(settings);
            return;
        }
        settingsMapper.insert(settings);
    }

}
