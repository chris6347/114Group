package com.tanhua.domain.db;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("tb_user")
public class User extends BasePojo{

    private Long id;
    private String mobile;
    @JSONField(serialize = false)
    private String password; // 密码 json序列化时忽略 TODO

}
