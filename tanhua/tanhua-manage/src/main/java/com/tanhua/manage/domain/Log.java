package com.tanhua.manage.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Log {

    // id
    private Long id;
    // 用户id
    private Long userId;
    // 操作时间
    private String logTime;
    // 登录地点
    private String place;
    // 登录设备
    private String equipment;
    // 操作类型
    // 0102登录,0101注册,0201发动态,0202浏览动态,0203动态点赞,0204动态喜欢
    // 0205为评论，0206为动态取消点赞，0207为动态取消喜欢，0301为发小视频，0302为小视频点赞，0303为小视频取消点赞，0304为小视频评论
    private String type;
    private Date created;

}
