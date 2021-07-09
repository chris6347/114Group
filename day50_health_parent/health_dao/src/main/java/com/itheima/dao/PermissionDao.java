package com.itheima.dao;

import com.itheima.health.pojo.Permission;

import java.util.Set;

public interface PermissionDao {
    /**
     * 根据角色的id来查询它有哪些权限。
     * @param rid
     * @return
     */
    Set<Permission> findPermissionByRid(int rid);
}
