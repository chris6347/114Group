package com.tanhua.dubbo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tanhua.domain.db.DimensionsFullScoreResult;
import com.tanhua.domain.db.Questions;

import java.util.List;

public interface QuestionsMapper extends BaseMapper<Questions> {
    List<DimensionsFullScoreResult> findFullCount(List<Long> questionIds);
}
