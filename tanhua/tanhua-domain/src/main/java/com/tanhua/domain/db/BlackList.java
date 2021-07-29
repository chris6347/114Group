package com.tanhua.domain.db;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_black_list")
public class BlackList extends BasePojo {
    private Long id;
    private Long userId;
    private Long blackUserId;    // 黑名单里的用户的id
}