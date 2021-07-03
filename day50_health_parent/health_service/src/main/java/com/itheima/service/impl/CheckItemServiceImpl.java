package com.itheima.service.impl;

import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.health.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
    这是检查项的service层
        1. 把这个类交给spring托管
        2. 添加事务
        3. 调用dao ， 让spring注入进来。

 */

@Service
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    //让spring注入进来dao
    @Autowired
    private CheckItemDao dao;

    /**
     * 新增检查项
     * @param checkItem
     * @return
     */
    @Override
    public int add(CheckItem checkItem) {
        return dao.add(checkItem);
    }

    /**
     *
     * @param queryPageBean 查询条件
     * @return
     */
    @Override
    public PageResult<CheckItem> findPage(QueryPageBean queryPageBean) {
        /*
            一般来说，service层的返回值，一般都是由dao层提供的。但是咋这里dao层没有办法提供PageResult。
            因为dao层其实也是去查询数据库要的数据，数据库是真没有办法提供PageResult...

            这里就需要我们手动封装了
         */

        // 总记录数
        long total = dao.findCount(queryPageBean) ;

        //  当前页的集合数据
        List<CheckItem> rows = dao.findPage(queryPageBean);

        //封装PageResult
        //PageResult<CheckItem> ps = new PageResult<CheckItem>(total , rows);

        return new PageResult<CheckItem>(total , rows);
    }
}
