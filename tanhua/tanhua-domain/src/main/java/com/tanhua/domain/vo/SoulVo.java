package com.tanhua.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class SoulVo {

    private String id;
    private String name;
    private String cover;
    private String level;
    private Integer star;

    private Integer isLock;
    private String reportId;

    private List<QuestionsVo> questions;

}
