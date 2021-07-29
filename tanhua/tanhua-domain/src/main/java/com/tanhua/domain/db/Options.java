package com.tanhua.domain.db;

import lombok.Data;

import java.io.Serializable;

@Data
public class Options implements Serializable {

    private Long id;
    private String optionId; // A B C D E F ...
    private Long questionId;
    private String option; // 选项内容
    private Double score;

}
