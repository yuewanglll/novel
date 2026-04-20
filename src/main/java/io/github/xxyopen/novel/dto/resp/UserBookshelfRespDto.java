package io.github.xxyopen.novel.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "书架列表-响应DTO")
public class UserBookshelfRespDto {

    @Schema(description = "小说ID")
    private Long bookId;

    @Schema(description = "小说名")
    private String bookName;

    @Schema(description = "作者名")
    private String authorName;

    @Schema(description = "小说封面")
    private String picUrl;

    @Schema(description = "小说分类名")
    private String categoryName;

    @Schema(description = "最新章节名")
    private String lastChapterName;

    @Schema(description = "最新章节更新时间")
    private String lastChapterUpdateTime;

    @Schema(description = "上次阅读的章节内容ID（阅读进度）")
    private Long preContentId;

    @Schema(description = "加入书架时间")
    private String createTime;
}
