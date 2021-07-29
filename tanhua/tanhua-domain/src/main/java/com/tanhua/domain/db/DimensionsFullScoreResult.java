package com.tanhua.domain.db;

import lombok.Data;

@Data
public class DimensionsFullScoreResult {

    private Long dimensionId;
    // 查询语句也要起别名
    private Double fullCount;

}
