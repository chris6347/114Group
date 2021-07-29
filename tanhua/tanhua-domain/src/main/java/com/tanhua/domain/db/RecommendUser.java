package com.tanhua.domain.db;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document(collection = "recommend_user")
public class RecommendUser implements Serializable {

    @Id  // 文档的唯一标识,在mongoDB中为ObjectId,他是唯一的
    private ObjectId id;  // 主键id
    @Indexed
    private Long userId;  // 推荐的用户
    private Long toUserId;  // 当前用户id
    @Indexed  // 声明该字段需要索引,可以大大提高查询效率
    private Double score = 0d;  // 推荐得分
    private String date;  // 日期

}
