package com.tanhua.domain.db;

import lombok.Data;

import java.io.Serializable;

@Data
public class Report implements Serializable {
    private Long id;
    private Integer soulType;
    private String cover;
    private String conclusion;
    private Double minScore;
    private Double maxScore;
}
