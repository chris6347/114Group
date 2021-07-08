package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用于处理检查组的业务
 */
@Service
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    //注入dao
    @Autowired
    private CheckGroupDao dao;

    /**
     * 查询所有
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        return dao.findAll();
    }

    /**
     * 更新操作
     *  1. 更新也有两份数据，一份是基本信息，一份是选中的检查项信息
     *  2. 这两份数据存放到的数据库表也不一样： 基本信息是存放在t_checkgroup, 用到的检查项信息，是放在中间表(t_checkgroup_checkitem)
     *  3. 更新检查组的基本信息，是很容易的，但是更新检查项选中的信息，有点绕..
     *
     *      3.1 更新检查组的基本信息，就按照我们往常熟悉的更新操作来做即可。
     *
     *      3.2 更新检查组的检查项的选中信息，有点绕。
     *          3.2.1 因为这里面可能涉及到3种情况：
     *              a. 可能从页面提交过来的检查项信息，变多了。
     *              b. 可能从页面提交过来的检查项信息，变少了。
     *              c. 可能从页面提交过来的检查项信息，根本没有变化。
     *          3.2.2 为了尽可能的减少我们编写的代码， 现在打算投机一把：
     *              把这个组包含的所有检查项信息都给删除掉，然后再重新添加进去即可。 这里操作的是中间表 t_checkgroup_checkitem
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    @Override
    public int update(CheckGroup checkGroup, int[] checkitemIds) {

        //1. 更新基本信息
        int row = dao.update(checkGroup);

        //2. 删除所有的检查组包含的检查项
        dao.deleteItemsByGroupId(checkGroup.getId());

        //3. 添加
        int row2 = 0 ;
        if(checkitemIds != null  && checkitemIds.length > 0){
            for (int checkitemId : checkitemIds) {
                row2 += dao.addGroupItems(checkGroup.getId() , checkitemId);
            }
        }

        return row >0 && row2  == checkitemIds.length  ?  1 : 0;
    }

    /**
     * 新增检查组
     *   1. 新增检查组的时候，有两份数据： 检查组的基本信息，检查组用到的检查项信息
     *   2. 这两份数据存放到的数据库表也不一样： 基本信息是存放在t_checkgroup, 用到的检查项信息，是放在中间表(t_checkgroup_checkitem)
     *   3. 有一个细节要考虑清楚：
     *       3.1 添加检查项信息到中间表的时候， 一定要有检查组的id。因为这个中间表就表现了检查组和检查项的关系
     *       3.2 但是在我们页面提交上来根本没有检查组的id
     *       3.3 实际上页面也不可能给我们提供检查组的id，这个检查组的id，应该是添加到数据库之后，由数据库生成的自增id。
     *       3.4 这就要求我们去添加中间表的时候，一定要有了检查组的id。这怎么才能拿到检查组的id呢？
     *       3.5 这就要求我们在新增检查组到检查组表里面的时候，顺便把主键id值给返回回来。
     * @param checkGroup 检查组的基本信息
     * @param checkitemIds 检查项的信息
     * @return
     */
    @Override
    public int add(CheckGroup checkGroup, int[] checkitemIds) { // checkitemIds = {28,29,30}
        //1. 先添加检查组，添加完毕检查组，我们才能拿到检查组的id值
        int row1 = dao.add(checkGroup);

        /*
            2. 添加检查项到中间表里面。
                2.1 只有检查组添加成功了，我们才去执行中间表的添加
                2.2 并且只有检查项的id是有的，那么才需要去做中间表的添加

                2.3 遍历每一个检查项，然后和检查组的id，一起添加到数据表里面
         */
        int row2 = 0 ;
        if(row1 > 0 && checkitemIds.length > 0 ){
            for (int checkitemId : checkitemIds) {
                row2 += dao.addCheckGroupItems(checkGroup.getId() ,checkitemId);
            }
        }

        //只有当检查组的表，操作影响行数 》0  ，并且检查项的操作影响行数  ==  检查项的个数。 那么即认为整个添加操作是
        //成功了， 这里的返回1 不代表影响的行数，只是一个标识成功的数字而已。
        return row1 > 0 && row2 == checkitemIds.length  ? 1 : 0;
    }

    /**
     * 检查组的分页效果 ： 打算使用分页插件来完成 PageHelper...
     * @param bean
     * @return
     */
    @Override
    public PageResult<CheckGroup> findPage(QueryPageBean bean) {

        /*
            dao是无法直接返回PageResult对象，因为它去查询某一张表的时候，没有办法直接得到一个PageResult对象，
            这就需要我们在这里手动封装了。
         */
        //1. 设置查询第几页，每页查询多少条
        PageHelper.startPage(bean.getCurrentPage(), bean.getPageSize() );

        //2. 调用dao
        Page<CheckGroup> page = dao.findPage(bean);

        //3. 封装数据
        long total =  page.getTotal() ; //得到总记录数
        List<CheckGroup> rows = page.getResult(); //得到当前页的集合数据

        return new PageResult<>(total , rows );
    }

    @Override
    public List<Integer> findItemsByGroupId(int groupId) {
        return dao.findItemsByGroupId(groupId);
    }
}
