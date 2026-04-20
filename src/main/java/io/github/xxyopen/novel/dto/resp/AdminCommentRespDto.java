package io.github.xxyopen.novel.dto.resp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminCommentRespDto {
    private Long id;
    private String bookName;
    private String commentContent;
    private String commentUser;
    private String commentTime;
}
