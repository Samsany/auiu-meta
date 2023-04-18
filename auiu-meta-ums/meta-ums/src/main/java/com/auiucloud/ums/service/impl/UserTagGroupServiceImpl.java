package com.auiucloud.ums.service.impl;

import cn.hutool.core.util.StrUtil;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.ums.domain.UserGroup;
import com.auiucloud.ums.service.IUserTagService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.auiucloud.ums.domain.UserTagGroup;
import com.auiucloud.ums.service.IUserTagGroupService;
import com.auiucloud.ums.mapper.UserTagGroupMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author dries
* @description 针对表【ums_user_tag_group(用户标签分类表)】的数据库操作Service实现
* @createDate 2023-04-03 22:22:38
*/
@Service
@RequiredArgsConstructor
public class UserTagGroupServiceImpl extends ServiceImpl<UserTagGroupMapper, UserTagGroup>
    implements IUserTagGroupService {

    private final IUserTagService userTagService;

    @Override
    public PageUtils listPage(Search search, UserTagGroup userTagGroup) {
        LambdaQueryWrapper<UserTagGroup> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(StrUtil.isNotBlank(userTagGroup.getName()), UserTagGroup::getName, userTagGroup.getName());
        queryWrapper.orderByDesc(UserTagGroup::getSort);
        return new PageUtils(this.page(PageUtils.getPage(search), queryWrapper));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeUserTagGroupById(Long id) {
        if (userTagService.checkHasChildTag(id)) {
            UserTagGroup tagGroup = this.getById(id);
            throw new ApiException("【" + tagGroup.getName() + "】已分配用户标签，不允许删除");
        }
        return this.removeById(id);
    }
}




