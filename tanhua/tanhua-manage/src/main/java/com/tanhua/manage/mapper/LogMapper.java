package com.tanhua.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tanhua.manage.domain.Log;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LogMapper extends BaseMapper<Log> {

    // 字符串判断大小: 字典顺序 判断asc码
    @Select("select count(distinct user_id) from tb_log where log_time>#{date}")
    Long countActiveUserAfterDate(String date);

    // 注册数
    @Select("select count(distinct user_id) from tb_log where type='0101' and log_time=#{date}")
    Long selectRegisterCount(String date);

    // 登录数
    @Select("select count(distinct user_id) from tb_log where type='0102' and log_time=#{date}")
    Long selectLoginCount(String date);

    // 次日留存,昨天注册的,今天活跃了
    // 注册
    @Select("select distinct user_id from tb_log where type='0101' and log_time=#{date}")
    List<Long> selectRegisterUserId(String date);

    //@Select("select count(distinct user_id) from tb_log where type!='0101' and log_time=#{date} and user_id in #{ids}")
    Long selectRetention1dCount(@Param("date") String date,@Param("ids") List<Long> ids);

    // 活跃
    @Select("select count(distinct user_id) from tb_log where type!='0101' and log_time=#{date}")
    Long selectActiveCount(String date);

}
