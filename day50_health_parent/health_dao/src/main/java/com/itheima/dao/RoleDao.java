package com.itheima.dao;

import com.itheima.health.pojo.Role;

import java.util.Set;

public interface RoleDao {

    /**
     * 根据用户的id来查询角色信息，一个用户可能有多个角色
     * @param uid
     * @return
     */
    Set<Role> findRoleByUid(int uid);
}
