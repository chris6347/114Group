package com.tanhua.domain.db;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
// 因为不希望他实例化所以抽象 , 一般抽取的类都抽象
public abstract class BasePojo implements Serializable {

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updated;

    @TableField(fill = FieldFill.INSERT)
    private Date created;

}
