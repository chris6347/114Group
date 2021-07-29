package com.tanhua.dubbo.api;

import com.tanhua.domain.db.Question;

public interface QuestionApi {
    Question findById(Long userId);

    void save(Question question);
}
