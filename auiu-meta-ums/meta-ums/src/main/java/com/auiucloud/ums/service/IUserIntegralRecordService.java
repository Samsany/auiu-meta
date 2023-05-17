package com.auiucloud.ums.service;

import com.auiucloud.core.database.model.Search;
import com.auiucloud.core.database.utils.PageUtils;
import com.auiucloud.ums.domain.UserIntegralRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author dries
 * @description 针对表【ums_user_integral_record(用户积分记录表)】的数据库操作Service
 * @createDate 2023-05-10 18:01:35
 */
public interface IUserIntegralRecordService extends IService<UserIntegralRecord> {

    PageUtils selectUserPointReceivePage(Search search);

    PageUtils selectUserPointConsumptionPage(Search search);
}
