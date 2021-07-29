package com.tanhua.server.test;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.HashMap;
import java.util.Map;

public class JwtTest {

    @Test
    public void testJwt(){
        String secret = "itcast";

        Map<String,Object> claims= new HashMap<>();   // claims 索赔
        claims.put("mobile","12345789");
        claims.put("id","2");

        // 生成token
        String jwt = Jwts.builder()
                .setClaims(claims)      //设置响应数据体
                .signWith(SignatureAlgorithm.HS256,secret)   //设置加密方法和加密盐
                .compact();
        System.out.println(jwt);

        // 解析token
        Map<String,Object> body = Jwts.parser()     // 解析
                .setSigningKey(secret)     // 设置解密盐
                .parseClaimsJws(jwt)       // 解析请求体数据
                .getBody();

        System.out.println(body);
    }

    /*
    * 生成jwt使用的密钥
    * */
    @Test
    public void createSecret(){
        System.out.println(DigestUtils.md5Hex("itcast_tanhua"));
    }


}
