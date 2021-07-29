package com.tanhua.server.service;

import com.tanhua.domain.db.*;
import com.tanhua.domain.vo.*;
import com.tanhua.dubbo.api.SoulApi;
import com.tanhua.server.interceptor.UserHolder;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SoulService {

    @Reference
    private SoulApi soulApi;

    public List<SoulVo> findAll(){
        Long userId = UserHolder.getUserId();
        // 找到所有的 问卷
        List<Soul> souls = soulApi.findAllSoul();
        List<SoulVo> soulVoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(souls)) {
            // 查询levelId:level
            Map<Integer, String> levels = soulApi.findLevel();
            // 遍历问卷 补入问题
            soulVoList = souls.stream().map(soul -> {
                Long soulId = soul.getId();
                // 通过soulId找到该问卷下的所有问题
                List<Questions> questionsList = soulApi.findQuestionsBySoulId(soulId);
                // 遍历每个问题,找到对应的所有options,返回QuestionsVo
                List<QuestionsVo> questionsVoList = questionsList.stream().map(questions -> {
                    List<Options> optionsList = soulApi.findOptionsByQuestionsId(questions.getId());

                    // 用questions的id 查option
                    List<OptionsVo> optionsVoList = optionsList.stream().map(options -> {
                        OptionsVo optionsVo = new OptionsVo();
                        optionsVo.setOption(options.getOption());
                        optionsVo.setId(options.getId().toString());
                        return optionsVo;
                    }).collect(Collectors.toList());
                    // 构建QuestionsVo
                    QuestionsVo questionsVo = new QuestionsVo();
                    // 封装该问题下对应的options
                    questionsVo.setOptions(optionsVoList);
                    // 封装问题txt
                    questionsVo.setQuestion(questions.getQuestion());
                    // 封装问题编号
                    questionsVo.setId(questions.getId().toString());
                    return questionsVo;
                }).collect(Collectors.toList());
                // 构建SoulVo
                SoulVo soulVo = new SoulVo();
                // 封装结果
                BeanUtils.copyProperties(soul, soulVo);
                soulVo.setId(soulId.toString());
                soulVo.setQuestions(questionsVoList);

                // 这里还剩下reportId和isLock没有封装 levelId还要转level varchar
                // 找到用户提交的报告,里面有reportId
                UserReport userReport = soulApi.findUserReport(userId,soulId);
                if (userReport!=null) {
                    Long reportId = userReport.getId();
                    soulVo.setReportId(reportId.toString());
                }
                // 关联查询用户做过的题目的最高level,判断isLock
                soulVo.setIsLock(0);
                Integer levelId = soul.getLevelId();
                if (levelId>1) {
                    Integer maxLevelId = soulApi.findMaxLevelByUserId(userId);
                    // 该用户没做过任何试题,返回的maxLevelId为null
                    if (maxLevelId!=null&&maxLevelId<levelId-1) {
                        // 没做过比当前等级-1更高的问卷,设置该问卷上锁
                        soulVo.setIsLock(1);
                    }
                }
                soulVo.setLevel(levels.get(levelId));
                return soulVo;
            }).collect(Collectors.toList());
        }
        return soulVoList;
    }

    public void postAnswer(List<PostSoulVo> postSoulVoList) {
        List<Long> optionIds = postSoulVoList.stream().map(PostSoulVo::getOptionId).collect(Collectors.toList());
        List<Long> questionIds = postSoulVoList.stream().map(PostSoulVo::getQuestionId).collect(Collectors.toList());
        soulApi.postAnswer(UserHolder.getUserId(),optionIds,questionIds);
    }

    public ReportDimensionsVo getReport(Long reportId) {
        Long userId = UserHolder.getUserId();
        Report report = soulApi.getReport(reportId);
        List<DimensionsVo> dimensionsVo = soulApi.getDimensionsById(userId,reportId);
        List<UserInfo> similar = soulApi.getSimilar(userId, reportId);
        List<UserInfoVo> userInfoVoList = similar.stream().map(userInfo -> {
            UserInfoVo vo = new UserInfoVo();
            vo.setId(userInfo.getId());
            vo.setAvatar(userInfo.getAvatar());
            return vo;
        }).collect(Collectors.toList());
        ReportDimensionsVo vo = new ReportDimensionsVo();
        vo.setConclusion(report.getConclusion());
        vo.setCover(report.getCover());
        vo.setDimensions(dimensionsVo);
        vo.setSimilarYou(userInfoVoList);
        return vo;
    }
}
