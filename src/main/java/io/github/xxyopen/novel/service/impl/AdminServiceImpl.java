package io.github.xxyopen.novel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.xxyopen.novel.core.common.resp.RestResp;
import io.github.xxyopen.novel.dao.entity.AuthorInfo;
import io.github.xxyopen.novel.dao.entity.BookChapter;
import io.github.xxyopen.novel.dao.entity.BookComment;
import io.github.xxyopen.novel.dao.entity.BookInfo;
import io.github.xxyopen.novel.dao.mapper.AuthorInfoMapper;
import io.github.xxyopen.novel.dao.mapper.BookChapterMapper;
import io.github.xxyopen.novel.dao.mapper.BookCommentMapper;
import io.github.xxyopen.novel.dao.mapper.BookInfoMapper;
import io.github.xxyopen.novel.dto.resp.AdminAuthorRespDto;
import io.github.xxyopen.novel.dto.resp.AdminBookRespDto;
import io.github.xxyopen.novel.dto.resp.AdminCommentRespDto;
import io.github.xxyopen.novel.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final BookInfoMapper bookInfoMapper;
    private final BookChapterMapper bookChapterMapper;
    private final BookCommentMapper bookCommentMapper;
    private final AuthorInfoMapper authorInfoMapper;

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // ─── 小说管理 ───────────────────────────────────────

    @Override
    public RestResp<List<AdminBookRespDto>> listBooks(int pageNum, int pageSize) {
        Page<BookInfo> page = new Page<>(pageNum, pageSize);
        bookInfoMapper.selectPage(page, new LambdaQueryWrapper<BookInfo>()
                .orderByDesc(BookInfo::getUpdateTime));
        List<AdminBookRespDto> list = page.getRecords().stream()
                .map(b -> AdminBookRespDto.builder()
                        .id(b.getId())
                        .bookName(b.getBookName())
                        .authorName(b.getAuthorName())
                        .categoryName(b.getCategoryName())
                        .bookStatus(b.getBookStatus())
                        .wordCount(b.getWordCount() == null ? 0L : b.getWordCount().longValue())
                        .visitCount(b.getVisitCount())
                        .commentCount(b.getCommentCount())
                        .updateTime(b.getUpdateTime() != null
                                ? b.getUpdateTime().format(FMT) : null)
                        .build())
                .collect(Collectors.toList());
        return RestResp.ok(list);
    }

    @Override
    public RestResp<Void> deleteBook(Long bookId) {
        // 删除小说下所有章节
        bookChapterMapper.delete(new LambdaQueryWrapper<BookChapter>()
                .eq(BookChapter::getBookId, bookId));
        // 删除小说信息
        bookInfoMapper.deleteById(bookId);
        return RestResp.ok();
    }

    @Override
    public RestResp<Void> deleteChapter(Long chapterId) {
        bookChapterMapper.deleteById(chapterId);
        return RestResp.ok();
    }

    // ─── 评论管理 ───────────────────────────────────────

    @Override
    public RestResp<List<AdminCommentRespDto>> listComments(int pageNum, int pageSize) {
        Page<BookComment> page = new Page<>(pageNum, pageSize);
        bookCommentMapper.selectPage(page, new LambdaQueryWrapper<BookComment>()
                .orderByDesc(BookComment::getCreateTime));
        List<AdminCommentRespDto> list = page.getRecords().stream()
                .map(c -> AdminCommentRespDto.builder()
                        .id(c.getId())
                        .commentContent(c.getCommentContent())
                        .commentUser(String.valueOf(c.getUserId()))
                        .commentTime(c.getCreateTime() != null
                                ? c.getCreateTime().format(FMT) : null)
                        .build())
                .collect(Collectors.toList());
        return RestResp.ok(list);
    }

    @Override
    public RestResp<Void> deleteComment(Long commentId) {
        bookCommentMapper.deleteById(commentId);
        return RestResp.ok();
    }

    // ─── 作者管理 ───────────────────────────────────────

    @Override
    public RestResp<List<AdminAuthorRespDto>> listAuthors(int pageNum, int pageSize) {
        Page<AuthorInfo> page = new Page<>(pageNum, pageSize);
        authorInfoMapper.selectPage(page, new LambdaQueryWrapper<AuthorInfo>()
                .orderByDesc(AuthorInfo::getCreateTime));
        List<AdminAuthorRespDto> list = page.getRecords().stream()
                .map(a -> AdminAuthorRespDto.builder()
                        .id(a.getId())
                        .penName(a.getPenName())
                        .telPhone(a.getTelPhone())
                        .email(a.getEmail())
                        .workDirection(a.getWorkDirection())
                        .status(a.getStatus())
                        .createTime(a.getCreateTime() != null
                                ? a.getCreateTime().format(FMT) : null)
                        .build())
                .collect(Collectors.toList());
        return RestResp.ok(list);
    }

    @Override
    public RestResp<Void> updateAuthorStatus(Long authorId, Integer status) {
        authorInfoMapper.update(null, new LambdaUpdateWrapper<AuthorInfo>()
                .eq(AuthorInfo::getId, authorId)
                .set(AuthorInfo::getStatus, status));
        return RestResp.ok();
    }
}