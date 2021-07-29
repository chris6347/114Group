package com.tanhua.domain.vo;

import lombok.Data;

@Data
public class VoiceVo {

    private Integer id;
    private String avatar;
    private String nickname;
    private String gender;
    private Integer age;
    private String soundUrl;
    private Integer remainingTimes; //剩余次数

}
