package com.tanhua.dubbo.api;

import com.tanhua.domain.mongo.Voice;

import java.util.List;

public interface VoiceApi {

    void save(Voice voice);

    List<Voice> findAll();

    void deleteOne(Voice voice);
}
