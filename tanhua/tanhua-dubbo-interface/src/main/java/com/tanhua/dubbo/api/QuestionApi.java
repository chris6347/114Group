package com.tanhua.dubbo.api;

import com.tanhua.domain.db.Question;

public interface QuestionApi {

    Question findByUserId(Long userId);

    void updateQuestion(Long userId, String content);
}
