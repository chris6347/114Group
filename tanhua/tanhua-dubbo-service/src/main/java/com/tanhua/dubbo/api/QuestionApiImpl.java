package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.tanhua.domain.db.Question;
import com.tanhua.dubbo.mapper.QuestionMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class QuestionApiImpl implements QuestionApi{

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public Question findByUserId(Long userId) {
        QueryWrapper<Question> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        return questionMapper.selectOne(wrapper);
    }

    @Override
    public void updateQuestion(Long userId, String content) {
        Question question = findByUserId(userId);
        Question newQuestion = new Question();
        newQuestion.setTxt(content);
        newQuestion.setUserId(userId);
        if ( question == null ) {
            questionMapper.insert(newQuestion);
        } else {
            UpdateWrapper<Question> wrapper = new UpdateWrapper<>();
            wrapper.eq("user_id",userId);
            questionMapper.update(newQuestion,wrapper);
        }
    }

}
