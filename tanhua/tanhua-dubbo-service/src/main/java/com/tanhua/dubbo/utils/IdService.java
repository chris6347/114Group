package com.tanhua.dubbo.utils;

import com.tanhua.domain.mongo.Sequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class IdService {

    @Autowired
    private MongoTemplate mongoTemplate;

/*
* 根据集合名,生成自增id
* */
    public Long nextId(String collectionName) {
        Query query = Query.query(Criteria.where("collName").is(collectionName));
        Update update = new Update();
        update.inc("seqId",1);

        FindAndModifyOptions options = new FindAndModifyOptions();  // 查找和修改选项
        options.upsert(true);   // upsert:插入
        options.returnNew(true);
        Sequence sequence = mongoTemplate.findAndModify(query,update,options,Sequence.class);
        return sequence.getSeqId();
    }

}
