package com.tanhua.domain.mongo;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document(collection = "user_location")
public class UserLocation implements Serializable {
    @Id
    private ObjectId id;
    @Indexed
    private Long userId;
    // 反序列化时会失败
    private GeoJsonPoint location; //x:经度 y:纬度
    private String address; // 位置描述
    private Long created;
    private Long updated;
}
