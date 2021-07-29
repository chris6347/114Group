package com.tanhua.dubbo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tanhua.domain.db.UserDimensions;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface UserDimensionsMapper extends BaseMapper<UserDimensions> {

    int insertUserDimensions(List<UserDimensions> userDimensionsList);

    @Update("update tb_user_dimensions set rate = #{rate} " +
            "where user_id=#{userId} and soul_id=(select soul_id from tb_questions where id = #{questionId}) and dimension_id = #{dimensionId}")
    int updateUserDimensions(UserDimensions userDimensions);

}
