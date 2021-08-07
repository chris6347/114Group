package com.itheima.test;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.Update;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.dao.UserMapper;
import com.itheima.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.management.Query;
import java.util.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelectById(){
        User user = userMapper.selectById(1);
        System.out.println(user);
    }

    @Test
    public void testInsertUser(){
        User user = new User();
        user.setUserName("lichao3");
        user.setPassword("xueyan3");
        int count = userMapper.insert(user);
        System.out.println(user.getId());
        System.out.println(count);
    }

    @Test
    public void testDeleteById(){
        int count = userMapper.deleteById(1412279071270596615L);
        System.out.println(count);
    }

    @Test
    public void testDeleteBatchIds(){
        List ids = new ArrayList();
        Collections.addAll(ids,1412279071270596614L,1412279071270596613L);
        int count = userMapper.deleteBatchIds(ids);
        System.out.println(count);
    }

    @Test
    public void testDeleteByMap(){
        Map<String, Object> map = new HashMap<>();
        // delete from tb_user where user_name = ? and age = ?
        map.put("user_name","sunqi");
        map.put("age",24);
        userMapper.deleteByMap(map);
    }

    @Test
    public void testUpdateById(){
        User user = new User();
        user.setId(2L);
        user.setPassword("111111");
        int count = userMapper.updateById(user);
    }

    @Test
    public void testUpdateEntity(){
        User user = new User();
        user.setUserName("zhaoli");
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",4);
        updateWrapper.set("email",null);
        userMapper.update(user,updateWrapper);
    }

    @Test
    public void testSelectPage(){
        int current = 1; //当前页数
        int size = 2;    //每页条数
        IPage<User> page = new Page<>(current,size);
        userMapper.selectPage(page,null);

        List<User> records = page.getRecords(); //当前页数据
        long pages = page.getPages();  //总页数
        long total = page.getTotal();
        System.out.println(records);
        System.out.println(pages);
        System.out.println(total);
    }

    @Test
    public void testWrapper1(){
        // 创建查询构建器
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        // 设置条件
        wrapper.eq("user_name","lisi").lt("age",23).in("name","李四","王五");
        List<User> users = userMapper.selectList(wrapper);
        System.out.println(users);
    }

    @Test
    public void testOr(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("password",123456).or().lt("age",21).in("user_name","lisi");
        List<User> users = userMapper.selectList(queryWrapper);
        System.out.println(users);
    }

    @Test
    public void testWrapper3(){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.likeLeft("user_name","zhang");
        // select * from .. where user_name like %zhang
        List<User> users = userMapper.selectList(wrapper);
        System.out.println(users);
    }

    @Test
    public void testWrapper4(){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.between("age",20,28).orderByDesc("age");
        System.out.println(userMapper.selectList(wrapper));
    }

    @Test
    public void testWrapper5(){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("id","user_name")
                .eq("user_name","lisi").or().lt("age",23)
                .in("name","李四","王五")
                .orderBy(true,true,"age")
                ;
        // select id,user_name from tb_user where user_name = lisi or age<23 and name in ("","")
        //      order by age asc
        List<User> users = userMapper.selectList(wrapper);
        System.out.println(users);
    }

    @Test
    public void testWrapper6(){
        IPage<User> page = new Page<>(1,10);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.in("password",123456,"itheima").le("age",21);
        userMapper.selectPage(page,wrapper);
        System.out.println(page.getRecords());
    }

    @Test
    public void testWrapper7(){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName,"zhangsan");
        List<User> users =userMapper.selectList(wrapper);
        System.out.println(users);
    }

    @Test
    public void testDeleteByUserName(){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName,"bbb");
        int count = userMapper.delete(wrapper);
        System.out.println(count);
    }

    @Test
    public void testWrapper9(){
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getUserName,"zhangsan").set(User::getPassword,"333333");
        userMapper.update(new User(),wrapper);
    }

    @Test
    public void testWrapper10(){
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.eq("user_name","lisi");
        User user = new User();
        user.setPassword("3333");
        user.setAge(33);
        userMapper.update(user,wrapper);
    }

}
