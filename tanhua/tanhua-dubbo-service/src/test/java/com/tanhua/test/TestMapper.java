package com.tanhua.test;

import com.tanhua.domain.db.DimensionsFullScoreResult;
import com.tanhua.domain.db.DimensionsScoreResult;
import com.tanhua.dubbo.DubboServiceApplication;
import com.tanhua.dubbo.mapper.OptionsDimensionsMapper;
import com.tanhua.dubbo.mapper.QuestionsMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = DubboServiceApplication.class)
@RunWith(SpringRunner.class)
public class TestMapper {

    @Autowired
    private OptionsDimensionsMapper optionsDimensionsMapper;

    @Autowired
    private QuestionsMapper questionsMapper;

    @Test
    public void testODM(){
        List<Long> ids = new ArrayList<>();
        Collections.addAll(ids,1L,5L,12L,13L);
        List<DimensionsScoreResult> scoreCount = optionsDimensionsMapper.findScoreCount(ids);
        System.out.println(scoreCount);
        System.out.println(scoreCount.get(0).getScoreCount());
        //System.out.println(scoreCount);
        /*for (Map<Long, Long> longLongMap : scoreCount) {
            System.out.println(longLongMap);
        }
        System.out.println(scoreCount.get(0).get(1L));*/
    }

    @Test
    public void testFullCount(){
        List<Long> ids = new ArrayList<>();
        Collections.addAll(ids,1L,2L,3L,4L,5L);
        List<DimensionsFullScoreResult> fullCount = questionsMapper.findFullCount(ids);
        System.out.println(fullCount);
    }

}
