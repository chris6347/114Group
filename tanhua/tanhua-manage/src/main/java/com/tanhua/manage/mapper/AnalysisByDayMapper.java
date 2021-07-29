package com.tanhua.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tanhua.manage.domain.AnalysisByDay;
import com.tanhua.manage.vo.DataPointVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AnalysisByDayMapper extends BaseMapper<AnalysisByDay> {

    @Select("select sum(num_registered) from tb_analysis_by_day")
    Long totalUserCount();

    @Select("select * from tb_analysis_by_day where record_date=#{today}")
    AnalysisByDay findByDate(String today);

    // date_format(record_date,'%Y-%m-%d') : 将日期转为字符串   Y大写表示为4位数
    @Select("select date_format(record_date,'%Y-%m-%d') title,num_${type} amount from tb_analysis_by_day where record_date between #{sd} and #{ed}")
    List<DataPointVo> findBetweenDate(@Param("type") String type, @Param("sd") String sd, @Param("ed") String ed);

}