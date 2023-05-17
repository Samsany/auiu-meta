package com.auiucloud.ums.service.impl;

import com.auiucloud.core.common.utils.SecurityUtil;
import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.ums.domain.UserIntegralRecord;
import com.auiucloud.ums.enums.UserPointEnums;
import com.auiucloud.ums.mapper.UserIntegralRecordMapper;
import com.auiucloud.ums.service.IUserIntegralRecordService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author dries
 * @description 针对表【ums_user_integral_record(用户积分记录表)】的数据库操作Service实现
 * @createDate 2023-05-10 18:01:35
 */
@Service
public class UserIntegralRecordServiceImpl extends ServiceImpl<UserIntegralRecordMapper, UserIntegralRecord>
        implements IUserIntegralRecordService {

    @Override
    public PageUtils selectUserPointReceivePage(Search search) {
        LambdaQueryWrapper<UserIntegralRecord> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserIntegralRecord::getType, UserPointEnums.ChangeTypeEnum.INCREASE.getValue());
        queryWrapper.eq(UserIntegralRecord::getStatus, UserPointEnums.StatusEnum.SUCCESS.getValue());
        queryWrapper.eq(UserIntegralRecord::getUId, SecurityUtil.getUserId());
        queryWrapper.orderByDesc(UserIntegralRecord::getCreateTime);
        return new PageUtils(this.page(PageUtils.getPage(search), queryWrapper));
    }

    @Override
    public PageUtils selectUserPointConsumptionPage(Search search) {
        LambdaQueryWrapper<UserIntegralRecord> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserIntegralRecord::getType, UserPointEnums.ChangeTypeEnum.DECREASE.getValue());
        queryWrapper.eq(UserIntegralRecord::getStatus, UserPointEnums.StatusEnum.SUCCESS.getValue());
        queryWrapper.eq(UserIntegralRecord::getUId, SecurityUtil.getUserId());
        queryWrapper.orderByDesc(UserIntegralRecord::getCreateTime);
        return new PageUtils(this.page(PageUtils.getPage(search), queryWrapper));
    }

}




