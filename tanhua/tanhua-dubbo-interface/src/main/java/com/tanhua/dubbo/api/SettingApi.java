package com.tanhua.dubbo.api;

import com.tanhua.domain.db.Settings;
import com.tanhua.domain.vo.SettingsVo;

public interface SettingApi {

    Settings findByUserId(Long userId);

    int save(Settings settings);

}
