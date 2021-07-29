package com.tanhua.domain.vo;
import lombok.Data;

import java.util.List;
@Data
public class QuestionsVo {

    private String id;
    private String question;

    private List<OptionsVo> options;

}
