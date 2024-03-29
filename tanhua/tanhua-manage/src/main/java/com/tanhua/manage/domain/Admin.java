package com.tanhua.manage.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin{
    /**
     * id
     */
    private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    @JSONField(serialize = false)
    private String password;
    /**
     * 头像
     */
    private String avatar;
    /**
     * token
     */
    @TableField(exist = false)
    private String token;
}
