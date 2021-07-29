package com.tanhua.domain.db;

import lombok.Data;

import java.io.Serializable;

@Data
public class Soul implements Serializable {

    private Long id;
    private String name;
    private String cover;
    private Integer levelId;
    private Integer star;
    private Integer type;

}
