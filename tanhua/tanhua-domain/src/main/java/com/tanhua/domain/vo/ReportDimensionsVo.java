package com.tanhua.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class ReportDimensionsVo{

    private String conclusion;
    private String cover;

    private List<DimensionsVo> dimensions;
    private List<UserInfoVo> similarYou;

}
