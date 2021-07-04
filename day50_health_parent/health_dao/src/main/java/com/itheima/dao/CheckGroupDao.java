package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.entity.QueryPageBean;
import com.itheima.health.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CheckGroupDao {

    /**
     * 添加检查组
     * @param checkGroup
     * @return 影响的行数
     */
    int add(CheckGroup checkGroup);

    /**
     * 添加检查组和检查项到中间表
     * @param groupId
     * @param itemId
     * @return
     */
    int addCheckGroupItems(@Param("groupId") int groupId, @Param("itemId") int itemId);


    /**
     * 分页查询检查组
     * @param bean
     * @return
     */
    Page<CheckGroup> findPage(QueryPageBean bean);
}
