package com.itheima.test;

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

@SpringBootTest
@RunWith(SpringRunner.class)
public class ClassTest02 {

    @Autowired
    public MongoTemplate mongoTemplate;

    @Test
    public void test1(){
        List<User> all = mongoTemplate.findAll(User.class);
        for (User user : all) {
            System.out.println(user);
        }
    }

    @Test
    public void testQuery(){
        Query query = new Query();
        query.addCriteria(Criteria.where("age").gte(20));
        List<User> list = mongoTemplate.find(query, User.class);
        list.forEach(System.out::println);
    }

    @Test
    public void testFindPage(){
        Query query = new Query();
        //query.with(PageRequest.of(2-1,2));
        query.skip((2-1)*2).limit(2);
        List<User> list = mongoTemplate.find(query, User.class);
        list.forEach(System.out::println);
    }

    @Test
    public void testOrderBy(){
        Query query = new Query();
        query.with(Sort.by(Sort.Order.desc("age"))).with(Sort.by(Sort.Order.asc("id")));
        List<User> list = mongoTemplate.find(query, User.class);
        list.forEach(System.out::println);
    }

    @Test
    public void testInsert(){
        User user = new User();
        user.setAge(1);
        user.setUsername("李文豪");
        user.setAddress("深圳湾1号");
        user.setId(8);
        mongoTemplate.insert(user);
    }

    @Test
    public void testUpdate(){
        Query query = new Query();
        Update update = new Update();
        query.addCriteria(Criteria.where("_id").is(1));
        update.set("username","王越").set("address","农民房");
        mongoTemplate.updateFirst(query,update,User.class);
    }


}
