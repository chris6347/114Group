package com.itheima.test;

import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.print.Doc;

public class MongoApi {

    MongoCollection<Document> userTable = null;
    MongoClient client = null;

    @Before
    public void init(){
        // 创建客户端
        client = MongoClients.create("mongodb://127.0.0.1:27017");
        // 获取数据库
        MongoDatabase database = client.getDatabase("demo");
        // 获取要操作的集合
        userTable = database.getCollection("user");
    }

    @After
    public void close(){
        client.close();
    }

    @Test
    public void testQuery(){
        // 对集合操作
        FindIterable<Document> documents = userTable.find();
        for (Document document : documents) {
            // Document实现了Map接口  Map<String,Object>
            System.out.println(document);
        }
    }

    @Test
    public void testQueryWithCondition(){
        // 对集合操作,年龄>21
        Bson filter = Filters.gt("age",21);
        FindIterable<Document> documents = userTable.find(filter);
        for (Document document : documents) {
            System.out.println(document);
        }
    }

    @Test
    public void testPage(){
        FindIterable<Document> documents = userTable.find().skip(2).limit(2);
        for (Document document : documents) {
            System.out.println(document);
        }
    }

    @Test
    public void testUpdate(){
        userTable.updateOne(Filters.eq("_id",1), Updates.set("age",40));
    }

    @Test
    public void testAdd(){
        Document document = new Document();
        document.append("_id",7).append("username","007").append("age",30);
        userTable.insertOne(document);
    }

    @Test
    public void testDelete(){
        userTable.deleteMany(new BasicDBObject());
    }

}
