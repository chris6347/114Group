package com.tanhua.domain.db;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserAnswer implements Serializable {

    private Long id;
    private Long userId;
    private Long optionId;
    private Integer levelId;
    private Long questionId;

}
