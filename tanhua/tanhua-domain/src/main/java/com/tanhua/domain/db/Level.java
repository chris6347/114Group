package com.tanhua.domain.db;

import lombok.Data;

import java.io.Serializable;

@Data
public class Level implements Serializable {

    private Integer id;
    private String type;

}
