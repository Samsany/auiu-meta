package com.auiucloud.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.auiucloud.auth.domain.SocialUser;
import com.auiucloud.auth.service.SocialUserService;
import com.auiucloud.auth.mapper.SocialUserMapper;
import org.springframework.stereotype.Service;

/**
* @author dries
* @description 针对表【social_user(第三方用户表)】的数据库操作Service实现
* @createDate 2023-02-26 22:46:21
*/
@Service
public class SocialUserServiceImpl extends ServiceImpl<SocialUserMapper, SocialUser>
    implements SocialUserService{

}




