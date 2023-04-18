package com.auiucloud.ums.service.impl;

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

}




