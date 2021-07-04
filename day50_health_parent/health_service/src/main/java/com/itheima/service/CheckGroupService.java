package com.itheima.service;

import com.itheima.health.pojo.CheckGroup;

public interface CheckGroupService {

    /**
     * 新增检查组
     * @param checkGroup 检查组的基本信息
     * @param checkitemIds 检查项的信息
     * @return
     */
    int add(CheckGroup checkGroup , int [] checkitemIds);

}
