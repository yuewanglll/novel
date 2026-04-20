package io.github.xxyopen.novel.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "关注操作-请求DTO")
public class UserFollowReqDto {

    @Schema(description = "作者ID")
    private Long authorId;
}
