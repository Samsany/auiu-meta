package com.auiucloud.auth.service.impl;

import com.auiucloud.auth.domain.SocialUser;
import com.auiucloud.auth.domain.SocialUserAuth;
import com.auiucloud.auth.mapper.SocialUserMapper;
import com.auiucloud.auth.service.SocialUserAuthService;
import com.auiucloud.auth.service.SocialUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author dries
 * @description 针对表【social_user(第三方用户表)】的数据库操作Service实现
 * @createDate 2023-02-26 22:46:21
 */
@Service
@RequiredArgsConstructor
public class SocialUserServiceImpl extends ServiceImpl<SocialUserMapper, SocialUser> implements SocialUserService {

    private final SocialUserAuthService socialUserAuthService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean registerUserBySocial(Long userId, SocialUser socialUser) {
        boolean result = this.save(socialUser);
        if (result) {
            SocialUserAuth build = SocialUserAuth.builder().userId(userId).socialUserId(socialUser.getId()).build();
            socialUserAuthService.save(build);
        }
        return result;
    }

}




