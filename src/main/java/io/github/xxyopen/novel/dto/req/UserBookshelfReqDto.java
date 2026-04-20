package io.github.xxyopen.novel.dto.req;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "书架操作-请求DTO")
public class UserBookshelfReqDto {

    @Schema(description = "小说ID", required = true)
    private String bookId;
}