package com.tanhua.domain.db;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public abstract class BasePojo implements Serializable {

    @TableField(fill = FieldFill.INSERT)        // 插入时自动填充
    private Date created;

    @TableField(fill = FieldFill.INSERT_UPDATE)   // 插入和修改时自动填充
    private Date updated;

}
