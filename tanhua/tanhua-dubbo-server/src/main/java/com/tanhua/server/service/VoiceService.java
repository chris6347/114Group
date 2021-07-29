package com.tanhua.server.service;

import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.tanhua.commons.exception.TanHuaException;
import com.tanhua.commons.templates.OssTemplate;
import com.tanhua.domain.db.UserInfo;
import com.tanhua.domain.mongo.Voice;
import com.tanhua.domain.vo.VoiceVo;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.dubbo.api.VoiceApi;
import com.tanhua.server.interceptor.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class VoiceService {

    @Reference
    private VoiceApi voiceApi;

    @Reference
    private UserInfoApi userInfoApi;

    @Autowired
    private FdfsWebServer fdfsWebServer;

    @Autowired
    private FastFileStorageClient client;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${tanhua.voiceCountPrefix}")
    private String voicePrefix;

    @Autowired
    private OssTemplate ossTemplate;

    public void sendVoice(MultipartFile soundFile) {
        Long userId = UserHolder.getUserId();
        if (null == soundFile) {
            return;
        }
        try {
            String url = ossTemplate.upload(soundFile.getOriginalFilename(), soundFile.getInputStream());
            log.info("voiceUrl:{}",url);
            Voice voice = new Voice(userId,url);
            voiceApi.save(voice);
        } catch (IOException e) {
            e.printStackTrace();
            throw new TanHuaException("语音发送失败");
        }
        /*String name = soundFile.getOriginalFilename();
        try {
            String suffix = name.substring(name.indexOf(".")+1);
            StorePath storePath = client.uploadFile(soundFile.getInputStream(), soundFile.getSize(), suffix, null);
            String url = fdfsWebServer.getWebServerUrl()+"/"+storePath.getFullPath();
            log.info("voiceUrl:{}",url);
            Voice voice = new Voice();
            voice.setUserId(userId);
            voice.setSoundUrl(url);
            voiceApi.save(voice);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public VoiceVo receiveVoice() {
        Long userId = UserHolder.getUserId();
        String key = voicePrefix+userId;
        Integer voiceCount = (Integer) redisTemplate.opsForValue().get(key);
        if (null == voiceCount) {
            redisTemplate.opsForValue().set(key,10,1,TimeUnit.DAYS);
            voiceCount = 10;
        }
        if (voiceCount <= 0) {
            throw new TanHuaException("今日接收次数用完");
        }
        VoiceVo vo = new VoiceVo();
        List<Voice> voices = voiceApi.findAll();
        if (!CollectionUtils.isEmpty(voices)) {
            // 不包尾吗
            int index = RandomUtils.nextInt(0, voices.size());
            Voice voice = voices.get(index);
            UserInfo userInfo = userInfoApi.findById(voice.getUserId());
            BeanUtils.copyProperties(userInfo,vo);
            vo.setId(userInfo.getId().intValue());
            vo.setSoundUrl(voice.getSoundUrl());
            voiceCount -= 1;
            vo.setRemainingTimes(voiceCount);
            // 删除
            if (voices.size()>=10) {
                voiceApi.deleteOne(voice);
            }
        }
        // 减次数
        redisTemplate.opsForValue().set(key,voiceCount,1, TimeUnit.DAYS);
        return vo;
    }
}
