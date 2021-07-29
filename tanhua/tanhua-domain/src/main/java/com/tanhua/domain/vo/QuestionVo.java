package com.tanhua.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class QuestionVo implements Serializable {

    private Long id;
    private Long userId;
    // 问题内容
    private String txt;

}
