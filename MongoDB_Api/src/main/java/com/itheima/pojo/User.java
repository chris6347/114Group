package com.itheima.pojo;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
//Document 与mongodb的collection集合做映射
// 默认是表名
@Document//(collection = "user")
public class User {

    // 自动映射mongodb的_id   用类型映射
    @Id
    private Integer id;

    @Indexed
    private String username;
    private int age;
    private String address;

}
