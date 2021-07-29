package com.tanhua.domain.db;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_question")
public class Question extends BasePojo {
    private Long id;
    private Long userId;
    //问题内容
    private String txt;
}