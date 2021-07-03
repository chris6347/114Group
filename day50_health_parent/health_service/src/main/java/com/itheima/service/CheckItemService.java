package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.health.pojo.CheckItem;

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
}
