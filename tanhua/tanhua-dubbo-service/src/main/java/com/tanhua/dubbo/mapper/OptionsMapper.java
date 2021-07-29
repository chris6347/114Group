package com.tanhua.dubbo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tanhua.domain.db.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OptionsMapper extends BaseMapper<Options> {

    @Select("select * from tb_options where question_id=#{questionsId}")
    List<Options> findByQuestionsId(Long questionsId);

    Double findScoreCount(List<Long> optionIds);

}
