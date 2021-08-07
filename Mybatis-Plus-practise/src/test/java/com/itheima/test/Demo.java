package com.itheima.test;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.dao.UserDao;
import com.itheima.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Demo {

    @Autowired
    private UserDao userDao;

    @Test
    public void testSelectById(){
        User user = userDao.selectById(1);
        System.out.println(user);
    }

    @Test
    public void testInsertUser(){
        User user = new User();
        user.setUserName("lc");
        user.setPassword("xy");
        int count = userDao.insert(user);
        System.out.println(user.getId());
        System.out.println(count);
    }

    @Test
    public void testDelectById(){
        int count = userDao.deleteById(1412279071270596613L);
        System.out.println(count);
    }

    @Test
    public void testDeleteBatchIds(){
        Collection<Long> collection = new ArrayList<>();
        collection.add(1412279071270596614L);
        collection.add(1412279071270596615L);
        collection.add(1412279071270596616L);
        int rows = userDao.deleteBatchIds(collection);
        System.out.println(rows);
    }

    @Test
    public void testDeleteByMap(){
        Map<String,Object> map = new HashMap<>();
        map.put("user_name","lichao");
        map.put("password","xueyan");
        int row = userDao.deleteByMap(map);
        System.out.println(row);
    }

    @Test
    public void testDeleteByWrapper(){
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.eq("user_name","heming").or().ge("age",28);
        int row = userDao.delete(wrapper);
        System.out.println(row);
    }

    @Test
    public void testUpdateById(){
        User user = new User();
        user.setAge(29);
        user.setId(4L);
        int row = userDao.updateById(user);
        System.out.println(row);
    }

    @Test
    public void testUpdate(){
        User user = new User();
        user.setUserName("itbus");
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.set("name","hm");
        wrapper.eq("user_name","itcast").or().between("age",1,2);
        int row = userDao.update(user,wrapper);
        System.out.println(row);
    }

    @Test
    public void testWrapper1(){
        IPage<User> page = new Page<>(2,2);
        userDao.selectPage(page,null);
        page.getTotal();
        page.getPages();
        page.getCurrent();
        System.out.println(page.getRecords());
    }

    @Test
    public void testWrapper2(){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("age");
        IPage<User> page = new Page<>(1,3);
        userDao.selectPage(page,wrapper);
        System.out.println(page.getRecords());
    }

    @Test
    public void testWrapper3(){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.likeRight("user_name","");
        List<User> list = userDao.selectList(wrapper);
        System.out.println(list);
    }

    @Test
    public void testWrapper4(){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.in("age",18,20,21);
        List<User> list = userDao.selectList(wrapper);
        System.out.println(list);
    }

    @Test
    public void testWrapper5(){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("id","age","user_name").notIn("age",18,0);
        IPage<User> page = new Page<>();
        userDao.selectPage(page,wrapper);
        System.out.println(page.getRecords());
    }

    @Test
    public void testLambdaWrapper1(){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(User::getAge,18,20);
        System.out.println(userDao.selectList(wrapper));
    }

    @Test
    public void testLambdaWrapper2(){
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getAge,29).set(User::getAge,null);
        userDao.update(null,wrapper);
    }

}
