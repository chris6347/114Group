package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.entity.QueryPageBean;
import com.itheima.health.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CheckGroupDao {

    /**
     * 查询所有
     * @return
     */
    List<CheckGroup> findAll();

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


    /**
     * 根据组id查询所有的检查项
     * @param groupId
     * @return
     */
    List<Integer> findItemsByGroupId(int groupId);


    /**
     * 更新基本信息
     * @param checkGroup
     * @return
     */
    int update(CheckGroup checkGroup);

    /**
     * 根据组的id删除检查项数据
     * @param groupId
     * @return
     */
    int deleteItemsByGroupId(int groupId);


    /**
     * 添加检查组的检查项信息
     * @param groupId
     * @param itemId
     * @return
     */
    int addGroupItems(@Param("groupId") int groupId , @Param("itemId") int itemId);
}
