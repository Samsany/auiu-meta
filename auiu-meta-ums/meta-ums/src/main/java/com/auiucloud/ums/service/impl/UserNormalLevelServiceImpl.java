package com.auiucloud.ums.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.auiucloud.core.common.exception.ApiException;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.ums.domain.UserNormalLevel;
import com.auiucloud.ums.mapper.UserNormalLevelMapper;
import com.auiucloud.ums.service.IMemberService;
import com.auiucloud.ums.service.IUserNormalLevelService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author dries
 * @description 针对表【ums_user_normal_level(普通会员等级表)】的数据库操作Service实现
 * @createDate 2023-04-03 22:22:38
 */
@Service
@RequiredArgsConstructor
public class UserNormalLevelServiceImpl extends ServiceImpl<UserNormalLevelMapper, UserNormalLevel>
        implements IUserNormalLevelService {

    private final IMemberService memberService;

    @Override
    public PageUtils listPage(Search search, UserNormalLevel level) {
        LambdaQueryWrapper<UserNormalLevel> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(StrUtil.isNotBlank(level.getName()), UserNormalLevel::getName, level.getName());
        queryWrapper.eq(ObjectUtil.isNotNull(level.getStatus()), UserNormalLevel::getStatus, level.getStatus());
        return new PageUtils(this.page(PageUtils.getPage(search), queryWrapper));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeUserNormalLevelById(Long id) {
        if (memberService.checkHasUserByLevelId(id)) {
            UserNormalLevel userNormalLevel = this.getById(id);
            throw new ApiException("【" + userNormalLevel.getName() + "】已分配用户，不允许删除");
        }
        return this.removeById(id);
    }

    @Override
    public boolean checkLevelNameExist(UserNormalLevel level) {
        LambdaQueryWrapper<UserNormalLevel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserNormalLevel::getName, level.getName());
        queryWrapper.ne(level.getId() != null, UserNormalLevel::getId, level.getId());
        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }

    @Override
    public boolean checkLevelGradeExist(UserNormalLevel level) {
        LambdaQueryWrapper<UserNormalLevel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserNormalLevel::getGrade, level.getGrade());
        queryWrapper.ne(level.getId() != null, UserNormalLevel::getId, level.getId());
        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }

    @Override
    public boolean checkLevelExperienceExist(UserNormalLevel level) {
        LambdaQueryWrapper<UserNormalLevel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserNormalLevel::getExperience, level.getExperience());
        queryWrapper.ne(level.getId() != null, UserNormalLevel::getId, level.getId());
        queryWrapper.last("limit 1");
        return this.count(queryWrapper) > 0;
    }
}




