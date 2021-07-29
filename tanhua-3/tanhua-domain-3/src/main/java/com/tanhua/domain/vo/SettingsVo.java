package com.tanhua.domain.vo;

import lombok.Data;

@Data
public class SettingsVo {
    private Long id;
    private String strangerQuestion;
    private String phone;
    private boolean likeNotification = true;
    private boolean pinglunNotification = true;
    private boolean gonggaoNotification = true;
}