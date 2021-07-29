package com.tanhua.manage.job;

import com.tanhua.manage.domain.AnalysisByDay;
import com.tanhua.manage.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LogJob {

    @Autowired
    private LogService logService;

    //@Scheduled(cron = "0 0 4 * * ?")
    @Scheduled(initialDelay = 10000,fixedRate = 10*60*1000)
    public void logJob(){
        log.info("[开始]日志数据统计");
        logService.saveCountByDay();
        log.info("日志数据统计[完成]");
    }

}
