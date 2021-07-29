package com.tanhua.dubbo.api;

import com.tanhua.domain.mongo.UserLocation;
import com.tanhua.domain.vo.UserLocationVo;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

@Service
public class UserLocationApiImpl implements UserLocationApi{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(Double latitude, Double longitude, UserLocation userLocation) {
        Query query = new Query(Criteria.where("userId").is(userLocation.getUserId()));
        GeoJsonPoint location = new GeoJsonPoint(latitude,longitude);
        Long now = System.currentTimeMillis();
        if (mongoTemplate.exists(query,UserLocation.class)) {
            Update update = new Update();
            update.set("location",location);
            update.set("address",userLocation.getAddress());
            update.set("updated",now);
            mongoTemplate.updateFirst(query,update,UserLocation.class);
            return;
        }
        userLocation.setLocation(location);
        userLocation.setUpdated(now);
        userLocation.setCreated(now);
        mongoTemplate.insert(userLocation);
    }

    @Override
    public void addLocation(Double latitude, Double longitude, String address, Long userId) {
        UserLocation userLocation = new UserLocation();
        userLocation.setUserId(userId);
        userLocation.setAddress(address);
        userLocation.setLocation(new GeoJsonPoint(latitude,longitude));
        userLocation.setCreated(System.currentTimeMillis());
        userLocation.setUpdated(System.currentTimeMillis());
        mongoTemplate.insert(userLocation);
    }

    @Override
    public List<UserLocationVo> searchNearUser(Long userId, Integer distance) {
        Query query = new Query(Criteria.where("userId").is(userId));
        UserLocation userL = mongoTemplate.findOne(query, UserLocation.class);
        if ( null == userL ) {
            return null;
        }
        GeoJsonPoint location = userL.getLocation();
        // 以半径distance构建圆Cycle  // metrics: 指标  , kilometers: 公里 , radius: 半径
        Distance radius = new Distance(distance.doubleValue()/1000, Metrics.KILOMETERS);
        Circle circle = new Circle(location,radius);

        query = new Query(Criteria.where("userId").ne(userId));
        query.addCriteria(Criteria.where("location").withinSphere(circle));
        List<UserLocation> nearUserLocations = mongoTemplate.find(query, UserLocation.class);
        return UserLocationVo.formatToList(nearUserLocations);
    }

}
