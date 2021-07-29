package com.tanhua.server.service;

import com.tanhua.domain.mongo.UserLocation;
import com.tanhua.dubbo.api.UserLocationApi;
import com.tanhua.server.interceptor.UserHolder;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    @Reference
    private UserLocationApi userLocationApi;

    public void reportLocation(Double latitude, Double longitude, String addrStr) {
        UserLocation userLocation = new UserLocation();
        userLocation.setAddress(addrStr);
        userLocation.setUserId(UserHolder.getUserId());
        userLocationApi.save(latitude,longitude,userLocation);
    }

}
