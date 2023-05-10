package com.auiucloud.ums.service;

import com.auiucloud.ums.domain.UserLevelRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author dries
* @description 针对表【ums_user_level(付费会员等级记录表)】的数据库操作Service
* @createDate 2023-04-03 22:22:38
*/
public interface IUserLevelService extends IService<UserLevelRecord> {

    UserLevelRecord selectUserLevelRecordByUId(Long uId);

}
