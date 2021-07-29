package com.tanhua.dubbo.api;

import com.tanhua.domain.db.Settings;

public interface SettingsApi {
    Settings findSettingsById(Long userId);

    void updateNotification(Settings settings);
}
