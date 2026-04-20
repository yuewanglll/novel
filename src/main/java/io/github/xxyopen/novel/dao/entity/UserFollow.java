package io.github.xxyopen.novel.dao.entity;



import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user_follow")
public class UserFollow {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 关注者用户ID */
    private Long userId;

    /** 被关注的作者ID（author_info.id） */
    private Long authorId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}