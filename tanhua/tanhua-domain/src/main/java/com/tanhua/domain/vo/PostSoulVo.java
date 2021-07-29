package com.tanhua.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PostSoulVo implements Serializable {

    private Long questionId;
    private Long optionId;

}
