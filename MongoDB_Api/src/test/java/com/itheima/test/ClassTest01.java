package com.itheima.test;


import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.BSON;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Before;
import org.junit.Test;

public class ClassTest01 {

    MongoClient client = null;
    MongoCollection<Document> collection = null;

    @Before
    public void init(){
        client = MongoClients.create("mongodb://127.0.0.1:27017");
        MongoDatabase database = client.getDatabase("demo");
        collection = database.getCollection("user");
/*        FindIterable<Document> documents = collection.find();
        for (Document document : documents) {
            System.out.println(document);
        }*/
    }

    @Test
    public void testQueryWithCondition(){
        Bson filter = Filters.eq("age", 22);
        FindIterable<Document> documents = collection.find(filter);
        for (Document document : documents) {
            System.out.println(document);
        }
    }

    @Test
    public void testFindPage(){
        FindIterable<Document> limit = collection.find().skip(2).limit(2);
        for (Document document : limit) {
            System.out.println(limit);
        }
    }

    @Test
    public void testUpdate(){
        collection.updateOne(Filters.eq("_id",1), Updates.set("age","40"));
    }

    @Test
    public void testAdd(){
        Document document = new Document();
        document.append("_id",7).append("username","(๑′ᴗ‵๑)Ｉ Lᵒᵛᵉᵧₒᵤ❤");
        collection.insertOne(document);
    }

    @Test
    public void testDelete(){

    }

}
