package com.auiucloud.ums.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.ums.domain.UserTag;
import com.auiucloud.ums.mapper.UserTagMapper;
import com.auiucloud.ums.service.IMemberService;
import com.auiucloud.ums.service.IUserTagService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author dries
 * @description 针对表【ums_user_tag(用户标签表)】的数据库操作Service实现
 * @createDate 2023-04-03 22:22:38
 */
@Service
@RequiredArgsConstructor
public class UserTagServiceImpl extends ServiceImpl<UserTagMapper, UserTag>
        implements IUserTagService {

    private final IMemberService memberService;

    @Override
    public PageUtils listPage(Search search, UserTag userTag) {
        LambdaQueryWrapper<UserTag> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ObjectUtil.isNotNull(userTag.getGroupId()), UserTag::getGroupId, userTag.getGroupId());
        queryWrapper.like(StrUtil.isNotBlank(userTag.getName()), UserTag::getName, userTag.getName());
        queryWrapper.orderByDesc(UserTag::getSort);
        return new PageUtils(this.page(PageUtils.getPage(search), queryWrapper));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeUserTagById(Long id) {
        if (memberService.checkHasUserByTagId(id)) {
            UserTag userTag = this.getById(id);
            throw new ApiException("【" + userTag.getName() + "】已分配用户，不允许删除");
        }
        return this.removeById(id);
    }

    @Override
    public boolean checkHasChildTag(Long tagGroupId) {
        LambdaQueryWrapper<UserTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserTag::getGroupId, tagGroupId);
        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }

}




