package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.health.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {

    /**
     * 查询所有
     * @return
     */
    List<CheckGroup> findAll();
    /**
     * 更新检查组
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    int update(CheckGroup checkGroup , int [] checkitemIds);


    /**
     * 新增检查组
     * @param checkGroup 检查组的基本信息
     * @param checkitemIds 检查项的信息
     * @return
     */
    int add(CheckGroup checkGroup , int [] checkitemIds);


    /**
     * 分页查询检查组
     * @param bean
     * @return
     */
    PageResult<CheckGroup> findPage(QueryPageBean bean);


    /**
     * 根据组id查询所有的检查项
     * @param groupId
     * @return
     */
    List<Integer> findItemsByGroupId(int groupId);
}
