package com.tanhua.dubbo.api;

import com.tanhua.domain.mongo.Voice;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@Service
public class VoiceApiImpl implements VoiceApi{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(Voice voice) {
        mongoTemplate.insert(voice);
    }

    @Override
    public List<Voice> findAll() {
        return mongoTemplate.findAll(Voice.class);
    }

    @Override
    public void deleteOne(Voice voice) {
        mongoTemplate.remove(voice);
    }

}
