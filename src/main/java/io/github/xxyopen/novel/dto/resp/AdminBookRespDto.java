package io.github.xxyopen.novel.dto.resp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminBookRespDto {
    private Long id;
    private String bookName;
    private String authorName;
    private String categoryName;
    private Integer bookStatus;
    private Long wordCount;
    private Long visitCount;
    private Integer commentCount;
    private String updateTime;
}