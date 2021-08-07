package com.itheima.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@TableName(value = "tb_user") //指定表名
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String userName;
    private String password;
    private String name;
    @TableField(exist = false)
    private int age;
    @TableField("email")
    private String email;

}
