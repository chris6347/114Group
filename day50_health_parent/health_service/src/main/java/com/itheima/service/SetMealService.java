package com.itheima.service;

import com.itheima.health.pojo.Setmeal;

public interface SetMealService {

    /**
     * 新增套餐
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    int add(Setmeal setmeal , int [] checkgroupIds);
}
