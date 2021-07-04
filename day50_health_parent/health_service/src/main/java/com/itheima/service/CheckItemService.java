package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {

    /**
     * 添加检查项
     * @param checkItem
     * @return 影响的行数
     */
    int add(CheckItem checkItem);

    /**
     * 分页查询
     * @param queryPageBean 查询条件
     * @return PageResult
     */
    PageResult<CheckItem> findPage(QueryPageBean queryPageBean);


    /**
     * 删除检查项
     * @param id
     * @return 影响行数
     */
    int delete(int id);

    /**
     * 更新检查项
     * @param checkItem
     * @return 影响的行数
     */
    int update(CheckItem checkItem);

    /**
     * 查询所有的检查项
     * @return
     */
    List<CheckItem> findAll();
}
