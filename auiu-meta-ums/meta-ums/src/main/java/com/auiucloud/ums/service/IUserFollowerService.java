package com.auiucloud.ums.service;

import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.ums.domain.UserFollower;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author dries
* @description 针对表【ums_user_follower(我的关注粉丝表)】的数据库操作Service
* @createDate 2023-04-24 10:03:15
*/
public interface IUserFollowerService extends IService<UserFollower> {

    List<UserFollower> selectAllFollowerListByUIds(List<Long> userIds);

    ApiResult<?> attentionUser(Long userId);

    long countUserFollower(Long uId);

    long countUserAttention(Long uId);

    boolean checkedAttentionUser(Long userId, Long creatorId);

}
