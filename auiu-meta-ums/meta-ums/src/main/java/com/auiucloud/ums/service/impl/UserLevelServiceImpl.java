package com.auiucloud.ums.service.impl;

import com.auiucloud.core.common.constant.CommonConstant;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.auiucloud.ums.domain.UserLevelRecord;
import com.auiucloud.ums.service.IUserLevelService;
import com.auiucloud.ums.mapper.UserLevelMapper;
import org.springframework.stereotype.Service;

/**
* @author dries
* @description 针对表【ums_user_level(付费会员等级记录表)】的数据库操作Service实现
* @createDate 2023-04-03 22:22:38
*/
@Service
public class UserLevelServiceImpl extends ServiceImpl<UserLevelMapper, UserLevelRecord>
    implements IUserLevelService {

    @Override
    public UserLevelRecord selectUserLevelRecordByUId(Long uId) {
        return this.getOne(Wrappers.<UserLevelRecord>lambdaQuery()
                .eq(UserLevelRecord::getUid, uId)
                .eq(UserLevelRecord::getStatus, CommonConstant.STATUS_NORMAL_VALUE)
                .last("limit 1")
        );
    }
}




