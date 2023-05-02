package com.auiucloud.auth.service;

import com.auiucloud.auth.domain.SocialUser;
import com.auiucloud.auth.model.AppletAuthCallback;
import com.auiucloud.auth.model.AppletUserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author dries
* @description 针对表【social_user(第三方用户表)】的数据库操作Service
* @createDate 2023-02-26 22:46:21
*/
public interface ISocialUserService extends IService<SocialUser> {

    Boolean bindMember2Social(Long userId, SocialUser socialUser);

    AppletUserInfo registerMemberBySocial(AppletUserInfo userInfo, AppletAuthCallback callback);
}
