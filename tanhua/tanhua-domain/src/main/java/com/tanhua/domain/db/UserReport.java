package com.tanhua.domain.db;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_user_report")
public class UserReport implements Serializable {

    private Long id;
    private Long userId;
    private Long soulId;
    private Double score;
    private Long reportId;

    @TableField(exist = false)
    private Long questionId;

}
