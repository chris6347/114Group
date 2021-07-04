package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.health.pojo.CheckGroup;

public interface CheckGroupService {

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
}
