package io.github.xxyopen.novel.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 作家信息 DTO
 */
@Data
@Builder
public class AuthorInfoDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 作家ID
     */
    private Long id;

    /**
     * 作家笔名
     */
    private String penName;

    /**
     * 作家状态;0-正常 1-封禁
     */
    private Integer status;

}
