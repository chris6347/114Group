package com.itheima.service;

import java.text.ParseException;
import java.util.List;

public interface OrderSettingService {

    /**
     * 批量导入预约信息
     * @param list
     * @return
     */
    int add( List<String[]> list) throws ParseException;
}
