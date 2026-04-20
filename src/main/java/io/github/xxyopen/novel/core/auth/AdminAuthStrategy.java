package io.github.xxyopen.novel.core.auth;

import io.github.xxyopen.novel.core.common.constant.ErrorCodeEnum;
import io.github.xxyopen.novel.core.common.exception.BusinessException;
import io.github.xxyopen.novel.core.util.JwtUtils;
import io.github.xxyopen.novel.manager.cache.UserInfoCacheManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 平台后台管理系统 认证授权策略
 */
@Component
@RequiredArgsConstructor
public class AdminAuthStrategy implements AuthStrategy {

    private final JwtUtils jwtUtils;

    private final UserInfoCacheManager userInfoCacheManager;
    @Override
    public void auth(String token, String requestUri) throws BusinessException {
        System.out.println("=== AdminAuthStrategy.auth 被调用 ===");
        System.out.println("=== token: " + token);
        // 复用 SSO 统一认证，解析 token 并设置 userId 到当前线程
        Long userId = authSSO(jwtUtils, userInfoCacheManager, token);

        // 额外校验：判断该用户是否是管理员（role = 1）
        var userInfo = userInfoCacheManager.getUser(userId);
        System.out.println("=== userInfo: " + userInfo);
        System.out.println("=== role: " + (userInfo != null ? userInfo.getRole() : "null"));

        if (userInfo == null || !Integer.valueOf(1).equals(userInfo.getRole())) {
            System.out.println("=== 鉴权失败，role不是1 ===");
            throw new BusinessException(ErrorCodeEnum.USER_UN_AUTH);
        }
        System.out.println("=== 鉴权成功 ===");
    }

}