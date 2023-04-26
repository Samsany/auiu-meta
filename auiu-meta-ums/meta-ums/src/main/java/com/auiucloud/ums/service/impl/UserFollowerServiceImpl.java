package com.auiucloud.ums.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.auiucloud.ums.domain.UserFollower;
import com.auiucloud.ums.service.IUserFollowerService;
import com.auiucloud.ums.mapper.UserFollowerMapper;
import org.springframework.stereotype.Service;

/**
* @author dries
* @description 针对表【ums_user_follower(我的关注粉丝表)】的数据库操作Service实现
* @createDate 2023-04-24 10:03:15
*/
@Service
public class UserFollowerServiceImpl extends ServiceImpl<UserFollowerMapper, UserFollower>
    implements IUserFollowerService {

}




