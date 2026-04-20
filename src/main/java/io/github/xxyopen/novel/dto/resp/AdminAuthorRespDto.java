package io.github.xxyopen.novel.dto.resp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminAuthorRespDto {
    private Long id;
    private String penName;
    private String telPhone;
    private String email;
    private Integer workDirection;
    private Integer status;
    private String createTime;
}
