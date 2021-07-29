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
public class MongoTemplate02 {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testFindAll(){
        List<User> all = mongoTemplate.findAll(User.class);
        all.forEach(System.out::println);
    }

    @Test
    public void testFindWithCondition(){
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").gte(2));
        List<User> list = mongoTemplate.find(query, User.class);
        list.forEach(System.out::println);
    }

    @Test
    public void testFindPage(){
        Query query = new Query();
        query.with(PageRequest.of(0,2));
        List<User> list = mongoTemplate.find(query, User.class);
        list.forEach(System.out::println);
    }

    @Test
    public void testOrderBy(){
        Query query = new Query();
        query.with(Sort.by(Sort.Order.desc("age")));
        query.with(Sort.by(Sort.Order.asc("_id")));
        List<User> list = mongoTemplate.find(query, User.class);
        list.forEach(s-> System.out.println(s));
    }

    @Test
    public void testAdd(){
        User user = new User();
        user.setId(6);
        user.setUsername("lichao");
        user.setAddress("兴东");
        user.setAge(18);
        mongoTemplate.insert(user);
    }

    @Test
    public void testUpdate(){
        Update update = new Update();
        update.set("address","深圳湾1号").set("age",81);
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is("lichao"));
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
        Update update = new Update();
        update.inc("age",100);
        Query query = new Query();
        mongoTemplate.updateFirst(query,update,"user");
    }

}
