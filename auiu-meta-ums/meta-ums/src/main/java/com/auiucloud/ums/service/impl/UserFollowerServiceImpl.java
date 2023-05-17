package com.auiucloud.ums.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.auiucloud.core.common.api.ApiResult;
import com.auiucloud.core.common.constant.CommonConstant;
import com.auiucloud.core.common.utils.SecurityUtil;
import com.auiucloud.ums.domain.UserFollower;
import com.auiucloud.ums.mapper.UserFollowerMapper;
import com.auiucloud.ums.service.IUserFollowerService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author dries
 * @description 针对表【ums_user_follower(我的关注粉丝表)】的数据库操作Service实现
 * @createDate 2023-04-24 10:03:15
 */
@Service
public class UserFollowerServiceImpl extends ServiceImpl<UserFollowerMapper, UserFollower>
        implements IUserFollowerService {

    @Override
    public List<UserFollower> selectAllFollowerListByUIds(List<Long> userIds) {
        LambdaQueryWrapper<UserFollower> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserFollower::getStatus, CommonConstant.STATUS_NORMAL_VALUE);
        queryWrapper.and(qr ->
                qr.in(UserFollower::getUId, userIds)
                        .or().in(UserFollower::getFollowerId, userIds));
        return Optional.ofNullable(this.list(queryWrapper))
                .orElse(Collections.emptyList());
    }

    /**
     * 关注/取消关注
     *
     * @param userId 用户ID
     * @return ApiResult<?>
     */
    @Override
    public ApiResult<?> attentionUser(Long userId) {
        Long uId = SecurityUtil.getUserId();
        LambdaQueryWrapper<UserFollower> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserFollower::getUId, userId);
        queryWrapper.eq(UserFollower::getFollowerId, uId);
        UserFollower userFollower = this.getOne(queryWrapper);
        if (ObjectUtil.isNotNull(userFollower)) {
            String message;
            if (userFollower.getStatus().equals(CommonConstant.STATUS_NORMAL_VALUE)) {
                userFollower.setStatus(CommonConstant.STATUS_DISABLE_VALUE);
                message = "取消关注成功";
            } else {
                userFollower.setStatus(CommonConstant.STATUS_NORMAL_VALUE);
                message = "关注成功";
            }
            return ApiResult.condition(message, this.updateById(userFollower));
        } else {
            UserFollower build = UserFollower.builder()
                    .uId(userId)
                    .followerId(uId)
                    .status(CommonConstant.STATUS_NORMAL_VALUE)
                    .build();
            return ApiResult.condition("关注成功", this.save(build));
        }
    }

    /**
     * 查询粉丝数
     *
     * @param uId 用户ID
     * @return long
     */
    @Override
    public long countUserFollower(Long uId) {
        LambdaQueryWrapper<UserFollower> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserFollower::getStatus, CommonConstant.STATUS_NORMAL_VALUE);
        queryWrapper.eq(UserFollower::getUId, uId);
        return this.count(queryWrapper);
    }

    /**
     * 查询我的关注数
     *
     * @param uId 用户ID
     * @return long
     */
    @Override
    public long countUserAttention(Long uId) {
        LambdaQueryWrapper<UserFollower> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserFollower::getStatus, CommonConstant.STATUS_NORMAL_VALUE);
        queryWrapper.eq(UserFollower::getFollowerId, uId);
        return this.count(queryWrapper);
    }

    @Override
    public boolean checkedAttentionUser(Long userId, Long creatorId) {
        LambdaQueryWrapper<UserFollower> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserFollower::getStatus, CommonConstant.STATUS_NORMAL_VALUE);
        queryWrapper.eq(UserFollower::getUId, creatorId);
        queryWrapper.eq(UserFollower::getFollowerId, userId);
        return this.count(queryWrapper) > 0;
    }

}




