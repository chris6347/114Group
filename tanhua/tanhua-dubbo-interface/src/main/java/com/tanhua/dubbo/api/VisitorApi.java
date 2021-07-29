package com.tanhua.dubbo.api;

import com.tanhua.domain.mongo.Visitor;
import com.tanhua.domain.vo.PageResult;

import java.util.List;

public interface VisitorApi {

    List<Visitor> queryLast4Visitors(Long userId, Long lastQueryTime);

    void save(Visitor visitor);

    PageResult findPage(Long page, Long pagesize, Long userId);

}
