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

    /**
     * 删除检查项
     *      思路：
     *          1. 检查项不能按照以前的操作办法，直接删除。
     *          2. 要做判断，判断检查项是否有被检查组所使用
     *              2.1 有：不能删除
     *              2.2 没有： 可以删除
     *          3. 判断检查项是否有被检查组使用，其实很容易，需要拿着检查项的id值去查询中间表（t_checkgroup_checkitem）
     *              如果在里面能查询出来有记录，那么则表示有被检查项使用。只需要知道总数即可。
     *              返回： =0 ，即表示没有检查组使用
     *              返回： >0 , 表示有检查组使用。
     *          4. 这就表示了要想完成检查项的删除工作，dao需要提供两个方法
     *              4.1 查询检查项是否被检查组使用
     *              4.2 删除的方法
     *
     * @param id
     * @return
     */
    @Override
    public int delete(int id) {
        //1. 先查询检查项是否有被检查组使用
        long count = dao.findCountById(id);
        if(count > 0){ //表示有检查组使用
            System.out.println("有检查组使用该检查项，禁止删除！");
            return 0; // 返回给controller 表示一行都没有删除掉
        }

        //2. 删除检查项
        return dao.delete(id);
    }

    @Override
    public int update(CheckItem checkItem) {
        System.out.println("checkItem==" + checkItem);
        return dao.update(checkItem);
    }

    /**
     * 查询所有的检查项
     * @return
     */
    @Override
    public List<CheckItem> findAll() {
        return dao.findAll();
    }


}
