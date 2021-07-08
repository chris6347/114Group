package com.itheima.dao;

import com.itheima.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SetMealDao {

    /**
     * 查询所有图片
     * @return
     */
    List<String> findAllImg();

    /**
     * 保存套餐的基本信息
     * @param setmeal
     * @return
     */
    int add(Setmeal setmeal);

    /**
     * 保存套餐包含的检查组信息
     * @param setmealId
     * @param groupId
     * @return
     */
    int addSetMealCheckGroups(@Param("setmealId") int setmealId , @Param("groupId") int groupId);
}
