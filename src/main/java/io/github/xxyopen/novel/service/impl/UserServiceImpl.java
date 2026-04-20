package io.github.xxyopen.novel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.github.xxyopen.novel.core.common.constant.CommonConsts;
import io.github.xxyopen.novel.core.common.constant.ErrorCodeEnum;
import io.github.xxyopen.novel.core.common.exception.BusinessException;
import io.github.xxyopen.novel.core.common.resp.RestResp;
import io.github.xxyopen.novel.core.constant.DatabaseConsts;
import io.github.xxyopen.novel.core.constant.SystemConfigConsts;
import io.github.xxyopen.novel.core.util.JwtUtils;
import io.github.xxyopen.novel.dao.entity.*;
import io.github.xxyopen.novel.dao.mapper.*;
import io.github.xxyopen.novel.dto.req.UserInfoUptReqDto;
import io.github.xxyopen.novel.dto.req.UserLoginReqDto;
import io.github.xxyopen.novel.dto.req.UserRegisterReqDto;
import io.github.xxyopen.novel.dto.resp.*;
import io.github.xxyopen.novel.manager.redis.VerifyCodeManager;
import io.github.xxyopen.novel.service.UserService;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * 会员模块 服务实现类
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserInfoMapper userInfoMapper;

    private final VerifyCodeManager verifyCodeManager;

    private final UserFeedbackMapper userFeedbackMapper;

    private final UserBookshelfMapper userBookshelfMapper;

    private final JwtUtils jwtUtils;

    private final BookInfoMapper bookInfoMapper;

    private final UserFollowMapper userFollowMapper;

    @Override
    public RestResp<UserRegisterRespDto> register(UserRegisterReqDto dto) {
        // 校验图形验证码是否正确
        if (!verifyCodeManager.imgVerifyCodeOk(dto.getSessionId(), dto.getVelCode())) {
            // 图形验证码校验失败
            throw new BusinessException(ErrorCodeEnum.USER_VERIFY_CODE_ERROR);
        }

        // 校验手机号是否已注册
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.UserInfoTable.COLUMN_USERNAME, dto.getUsername())
            .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        if (userInfoMapper.selectCount(queryWrapper) > 0) {
            // 手机号已注册
            throw new BusinessException(ErrorCodeEnum.USER_NAME_EXIST);
        }

        // 注册成功，保存用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setPassword(
            DigestUtils.md5DigestAsHex(dto.getPassword().getBytes(StandardCharsets.UTF_8)));
        userInfo.setUsername(dto.getUsername());
        userInfo.setNickName(dto.getUsername());
        userInfo.setCreateTime(LocalDateTime.now());
        userInfo.setUpdateTime(LocalDateTime.now());
        userInfo.setSalt("0");
        userInfoMapper.insert(userInfo);

        // 删除验证码
        verifyCodeManager.removeImgVerifyCode(dto.getSessionId());




        // 生成JWT 并返回
        return RestResp.ok(
            UserRegisterRespDto.builder()
                .token(jwtUtils.generateToken(userInfo.getId(), SystemConfigConsts.NOVEL_FRONT_KEY))
                .uid(userInfo.getId())
                .build()
        );

    }

    @Override
    public RestResp<UserLoginRespDto> login(UserLoginReqDto dto) {
        // 查询用户信息
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.UserInfoTable.COLUMN_USERNAME, dto.getUsername())
            .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);
        if (Objects.isNull(userInfo)) {
            // 用户不存在
            throw new BusinessException(ErrorCodeEnum.USER_ACCOUNT_NOT_EXIST);
        }

        // 判断密码是否正确
        if (!Objects.equals(userInfo.getPassword()
            , DigestUtils.md5DigestAsHex(dto.getPassword().getBytes(StandardCharsets.UTF_8)))) {
            // 密码错误
            throw new BusinessException(ErrorCodeEnum.USER_PASSWORD_ERROR);
        }

        // 登录成功，生成JWT并返回
        return RestResp.ok(UserLoginRespDto.builder()
            .token(jwtUtils.generateToken(userInfo.getId(), SystemConfigConsts.NOVEL_FRONT_KEY))
            .uid(userInfo.getId())
            .nickName(userInfo.getNickName()).build());
    }

    @Override
    public RestResp<Void> saveFeedback(Long userId, String content) {
        UserFeedback userFeedback = new UserFeedback();
        userFeedback.setUserId(userId);
        userFeedback.setContent(content);
        userFeedback.setCreateTime(LocalDateTime.now());
        userFeedback.setUpdateTime(LocalDateTime.now());
        userFeedbackMapper.insert(userFeedback);
        return RestResp.ok();
    }

    @Override
    public RestResp<Void> updateUserInfo(UserInfoUptReqDto dto) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(dto.getUserId());
        userInfo.setNickName(dto.getNickName());
        userInfo.setUserPhoto(dto.getUserPhoto());
        userInfo.setUserSex(dto.getUserSex());
        userInfoMapper.updateById(userInfo);
        return RestResp.ok();
    }

    @Override
    public RestResp<Void> deleteFeedback(Long userId, Long id) {
        QueryWrapper<UserFeedback> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.CommonColumnEnum.ID.getName(), id)
            .eq(DatabaseConsts.UserFeedBackTable.COLUMN_USER_ID, userId);
        userFeedbackMapper.delete(queryWrapper);
        return RestResp.ok();
    }

    @Override
    public RestResp<Integer> getBookshelfStatus(Long userId, String bookId) {
        QueryWrapper<UserBookshelf> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.UserBookshelfTable.COLUMN_USER_ID, userId)
            .eq(DatabaseConsts.UserBookshelfTable.COLUMN_BOOK_ID, bookId);
        return RestResp.ok(
            userBookshelfMapper.selectCount(queryWrapper) > 0
                ? CommonConsts.YES
                : CommonConsts.NO
        );
    }

    @Override
    public RestResp<UserInfoRespDto> getUserInfo(Long userId) {
        UserInfo userInfo = userInfoMapper.selectById(userId);
        return RestResp.ok(UserInfoRespDto.builder()
            .nickName(userInfo.getNickName())
            .userSex(userInfo.getUserSex())
            .userPhoto(userInfo.getUserPhoto())
            .build());
    }


    /**
     * 加入书架
     */
    @Override
    public RestResp<Void> addBookshelf(Long userId, String bookId) {
        // 检查是否已在书架
        Long count = userBookshelfMapper.selectCount(
                new LambdaQueryWrapper<UserBookshelf>()
                        .eq(UserBookshelf::getUserId, userId)
                        .eq(UserBookshelf::getBookId, Long.valueOf(bookId))
        );
        if (count > 0) {
            // 已在书架，直接返回成功（幂等处理）
            return RestResp.ok();
        }
        // 插入书架记录
        UserBookshelf bookshelf = new UserBookshelf();
        bookshelf.setUserId(userId);
        bookshelf.setBookId(Long.valueOf(bookId));
        userBookshelfMapper.insert(bookshelf);
        return RestResp.ok();
    }

    /**
     * 移出书架
     */
    @Override
    public RestResp<Void> removeBookshelf(Long userId, String bookId) {
        userBookshelfMapper.delete(
                new LambdaQueryWrapper<UserBookshelf>()
                        .eq(UserBookshelf::getUserId, userId)
                        .eq(UserBookshelf::getBookId, Long.valueOf(bookId))
        );
        return RestResp.ok();
    }

    /**
     * 查询书架列表
     */
    @Override
    public RestResp<List<UserBookshelfRespDto>> listBookshelf(Long userId) {
        // 查询书架记录
        List<UserBookshelf> bookshelfList = userBookshelfMapper.selectList(
                new LambdaQueryWrapper<UserBookshelf>()
                        .eq(UserBookshelf::getUserId, userId)
                        .orderByDesc(UserBookshelf::getUpdateTime)
        );
        if (bookshelfList.isEmpty()) {
            return RestResp.ok(Collections.emptyList());
        }
        // 提取所有 bookId
        List<Long> bookIds = bookshelfList.stream()
                .map(UserBookshelf::getBookId)
                .collect(Collectors.toList());
        // 批量查询小说信息
        List<BookInfo> bookInfoList = bookInfoMapper.selectList(
                new LambdaQueryWrapper<BookInfo>()
                        .in(BookInfo::getId, bookIds)
        );
        // 转成 Map 方便查找
        Map<Long, BookInfo> bookInfoMap = bookInfoList.stream()
                .collect(Collectors.toMap(BookInfo::getId, b -> b));
        // 组装响应数据
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<UserBookshelfRespDto> result = bookshelfList.stream()
                .map(shelf -> {
                    BookInfo book = bookInfoMap.get(shelf.getBookId());
                    if (book == null) return null;
                    return UserBookshelfRespDto.builder()
                            .bookId(book.getId())
                            .bookName(book.getBookName())
                            .authorName(book.getAuthorName())
                            .picUrl(book.getPicUrl())
                            .categoryName(book.getCategoryName())
                            .lastChapterName(book.getLastChapterName())
                            .lastChapterUpdateTime(
                                    book.getLastChapterUpdateTime() != null
                                            ? book.getLastChapterUpdateTime().format(formatter)
                                            : null
                            )
                            .preContentId(shelf.getPreContentId())
                            .createTime(
                                    shelf.getCreateTime() != null
                                            ? shelf.getCreateTime().format(formatter)
                                            : null
                            )
                            .build();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return RestResp.ok(result);
    }

    @Override
    public RestResp<Void> updateReadingProgress(
            Long userId, String bookId, Long contentId) {
        userBookshelfMapper.update(null,
                new LambdaUpdateWrapper<UserBookshelf>()
                        .eq(UserBookshelf::getUserId, userId)
                        .eq(UserBookshelf::getBookId, Long.valueOf(bookId))
                        .set(UserBookshelf::getPreContentId, contentId)
        );
        return RestResp.ok();
    }


    /**
     * 关注作者
     * <p>
     * 实现幂等性处理：若用户已关注该作者，直接返回成功，避免重复插入数据。
     * </p>
     *
     * @param userId   当前登录用户ID（关注者）
     * @param authorId 被关注的作者ID
     * @return 操作结果，成功返回 RestResp.ok()
     */
    @Override
    public RestResp<Void> followAuthor(Long userId, Long authorId) {
        // 幂等处理：已关注则直接返回成功
        Long count = userFollowMapper.selectCount(
                new LambdaQueryWrapper<UserFollow>()
                        .eq(UserFollow::getUserId, userId)
                        .eq(UserFollow::getAuthorId, authorId)
        );
        if (count > 0) {
            return RestResp.ok();
        }
        UserFollow follow = new UserFollow();
        follow.setUserId(userId);
        follow.setAuthorId(authorId);
        follow.setCreateTime(LocalDateTime.now());
        userFollowMapper.insert(follow);
        return RestResp.ok();
    }

    /**
     * 取消关注作者
     * <p>
     * 无论关注关系是否存在，执行删除操作后均返回成功，保证接口幂等性。
     * </p>
     *
     * @param userId   当前登录用户ID
     * @param authorId 被取消关注的作者ID
     * @return 操作结果，成功返回 RestResp.ok()
     */
    @Override
    public RestResp<Void> unfollowAuthor(Long userId, Long authorId) {
        userFollowMapper.delete(
                new LambdaQueryWrapper<UserFollow>()
                        .eq(UserFollow::getUserId, userId)
                        .eq(UserFollow::getAuthorId, authorId)
        );
        return RestResp.ok();
    }

    /**
     * 获取用户对作者的关注状态
     *
     * @param userId   当前登录用户ID
     * @param authorId 目标作者ID
     * @return 关注状态：1-已关注，0-未关注
     */
    @Override
    public RestResp<Integer> getFollowStatus(Long userId, Long authorId) {
        Long count = userFollowMapper.selectCount(
                new LambdaQueryWrapper<UserFollow>()
                        .eq(UserFollow::getUserId, userId)
                        .eq(UserFollow::getAuthorId, authorId)
        );
        return RestResp.ok(count > 0 ? 1 : 0);
    }

    /**
     * 获取用户的关注列表（含作者基本信息）
     *
     * @param userId 当前登录用户ID
     * @return 关注列表，若无数据则返回空列表，避免返回 null
     */
    @Override
    public RestResp<List<UserFollowRespDto>> listFollows(Long userId) {
        List<UserFollowRespDto> list = userFollowMapper.listFollowWithAuthorInfo(userId);
        return RestResp.ok(list == null ? Collections.emptyList() : list);
    }
}
