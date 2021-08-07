package com.itheima.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

//@TableName("user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    // 表中主键
    // value:表中主键字段名
    // type:主键的生成策略
    @TableId(value = "id",type = IdType.ID_WORKER)
    // ID_WORKER  UUID  ID_WORKER_STR  这三种只有当插入对象id为空才自动填充
    // ID_WORKER: 生成全局唯一,有序的id,雪花算法
    // AUTO: 数据库ID自增
    // NONE:未设置主键类型
    // INPUT: 通过自己注册自动填充插件进行填充
    // 默认使用ID_WORKER
    private Long id;
    // 属性名要用小驼峰
    @TableField("user_name")
    private String userName;
    private String password;
    private Integer age;
    private String name;
    private String email;

    @TableField(exist = false) // 忽略这个列,数据库没有这个列
                               // value=列名
    private String abc;

}
