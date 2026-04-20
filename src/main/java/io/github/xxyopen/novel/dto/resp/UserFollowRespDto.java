package io.github.xxyopen.novel.dto.resp;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "关注列表-响应DTO")
public class UserFollowRespDto {

    @Schema(description = "作者ID")
    private Long authorId;

    @Schema(description = "作者笔名")
    private String penName;

    @Schema(description = "作品方向 0-男频 1-女频")
    private Integer workDirection;

    @Schema(description = "关注时间")
    private String followTime;
}