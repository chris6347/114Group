package com.tanhua.domain.db;

import lombok.Data;

import java.io.Serializable;

@Data
public class Questions implements Serializable {

    private Long id;
    private Long soulId;
    private String question;
    private Integer dimensionId;
    private Double fullScore;

}
