package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanhua.domain.db.Question;
import com.tanhua.dubbo.mapper.QuestionMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class QuestionApiImpl implements QuestionApi{

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public Question findById(Long userId) {
        QueryWrapper<Question> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        return questionMapper.selectOne(wrapper);
    }

    @Override
    public void save(Question question) {
        Question q = findById(question.getUserId());
        if (null == q) {
            questionMapper.insert(question);
            return;
        }
        question.setId(q.getId());
        questionMapper.updateById(question);
    }

}
