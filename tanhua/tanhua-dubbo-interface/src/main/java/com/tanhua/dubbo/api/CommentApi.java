package com.tanhua.dubbo.api;

import com.tanhua.domain.mongo.Comment;
import com.tanhua.domain.vo.PageResult;

public interface CommentApi {

    Long save(Comment comment);

    Long remove(Comment comment);

    PageResult findPage(String movementId, Long page, Long pagesize);

    Long likeComment(Comment comment);

    Long disLikeComment(Comment comment);

    PageResult findPageByCommentType(Long userId, Integer commentType, Long page, Long pagesize);

}
