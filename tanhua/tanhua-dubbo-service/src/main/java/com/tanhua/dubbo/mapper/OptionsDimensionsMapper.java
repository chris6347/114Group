package com.tanhua.dubbo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tanhua.domain.db.DimensionsScoreResult;
import com.tanhua.domain.db.OptionsDimensions;

import java.util.List;
import java.util.Map;

public interface OptionsDimensionsMapper extends BaseMapper<OptionsDimensions> {
    List<DimensionsScoreResult> findScoreCount(List<Long> optionIds);
}
