package com.tanhua.manage.service;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.tanhua.manage.domain.AnalysisByDay;
import com.tanhua.manage.mapper.AnalysisByDayMapper;
import com.tanhua.manage.mapper.LogMapper;
import com.tanhua.manage.utils.ComputeUtil;
import com.tanhua.manage.vo.AnalysisSummaryVo;
import com.tanhua.manage.vo.AnalysisUsersVo;
import com.tanhua.manage.vo.DataPointVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class AnalysisService {

    @Autowired
    private LogMapper logMapper;

    @Autowired
    private AnalysisByDayMapper analysisByDayMapper;

    public AnalysisSummaryVo getSummary() {
        // 获取今天的字符串
        String today = DateUtil.today();
        // 获取昨天的字符串
        String yesterday = DateUtil.yesterday().toDateStr();
        // 获取过去7天  offsetDay:对日期中的天数进行操作
        String last7days = DateUtil.offsetDay(new Date(),-7).toDateStr();
        // 获取过去30天
        String last30days = DateUtil.offsetDay(new Date(),-30).toDateStr();
        log.info("today:{},yesterday:{},last7days:{},dast30days:{}",today,yesterday,last7days,last30days);
        Long totalUserCount = analysisByDayMapper.totalUserCount();
        Integer todayRegisterUser = analysisByDayMapper.findByDate(today).getNumRegistered();
        Long active30UserCount = logMapper.countActiveUserAfterDate(last30days);
        Long active7UserCount = logMapper.countActiveUserAfterDate(last7days);

        AnalysisByDay todayAnalysis = analysisByDayMapper.findByDate(today);
        AnalysisByDay yesterdayAnalysis = analysisByDayMapper.findByDate(yesterday);
        Integer registeredYesterday = yesterdayAnalysis.getNumRegistered();
        // 今日新增用户涨跌率
        BigDecimal newUsersTodayRate = ComputeUtil.computeRate(todayAnalysis.getNumRegistered().longValue(),registeredYesterday.longValue());
        // 今日登录次数涨跌率
        Integer todayLoginCount = todayAnalysis.getNumLogin();
        BigDecimal loginTimesTodayRate = ComputeUtil.computeRate(todayLoginCount.longValue(),yesterdayAnalysis.getNumLogin().longValue());
        // 今日活跃次数涨跌率
        Integer todayActiveUserCount = todayAnalysis.getNumActive();
        BigDecimal activeUsersTodayRate = ComputeUtil.computeRate(todayActiveUserCount.longValue(),yesterdayAnalysis.getNumActive().longValue());

        // =======================返回数据================================
        AnalysisSummaryVo vo = new AnalysisSummaryVo();
        // 用户总数
        vo.setCumulativeUsers(totalUserCount);
        // 过去30天活跃用户
        vo.setActivePassMonth(active30UserCount);
        // 过去7天活跃用户
        vo.setActivePassWeek(active7UserCount);
        // 今日新增用户
        vo.setNewUsersToday(todayRegisterUser.longValue());
        // 今日新增用户涨跌率 . 单位百分数   今天-昨天/昨天
        vo.setNewUsersTodayRate(newUsersTodayRate);
        // 今日登录次数
        vo.setLoginTimesToday(todayLoginCount.longValue());
        // 今日登录次数涨跌率 . 单位百分数   今天-昨天/昨天
        vo.setLoginTimesTodayRate(loginTimesTodayRate);
        // 今日活跃用户
        vo.setActiveUsersToday(todayActiveUserCount.longValue());
        // 今日活跃用户涨跌率 . 单位百分数   今天-昨天/昨天
        vo.setActiveUsersTodayRate(activeUsersTodayRate);
        // 过去7天平均日使用时长 . 单位秒

        // 昨日活跃用户

        // 昨日活跃用户涨跌率 . 单位百分数

        return vo;
    }

    public AnalysisUsersVo getUsersCount(Long sd, Long ed, Integer typeCode) {
        String type = "";
        switch (typeCode) {
            case 101:
                type = "registered";
                break;
            case 102:
                type = "active";
                break;
            default:
                type = "retention1d";
        }
        String thisYearStartDate = DateUtil.date(sd).toDateStr();
        String thisYearEndDate = DateUtil.date(ed).toDateStr();
        List<DataPointVo> thisYearData = analysisByDayMapper.findBetweenDate(type,thisYearStartDate,thisYearEndDate);

        String lastYearStartDate = DateUtil.date(sd).offset(DateField.YEAR, -1).toDateStr();
        String lastYearEndDate = DateUtil.date(ed).offset(DateField.YEAR,-1).toDateStr();
        List<DataPointVo> lastYearData = analysisByDayMapper.findBetweenDate(type,lastYearStartDate,lastYearEndDate);
        AnalysisUsersVo vo = new AnalysisUsersVo();
        vo.setThisYear(thisYearData);
        vo.setLastYear(lastYearData);
        return vo;
    }

    public void save(AnalysisByDay analysisByDay) {
        analysisByDay.setCreated(new Date());
        analysisByDay.setUpdated(new Date());
        analysisByDayMapper.insert(analysisByDay);
    }

}
