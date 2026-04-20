package io.github.xxyopen.novel.controller.front;

import io.github.xxyopen.novel.core.auth.UserHolder;
import io.github.xxyopen.novel.core.common.req.PageReqDto;
import io.github.xxyopen.novel.core.common.resp.PageRespDto;
import io.github.xxyopen.novel.core.common.resp.RestResp;
import io.github.xxyopen.novel.core.constant.ApiRouterConsts;
import io.github.xxyopen.novel.core.constant.SystemConfigConsts;
import io.github.xxyopen.novel.dao.entity.UserInfo;
import io.github.xxyopen.novel.dao.mapper.UserInfoMapper;
import io.github.xxyopen.novel.dto.req.*;
import io.github.xxyopen.novel.dto.resp.*;
import io.github.xxyopen.novel.service.BookService;
import io.github.xxyopen.novel.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前台门户-用户模块 API 控制器
 *
 */
@Tag(name = "UserController", description = "前台门户-用户模块")
@SecurityRequirement(name = SystemConfigConsts.HTTP_AUTH_HEADER_NAME)
@RestController
@RequestMapping(ApiRouterConsts.API_FRONT_USER_URL_PREFIX)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final BookService bookService;

    private final UserInfoMapper userInfoMapper;
    /**
     * 用户注册接口
     */
    @Operation(summary = "用户注册接口")
    @PostMapping("register")
    public RestResp<UserRegisterRespDto> register(@Valid @RequestBody UserRegisterReqDto dto) {
        return userService.register(dto);
    }

    /**
     * 用户登录接口
     */
    @Operation(summary = "用户登录接口")
    @PostMapping("login")
    public RestResp<UserLoginRespDto> login(@Valid @RequestBody UserLoginReqDto dto) {
        return userService.login(dto);
    }

    /**
     * 用户信息查询接口
     */
    @Operation(summary = "用户信息查询接口")
    @GetMapping
    public RestResp<UserInfoRespDto> getUserInfo() {
        return userService.getUserInfo(UserHolder.getUserId());
    }

    @Operation(summary = "查询当前用户角色接口")
    @GetMapping("role")
    public RestResp<Integer> getUserRole() {
        Long userId = UserHolder.getUserId();
        UserInfo userInfo = userInfoMapper.selectById(userId);
        return RestResp.ok(userInfo != null ? userInfo.getRole() : 0);
    }


    /**
     * 用户信息修改接口
     */
    @Operation(summary = "用户信息修改接口")
    @PutMapping
    public RestResp<Void> updateUserInfo(@Valid @RequestBody UserInfoUptReqDto dto) {
        dto.setUserId(UserHolder.getUserId());
        return userService.updateUserInfo(dto);
    }

    /**
     * 用户反馈提交接口
     */
    @Operation(summary = "用户反馈提交接口")
    @PostMapping("feedback")
    public RestResp<Void> submitFeedback(@RequestBody String content) {
        return userService.saveFeedback(UserHolder.getUserId(), content);
    }

    /**
     * 用户反馈删除接口
     */
    @Operation(summary = "用户反馈删除接口")
    @DeleteMapping("feedback/{id}")
    public RestResp<Void> deleteFeedback(@Parameter(description = "反馈ID") @PathVariable Long id) {
        return userService.deleteFeedback(UserHolder.getUserId(), id);
    }

    /**
     * 发表评论接口
     */
    @Operation(summary = "发表评论接口")
    @PostMapping("comment")
    public RestResp<Void> comment(@Valid @RequestBody UserCommentReqDto dto) {
        dto.setUserId(UserHolder.getUserId());
        return bookService.saveComment(dto);
    }

    /**
     * 修改评论接口
     */
    @Operation(summary = "修改评论接口")
    @PutMapping("comment/{id}")
    public RestResp<Void> updateComment(@Parameter(description = "评论ID") @PathVariable Long id,
        String content) {
        return bookService.updateComment(UserHolder.getUserId(), id, content);
    }

    /**
     * 删除评论接口
     */
    @Operation(summary = "删除评论接口")
    @DeleteMapping("comment/{id}")
    public RestResp<Void> deleteComment(@Parameter(description = "评论ID") @PathVariable Long id) {
        return bookService.deleteComment(UserHolder.getUserId(), id);
    }

    /**
     * 查询书架状态接口 0-不在书架 1-已在书架
     */
    @Operation(summary = "查询书架状态接口")
    @GetMapping("bookshelf_status")
    public RestResp<Integer> getBookshelfStatus(@Parameter(description = "小说ID") String bookId) {
        return userService.getBookshelfStatus(UserHolder.getUserId(), bookId);
    }


    /**
     * 分页查询我的评论
     */
    @Operation(summary = "查询本人评论接口")
    @GetMapping("comments")
    public RestResp<PageRespDto<UserCommentRespDto>> listComments(PageReqDto pageReqDto) {
        return bookService.listComments(UserHolder.getUserId(), pageReqDto);
    }

    @Operation(summary = "加入书架接口")
    @PostMapping("bookshelf")
    public RestResp<Void> addBookshelf(
            @RequestBody UserBookshelfReqDto dto) {
        return userService.addBookshelf(UserHolder.getUserId(), dto.getBookId());
    }

    @Operation(summary = "移出书架接口")
    @DeleteMapping("bookshelf")
    public RestResp<Void> removeBookshelf(
            @RequestBody UserBookshelfReqDto dto) {
        return userService.removeBookshelf(UserHolder.getUserId(), dto.getBookId());
    }

    @Operation(summary = "查询书架列表接口")
    @GetMapping("bookshelf")
    public RestResp<List<UserBookshelfRespDto>> listBookshelf() {
        return userService.listBookshelf(UserHolder.getUserId());
    }

    @Operation(summary = "更新阅读进度接口")
    @PutMapping("bookshelf/reading_progress")
    public RestResp<Void> updateReadingProgress(
            @Parameter(description = "小说ID") @RequestParam String bookId,
            @Parameter(description = "章节内容ID") @RequestParam Long contentId) {
        return userService.updateReadingProgress(
                UserHolder.getUserId(), bookId, contentId);
    }

    @Operation(summary = "关注作者接口")
    @PostMapping("follow")
    public RestResp<Void> followAuthor(
            @RequestBody UserFollowReqDto dto) {
        return userService.followAuthor(UserHolder.getUserId(), dto.getAuthorId());
    }

    @Operation(summary = "取消关注接口")
    @DeleteMapping("follow")
    public RestResp<Void> unfollowAuthor(
            @RequestBody UserFollowReqDto dto) {
        return userService.unfollowAuthor(UserHolder.getUserId(), dto.getAuthorId());
    }

    @Operation(summary = "查询关注状态接口")
    @GetMapping("follow_status")
    public RestResp<Integer> getFollowStatus(
            @Parameter(description = "作者ID") @RequestParam Long authorId) {
        return userService.getFollowStatus(UserHolder.getUserId(), authorId);
    }

    @Operation(summary = "查询关注列表接口")
    @GetMapping("follows")
    public RestResp<List<UserFollowRespDto>> listFollows() {
        return userService.listFollows(UserHolder.getUserId());
    }

}
