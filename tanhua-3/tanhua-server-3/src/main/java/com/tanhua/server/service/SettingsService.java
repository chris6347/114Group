package com.tanhua.server.service;

import com.tanhua.domain.db.Question;
import com.tanhua.domain.db.Settings;
import com.tanhua.domain.db.User;
import com.tanhua.domain.vo.SettingsVo;
import com.tanhua.dubbo.api.QuestionApi;
import com.tanhua.dubbo.api.SettingsApi;
import com.tanhua.server.interceptor.UserHolder;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {

    @Reference
    private SettingsApi settingsApi;

    @Reference
    private QuestionApi questionApi;

    public SettingsVo findSettings(){
        User user = UserHolder.getUser();
        Settings settings = settingsApi.findSettingsById(user.getId());
        SettingsVo vo = new SettingsVo();
        if (null != settings) {
            BeanUtils.copyProperties(settings,vo);
        }
        Question question = questionApi.findById(user.getId());
        vo.setStrangerQuestion("你真的喜欢我吗?");
        if (null != question) {
            vo.setStrangerQuestion(question.getTxt());
        }
        vo.setPhone(user.getMobile());
        vo.setId(user.getId());
        return vo;
    }

    public void updateNotification(SettingsVo vo) {
        Settings settings = new Settings();
        BeanUtils.copyProperties(vo,settings);
        Long userId = UserHolder.getUserId();
        settings.setUserId(userId);
        settingsApi.updateNotification(settings);
    }

    public void updateQuestion(String content) {
        Question question = new Question();
        question.setTxt(content);
        question.setUserId(UserHolder.getUserId());
        questionApi.save(question);
    }

}
