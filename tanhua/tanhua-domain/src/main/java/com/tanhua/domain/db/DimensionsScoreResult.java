package com.tanhua.domain.db;

import lombok.Data;

@Data
public class DimensionsScoreResult {

    private Long dimensionId;
    // 作为myBatis返回结果,查询语句该字段需要取别名
    private Double scoreCount;

}
