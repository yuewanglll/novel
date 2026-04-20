package io.github.xxyopen.novel.service;

import io.github.xxyopen.novel.core.common.resp.RestResp;
import io.github.xxyopen.novel.dto.resp.AdminAuthorRespDto;
import io.github.xxyopen.novel.dto.resp.AdminBookRespDto;
import io.github.xxyopen.novel.dto.resp.AdminCommentRespDto;
import java.util.List;

public interface AdminService {
    RestResp<List<AdminBookRespDto>> listBooks(int pageNum, int pageSize);
    RestResp<Void> deleteBook(Long bookId);
    RestResp<Void> deleteChapter(Long chapterId);
    RestResp<List<AdminCommentRespDto>> listComments(int pageNum, int pageSize);
    RestResp<Void> deleteComment(Long commentId);
    RestResp<List<AdminAuthorRespDto>> listAuthors(int pageNum, int pageSize);
    RestResp<Void> updateAuthorStatus(Long authorId, Integer status);
}
