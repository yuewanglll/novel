package io.github.xxyopen.novel.controller.front;


import io.github.xxyopen.novel.core.common.resp.RestResp;
import io.github.xxyopen.novel.dto.resp.AdminAuthorRespDto;
import io.github.xxyopen.novel.dto.resp.AdminBookRespDto;
import io.github.xxyopen.novel.dto.resp.AdminCommentRespDto;
import io.github.xxyopen.novel.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "AdminController", description = "平台后台管理")
@SecurityRequirement(name = "Authorization")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // ─── 小说管理 ───────────────────────────────────────

    @Operation(summary = "查询所有小说列表")
    @GetMapping("/books")
    public RestResp<List<AdminBookRespDto>> listBooks(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int pageSize) {
        return adminService.listBooks(pageNum, pageSize);
    }

    @Operation(summary = "删除小说")
    @DeleteMapping("/book/{bookId}")
    public RestResp<Void> deleteBook(
            @Parameter(description = "小说ID") @PathVariable Long bookId) {
        return adminService.deleteBook(bookId);
    }

    @Operation(summary = "删除章节")
    @DeleteMapping("/chapter/{chapterId}")
    public RestResp<Void> deleteChapter(
            @Parameter(description = "章节ID") @PathVariable Long chapterId) {
        return adminService.deleteChapter(chapterId);
    }

    // ─── 评论管理 ───────────────────────────────────────

    @Operation(summary = "查询所有评论列表")
    @GetMapping("/comments")
    public RestResp<List<AdminCommentRespDto>> listComments(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int pageSize) {
        return adminService.listComments(pageNum, pageSize);
    }

    @Operation(summary = "删除评论")
    @DeleteMapping("/comment/{commentId}")
    public RestResp<Void> deleteComment(
            @Parameter(description = "评论ID") @PathVariable Long commentId) {
        return adminService.deleteComment(commentId);
    }

    // ─── 作者管理 ───────────────────────────────────────

    @Operation(summary = "查询作者列表")
    @GetMapping("/authors")
    public RestResp<List<AdminAuthorRespDto>> listAuthors(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int pageSize) {
        return adminService.listAuthors(pageNum, pageSize);
    }

    @Operation(summary = "修改作者状态（封禁/恢复）")
    @PutMapping("/author/{authorId}/status")
    public RestResp<Void> updateAuthorStatus(
            @Parameter(description = "作者ID") @PathVariable Long authorId,
            @Parameter(description = "状态 0-正常 1-封禁") @RequestParam Integer status) {
        return adminService.updateAuthorStatus(authorId, status);
    }
}
