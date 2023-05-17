package com.auiucloud.auth.service;

import com.auiucloud.auth.domain.SocialUserAuth;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author dries
 * @description 针对表【social_user_auth(第三方用户 & 系统用户关联表)】的数据库操作Service
 * @createDate 2023-02-26 22:46:21
 */
public interface ISocialUserAuthService extends IService<SocialUserAuth> {

}
