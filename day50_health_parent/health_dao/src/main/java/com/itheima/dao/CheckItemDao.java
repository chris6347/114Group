package com.itheima.dao;

import com.itheima.entity.QueryPageBean;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {

    /**
     * 查询所有
     * @return
     */
    List<CheckItem> findAll();


    /**
     * 添加检查项
     * @param checkItem
     * @return 影响的行数
     */
    int add(CheckItem checkItem);


    /**
     * 更新检查项
     * @param checkItem
     * @return
     */
    int update(CheckItem checkItem);


    /**
     * 查询表的总记录数 ，但是不能盲目的认为就是整张表的总记录数，因为可能会有附带的查询条件。
     * @param queryPageBean
     * @return
     */
    long findCount(QueryPageBean queryPageBean);


    /**
     * 查询当前页的集合数据
     * @param queryPageBean
     * @return
     */
    List<CheckItem> findPage(QueryPageBean queryPageBean);


    /**
     * 根据检查项的id值查询是否有被检查组使用
     * @param id
     * @return 被检查组使用的总数
     */
    long findCountById (int id);

    /**
     * 根据id删除检查项
     * @param id
     * @return
     */
    int delete(int id);
}
