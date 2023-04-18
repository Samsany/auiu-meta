package com.auiucloud.ums.service.impl;

import cn.hutool.core.util.StrUtil;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.ums.domain.Member;
import com.auiucloud.ums.domain.UserGroup;
import com.auiucloud.ums.mapper.UserGroupMapper;
import com.auiucloud.ums.service.IMemberService;
import com.auiucloud.ums.service.IUserGroupService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author dries
 * @description 针对表【ums_user_group(用户分组表)】的数据库操作Service实现
 * @createDate 2023-04-03 22:21:29
 */
@Service
@RequiredArgsConstructor
public class UserGroupServiceImpl extends ServiceImpl<UserGroupMapper, UserGroup>
        implements IUserGroupService {

    private final IMemberService memberService;

    @Override
    public PageUtils listPage(Search search, UserGroup userGroup) {
        LambdaQueryWrapper<UserGroup> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(StrUtil.isNotBlank(userGroup.getGroupName()), UserGroup::getGroupName, userGroup.getGroupName());
        queryWrapper.orderByDesc(UserGroup::getSort);
        return new PageUtils(this.page(PageUtils.getPage(search), queryWrapper));
    }

    @Override
    public boolean saveUserGroup(UserGroup userGroup) {
        if (this.checkUserGroupNameExist(userGroup)) {
            throw new ApiException("【" + userGroup.getGroupName() + "】已存在");
        }
        return this.save(userGroup);
    }

    @Override
    public boolean updateUserGroupById(UserGroup userGroup) {
        if (this.checkUserGroupNameExist(userGroup)) {
            throw new ApiException("【" + userGroup.getGroupName() + "】已存在");
        }
        return this.updateById(userGroup);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeUserGroupById(Long id) {
        if (memberService.checkHasUserByGroupId(id)) {
            UserGroup userGroup = this.getById(id);
            throw new ApiException("【" + userGroup.getGroupName() + "】已分配用户，不允许删除");
        }
        return this.removeById(id);
    }

    @Override
    public boolean checkUserGroupNameExist(UserGroup userGroup) {
        LambdaQueryWrapper<UserGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserGroup::getGroupName, userGroup.getGroupName());
        queryWrapper.ne(userGroup.getId() != null, UserGroup::getId, userGroup.getId());
        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }

}




