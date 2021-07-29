package com.tanhua.domain.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "voice")
public class Voice implements Serializable {

    @Id
    private ObjectId id;
    private Long userId;
    private String soundUrl;

    public Voice(Long userId,String soundUrl){
        this.userId = userId;
        this.soundUrl = soundUrl;
    }

}
