package com.tanhua.dubbo.api;

import com.tanhua.domain.mongo.Video;
import com.tanhua.domain.vo.PageResult;

public interface VideoApi {
    void postVideo(Video video);

    PageResult findPage(Long page, Long pageSize);

}
