package com.itheima.security;

import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/*
    1. 这是一个用于提供认证授权的服务类
    2. 它要去关联dao 查询出来用户的数据，并且赋予这个用户拥有哪些权限。
    3. 把这个类交给spring管理，这样才能注入进来service层里面的UserService
 */
@Component
public class SpringSecurityUserService implements UserDetailsService {


    @Autowired
    private UserService userService;

    /**
     * 根据用户名找用户
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //1. 查询数据库
        User user = userService.findUserByUsername(username);

        //2. 不一定能找到用户
        if(user == null){ //没有这个用户
            return null;
        }

        //3. 如果找到了这个用户，那么在这里构建授权，权限集合。
        List<GrantedAuthority> list = new ArrayList<>();

        //3.1 把用户的权限给拿出来，封装到上面的list集合中去。
        //3.1.1 得到用户的角色信息
        Set<Role> roles = user.getRoles();
        //3.1.2 遍历每一种角色
        for (Role role : roles) {
            //3.1.3 角色身上有权限，所以拿出来这个角色所有权限
            Set<Permission> permissions = role.getPermissions();
            //3.1.4 遍历每一种权限。
            for (Permission permission : permissions) {

                //3.1.5 把权限装到集合中去。
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }
        //4. 返回用户的对象： 用户名、密码、权限的集合
        return new org.springframework.security.core.userdetails.User(user.getUsername() , user.getPassword() , list);
    }
}
