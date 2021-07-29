package com.tanhua.dubbo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tanhua.domain.db.Soul;
import org.apache.ibatis.annotations.Select;

public interface SoulMapper extends BaseMapper<Soul> {

    @Select("select max(level_id) from tb_soul where id in (select soul_id from tb_user_report where user_id = #{userId})")
    Integer findMaxLevel(Long userId);

}
