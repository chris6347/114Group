package com.itheima.test;

import com.itheima.SpringBootMongoDBApplication;
import com.itheima.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest(classes = SpringBootMongoDBApplication.class)
@RunWith(SpringRunner.class)
public class SpringBootMongo {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testFindAll(){
        //查询的集合(表)是User类上的注解上collection属性值
        List<User> users = mongoTemplate.find(new Query(),User.class);
        users.forEach(System.out::println);
    }

    @Test
    public void testFindWithCondition(){
        Query query = new Query();
        // criteria: 标准
        query.addCriteria(Criteria.where("age").gt(21));
        List<User> u = mongoTemplate.find(query, User.class);
        u.forEach(System.out::println);
    }

    @Test
    public void testFindPage(){
        Query query = new Query();
        query.addCriteria(Criteria.where("age").gt(20));
        //query.skip(2).limit(2);  自己算
        query.with(PageRequest.of(1,2)); //使用PageRequest.of
        List<User> users = mongoTemplate.find(query, User.class);
        users.forEach(System.out::println);
    }

    @Test
    public void testOrderBy(){
        Query query = new Query();
        // 年龄降序
        query.with(Sort.by(Sort.Order.desc("age")));
        List<User> users = mongoTemplate.find(query, User.class);
        users.forEach(System.out::println);
    }

    @Test
    public void testAdd(){
        User user = new User();
        user.setUsername("lichao");
        user.setAge(30);
        user.setAddress("兴东");
        user.setId(6);
        mongoTemplate.insert(user);
    }

    @Test
    public void testUpdate(){
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is("lichao"));
        Update update = new Update();
        update.set("age",1);
        update.set("address","深圳湾1号");
        //p3 : 集合名
        mongoTemplate.updateFirst(query,update,"user");
    }

    @Test
    public void testDelete(){
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is("lichao"));
        mongoTemplate.remove(query,User.class);
    }

    @Test
    public void testUpdateInc(){
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is("lisi"));
        Update update = new Update();
        update.inc("age",-1);
        mongoTemplate.updateFirst(query,update,"user");
    }

}
