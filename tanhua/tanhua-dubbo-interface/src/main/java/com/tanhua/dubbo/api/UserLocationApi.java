package com.tanhua.dubbo.api;

import com.tanhua.domain.mongo.UserLocation;
import com.tanhua.domain.vo.UserLocationVo;

import java.util.List;

public interface UserLocationApi {

    void save(Double latitude, Double longitude, UserLocation userLocation);

    // 测试添加
    void addLocation(Double latitude,Double longitude,String address,Long userId);

    List<UserLocationVo> searchNearUser(Long userId, Integer distance);

}
