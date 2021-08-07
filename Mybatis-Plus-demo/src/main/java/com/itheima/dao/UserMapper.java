package com.itheima.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.pojo.User;


/*
* 使用mp定义Mapper,需要让Mapper接口 继承 BaseMapper接口
* */
public interface UserMapper extends BaseMapper<User> {}
