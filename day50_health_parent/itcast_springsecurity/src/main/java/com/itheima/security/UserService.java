package com.itheima.security;


import com.itheima.health.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    1. 这是用于提供认证服务的类
    2. 它里面负责取查询用户，然后赋予这个用户什么样的权限。
    3. 真正的生产环境下， 应该是要查询数据库的，这里为了熟悉这种写法，所以先不查询数据库
        而是使用map集合来替代数据库。

    实现：
        1. 使用静态代码块，往map集合里面先装好若干个用户。
 */
public class UserService implements UserDetailsService {

    //这是一个map集合，用来保存用户对象，KEY: 用户名， value： 用户对象User
    static Map<String , User> map = new HashMap<>();

    static{

        BCryptPasswordEncoder pe = new BCryptPasswordEncoder();

        //第一个用户
        User user01 =new User();
        user01.setUsername("admin");
//        user01.setPassword("admin"); //明文
        user01.setPassword(pe.encode("admin")); //密文
        map.put(user01.getUsername() , user01);

        //第二个用户
        User user02 =new User();
        user02.setUsername("zhangsan");
//        user02.setPassword("123"); //明文
        user02.setPassword(pe.encode("123")); //密文
        map.put(user02.getUsername() , user02);

    }

    /**
     * 根据用户名来查询用户。它是用用户名来查的用户，不是用用户名和密码一起来查。
     * @param username 登录的时候，传递过来的用户名
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("来进行权限认证了：" + username);

        //1. 查询用户
        User user = map.get(username);

        //2. 判空
        if(user == null){ //表明没有这个用户
            return null;
        }

        //3. 有这个用户，给这个用户构建权限，也就是赋予这个用户什么样的权限。
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority("ROLE_ADMIN")); //赋予管理员权限
        list.add(new SimpleGrantedAuthority("add")); //赋予添加权限
        list.add(new SimpleGrantedAuthority("delete")); //赋予删除权限

        //验证通过之后，把用户的数据和用户的权限给返回给springsecurity。
        return new org.springframework.security.core.userdetails.User(user.getUsername() , user.getPassword(), list);
    }
}
