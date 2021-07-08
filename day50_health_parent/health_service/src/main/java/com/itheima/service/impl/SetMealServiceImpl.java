package com.itheima.service.impl;

import com.itheima.dao.SetMealDao;
import com.itheima.health.pojo.Setmeal;
import com.itheima.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SetMealServiceImpl implements SetMealService {

    //注入dao
    @Autowired
    private SetMealDao dao;

    /**
     * 新增套餐 ，包含两份数据：基本信息，包含的检查组信息
     *  1. 基本信息保存到套餐表里面 t_setmeal
     *  2. 检查组信息保存在中间表  t_setmeal_checkgroup
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    @Override
    public int add(Setmeal setmeal, int[] checkgroupIds) {

        //1. 保存套餐基本信息
        int row1 = dao.add(setmeal);

        //2. 保存包含的检查组信息
        int row2 = 0 ;
        if(checkgroupIds !=null && checkgroupIds.length > 0 ){
            for (int checkgroupId : checkgroupIds) {
                row2 += dao.addSetMealCheckGroups(setmeal.getId() , checkgroupId);
            }
        }

        return row1 > 0 && row2 ==checkgroupIds.length ? 1 :  0;
    }
}
