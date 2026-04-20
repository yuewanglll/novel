package io.github.xxyopen.novel.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.xxyopen.novel.dao.entity.UserFollow;
import io.github.xxyopen.novel.dto.resp.UserFollowRespDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface UserFollowMapper extends BaseMapper<UserFollow> {

    /**
     * 查询用户关注列表（关联作者信息）
     */
    List<UserFollowRespDto> listFollowWithAuthorInfo(@Param("userId") Long userId);
}