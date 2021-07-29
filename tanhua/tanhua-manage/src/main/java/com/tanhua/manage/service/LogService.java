package com.tanhua.manage.service;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanhua.manage.domain.AnalysisByDay;
import com.tanhua.manage.domain.Log;
import com.tanhua.manage.mapper.LogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class LogService {

    @Autowired
    private LogMapper logMapper;

    @Autowired
    private AnalysisService analysisService;

    public void add(Log log) {
        log.setCreated(new Date());
        logMapper.insert(log);
    }

    public void saveCountByDay() {
        AnalysisByDay analysisByDay = new AnalysisByDay();
        String yesterday = DateUtil.yesterday().toDateStr();
        Long loginCount = logMapper.selectLoginCount(yesterday);
        Long activeCount = logMapper.selectActiveCount(yesterday);
        Long registerCount = logMapper.selectRegisterCount(yesterday);
        String yesterday2 = DateUtil.offsetDay(new Date(),-2).toDateStr();
        List<Long> ids = logMapper.selectRegisterUserId(yesterday2);
        //System.out.println("ids========"+ids);
        Long retention1dCount = 0L;
        if (ids!=null && ids.size()>0) {
            retention1dCount = logMapper.selectRetention1dCount(yesterday, ids);
        }
        analysisByDay.setRecordDate(new Date());
        analysisByDay.setNumLogin(loginCount.intValue());
        analysisByDay.setNumActive(activeCount.intValue());
        analysisByDay.setNumRegistered(registerCount.intValue());
        analysisByDay.setNumRetention1d(retention1dCount.intValue());
        analysisService.save(analysisByDay);
    }

}
