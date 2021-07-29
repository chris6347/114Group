package com.tanhua.domain.db;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_settings")
public class Settings extends BasePojo {
    private Long id;
    private Long userId;
    private Boolean likeNotification = true;    // 喜欢通知
    private Boolean pinglunNotification = true;    // 评论通知
    private Boolean gonggaoNotification = true;    // 公告通知
}