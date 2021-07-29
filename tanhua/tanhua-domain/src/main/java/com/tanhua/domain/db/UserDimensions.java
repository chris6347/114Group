package com.tanhua.domain.db;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserDimensions implements Serializable {

    private Long id;
    private Long userId;
    private Long soulId;
    private Long dimensionId;
    private Double rate;

    @TableField(exist = false)
    private Long questionId;

}
