package com.itheima.test;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.dao.UserDao;
import com.itheima.pojo.User;
import com.mysql.jdbc.PreparedStatement;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserMapperTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void testSelectById(){
        User user = userDao.selectById(6);
        System.out.println(user);
    }

    @Test
    public void testAdd(){
        User user = new User();
        user.setUserName("zwz");
        user.setAge(123);
        user.setName("wzw");
        user.setPassword("321");
        userDao.insert(user);
    }

    @Test
    public void testDeleteById(){
        userDao.deleteById(5);
    }

    @Test
    public void testDeleteByBatchIds(){
        List<Integer> list = new ArrayList<>();
        userDao.deleteBatchIds(list);  // forEach标签  list没值时报错
    }

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    // 效率高的数据库批量操作方法.
    // 原生的数据库的批量操作
    // 事务的话需要手动设置. 不能用@Transcational
    @Test
    public void testSqlSessionFactory(){
        // ExecutorType.BATCH : 批量操作
        // sqlSession : 二级缓存
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        List<Integer> list = new ArrayList<>();
        list.add(3);
        list.add(5);
        for (Integer integer : list) {
            mapper.deleteById(integer);
        }
        PreparedStatement ps = null;
        sqlSession.commit();
        // 清空二级缓存
        sqlSession.flushStatements();
        sqlSession.close();
    }

    @Test
    public void testDeleteByMap(){
        // map用于构建条件
        Map<String,Object> map = new HashMap<>();
        map.put("age",1);
        map.put("user_name","huanan1");
        userDao.deleteByMap(map);
    }

    @Test
    public void testUpdateById(){
        User user = new User();
        user.setId(1L);
        user.setName("黎宏恩");
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.eq("id",1).set("email",null);
        // p1: 更新后的数据(如果属性值为null,则不会出现在update语句里),p1可以为null
        // p2: 更新的条件里属性值为null,也会出现在update语句里
        userDao.update(null,wrapper);
    }

    @Test
    public void testFindPage(){
        IPage<User> page = new Page<>(1,2);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        userDao.selectPage(page,null);
        // 上面这个方法,返回值就是page,和之前的page是同一个对象
        System.out.println(page.getTotal());
        System.out.println(page.getRecords());
    }

    @Test
    public void testQueryWrapper(){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        IPage<User> page = new Page<>(1,2);
        // 年龄小于23
        wrapper.lt("age","23")
                .in("name","张三","黎宏恩")
                .or()
                .in("age",1,2)
                .likeRight("name","黎")
                .orderByDesc("age")
                .select("name");
        // selectOne只能查一个,如果语句返回多条记录则报错:TooManyResultException
        //User user = userDao.selectOne(wrapper);
        //System.out.println(user);
        userDao.selectPage(page,wrapper);
        List<User> users = page.getRecords();
        users.forEach(System.out::println);
    }

    @Test
    public void testLambdaWrapper(){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.likeRight(User::getPassword,"3");
        String sqlSelect = wrapper.getSqlSelect();
        System.out.println(sqlSelect);
        List<User> list = userDao.selectList(wrapper);
        list.forEach(System.out::println);
    }

}
