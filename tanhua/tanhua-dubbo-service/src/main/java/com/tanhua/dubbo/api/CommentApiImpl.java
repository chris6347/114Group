package com.tanhua.dubbo.api;

import com.tanhua.domain.mongo.Comment;
import com.tanhua.domain.mongo.Publish;
import com.tanhua.domain.vo.PageResult;
import org.apache.dubbo.config.annotation.Service;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentApiImpl implements CommentApi{

    @Autowired
    private MongoTemplate mongoTemplate;

    // 1.在publish表中找到作者id存入comment
    // 2.最后完善comment,插入到mongoDB中
    // 3.找到publish表中对应的动态,使评论数+1
    // 4.判断评论类型返回对应的count
    @Override
    public Long save(Comment comment) {
        ObjectId publishId = comment.getTargetId();
        Query publishQuery = new Query();
        publishQuery.addCriteria(Criteria.where("_id").is(publishId));
        // 查询动态表,获取作者id
        Publish publish = mongoTemplate.findOne(publishQuery,Publish.class);
        comment.setTargetUserId(publish.getUserId());
        // 补充创建时间
        comment.setCreated(System.currentTimeMillis());
        // 往评论表添加点赞数据
        mongoTemplate.insert(comment);

        // 更新动态的点赞数
        Update publishUpdate = new Update();
        publishUpdate.inc(comment.getCol(), 1);
        mongoTemplate.updateFirst(publishQuery,publishUpdate,Publish.class);
        if (comment.getCommentType() == 3) {
            return publish.getLoveCount().longValue()+1;
        }
        if (comment.getCommentType() == 2) {
            return publish.getCommentCount().longValue()+1;
        }
        return publish.getLikeCount().longValue()+1;
    }

    // 1.删除对应的评论表comment的文档
    // 2.找到publish表中的动态,对应类型数量-1
    // 3.判断返回count
    @Override
    public Long remove(Comment comment) {
        ObjectId publishId = comment.getTargetId();
        // 删除的三个条件 : targetId,commentType,userId  TODO 评论时如何区分?不走这个方法吗?
        Query query = new Query();
        query.addCriteria(Criteria.where("targetId").is(publishId))
                .addCriteria(Criteria.where("commentType").is(comment.getCommentType()))
                .addCriteria(Criteria.where("userId").is(comment.getUserId()));
        mongoTemplate.remove(query,Comment.class);

        Query publishQuery = new Query();
        publishQuery.addCriteria(Criteria.where("_id").is(publishId));
        Update update = new Update();
        update.inc(comment.getCol(),-1);
        mongoTemplate.updateFirst(publishQuery,update,Publish.class);
        Publish publish = mongoTemplate.findOne(publishQuery,Publish.class);
        Integer commentType = comment.getCommentType();
        return commentType==3?publish.getLoveCount().longValue():commentType==2?publish.getCommentCount():publish.getLikeCount();
    }

    @Override
    public PageResult findPage(String movementId, Long page, Long pagesize) {
        Query query = new Query();
        if (null != movementId) {
            query.addCriteria(Criteria.where("targetId").is(new ObjectId(movementId)).and("commentType").is(2));
        }
        Long count = mongoTemplate.count(query,Comment.class);
        List<Comment> comments = null;
        if (count>0) {
            query.with(Sort.by(Sort.Order.desc("created")))
                    .with(PageRequest.of(page.intValue() - 1, pagesize.intValue()));
            comments = mongoTemplate.find(query, Comment.class);
        }
        return PageResult.pageResult(page,pagesize,comments,count);
    }

    @Override
    public Long likeComment(Comment comment) {
        comment.setCreated(System.currentTimeMillis());
        Comment targetComment = mongoTemplate.findById(comment.getTargetId(), Comment.class);
        comment.setTargetUserId(targetComment.getUserId());
        mongoTemplate.insert(comment);
        Update update = new Update();
        update.inc("likeCount",1);
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(comment.getTargetId()));
        mongoTemplate.updateFirst(query,update,Comment.class);
        return targetComment.getLikeCount().longValue()+1;
    }

    @Override
    public Long disLikeComment(Comment comment) {
        Query query = new Query();
        query.addCriteria(Criteria.where("targetId").is(comment.getTargetId())
            .and("commentType").is(comment.getCommentType()).and("userId").is(comment.getUserId()));
        mongoTemplate.remove(query,Comment.class);
        Update update = new Update();
        update.inc("likeCount",-1);
        query = new Query();
        query.addCriteria(Criteria.where("_id").is(comment.getTargetId()));
        mongoTemplate.updateFirst(query,update,Comment.class);
        Comment targerComment = mongoTemplate.findOne(query, Comment.class);
        return targerComment.getLikeCount().longValue();
    }

    @Override
    public PageResult findPageByCommentType(Long targetUserId, Integer commentType, Long page, Long pagesize) {
        Query query = new Query();
        query.addCriteria(Criteria.where("targetUserId").is(targetUserId)
                .and("commentType").is(commentType));
        long count = mongoTemplate.count(query, Comment.class);
        List<Comment> comments = new ArrayList<>();
        if (count>0) {
            query.skip((page-1)*pagesize).limit(pagesize.intValue())
                    .with(Sort.by(Sort.Order.desc("created")));
            comments=mongoTemplate.find(query,Comment.class);
        }
        return PageResult.pageResult(page,pagesize,comments,count);
    }

}
