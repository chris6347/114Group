package com.tanhua.dubbo.api;

import com.tanhua.domain.db.*;
import com.tanhua.domain.vo.DimensionsVo;
import com.tanhua.domain.vo.ReportDimensionsVo;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public interface SoulApi {

    List<Soul> findAllSoul();

    List<Questions> findQuestionsBySoulId(Long soulId);

    List<Options> findOptionsByQuestionsId(Long questionsId);

/*    Report findReport(Long soulId, Long userId);*/

    UserReport findUserReport(Long userId, Long soulId);

    Integer findMaxLevelByUserId(Long userId);

    Map<Integer,String> findLevel();

    void postAnswer(Long userId,List<Long> optionIds,List<Long> questionIds);

    List<UserInfo> getSimilar(Long userId, Long reportId);

    Report getReport(Long reportId);

    List<DimensionsVo> getDimensionsById(Long userId,Long reportId);

}
