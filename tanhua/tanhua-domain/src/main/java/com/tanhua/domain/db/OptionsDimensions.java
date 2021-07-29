package com.tanhua.domain.db;

import lombok.Data;

import java.io.Serializable;

@Data
public class OptionsDimensions implements Serializable {

    private Long id;
    private Long optionId;
    private Double score;
    private Integer dimensionId;

}
