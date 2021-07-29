package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanhua.domain.db.*;
import com.tanhua.domain.vo.DimensionsVo;
import com.tanhua.domain.vo.ReportDimensionsVo;
import com.tanhua.dubbo.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SoulApiImpl implements SoulApi{

    @Autowired
    private SoulMapper soulMapper;

    @Autowired
    private QuestionsMapper questionsMapper;

    @Autowired
    private OptionsMapper optionsMapper;

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private UserReportMapper userReportMapper;

    @Autowired
    private LevelMapper levelMapper;

    @Autowired
    private OptionsDimensionsMapper optionsDimensionsMapper;

    @Autowired
    private DimensionsMapper dimensionsMapper;

    @Autowired
    private UserDimensionsMapper userDimensionsMapper;

    @Autowired
    private UserInfoApi userInfoApi;

    @Override
    public List<Soul> findAllSoul() {
        return soulMapper.selectList(new QueryWrapper<>());
    }

    @Override
    public List<Questions> findQuestionsBySoulId(Long soulId) {
        QueryWrapper<Questions> wrapper = new QueryWrapper<>();
        wrapper.eq("soul_id",soulId);
        return questionsMapper.selectList(wrapper);
    }

    @Override
    public List<Options> findOptionsByQuestionsId(Long questionsId) {
        return optionsMapper.findByQuestionsId(questionsId);
    }

/*    @Override
    public Report findReport(Long soulId, Long userId) {
        QueryWrapper<Report> wrapper = new QueryWrapper<>();
        wrapper.eq("soul_id",soulId).eq("user_id",userId);
        return reportMapper.selectOne(wrapper);
    }*/

    @Override
    public UserReport findUserReport(Long userId, Long soulId) {
        QueryWrapper<UserReport> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId).eq("soul_id",soulId);
        return userReportMapper.selectOne(wrapper);
    }

    @Override
    public Integer findMaxLevelByUserId(Long userId) {
        return soulMapper.findMaxLevel(userId);
    }

    @Override
    public Map<Integer,String> findLevel() {
        List<Level> levels = levelMapper.selectList(null);
        return levels.stream().collect(Collectors.toMap(Level::getId,Level::getType));
    }

    // 1. 提交基本数据
    @Override
    public void postAnswer(Long userId,List<Long> optionIds, List<Long> questionIds) {
        // 提交用户提交的答案

        // 根据optionsId找出总分
        Double scoreCount = optionsMapper.findScoreCount(optionIds);
        // 按区间最小分数升序排序查询出所有report  (min_score,max_score]
        QueryWrapper<Report> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("min_score");
        List<Report> reports = reportMapper.selectList(wrapper);
        // 遍历reports,找到最后一个比用户总分小的min_score,那么这个reportId就是结果
        int index = -1 ;
        for (int i = 0; i < reports.size(); i++) {
            Double min = reports.get(i).getMinScore();
            if (scoreCount > min) {
                index = i;
            }
        }
        Long reportId = reports.get(index).getId();
        // 提交soul_id,总分,user_id,report_id
        UserReport userReport = new UserReport(null,userId,null,scoreCount,reportId,questionIds.get(0));
        // 如果用户已经提交过了,则修改
        UserReport ur = userReportMapper.selectReport(userId,questionIds.get(0));
        if (ur == null) {
            userReportMapper.postAnswer(userReport);
        } else {
            userReportMapper.updateAnswer(userReport);
        }
        postDimensions(userId,optionIds,questionIds);
    }

    // 2.提交维度数据
    public void postDimensions(Long userId,List<Long> optionIds,List<Long> questionIds){
        // 根据optionsId找到用户对应纬度的总分
        List<DimensionsScoreResult> scoreCount = optionsDimensionsMapper.findScoreCount(optionIds);
        Map<Long, Double> scoreCountMap = scoreCount.stream().collect(Collectors.toMap(DimensionsScoreResult::getDimensionId, DimensionsScoreResult::getScoreCount));
        // 找对应的总满分 在questions表中
        List<DimensionsFullScoreResult> fullCount = questionsMapper.findFullCount(questionIds);
        Map<Long, Double> fullCountMap = fullCount.stream().collect(Collectors.toMap(DimensionsFullScoreResult::getDimensionId, DimensionsFullScoreResult::getFullCount));

        // 得到所有用户提交的纬度类型
        Set<Long> dimensionId = scoreCountMap.keySet();
        // 计算封装
        List<UserDimensions> userDimensionsList = dimensionId.stream().map(id -> {
            UserDimensions userDimensions = new UserDimensions();
            userDimensions.setUserId(userId);
            userDimensions.setDimensionId(id);
            //计算 纬度id : (用户得分/对应总分)
            Double rate = scoreCountMap.get(id) / fullCountMap.get(id);
            userDimensions.setRate(rate);
            userDimensions.setQuestionId(questionIds.get(0));
            return userDimensions;
        }).collect(Collectors.toList());
        if(userReportMapper.selectReport(userId,questionIds.get(0))==null) {
            // 插入用户维度结果
            int i = userDimensionsMapper.insertUserDimensions(userDimensionsList);
            log.info("===================插入了{}行===================",i);
            return;
        }
        // 有提交则修改
        for (UserDimensions userDimensions : userDimensionsList) {
            userDimensionsMapper.updateUserDimensions(userDimensions);
        }
    }

    // 找维度
    public Map<Long,String> findAllDimensions(){
        List<Dimensions> dimensions = dimensionsMapper.selectList(null);
        return dimensions.stream().collect(Collectors.toMap(Dimensions::getId,Dimensions::getDimension));
    }


    @Override
    public List<UserInfo> getSimilar(Long userId, Long reportId) {
        QueryWrapper<UserReport> wrapper = new QueryWrapper<>();
        wrapper.ne("user_id",userId).eq("report_id",reportId).select("user_id");
        IPage<UserReport> page = new Page<>(1,10);
        userReportMapper.selectPage(page,wrapper);
        List<UserReport> records = page.getRecords();
        List<Long> ids = records.stream().map(UserReport::getUserId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(ids)) {
            ids = new ArrayList<>();
            Collections.addAll(ids,1L,2L,3L,4L,5L,6L,7L,8L,9L,10L);
        }
        return userInfoApi.findByBatchIds(ids);
    }

    @Override
    public Report getReport(Long reportId){
        UserReport userReport = userReportMapper.selectById(reportId);
        return reportMapper.selectById(userReport.getReportId());
    }

    @Override
    public List<DimensionsVo> getDimensionsById(Long userId,Long reportId){
        QueryWrapper<UserReport> reportWrapper = new QueryWrapper<>();
        reportWrapper.select("soul_id").eq("id",reportId);
        QueryWrapper<UserDimensions> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId).eq("soul_id",userReportMapper.selectOne(reportWrapper).getSoulId());
        // 得到了该用户对应该试卷的维度和维度值
        List<UserDimensions> userDimensions = userDimensionsMapper.selectList(wrapper);
        Map<Long, String> allDimensions = findAllDimensions();
        return userDimensions.stream().map(userDimension -> {
            DimensionsVo vo = new DimensionsVo();
            vo.setKey(allDimensions.get(userDimension.getDimensionId()));
            vo.setValue(userDimension.getRate().toString());
            return vo;
        }).collect(Collectors.toList());
    }



/*    public  List<UserScore> findAllUserScoreByUserId(Long userId){
        QueryWrapper<UserScore> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        return userScoreMapper.selectList(wrapper);
    }*/

}
