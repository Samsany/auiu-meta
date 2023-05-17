package com.auiucloud.auth.service.impl;

import com.auiucloud.auth.domain.SocialUserAuth;
import com.auiucloud.auth.mapper.SocialUserAuthMapper;
import com.auiucloud.auth.service.ISocialUserAuthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author dries
 * @description 针对表【social_user_auth(第三方用户 & 系统用户关联表)】的数据库操作Service实现
 * @createDate 2023-02-26 22:46:21
 */
@Service
public class SocialUserAuthServiceImpl extends ServiceImpl<SocialUserAuthMapper, SocialUserAuth>
        implements ISocialUserAuthService {

}




