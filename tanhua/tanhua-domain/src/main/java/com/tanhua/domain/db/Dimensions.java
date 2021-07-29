package com.tanhua.domain.db;

import lombok.Data;

import java.io.Serializable;

@Data
public class Dimensions implements Serializable {

    private Long id;
    private String dimension;

}
