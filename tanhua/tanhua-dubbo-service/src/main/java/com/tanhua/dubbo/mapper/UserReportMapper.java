package com.tanhua.dubbo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tanhua.domain.db.UserReport;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface UserReportMapper extends BaseMapper<UserReport> {

    @Insert("insert into tb_user_report values(null,#{userId},(select soul_id from tb_questions where id = #{questionId} ),#{score},#{reportId})")
    int postAnswer(UserReport userReport);

    @Update("update tb_user_report set score = #{score},report_id = #{reportId} where user_id=#{userId} and soul_id = (select soul_id from tb_questions where id = #{questionId} )")
    int updateAnswer(UserReport userReport);

    @Select("select * from tb_user_report where user_id = #{userId} and soul_id = (select soul_id from tb_questions where id = #{questionId})")
    UserReport selectReport(@Param("userId") Long userId,@Param("questionId") Long questionId);

}
